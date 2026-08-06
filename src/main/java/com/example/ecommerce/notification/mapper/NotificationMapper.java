package com.example.ecommerce.notification.mapper;

import com.example.ecommerce.notification.dto.response.NotificationResponse;
import com.example.ecommerce.notification.dto.response.NotificationTemplateResponse;
import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationTemplate;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for notification log entities and templates.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationResponse toResponse(Notification notification);

    NotificationTemplateResponse toTemplateResponse(NotificationTemplate template);

    List<NotificationResponse> toResponseList(List<Notification> notifications);

    List<NotificationTemplateResponse> toTemplateResponseList(List<NotificationTemplate> templates);
}
