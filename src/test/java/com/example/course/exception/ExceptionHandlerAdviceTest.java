package com.example.course.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerAdviceTest {

    private final ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

    @Test
    void handleResourceNotFound_ShouldReturn404() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Course not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiError> response =
                advice.handleResourceNotFound(ex, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Resource Not Found", response.getBody().error());
        assertEquals("Course not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleUnauthorized_ShouldReturn401() {
        UnauthorizedException ex =
                new UnauthorizedException("Unauthorized access");

        ResponseEntity<ApiError> response =
                advice.handleUnauthorized(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().status());
        assertEquals("Unauthorized", response.getBody().error());
        assertEquals("Unauthorized access", response.getBody().message());
    }


    @Test
    void handleForbidden_ShouldReturn403() {
        ForbiddenException ex =
                new ForbiddenException("Forbidden access");

        ResponseEntity<ApiError> response =
                advice.handleForbidden(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().status());
        assertEquals("Forbidden", response.getBody().error());
        assertEquals("Forbidden access", response.getBody().message());
    }

    @Test
    void handleUserNotFound_ShouldReturn404() {
        UserNotFoundException ex =
                new UserNotFoundException("User not found");

        ResponseEntity<ApiError> response =
                advice.handleForbidden(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("User not found", response.getBody().error());
        assertEquals("User not found", response.getBody().message());
    }

    @Test
    void handleIncorrectStatusForEnrollment_ShouldReturn406() {
        IncorrectStatusForEnrollment ex =
                new IncorrectStatusForEnrollment("Invalid status");

        ResponseEntity<ApiError> response =
                advice.handleForbidden(ex);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(406, response.getBody().status());
        assertEquals("Status value not acceptable", response.getBody().error());
        assertEquals("Invalid status", response.getBody().message());
    }

    @Test
    void handleValidationExceptions_ShouldReturn400WithFieldErrors() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "object");

        bindingResult.addError(
                new FieldError("object", "name", "must not be blank"));
        bindingResult.addError(
                new FieldError("object", "email", "must be valid"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiError> response =
                advice.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Validation Error", response.getBody().error());
        assertTrue(response.getBody().message().contains("name: must not be blank"));
        assertTrue(response.getBody().message().contains("email: must be valid"));
    }

    @Test
    void handleIncorrectEnrollment_ShouldReturn503() {
        EnrollmentNotAllowed ex =
                new EnrollmentNotAllowed("Enrollment not allowed in this course");

        ResponseEntity<ApiError> response =
                advice.handleForbidden(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(503, response.getBody().status());
        assertEquals("Service not available", response.getBody().error());
        assertEquals("Enrollment not allowed in this course", response.getBody().message());
    }

}
