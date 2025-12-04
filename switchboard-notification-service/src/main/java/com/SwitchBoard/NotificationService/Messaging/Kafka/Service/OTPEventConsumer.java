package com.SwitchBoard.NotificationService.Messaging.Kafka.Service;


import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import switchboard.schemas.OTPNotificationEvent;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("dev")
public class OTPEventConsumer {

    private final OTPService otpService;

    @KafkaListener(topics = "otp-topic", groupId = "otp-consumer-group")
    public void consume(OTPNotificationEvent optNotificationEvent) {
            log.info("OTPEventConsumer :: consume :: Received OTP event for email: {}", optNotificationEvent.getEmailID());
            otpService.sendOTP(new OTPRequestBody(optNotificationEvent.getEmailID(),optNotificationEvent.getOtp()));
            log.info("OTPEventConsumer :: consume :: Processed OTP event for email: {}", optNotificationEvent.getEmailID());
    }
}
