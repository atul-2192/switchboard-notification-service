package com.SwitchBoard.NotificationService.Messaging.RabbitMQ.Service;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Service.OnboaringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import switchboard.schemas.OnboardingEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitOnboardingEventConsumerTest {

    @Mock
    private OnboaringService onboaringService;

    @InjectMocks
    private RabbitOnboardingEventConsumer rabbitOnboardingEventConsumer;

    private OnboardingEvent event;

    @BeforeEach
    void setUp() {
        event = new OnboardingEvent();
        event.setEmailID("john.doe@example.com");
        event.setFullName("John Doe");
    }

    @Test
    void consume_ValidEvent_CallsOnboardingService() {
        // Arrange
        ArgumentCaptor<OnboardingRequestBody> captor = ArgumentCaptor.forClass(OnboardingRequestBody.class);
        when(onboaringService.sendOnboardingMail(any(OnboardingRequestBody.class)))
                .thenReturn(ApiResponse.success("Onboarding email sent", true));

        // Act
        rabbitOnboardingEventConsumer.consume(event);

        // Assert
        verify(onboaringService, times(1)).sendOnboardingMail(captor.capture());
        OnboardingRequestBody capturedRequest = captor.getValue();
        assertEquals("john.doe@example.com", capturedRequest.getEmailID());
        assertEquals("John Doe", capturedRequest.getFullName());
    }

    @Test
    void consume_EventWithDifferentData_ProcessesCorrectly() {
        // Arrange
        event.setEmailID("jane.smith@example.com");
        event.setFullName("Jane Smith");
        ArgumentCaptor<OnboardingRequestBody> captor = ArgumentCaptor.forClass(OnboardingRequestBody.class);

        // Act
        rabbitOnboardingEventConsumer.consume(event);

        // Assert
        verify(onboaringService, times(1)).sendOnboardingMail(captor.capture());
        assertEquals("jane.smith@example.com", captor.getValue().getEmailID());
        assertEquals("Jane Smith", captor.getValue().getFullName());
    }

    @Test
    void consume_MultipleEvents_ProcessesAll() {
        // Arrange
        OnboardingEvent event1 = new OnboardingEvent();
        event1.setEmailID("user1@example.com");
        event1.setFullName("User One");

        OnboardingEvent event2 = new OnboardingEvent();
        event2.setEmailID("user2@example.com");
        event2.setFullName("User Two");

        // Act
        rabbitOnboardingEventConsumer.consume(event1);
        rabbitOnboardingEventConsumer.consume(event2);

        // Assert
        verify(onboaringService, times(2)).sendOnboardingMail(any(OnboardingRequestBody.class));
    }

    @Test
    void consume_ServiceThrowsException_ExceptionPropagates() {
        // Arrange
        doThrow(new RuntimeException("Email service down"))
                .when(onboaringService).sendOnboardingMail(any(OnboardingRequestBody.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            rabbitOnboardingEventConsumer.consume(event);
        });
        verify(onboaringService, times(1)).sendOnboardingMail(any(OnboardingRequestBody.class));
    }
}
