package com.SwitchBoard.NotificationService.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_WithMessage_CreatesException() {
        // Arrange
        String message = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_IsRuntimeException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
