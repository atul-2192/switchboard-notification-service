package com.SwitchBoard.NotificationService.Service;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;

public interface OTPService {

    public ApiResponse sendOTP(OTPRequestBody otpRequestBody);
}
