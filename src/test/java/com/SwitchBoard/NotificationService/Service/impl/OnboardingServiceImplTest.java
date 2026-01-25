package com.SwitchBoard.NotificationService.Service.impl;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Notification.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceImplTest {

    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private OnboardingServiceImpl onboardingService;

    private OnboardingRequestBody onboardingRequestBody;

    @BeforeEach
    void setUp() {
        onboardingRequestBody = new OnboardingRequestBody();
        onboardingRequestBody.setEmailID("john.doe@example.com");
        onboardingRequestBody.setFullName("John Doe");
    }

    @Test
    void sendOnboardingMail_Success() {
        // Arrange
        doNothing().when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Onboarding email sent successfully to john.doe@example.com", response.getMessage());
        assertNull(response.getData()); // The success(String, boolean) method sets data to null
        assertNotNull(response.getTimestamp());
        verify(notificationSender, times(1)).send(onboardingRequestBody);
    }

    @Test
    void sendOnboardingMail_ReturnsErrorResponse_WhenNotificationSenderFails() {
        // Arrange
        doThrow(new RuntimeException("SMTP connection failed"))
                .when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Unable to send onboarding email to john.doe@example.com"));
        assertEquals("500", response.getErrorCode());
        assertNotNull(response.getTimestamp());
        verify(notificationSender, times(1)).send(onboardingRequestBody);
    }

    @Test
    void sendOnboardingMail_HandlesNullPointerException() {
        // Arrange
        doThrow(new NullPointerException("Template not found"))
                .when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("500", response.getErrorCode());
        verify(notificationSender, times(1)).send(onboardingRequestBody);
    }

    @Test
    void sendOnboardingMail_WithEmptyFullName() {
        // Arrange
        onboardingRequestBody.setFullName("");
        doNothing().when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(notificationSender, times(1)).send(onboardingRequestBody);
    }

    @Test
    void sendOnboardingMail_WithNullEmailID() {
        // Arrange
        onboardingRequestBody.setEmailID(null);
        doThrow(new RuntimeException("Invalid email"))
                .when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Unable to send onboarding email to null"));
        verify(notificationSender, times(1)).send(onboardingRequestBody);
    }

    @Test
    void sendOnboardingMail_VerifyErrorResponseStructure() {
        // Arrange
        doThrow(new RuntimeException("Service error"))
                .when(notificationSender).send(any(OnboardingRequestBody.class));

        // Act
        ApiResponse response = onboardingService.sendOnboardingMail(onboardingRequestBody);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getMessage());
        assertNotNull(response.getErrorCode());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }
}
