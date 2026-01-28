package com.example.course.request;

import com.example.course.enums.StatusEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.OffsetDateTime;

import java.util.List;

@Data
public class EnrollmentRequest {

    @NotEmpty(message = "Lessons list cannot be empty")
    @jakarta.validation.Valid
    List<Enrollment> enrollments;

    @Data
    public static class Enrollment {
        @NotBlank(message = "learner ID cannot be empty or blank")
        String learnerId;

        StatusEnum status;
        @NotNull
        @FutureOrPresent(message = "Enrollment date must be today or future")
        OffsetDateTime enrolledAt;
    }
}
