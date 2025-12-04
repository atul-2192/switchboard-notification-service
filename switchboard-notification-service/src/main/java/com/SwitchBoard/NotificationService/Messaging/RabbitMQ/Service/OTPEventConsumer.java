package com.SwitchBoard.NotificationService.Messaging.RabbitMQ.Service;

import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import switchboard.schemas.OTPNotificationEvent;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class OTPEventConsumer {

    private final OTPService otpService;

    @RabbitListener(queues = "${rabbitmq.queue.otp}")
    public void consume(OTPNotificationEvent event) {
        log.info("OTPEventConsumer :: consume :: Received OTP for: {}", event.getEmailID());
        otpService.sendOTP(new OTPRequestBody(event.getEmailID(), event.getOtp()));
        log.info("OTPEventConsumer :: consume :: Processed OTP");
    }
}
