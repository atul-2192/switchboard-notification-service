package com.SwitchBoard.NotificationService.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceFilterTest {

    @InjectMocks
    private TraceFilter traceFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_WithExistingTraceId_UsesThatTraceId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn("existing-trace-id");
        when(request.getHeader("X-Correlation-Id")).thenReturn("existing-correlation-id");

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        // MDC should be cleared after filter execution
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void doFilterInternal_WithoutTraceId_GeneratesNewTraceId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn(null);
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        // MDC should be cleared after filter execution
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void doFilterInternal_WithOnlyTraceId_GeneratesCorrelationId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn("trace-123");
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("X-Trace-Id");
        verify(request, times(1)).getHeader("X-Correlation-Id");
    }

    @Test
    void doFilterInternal_WithOnlyCorrelationId_GeneratesTraceId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn(null);
        when(request.getHeader("X-Correlation-Id")).thenReturn("correlation-123");

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, times(1)).getHeader("X-Trace-Id");
        verify(request, times(1)).getHeader("X-Correlation-Id");
    }

    @Test
    void doFilterInternal_ClearsMDC_AfterFilterChain() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn("trace-123");
        when(request.getHeader("X-Correlation-Id")).thenReturn("correlation-123");

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void doFilterInternal_ClearsMDC_EvenWhenExceptionOccurs() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn("trace-123");
        when(request.getHeader("X-Correlation-Id")).thenReturn("correlation-123");
        doThrow(new ServletException("Filter chain error"))
                .when(filterChain).doFilter(request, response);

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            traceFilter.doFilterInternal(request, response, filterChain);
        });

        // MDC should still be cleared
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void doFilterInternal_InvokesFilterChain() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Trace-Id")).thenReturn("trace-123");
        when(request.getHeader("X-Correlation-Id")).thenReturn("correlation-123");

        // Act
        traceFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
