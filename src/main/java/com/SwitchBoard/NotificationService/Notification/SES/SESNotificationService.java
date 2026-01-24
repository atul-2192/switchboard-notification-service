package com.SwitchBoard.NotificationService.Notification.SES;

import com.SwitchBoard.NotificationService.Constants.MailSubjectConstant;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import com.SwitchBoard.NotificationService.Notification.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class SESNotificationService implements NotificationSender {

    private final SesClient sesClient;
    private final TemplateEngine templateEngine;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    @Value("${aws.ses.from-name:SwitchBoard Notification}")
    private String fromName;

    @Override
    public void send(OTPRequestBody otpRequestBody) {
        String emailID = otpRequestBody.getEmailID();
        String otp = otpRequestBody.getOtp();
        
        log.info("[SESNotificationService] :: sendOTP :: Preparing OTP email for {}", emailID);
        
        try {
            // Build email content using Thymeleaf template
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("expiryMinutes", "5 Minutes");

            String htmlContent = templateEngine.process("otp-template", context);

            // Create email message
            Message message = Message.builder()
                    .subject(Content.builder()
                            .data(MailSubjectConstant.OTP_SUBJECT)
                            .charset("UTF-8")
                            .build())
                    .body(Body.builder()
                            .html(Content.builder()
                                    .data(htmlContent)
                                    .charset("UTF-8")
                                    .build())
                            .build())
                    .build();

            // Create destination
            Destination destination = Destination.builder()
                    .toAddresses(emailID)
                    .build();

            // Create send email request
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(fromName + " <" + fromEmail + ">")
                    .destination(destination)
                    .message(message)
                    .build();

            // Send email using AWS SES
            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            
            log.info("[SESNotificationService] :: sendOTP :: OTP email sent successfully to {} :: MessageId: {}", 
                    emailID, response.messageId());

        } catch (SesException ex) {
            log.error("[SESNotificationService] :: sendOTP :: AWS SES error sending OTP to {} :: Code: {} :: {}", 
                    emailID, ex.awsErrorDetails().errorCode(), ex.getMessage(), ex);
            throw new EmailSendException("Failed to send OTP email to " + emailID + " via AWS SES", ex);
        } catch (Exception ex) {
            log.error("[SESNotificationService] :: sendOTP :: Error sending OTP to {} :: {}", 
                    emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send OTP email to " + emailID + " via AWS SES", ex);
        }
    }

    @Override
    public void send(OnboardingRequestBody onboardingRequestBody) {
        String emailID = onboardingRequestBody.getEmailID();
        String fullName = onboardingRequestBody.getFullName();
        
        log.info("[SESNotificationService] :: sendWelcome :: Preparing welcome email for {}", emailID);
        
        try {
            // Build email content using Thymeleaf template
            Context context = new Context();
            context.setVariable("fullName", fullName);
            
            String htmlContent = templateEngine.process("onboarding-template", context);

            // Create email message
            Message message = Message.builder()
                    .subject(Content.builder()
                            .data(MailSubjectConstant.WELCOME_SUBJECT)
                            .charset("UTF-8")
                            .build())
                    .body(Body.builder()
                            .html(Content.builder()
                                    .data(htmlContent)
                                    .charset("UTF-8")
                                    .build())
                            .build())
                    .build();

            // Create destination
            Destination destination = Destination.builder()
                    .toAddresses(emailID)
                    .build();

            // Create send email request
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(fromName + " <" + fromEmail + ">")
                    .destination(destination)
                    .message(message)
                    .build();

            // Send email using AWS SES
            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            
            log.info("[SESNotificationService] :: sendWelcome :: Welcome email sent successfully to {} :: MessageId: {}", 
                    emailID, response.messageId());

        } catch (SesException ex) {
            log.error("[SESNotificationService] :: sendWelcome :: AWS SES error sending welcome email to {} :: Code: {} :: {}", 
                    emailID, ex.awsErrorDetails().errorCode(), ex.getMessage(), ex);
            throw new EmailSendException("Failed to send welcome email to " + emailID + " via AWS SES", ex);
        } catch (Exception ex) {
            log.error("[SESNotificationService] :: sendWelcome :: Error sending welcome email to {} :: {}", 
                    emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send welcome email to " + emailID + " via AWS SES", ex);
        }
    }
}
