package com.SwitchBoard.NotificationService.DTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void constructor_WithAllParameters_CreatesApiResponse() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        ApiResponse response = new ApiResponse(true, "Success", "data", null, timestamp, "/api/test");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals("data", response.getData());
        assertNull(response.getErrorCode());
        assertEquals(timestamp, response.getTimestamp());
        assertEquals("/api/test", response.getPath());
    }

    @Test
    void noArgsConstructor_CreatesEmptyApiResponse() {
        // Act
        ApiResponse response = new ApiResponse();

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertNull(response.getErrorCode());
        assertNull(response.getTimestamp());
        assertNull(response.getPath());
    }

    @Test
    void success_WithMessageAndFlag_CreatesSuccessResponse() {
        // Act
        ApiResponse response = ApiResponse.success("Operation successful", true);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertNull(response.getData()); // This method sets data to null
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertNull(response.getPath());
    }

    @Test
    void success_WithMessageDataAndPath_CreatesSuccessResponse() {
        // Act
        ApiResponse response = ApiResponse.success("Data fetched", "testData", "/api/data");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Data fetched", response.getMessage());
        assertEquals("testData", response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertEquals("/api/data", response.getPath());
    }

    @Test
    void error_WithAllParameters_CreatesErrorResponse() {
        // Act
        ApiResponse response = ApiResponse.error("Error occurred", "ERR_001", "/api/error");

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Error occurred", response.getMessage());
        assertNull(response.getData());
        assertEquals("ERR_001", response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertEquals("/api/error", response.getPath());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        ApiResponse response = new ApiResponse();
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData("Test data");
        response.setErrorCode("TEST_ERROR");
        response.setTimestamp(timestamp);
        response.setPath("/test");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("Test data", response.getData());
        assertEquals("TEST_ERROR", response.getErrorCode());
        assertEquals(timestamp, response.getTimestamp());
        assertEquals("/test", response.getPath());
    }

    @Test
    void success_TimestampIsRecentlyCreated() {
        // Arrange
        LocalDateTime before = LocalDateTime.now();

        // Act
        ApiResponse response = ApiResponse.success("Test", true);

        // Assert
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(response.getTimestamp());
        assertTrue(!response.getTimestamp().isBefore(before));
        assertTrue(!response.getTimestamp().isAfter(after));
    }

    @Test
    void error_TimestampIsRecentlyCreated() {
        // Arrange
        LocalDateTime before = LocalDateTime.now();

        // Act
        ApiResponse response = ApiResponse.error("Error", "ERR", "/path");

        // Assert
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(response.getTimestamp());
        assertTrue(!response.getTimestamp().isBefore(before));
        assertTrue(!response.getTimestamp().isAfter(after));
    }
}
