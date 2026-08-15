package com.example.ecommerce.email.service;

import com.example.ecommerce.email.dto.request.SendWelcomeEmailRequest;
import com.example.ecommerce.email.service.impl.EmailServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Properties;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@example.com");
        // mailSender is now an optional field (no Redis/mail required to boot);
        // inject the mock explicitly since @InjectMocks only used the constructor.
        ReflectionTestUtils.setField(emailService, "mailSender", mailSender);
    }

    @Test
    void testSendWelcomeEmailSuccess() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("welcome"), any(Context.class))).thenReturn("<html>Welcome!</html>");

        SendWelcomeEmailRequest request = SendWelcomeEmailRequest.builder()
                .recipientEmail("john.doe@example.com")
                .userName("John Doe")
                .actionUrl("https://store.example.com")
                .build();

        emailService.sendWelcomeEmail(request);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
