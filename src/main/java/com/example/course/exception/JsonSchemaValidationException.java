package com.example.course.exception;

import com.networknt.schema.ValidationMessage;

import java.util.Set;

public class JsonSchemaValidationException extends RuntimeException {

    private final Set<ValidationMessage> errors;

    public JsonSchemaValidationException(String message, Set<ValidationMessage> errors){
        super(message);
        this.errors = errors;
    }

    public JsonSchemaValidationException(Set<ValidationMessage> errors) {
        super("JSON Schema validation failed");
        this.errors = errors;
    }

    public Set<ValidationMessage> getErrors() {
        return errors;
    }
}

