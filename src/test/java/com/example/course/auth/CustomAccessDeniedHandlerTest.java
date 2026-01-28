package com.example.course.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler handler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        handler = new CustomAccessDeniedHandler();

        // Mock HttpServletRequest and HttpServletResponse
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        // Capture response output
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter, true);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void handle_shouldReturn403WithJsonMessage() throws Exception {
        // Given
        AccessDeniedException exception = new AccessDeniedException("User does not have permission");

        // When
        handler.handle(request, response, exception);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json");

        String expectedJson = "{\"error\": \"Access denied: insufficient permissions!\"}";
        assertEquals(expectedJson, responseWriter.toString().trim());
    }
}
