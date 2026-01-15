package com.example.course.request;

import com.example.course.enums.StatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.OffsetDateTime;

import java.util.List;

@Data
public class EnrollmentRequest {

    @Size(max = 2, message = "You can submit at most 2 enrollments")
    List<Enrollment> enrollments;

    @Data
    public static class Enrollment {
        @NotEmpty(message = "learner ID cannot be empty or blank")
        String learnerId;
        @NotEmpty(message = "status cannot be empty or blank")
        StatusEnum status;
        @NotEmpty(message = "enrolledAt time cannot be empty or blank")
        OffsetDateTime enrolledAt;
    }
}
