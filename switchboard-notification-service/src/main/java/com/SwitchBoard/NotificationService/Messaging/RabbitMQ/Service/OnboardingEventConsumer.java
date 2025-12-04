package com.SwitchBoard.NotificationService.Messaging.RabbitMQ.Service;

import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Service.OnboaringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import switchboard.schemas.OnboardingEvent;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class OnboardingEventConsumer {

    private final OnboaringService onboaringService;

    @RabbitListener(queues = "${rabbitmq.queue.onboarding}")
    public void consume(OnboardingEvent event) {
        log.info("OnboardingEventConsumer :: consume :: Received: {}", event.getEmailID());
        onboaringService.sendOnboardingMail(
                new OnboardingRequestBody(event.getEmailID(), event.getFullName())
        );
        log.info("OnboardingEventConsumer :: consume :: Processed onboarding");
    }
}
