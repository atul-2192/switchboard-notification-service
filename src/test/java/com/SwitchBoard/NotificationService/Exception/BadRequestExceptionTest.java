package com.SwitchBoard.NotificationService.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void constructor_WithMessage_CreatesException() {
        // Arrange
        String message = "Invalid request data";

        // Act
        BadRequestException exception = new BadRequestException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_IsRuntimeException() {
        // Arrange
        BadRequestException exception = new BadRequestException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithEmptyMessage_CreatesException() {
        // Act
        BadRequestException exception = new BadRequestException("");

        // Assert
        assertEquals("", exception.getMessage());
    }

    @Test
    void constructor_WithNullMessage_CreatesException() {
        // Act
        BadRequestException exception = new BadRequestException(null);

        // Assert
        assertNull(exception.getMessage());
    }
}
