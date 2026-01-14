package com.SwitchBoard.NotificationService.Service.Notification;

import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import org.springframework.scheduling.annotation.Async;

public interface NotificationSender {

    public void send(OTPRequestBody otpRequestBody); // Send OTP for authentication

    public void send(OnboardingRequestBody onboardingRequestBody); // Send Onboarding Notification
}
