package com.example.ecommerce.notification.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.notification.channel.NotificationChannelStrategy;
import com.example.ecommerce.notification.dto.request.CreateTemplateRequest;
import com.example.ecommerce.notification.dto.request.NotificationFilterRequest;
import com.example.ecommerce.notification.dto.request.SendNotificationRequest;
import com.example.ecommerce.notification.dto.request.UpdateTemplateRequest;
import com.example.ecommerce.notification.dto.response.NotificationResponse;
import com.example.ecommerce.notification.dto.response.NotificationTemplateResponse;
import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationStatus;
import com.example.ecommerce.notification.entity.NotificationTemplate;
import com.example.ecommerce.notification.event.NotificationEvent;
import com.example.ecommerce.notification.event.NotificationEventPublisher;
import com.example.ecommerce.notification.mapper.NotificationMapper;
import com.example.ecommerce.notification.repository.NotificationRepository;
import com.example.ecommerce.notification.repository.NotificationTemplateRepository;
import com.example.ecommerce.notification.service.NotificationService;
import com.example.ecommerce.notification.specification.NotificationSpecification;
import com.example.ecommerce.notification.validator.NotificationValidator;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation handling multi-channel notification dispatching (Email, SMS, Push, In-App),
 * template interpolation, Spring `@Async` processing, event listener bridging, and template administration.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationValidator notificationValidator;
    private final NotificationEventPublisher eventPublisher;
    private final Map<NotificationChannel, NotificationChannelStrategy> channelStrategies;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationTemplateRepository templateRepository,
                                   UserRepository userRepository,
                                   NotificationMapper notificationMapper,
                                   NotificationValidator notificationValidator,
                                   NotificationEventPublisher eventPublisher,
                                   List<NotificationChannelStrategy> strategies) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.notificationMapper = notificationMapper;
        this.notificationValidator = notificationValidator;
        this.eventPublisher = eventPublisher;
        this.channelStrategies = new EnumMap<>(NotificationChannel.class);
        for (NotificationChannelStrategy strategy : strategies) {
            this.channelStrategies.put(strategy.getSupportedChannel(), strategy);
        }
    }

    @Override
    @Transactional
    public NotificationResponse sendNotification(SendNotificationRequest request) {
        log.info("Processing notification dispatch to [{}] via {}", request.getRecipient(), request.getChannel());
        notificationValidator.validateSendRequest(request);

        User targetUser = null;
        if (request.getUserId() != null) {
            targetUser = userRepository.findById(request.getUserId()).orElse(null);
        }

        String title = request.getTitle();
        String content = request.getContent();

        if (StringUtils.hasText(request.getTemplateCode())) {
            NotificationTemplate template = templateRepository.findByTemplateCodeAndDeletedFalse(request.getTemplateCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Notification template not found: " + request.getTemplateCode()));

            if (StringUtils.hasText(template.getSubject())) {
                title = interpolate(template.getSubject(), request.getTemplateParams());
            }
            content = interpolate(template.getBodyTemplate(), request.getTemplateParams());
        }

        Notification notification = Notification.builder()
                .user(targetUser)
                .recipient(request.getRecipient())
                .channel(request.getChannel())
                .type(request.getType())
                .templateCode(request.getTemplateCode())
                .title(title)
                .content(content)
                .status(NotificationStatus.PENDING)
                .build();

        NotificationChannelStrategy strategy = channelStrategies.get(request.getChannel());
        if (strategy != null) {
            try {
                boolean success = strategy.send(notification);
                if (success) {
                    notification.markSent();
                } else {
                    notification.markFailed("Gateway delivery returned false");
                }
            } catch (Exception e) {
                log.error("Failed to send notification via {}: {}", request.getChannel(), e.getMessage());
                notification.markFailed(e.getMessage());
            }
        } else {
            notification.markSent(); // Default for IN_APP or unconfigured channel
        }

        Notification saved = notificationRepository.save(notification);
        log.info("Saved notification ID {} with status {}", saved.getId(), saved.getStatus());

        return notificationMapper.toResponse(saved);
    }

    @Override
    public void sendNotificationAsync(SendNotificationRequest request) {
        log.info("Publishing async notification event for recipient [{}]", request.getRecipient());
        eventPublisher.publishNotificationEvent(request);
    }

    @Async
    @EventListener
    @Transactional
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("Handling async notification event ID {}", event.getEventId());
        sendNotification(event.getNotificationRequest());
    }

    @Override
    @Transactional
    public NotificationTemplateResponse createTemplate(CreateTemplateRequest request) {
        notificationValidator.validateTemplateCodeUnique(request.getTemplateCode());

        NotificationTemplate template = NotificationTemplate.builder()
                .templateCode(request.getTemplateCode())
                .channel(request.getChannel())
                .type(request.getType())
                .subject(request.getSubject())
                .bodyTemplate(request.getBodyTemplate())
                .active(true)
                .build();

        NotificationTemplate saved = templateRepository.save(template);
        log.info("Created notification template with code: {}", saved.getTemplateCode());
        return notificationMapper.toTemplateResponse(saved);
    }

    @Override
    @Transactional
    public NotificationTemplateResponse updateTemplate(Long id, UpdateTemplateRequest request) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + id));

        if (StringUtils.hasText(request.getSubject())) {
            template.setSubject(request.getSubject());
        }
        if (StringUtils.hasText(request.getBodyTemplate())) {
            template.setBodyTemplate(request.getBodyTemplate());
        }
        if (request.getActive() != null) {
            template.setActive(request.getActive());
        }

        NotificationTemplate saved = templateRepository.save(template);
        log.info("Updated notification template ID {}", saved.getId());
        return notificationMapper.toTemplateResponse(saved);
    }

    @Override
    public NotificationTemplateResponse getTemplateByCode(String templateCode) {
        NotificationTemplate template = templateRepository.findByTemplateCodeAndDeletedFalse(templateCode)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with code: " + templateCode));
        return notificationMapper.toTemplateResponse(template);
    }

    @Override
    public List<NotificationTemplateResponse> getAllTemplates() {
        return notificationMapper.toTemplateResponseList(templateRepository.findAll());
    }

    @Override
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        return notificationMapper.toResponse(notification);
    }

    @Override
    public PageResponse<NotificationResponse> getMyNotifications(Pageable pageable) {
        User currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new UnauthorizedException("User must be authenticated to view notifications");
        }
        Page<Notification> page = notificationRepository.findByUserIdAndDeletedFalse(currentUser.getId(), pageable);
        return PageResponse.from(page, notificationMapper::toResponse);
    }

    @Override
    public PageResponse<NotificationResponse> getNotifications(NotificationFilterRequest filter, Pageable pageable) {
        Specification<Notification> spec = NotificationSpecification.build(filter);
        Page<Notification> page = notificationRepository.findAll(spec, pageable);
        return PageResponse.from(page, notificationMapper::toResponse);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));

        notification.markRead();
        notificationRepository.save(notification);
    }

    private String interpolate(String template, Map<String, String> params) {
        if (!StringUtils.hasText(template) || params == null || params.isEmpty()) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String replacement = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, replacement);
        }
        return result;
    }

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            return userRepository.findByEmailIgnoreCase(email).orElse(null);
        }
        return null;
    }
}
