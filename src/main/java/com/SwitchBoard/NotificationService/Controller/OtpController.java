package com.SwitchBoard.NotificationService.Controller;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Service.OTPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/v1/notification/otp")
@Tag(name = "OTP Notification", description = "APIs for sending OTP via email")
public class OtpController {

    private final OTPService otpService;

    @PostMapping("/send")
    @Operation(
            summary = "Send OTP Email",
            description = "Sends a one-time password (OTP) to the user's registered email."
    )
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody OTPRequestBody otpRequestBody) {
        log.info("[OtpController] :: sendOTP :: Request received for email: {}", otpRequestBody.getEmailID());

        try {
            ApiResponse response = otpService.sendOTP(otpRequestBody);
            log.info("[OtpController] :: sendOTP :: OTP sent successfully to {}", otpRequestBody.getEmailID());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("[OtpController] :: sendOTP :: Failed to send OTP to {} :: Error: {}", otpRequestBody.getEmailID(), ex.getMessage(), ex);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to send OTP: " + ex.getMessage(),"500" ,null));
        }
    }
}