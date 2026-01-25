package com.SwitchBoard.NotificationService.Exception;

import com.SwitchBoard.NotificationService.DTO.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // Empty - remove unnecessary stubbing
    }

    @Test
    void handleResourceNotFound_ReturnsNotFoundResponse() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("User not found");
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleBadRequest_ReturnsBadRequestResponse() {
        // Arrange
        BadRequestException exception = new BadRequestException("Invalid input data");
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleBadRequest(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid input data", response.getBody().getMessage());
        assertEquals("BAD_REQUEST", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleUnauthorized_ReturnsUnauthorizedResponse() {
        // Arrange
        UnauthorizedException exception = new UnauthorizedException("Access denied");
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleUnauthorized(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Access denied", response.getBody().getMessage());
        assertEquals("UNAUTHORIZED", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleUnexpected_ReturnsInternalServerErrorResponse() {
        // Arrange
        UnexpectedException exception = new UnexpectedException("Unexpected error occurred");
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleUnexpected(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("UNEXPECTED_ERROR", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleValidation_ReturnsBadRequestResponse() {
        // Arrange
        FieldError fieldError = new FieldError("object", "field", "Field is required");
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleValidation(validationException, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Field is required", response.getBody().getMessage());
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleEmailSendException_ReturnsInternalServerErrorResponse() {
        // Arrange
        EmailSendException exception = new EmailSendException("Failed to send email", new RuntimeException());

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleEmailSendException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Failed to send email", response.getBody().getMessage());
        assertEquals("500: EMAIL_SEND_ERROR", response.getBody().getErrorCode());
        assertNull(response.getBody().getPath());
    }

    @Test
    void handleGlobalException_ReturnsInternalServerErrorResponse() {
        // Arrange
        Exception exception = new Exception("Generic error");
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleGlobalException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Internal Server Error"));
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void allExceptions_VerifyTimestampIsNotNull() {
        // Arrange & Act
        when(request.getRequestURI()).thenReturn("/api/test");
        ResponseEntity<ApiResponse> response1 = globalExceptionHandler.handleResourceNotFound(
                new ResourceNotFoundException("test"), request);
        ResponseEntity<ApiResponse> response2 = globalExceptionHandler.handleBadRequest(
                new BadRequestException("test"), request);
        ResponseEntity<ApiResponse> response3 = globalExceptionHandler.handleUnauthorized(
                new UnauthorizedException("test"), request);

        // Assert
        assertNotNull(response1.getBody().getTimestamp());
        assertNotNull(response2.getBody().getTimestamp());
        assertNotNull(response3.getBody().getTimestamp());
    }
}
