package com.SwitchBoard.NotificationService.Service;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import switchboard.schemas.OnboardingEvent;

public interface OnboaringService {
    public ApiResponse sendOnboardingMail(OnboardingRequestBody onboardingRequestBody);
}
