package com.SwitchBoard.NotificationService.Service.Notification;

import com.SwitchBoard.NotificationService.Constants.MailSubjectConstant;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationAdapter implements NotificationSender {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(OTPRequestBody otpRequestBody) {
        String emailID= otpRequestBody.getEmailID();
        String otp= otpRequestBody.getOtp();
        log.info("[EmailNotificationAdapter] :: sendOTP :: Preparing OTP email for {}", emailID);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailID);
            helper.setSubject(MailSubjectConstant.OTP_SUBJECT);

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("expiryMinutes", "5 Minutes" );

            String htmlContent = templateEngine.process("otp-template", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("[EmailNotificationAdapter] :: sendOTP :: OTP email sent to {}", emailID);

        } catch (Exception ex) {
            log.error("[EmailNotificationAdapter] :: sendOTP :: Error sending OTP to {} :: {}", emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send OTP email to " + emailID, ex);
        }
    }

    @Override
    public void send(OnboardingRequestBody onboardingRequestBody) {
        String emailID= onboardingRequestBody.getEmailID();
        String fullName= onboardingRequestBody.getFullName();
        log.info("[EmailNotificationAdapter] :: sendWelcome :: Preparing welcome email for {}", emailID);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailID);
            helper.setSubject(MailSubjectConstant.WELCOME_SUBJECT);

            Context context = new Context();
            context.setVariable("fullName", fullName);
            String htmlContent = templateEngine.process("onboarding-template", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("[EmailNotificationAdapter] :: sendWelcome :: Welcome email sent to {}", emailID);

        } catch (Exception ex) {
            log.error("[EmailNotificationAdapter] :: sendWelcome :: Error sending welcome email to {} :: {}", emailID, ex.getMessage(), ex);
            throw new EmailSendException("Failed to send welcome email to " + emailID, ex);
        }
    }
}