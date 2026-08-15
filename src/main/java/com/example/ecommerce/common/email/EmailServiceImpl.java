package com.example.ecommerce.common.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * SMTP-backed implementation of {@link EmailService}.
 *
 * <p>Builds a simple HTML message and delivers it via {@link JavaMailSender}.
 * When {@code app.mail.enabled} is {@code false} (typical for local development)
 * the code is only logged, never sent.</p>
 */
@Service("verificationEmailService")
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String HEADER_LOGO = "E-Commerce";

    /**
     * Optional so the application still starts when no {@code spring.mail.host} is
     * configured (mail is disabled by default; see {@code app.mail.enabled}).
     */
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean enabled;

    @Value("${app.mail.from:no-reply@example.com}")
    private String from;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendVerificationCode(String to, String code, VerificationPurpose purpose) {
        if (!enabled) {
            log.info("[MAIL DISABLED] {} code for {}: {}", purpose, to, code);
            return;
        }
        if (mailSender == null) {
            log.error("[MAIL UNAVAILABLE] No JavaMailSender bean — cannot send {} email to {}", purpose, to);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subjectFor(purpose));
            helper.setText(bodyFor(purpose, code), true);
            mailSender.send(message);
            log.info("Sent {} email to {}", purpose, to);
        } catch (MessagingException ex) {
            // Best-effort delivery: never fail the surrounding business transaction.
            log.error("Failed to send {} email to {}: {}", purpose, to, ex.getMessage(), ex);
        }
    }

    private String subjectFor(VerificationPurpose purpose) {
        return switch (purpose) {
            case EMAIL_VERIFICATION -> "Verify your email address";
            case PASSWORD_RESET -> "Reset your password";
        };
    }

    private String bodyFor(VerificationPurpose purpose, String code) {
        String message = switch (purpose) {
            case EMAIL_VERIFICATION ->
                    "<p>Use the code below to confirm your email address. It expires in a few minutes.</p>";
            case PASSWORD_RESET ->
                    "<p>Use the code below to reset your password. If you did not request this, you can ignore this email.</p>";
        };
        return """
                <div style="font-family:Arial,sans-serif;color:#333;max-width:480px;margin:auto">
                    <h2 style="color:#4f46e5">%s</h2>
                    %s
                    <div style="font-size:28px;font-weight:bold;letter-spacing:6px;background:#f3f4f6;padding:16px;text-align:center;border-radius:8px">%s</div>
                    <p style="font-size:12px;color:#888;margin-top:24px">If you have questions, contact support.</p>
                </div>
                """.formatted(HEADER_LOGO, message, code);
    }
}
