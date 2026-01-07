package com.example.course.request;

import com.example.course.enums.StatusEnum;
import lombok.Data;
import java.time.OffsetDateTime;

import java.util.List;

@Data
public class EnrollmentRequest {

    List<Enrollment> enrollments;

    @Data
    public static class Enrollment {
        String learnerId;

        StatusEnum status;

        OffsetDateTime enrolledAt;
    }
}
