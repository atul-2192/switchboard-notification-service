package com.SwitchBoard.NotificationService.Service.impl;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import com.SwitchBoard.NotificationService.DTO.OTPRequestBody;
import com.SwitchBoard.NotificationService.Exception.EmailSendException;
import com.SwitchBoard.NotificationService.Service.Notification.NotificationSender;
import com.SwitchBoard.NotificationService.Service.OTPService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final NotificationSender notificationSender;

    @Override
    public ApiResponse sendOTP(OTPRequestBody otpRequestBody) {


        log.info("[OTPServiceImpl] :: sendOTP :: Initiating OTP send to {}", otpRequestBody.getEmailID());

        try {
            notificationSender.send(otpRequestBody);
            log.info("[OTPServiceImpl] :: sendOTP :: Successfully sent OTP to {}", otpRequestBody);
            return ApiResponse.success("OTP sent successfully to " + otpRequestBody.getEmailID(), true);

        } catch (Exception ex) {
            log.error("[OTPServiceImpl] :: sendOTP :: Failed to send OTP to {} :: Error: {}", otpRequestBody.getEmailID(), ex.getMessage(), ex);
            throw new EmailSendException("Unable to send OTP to " + otpRequestBody.getEmailID(), ex);
        }
    }
}
