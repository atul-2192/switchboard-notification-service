package com.SwitchBoard.NotificationService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingRequestBody {
    private String emailID;
    private String fullName;
}
