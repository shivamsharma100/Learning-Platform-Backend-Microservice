package com.example.course.schemaValidator;

import com.example.course.exception.JsonSchemaValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JsonSchemaValidatorTest {

    private JsonSchemaValidator validator;

    @BeforeEach
    void setUp() throws Exception {
        validator = new JsonSchemaValidator();
    }

    @Test
    void shouldReturnTrueForValidJson() throws JsonProcessingException {
        String validJson = """
            {
              "enrollments": [
                {
                  "learnerId": "123",
                  "status": "available",
                  "enrolledAt": "2026-01-27T10:15:30Z"
                }
              ]
            }
            """;

        boolean result = validator.validate(validJson);

        assertThat(result).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldMissing() {
        String invalidJson = """
            {
              "enrollments": [
                {
                  "status": "ACTIVE",
                  "enrolledAt": "2025-01-27T10:15:30Z"
                }
              ]
            }
            """;

        assertThatThrownBy(() -> validator.validate(invalidJson))
                .isInstanceOf(JsonSchemaValidationException.class);
    }

    @Test
    void shouldThrowExceptionWhenExtraFieldPresent() {
        String invalidJson = """
            {
              "enrollments": [
                {
                  "learnerId": "learner-123",
                  "enrolledAt": "2025-01-27T10:15:30Z",
                  "extraField": "notAllowed"
                }
              ]
            }
            """;

        assertThatThrownBy(() -> validator.validate(invalidJson))
                .isInstanceOf(JsonSchemaValidationException.class);
    }

    @Test
    void shouldThrowExceptionForInvalidEnumValue() {
        String invalidJson = """
            {
              "enrollments": [
                {
                  "learnerId": "learner-123",
                  "status": "UNKNOWN",
                  "enrolledAt": "2025-01-27T10:15:30Z"
                }
              ]
            }
            """;

        assertThatThrownBy(() -> validator.validate(invalidJson))
                .isInstanceOf(JsonSchemaValidationException.class);
    }

    @Test
    void shouldThrowExceptionForInvalidDateFormat() {
        String invalidJson = """
            {
              "enrollments": [
                {
                  "learnerId": "learner-123",
                  "enrolledAt": "27-01-2025"
                }
              ]
            }
            """;

        assertThatThrownBy(() -> validator.validate(invalidJson))
                .isInstanceOf(JsonSchemaValidationException.class);
    }

    @Test
    void shouldThrowExceptionForCompletelyInvalidJson() {
        String invalidJson = """
            EnrollmentRequest
            """;

        assertThatThrownBy(() -> validator.validate(invalidJson))
                .isInstanceOf(JsonSchemaValidationException.class);
    }
}
