package com.SwitchBoard.NotificationService.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailSendExceptionTest {

    @Test
    void constructor_WithMessageAndCause_CreatesException() {
        // Arrange
        String message = "Failed to send email";
        Throwable cause = new RuntimeException("SMTP error");

        // Act
        EmailSendException exception = new EmailSendException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exception_IsRuntimeException() {
        // Arrange
        EmailSendException exception = new EmailSendException("Test", new Exception());

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithNullCause_CreatesException() {
        // Act
        EmailSendException exception = new EmailSendException("Test message", null);

        // Assert
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void getCause_ReturnsOriginalException() {
        // Arrange
        RuntimeException originalException = new RuntimeException("Original error");
        EmailSendException exception = new EmailSendException("Wrapped error", originalException);

        // Act
        Throwable cause = exception.getCause();

        // Assert
        assertSame(originalException, cause);
        assertEquals("Original error", cause.getMessage());
    }
}
