package com.SwitchBoard.NotificationService.Kafka.Service;

import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import com.SwitchBoard.NotificationService.Service.OnboaringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import switchboard.schemas.OTPNotificationEvent;
import switchboard.schemas.OnboardingEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingEventConsumer {

    private final OnboaringService onboaringService;

    @KafkaListener(topics = "onboarding-topic", groupId = "onboarding-consumer-group")
    public void consume(OnboardingEvent onboardingEvent) {
        log.info("OnboardingEventConsumer :: consume :: Received onboarding event for email: {}", onboardingEvent.getEmailID());
        onboaringService.sendOnboardingMail(new OnboardingRequestBody( onboardingEvent.getEmailID() , onboardingEvent.getFullName()));
        log.info("OnboardingEventConsumer :: consume :: Processed onboarding event for email: {}", onboardingEvent.getEmailID());
    }
}
