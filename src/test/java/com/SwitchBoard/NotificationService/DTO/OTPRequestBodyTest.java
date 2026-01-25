package com.SwitchBoard.NotificationService.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OTPRequestBodyTest {

    @Test
    void noArgsConstructor_CreatesEmptyObject() {
        // Act
        OTPRequestBody otpRequest = new OTPRequestBody();

        // Assert
        assertNotNull(otpRequest);
        assertNull(otpRequest.getEmailID());
        assertNull(otpRequest.getOtp());
    }

    @Test
    void allArgsConstructor_CreatesObjectWithAllFields() {
        // Act
        OTPRequestBody otpRequest = new OTPRequestBody("test@example.com", "123456");

        // Assert
        assertEquals("test@example.com", otpRequest.getEmailID());
        assertEquals("123456", otpRequest.getOtp());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        OTPRequestBody otpRequest = new OTPRequestBody();

        // Act
        otpRequest.setEmailID("user@test.com");
        otpRequest.setOtp("987654");

        // Assert
        assertEquals("user@test.com", otpRequest.getEmailID());
        assertEquals("987654", otpRequest.getOtp());
    }

    @Test
    void setEmailID_AcceptsNullValue() {
        // Arrange
        OTPRequestBody otpRequest = new OTPRequestBody("test@example.com", "123456");

        // Act
        otpRequest.setEmailID(null);

        // Assert
        assertNull(otpRequest.getEmailID());
        assertEquals("123456", otpRequest.getOtp());
    }

    @Test
    void setOtp_AcceptsEmptyString() {
        // Arrange
        OTPRequestBody otpRequest = new OTPRequestBody();

        // Act
        otpRequest.setOtp("");

        // Assert
        assertEquals("", otpRequest.getOtp());
    }

    @Test
    void equals_SameValues_ReturnsTrue() {
        // Arrange
        OTPRequestBody otpRequest1 = new OTPRequestBody("test@example.com", "123456");
        OTPRequestBody otpRequest2 = new OTPRequestBody("test@example.com", "123456");

        // Assert
        assertEquals(otpRequest1, otpRequest2);
    }

    @Test
    void hashCode_SameValues_ReturnsSameHashCode() {
        // Arrange
        OTPRequestBody otpRequest1 = new OTPRequestBody("test@example.com", "123456");
        OTPRequestBody otpRequest2 = new OTPRequestBody("test@example.com", "123456");

        // Assert
        assertEquals(otpRequest1.hashCode(), otpRequest2.hashCode());
    }

    @Test
    void toString_ReturnsStringRepresentation() {
        // Arrange
        OTPRequestBody otpRequest = new OTPRequestBody("test@example.com", "123456");

        // Act
        String result = otpRequest.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("123456"));
    }
}
