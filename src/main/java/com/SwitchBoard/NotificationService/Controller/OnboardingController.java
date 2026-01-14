package com.SwitchBoard.NotificationService.Controller;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.DTO.OnboardingRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import com.SwitchBoard.NotificationService.Service.OnboaringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/notification/onboarding")
@Tag(name = "Onboarding  Notification", description = "APIs for sending welcome message via email")
public class OnboardingController {
    private final OnboaringService onboaringService;

    @PostMapping("/send")
    @Operation(
            summary = "Send Onboarding Email",
            description = "Sends welcome message to the user's registered email."
    )
    public ResponseEntity<ApiResponse> sendOnboardingMail(@RequestBody OnboardingRequestBody onboardingRequestBody) {
        log.info("[OnboardingController] :: sendOnboardingMail :: Request received for email: {}", onboardingRequestBody.getEmailID());
        try {
            ApiResponse response = onboaringService.sendOnboardingMail(onboardingRequestBody);
            log.info("[OnboardingController] :: sendOnboardingMail :: Onboarding email sent successfully to {}", onboardingRequestBody.getEmailID());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.info("[OnboardingController] :: sendOnboardingMail :: Failed to send onboarding email to {} :: Error: {}", onboardingRequestBody.getEmailID(), ex.getMessage(), ex);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to send Onboarding Mail: " + ex.getMessage(),"500" ,null));
        }
    }
}
