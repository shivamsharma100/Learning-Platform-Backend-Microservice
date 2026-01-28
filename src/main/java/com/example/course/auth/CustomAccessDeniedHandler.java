package com.example.course.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String ERROR_MESSAGE = "{\"error\": \"Access denied: insufficient permissions!\"}";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Set 403 Forbidden status
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON response
        try (PrintWriter writer = response.getWriter()) {
            writer.write(ERROR_MESSAGE);
            writer.flush();
        }
    }
}
