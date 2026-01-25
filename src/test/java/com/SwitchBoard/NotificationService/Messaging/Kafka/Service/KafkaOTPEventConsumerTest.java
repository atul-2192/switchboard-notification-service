package com.SwitchBoard.NotificationService.Messaging.Kafka.Service;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import switchboard.schemas.OTPNotificationEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaOTPEventConsumerTest {

    @Mock
    private OTPService otpService;

    @InjectMocks
    private KafkaOTPEventConsumer kafkaOTPEventConsumer;

    private OTPNotificationEvent otpNotificationEvent;

    @BeforeEach
    void setUp() {
        otpNotificationEvent = new OTPNotificationEvent();
        otpNotificationEvent.setEmailID("test@example.com");
        otpNotificationEvent.setOtp("123456");
    }

    @Test
    void consume_ValidEvent_CallsOTPService() {
        // Arrange
        ArgumentCaptor<OTPRequestBody> captor = ArgumentCaptor.forClass(OTPRequestBody.class);
        when(otpService.sendOTP(any(OTPRequestBody.class)))
                .thenReturn(ApiResponse.success("OTP sent", true));

        // Act
        kafkaOTPEventConsumer.consume(otpNotificationEvent);

        // Assert
        verify(otpService, times(1)).sendOTP(captor.capture());
        OTPRequestBody capturedRequest = captor.getValue();
        assertEquals("test@example.com", capturedRequest.getEmailID());
        assertEquals("123456", capturedRequest.getOtp());
    }

    @Test
    void consume_EventWithDifferentEmail_ProcessesCorrectly() {
        // Arrange
        otpNotificationEvent.setEmailID("another@example.com");
        otpNotificationEvent.setOtp("654321");
        ArgumentCaptor<OTPRequestBody> captor = ArgumentCaptor.forClass(OTPRequestBody.class);

        // Act
        kafkaOTPEventConsumer.consume(otpNotificationEvent);

        // Assert
        verify(otpService, times(1)).sendOTP(captor.capture());
        assertEquals("another@example.com", captor.getValue().getEmailID());
        assertEquals("654321", captor.getValue().getOtp());
    }

    @Test
    void consume_MultipleEvents_ProcessesAll() {
        // Arrange
        OTPNotificationEvent event1 = new OTPNotificationEvent();
        event1.setEmailID("user1@example.com");
        event1.setOtp("111111");

        OTPNotificationEvent event2 = new OTPNotificationEvent();
        event2.setEmailID("user2@example.com");
        event2.setOtp("222222");

        // Act
        kafkaOTPEventConsumer.consume(event1);
        kafkaOTPEventConsumer.consume(event2);

        // Assert
        verify(otpService, times(2)).sendOTP(any(OTPRequestBody.class));
    }

    @Test
    void consume_ServiceThrowsException_ExceptionPropagates() {
        // Arrange
        doThrow(new RuntimeException("Service unavailable"))
                .when(otpService).sendOTP(any(OTPRequestBody.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            kafkaOTPEventConsumer.consume(otpNotificationEvent);
        });
        verify(otpService, times(1)).sendOTP(any(OTPRequestBody.class));
    }
}
