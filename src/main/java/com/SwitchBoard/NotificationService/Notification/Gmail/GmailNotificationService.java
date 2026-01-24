package com.SwitchBoard.NotificationService.Notification.Gmail;

import com.SwitchBoard.NotificationService.Constants.MailSubjectConstant;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import com.SwitchBoard.NotificationService.Notification.NotificationSender;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile({"dev","local"})
public class GmailNotificationService implements NotificationSender {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(OTPRequestBody otpRequestBody) {
        String emailID = otpRequestBody.getEmailID();
        String otp = otpRequestBody.getOtp();
        
        log.info("[GmailNotificationService] :: sendOTP :: Preparing OTP email for {}", emailID);
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailID);
            helper.setSubject(MailSubjectConstant.OTP_SUBJECT);

            // Build email content using Thymeleaf template
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("expiryMinutes", "5 Minutes");

            String htmlContent = templateEngine.process("otp-template", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            
            log.info("[GmailNotificationService] :: sendOTP :: OTP email sent successfully to {}", emailID);

        } catch (Exception ex) {
            log.error("[GmailNotificationService] :: sendOTP :: Error sending OTP to {} :: {}", 
                    emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send OTP email to " + emailID + " via Gmail", ex);
        }
    }

    @Override
    public void send(OnboardingRequestBody onboardingRequestBody) {
        String emailID = onboardingRequestBody.getEmailID();
        String fullName = onboardingRequestBody.getFullName();
        
        log.info("[GmailNotificationService] :: sendWelcome :: Preparing welcome email for {}", emailID);
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailID);
            helper.setSubject(MailSubjectConstant.WELCOME_SUBJECT);

            // Build email content using Thymeleaf template
            Context context = new Context();
            context.setVariable("fullName", fullName);
            
            String htmlContent = templateEngine.process("onboarding-template", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            
            log.info("[GmailNotificationService] :: sendWelcome :: Welcome email sent successfully to {}", emailID);

        } catch (Exception ex) {
            log.error("[GmailNotificationService] :: sendWelcome :: Error sending welcome email to {} :: {}", 
                    emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send welcome email to " + emailID + " via Gmail", ex);
        }
    }
}
