package com.SwitchBoard.NotificationService.Notification.Gmail;

import com.SwitchBoard.NotificationService.Constants.MailSubjectConstant;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GmailNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private GmailNotificationService gmailNotificationService;

    private OTPRequestBody otpRequestBody;
    private OnboardingRequestBody onboardingRequestBody;

    @BeforeEach
    void setUp() {
        otpRequestBody = new OTPRequestBody("test@example.com", "123456");
        onboardingRequestBody = new OnboardingRequestBody("user@example.com", "John Doe");
    }

    @Test
    void sendOTP_Success() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("otp-template"), any(Context.class)))
                .thenReturn("<html>OTP: 123456</html>");
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        gmailNotificationService.send(otpRequestBody);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(templateEngine, times(1)).process(eq("otp-template"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendOTP_ThrowsEmailSendException_WhenMailSenderFails() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("otp-template"), any(Context.class)))
                .thenReturn("<html>OTP: 123456</html>");
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            gmailNotificationService.send(otpRequestBody);
        });

        assertTrue(exception.getMessage().contains("Failed to send OTP email"));
        assertTrue(exception.getMessage().contains("test@example.com"));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendOnboarding_Success() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("onboarding-template"), any(Context.class)))
                .thenReturn("<html>Welcome John Doe</html>");
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        gmailNotificationService.send(onboardingRequestBody);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(templateEngine, times(1)).process(eq("onboarding-template"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendOnboarding_ThrowsEmailSendException_WhenTemplateProcessingFails() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("onboarding-template"), any(Context.class)))
                .thenThrow(new RuntimeException("Template not found"));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            gmailNotificationService.send(onboardingRequestBody);
        });

        assertTrue(exception.getMessage().contains("Failed to send welcome email"));
        assertTrue(exception.getMessage().contains("user@example.com"));
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendOTP_HandlesNullEmailGracefully() {
        // Arrange
        otpRequestBody.setEmailID(null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act & Assert - should throw exception due to null email
        assertThrows(EmailSendException.class, () -> {
            gmailNotificationService.send(otpRequestBody);
        });
    }

    @Test
    void sendOnboarding_VerifyContextVariables() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("onboarding-template"), any(Context.class)))
                .thenReturn("<html>Welcome</html>");
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        gmailNotificationService.send(onboardingRequestBody);

        // Assert
        verify(templateEngine, times(1)).process(eq("onboarding-template"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
