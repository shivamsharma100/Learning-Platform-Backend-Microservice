package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.entities.Enrollment;
import com.example.course.entities.Lesson;
import com.example.course.repositories.CourseRepository;
import com.example.course.repositories.EnrollmentRepositories;
import com.example.course.request.EnrollmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepositories enrollmentRepositories;
    private final CourseRepository courseRepository;

    public List<Enrollment> addEnrollments(String courseId, EnrollmentRequest enrollmentRequest){
        Course course  = courseRepository.findById(Long.parseLong(courseId)).orElseThrow(()->{
            throw new RuntimeException("Invalid course id provided");
        });
        List<Enrollment> enrollments = enrollmentRequest.getEnrollments().stream().map(enrollment -> Enrollment.builder().learnerId(Long.valueOf(enrollment.getLearnerId()))
                .status(enrollment.getStatus().name())
                .enrolledAt(OffsetDateTime.now())
                .course(course)
                .build()

        ).collect(Collectors.toList());
        return enrollmentRepositories.saveAll(enrollments);
    }

    public Enrollment getEnrollmentById(String courseId, String learnerId){
        Course course  = courseRepository.findById(Long.parseLong(courseId)).orElseThrow(()->{
            throw new RuntimeException("Invalid course id provided");
        });

        return enrollmentRepositories.findByCouseAndLearnerId(course, learnerId);
    }
}
