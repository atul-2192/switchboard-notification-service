package com.SwitchBoard.NotificationService.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequestBody {
    private String emailID;
    private String otp;
}
