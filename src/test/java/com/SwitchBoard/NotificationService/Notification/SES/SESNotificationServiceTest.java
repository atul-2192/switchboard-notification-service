package com.SwitchBoard.NotificationService.Notification.SES;

import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SESNotificationServiceTest {

    @Mock
    private SesClient sesClient;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private SESNotificationService sesNotificationService;

    private OTPRequestBody otpRequestBody;
    private OnboardingRequestBody onboardingRequestBody;

    @BeforeEach
    void setUp() {
        otpRequestBody = new OTPRequestBody("test@example.com", "123456");
        onboardingRequestBody = new OnboardingRequestBody("user@example.com", "Jane Smith");
        
        // Set required fields using ReflectionTestUtils
        ReflectionTestUtils.setField(sesNotificationService, "fromEmail", "no-reply@switchboard.com");
        ReflectionTestUtils.setField(sesNotificationService, "fromName", "SwitchBoard");
    }

    @Test
    void sendOTP_Success() {
        // Arrange
        when(templateEngine.process(eq("otp-template"), any(Context.class)))
                .thenReturn("<html>Your OTP: 123456</html>");
        
        SendEmailResponse mockResponse = SendEmailResponse.builder()
                .messageId("msg-123")
                .build();
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(mockResponse);

        // Act
        sesNotificationService.send(otpRequestBody);

        // Assert
        verify(templateEngine, times(1)).process(eq("otp-template"), any(Context.class));
        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void sendOTP_ThrowsEmailSendException_WhenSESFails() {
        // Arrange
        when(templateEngine.process(eq("otp-template"), any(Context.class)))
                .thenReturn("<html>Your OTP: 123456</html>");
        
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenThrow(new RuntimeException("SES Error"));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            sesNotificationService.send(otpRequestBody);
        });

        assertTrue(exception.getMessage().contains("Failed to send OTP email"));
        assertTrue(exception.getMessage().contains("test@example.com"));
        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void sendOnboarding_Success() {
        // Arrange
        when(templateEngine.process(eq("onboarding-template"), any(Context.class)))
                .thenReturn("<html>Welcome Jane Smith</html>");
        
        SendEmailResponse mockResponse = SendEmailResponse.builder()
                .messageId("msg-456")
                .build();
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(mockResponse);

        // Act
        sesNotificationService.send(onboardingRequestBody);

        // Assert
        verify(templateEngine, times(1)).process(eq("onboarding-template"), any(Context.class));
        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void sendOnboarding_ThrowsEmailSendException_WhenTemplateProcessingFails() {
        // Arrange
        when(templateEngine.process(eq("onboarding-template"), any(Context.class)))
                .thenThrow(new RuntimeException("Template error"));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            sesNotificationService.send(onboardingRequestBody);
        });

        assertTrue(exception.getMessage().contains("Failed to send welcome email"));
        assertTrue(exception.getMessage().contains("user@example.com"));
        verify(sesClient, never()).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void sendOTP_HandlesGenericException() {
        // Arrange
        when(templateEngine.process(eq("otp-template"), any(Context.class)))
                .thenReturn("<html>Your OTP: 123456</html>");
        
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Act & Assert
        assertThrows(EmailSendException.class, () -> {
            sesNotificationService.send(otpRequestBody);
        });
        
        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
    }
}
