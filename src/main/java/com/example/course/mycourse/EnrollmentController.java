package com.example.course.mycourse;

import com.example.course.entities.Enrollment;
import com.example.course.exception.JsonSchemaValidationException;
import com.example.course.request.EnrollmentRequest;
import com.example.course.schemaValidator.JsonSchemaValidator;
import com.example.course.service.EnrollmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
@Tag(name = "Enrollment Controller", description = "Operations related to Enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final JsonSchemaValidator jsonSchemaValidator;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Add new Enrollments for a course")
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<List<Enrollment>> addEnrollments(@Valid @RequestBody String rawJson,
                                                           @PathVariable String courseId) throws JsonProcessingException {


        if(!jsonSchemaValidator.validate(rawJson)){
            throw new JsonSchemaValidationException("Invalid Json", null);
        }
        objectMapper.registerModule(new JavaTimeModule());
        EnrollmentRequest enrollmentRequest =  objectMapper.readValue(rawJson, EnrollmentRequest.class);
        return ResponseEntity.ok(enrollmentService.addEnrollments(courseId, enrollmentRequest));
    }

    @PreAuthorize("hasAnyRole('LEARNER', 'ADMIN')")
    @Operation(summary = "Get Enrollments based on learnerId and courseId for a course")
    @GetMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<Enrollment> getEnrollments(@RequestParam String learnerID,
                                                     @PathVariable String courseId) {

        Enrollment enrollments = enrollmentService.getEnrollmentById(courseId, learnerID);
        return ResponseEntity.ok(enrollments);
    }
}
