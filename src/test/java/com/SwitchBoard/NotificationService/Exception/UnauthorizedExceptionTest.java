package com.SwitchBoard.NotificationService.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void constructor_WithMessage_CreatesException() {
        // Arrange
        String message = "Unauthorized access";

        // Act
        UnauthorizedException exception = new UnauthorizedException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_IsRuntimeException() {
        // Arrange
        UnauthorizedException exception = new UnauthorizedException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
