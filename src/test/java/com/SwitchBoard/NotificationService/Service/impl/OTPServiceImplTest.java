package com.SwitchBoard.NotificationService.Service.impl;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
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
class OTPServiceImplTest {

    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private OTPServiceImpl otpService;

    private OTPRequestBody otpRequestBody;

    @BeforeEach
    void setUp() {
        otpRequestBody = new OTPRequestBody();
        otpRequestBody.setEmailID("test@example.com");
        otpRequestBody.setOtp("123456");
    }

    @Test
    void sendOTP_Success() {
        // Arrange
        doNothing().when(notificationSender).send(any(OTPRequestBody.class));

        // Act
        ApiResponse response = otpService.sendOTP(otpRequestBody);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("OTP sent successfully to test@example.com", response.getMessage());
        assertNotNull(response.getTimestamp());
        verify(notificationSender, times(1)).send(otpRequestBody);
    }

    @Test
    void sendOTP_ThrowsEmailSendException_WhenNotificationSenderFails() {
        // Arrange
        doThrow(new RuntimeException("Email service unavailable"))
                .when(notificationSender).send(any(OTPRequestBody.class));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            otpService.sendOTP(otpRequestBody);
        });

        assertTrue(exception.getMessage().contains("Unable to send OTP to test@example.com"));
        verify(notificationSender, times(1)).send(otpRequestBody);
    }

    @Test
    void sendOTP_HandlesNullEmailID() {
        // Arrange
        otpRequestBody.setEmailID(null);
        doThrow(new RuntimeException("Invalid email"))
                .when(notificationSender).send(any(OTPRequestBody.class));

        // Act & Assert
        assertThrows(EmailSendException.class, () -> {
            otpService.sendOTP(otpRequestBody);
        });
        verify(notificationSender, times(1)).send(otpRequestBody);
    }

    @Test
    void sendOTP_HandlesEmptyOTP() {
        // Arrange
        otpRequestBody.setOtp("");
        doNothing().when(notificationSender).send(any(OTPRequestBody.class));

        // Act
        ApiResponse response = otpService.sendOTP(otpRequestBody);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(notificationSender, times(1)).send(otpRequestBody);
    }

    @Test
    void sendOTP_VerifySuccessResponseStructure() {
        // Arrange
        doNothing().when(notificationSender).send(any(OTPRequestBody.class));

        // Act
        ApiResponse response = otpService.sendOTP(otpRequestBody);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getMessage());
        assertNull(response.getData()); // The success(String, boolean) method sets data to null
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }
}
