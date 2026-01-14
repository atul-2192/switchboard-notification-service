package com.SwitchBoard.NotificationService.Service.impl;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Service.Notification.NotificationSender;
import com.SwitchBoard.NotificationService.Service.OnboaringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboaringService {

    private final NotificationSender notificationSender;

    @Override
    public ApiResponse sendOnboardingMail(OnboardingRequestBody onboardingRequestBody) {
        log.info( "OnboardingServiceImpl :: sendOnboardingMail :: Sending onboarding email to: {}", onboardingRequestBody.getEmailID());
        try {
            notificationSender.send(onboardingRequestBody);
            log.info("OnboardingServiceImpl :: sendOnboardingMail :: Successfully sent onboarding email to: {}", onboardingRequestBody.getEmailID());
            return ApiResponse.success("Onboarding email sent successfully to " + onboardingRequestBody.getEmailID(), true);
        } catch (Exception ex) {
            log.error("OnboardingServiceImpl :: sendOnboardingMail :: Failed to send onboarding email to: {} :: Error: {}", onboardingRequestBody.getEmailID(), ex.getMessage(), ex);
            return ApiResponse.error("Unable to send onboarding email to " + onboardingRequestBody.getEmailID(),"500", null);
        }
    }
}
