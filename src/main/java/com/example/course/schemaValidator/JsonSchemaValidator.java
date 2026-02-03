package com.example.course.schemaValidator;

import com.example.course.exception.JsonSchemaValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
public class JsonSchemaValidator {

    private final JsonSchema schema;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSchemaValidator() throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        try (InputStream is = getClass()
                .getResourceAsStream("/schema/enrollment-request.schema.json")) {
            this.schema = factory.getSchema(is);
        }
    }

    public boolean validate(String json) throws JsonProcessingException {
        try {
            JsonNode node = objectMapper.readTree(json);
            Set<ValidationMessage> errors = schema.validate(node);
            if (!errors.isEmpty()) {
                throw new JsonSchemaValidationException(errors);
            }
            return true;
        }catch(Exception e){
            throw new JsonSchemaValidationException(e.getMessage(), null);
        }
    }
}

