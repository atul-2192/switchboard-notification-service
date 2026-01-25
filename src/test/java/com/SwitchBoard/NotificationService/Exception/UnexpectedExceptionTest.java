package com.SwitchBoard.NotificationService.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnexpectedExceptionTest {

    @Test
    void constructor_WithMessage_CreatesException() {
        // Arrange
        String message = "Unexpected error occurred";

        // Act
        UnexpectedException exception = new UnexpectedException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_IsRuntimeException() {
        // Arrange
        UnexpectedException exception = new UnexpectedException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
