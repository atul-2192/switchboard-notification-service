package com.SwitchBoard.NotificationService.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingRequestBodyTest {

    @Test
    void noArgsConstructor_CreatesEmptyObject() {
        // Act
        OnboardingRequestBody request = new OnboardingRequestBody();

        // Assert
        assertNotNull(request);
        assertNull(request.getEmailID());
        assertNull(request.getFullName());
    }

    @Test
    void allArgsConstructor_CreatesObjectWithAllFields() {
        // Act
        OnboardingRequestBody request = new OnboardingRequestBody("john.doe@example.com", "John Doe");

        // Assert
        assertEquals("john.doe@example.com", request.getEmailID());
        assertEquals("John Doe", request.getFullName());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        OnboardingRequestBody request = new OnboardingRequestBody();

        // Act
        request.setEmailID("jane.smith@example.com");
        request.setFullName("Jane Smith");

        // Assert
        assertEquals("jane.smith@example.com", request.getEmailID());
        assertEquals("Jane Smith", request.getFullName());
    }

    @Test
    void setEmailID_AcceptsNullValue() {
        // Arrange
        OnboardingRequestBody request = new OnboardingRequestBody("test@example.com", "Test User");

        // Act
        request.setEmailID(null);

        // Assert
        assertNull(request.getEmailID());
        assertEquals("Test User", request.getFullName());
    }

    @Test
    void setFullName_AcceptsEmptyString() {
        // Arrange
        OnboardingRequestBody request = new OnboardingRequestBody();

        // Act
        request.setFullName("");

        // Assert
        assertEquals("", request.getFullName());
    }

    @Test
    void equals_SameValues_ReturnsTrue() {
        // Arrange
        OnboardingRequestBody request1 = new OnboardingRequestBody("test@example.com", "Test User");
        OnboardingRequestBody request2 = new OnboardingRequestBody("test@example.com", "Test User");

        // Assert
        assertEquals(request1, request2);
    }

    @Test
    void hashCode_SameValues_ReturnsSameHashCode() {
        // Arrange
        OnboardingRequestBody request1 = new OnboardingRequestBody("test@example.com", "Test User");
        OnboardingRequestBody request2 = new OnboardingRequestBody("test@example.com", "Test User");

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void toString_ReturnsStringRepresentation() {
        // Arrange
        OnboardingRequestBody request = new OnboardingRequestBody("test@example.com", "Test User");

        // Act
        String result = request.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("Test User"));
    }
}
