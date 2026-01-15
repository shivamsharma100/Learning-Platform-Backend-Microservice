package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.entities.Enrollment;
import com.example.course.exception.EnrollmentNotAllowed;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import com.example.course.repositories.EnrollmentRepositories;
import com.example.course.request.EnrollmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.course.AppConstants.MAX_ENROLLMENTS;
import static com.example.course.util.CourseUtil.checkIfCourseIsAvailable;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepositories enrollmentRepositories;
    private final CourseRepository courseRepository;

    public List<Enrollment> addEnrollments(String courseId, EnrollmentRequest enrollmentRequest){
        Course course  = courseRepository.findById(Integer.parseInt(courseId)).orElseThrow(()->{
            throw new ResourceNotFoundException("Invalid course id provided "+ courseId);
        });
        if(checkIfCourseIsAvailable(course)){
            log.info("Course is not open for enrollments");
            throw new EnrollmentNotAllowed("Enrollment in this course is not possible at this time");
        }
        List<Enrollment> enrollments = enrollmentRequest.getEnrollments().stream().map(enrollment -> Enrollment.builder().learnerId(Integer.parseInt(enrollment.getLearnerId()))
                .status(enrollment.getStatus().name())
                .enrolledAt(OffsetDateTime.now())
                .course(course)
                .build()

        ).collect(Collectors.toList());
        Integer countOfEnrollments = countEnrollments(course);
        if(countOfEnrollments+1==MAX_ENROLLMENTS){
            throw new EnrollmentNotAllowed("Enrollments size is greater than maximum limit allowed");
        }
        else if(countOfEnrollments>=2){
            throw new EnrollmentNotAllowed("Maximum limit of enrollment reached, please wait for new batch");
        }
        return enrollmentRepositories.saveAll(enrollments);
    }

    public Enrollment getEnrollmentById(String courseId, String learnerId){
        Course course  = courseRepository.findById(Integer.parseInt(courseId)).orElseThrow(()->{
            throw new ResourceNotFoundException("Invalid course id provided "+ courseId);
        });

        return enrollmentRepositories.findByCourseAndLearnerId(course, Integer.parseInt(learnerId));
    }

    private Integer countEnrollments(Course courseId){
        return enrollmentRepositories.getByCourseId(courseId);
    }
}
