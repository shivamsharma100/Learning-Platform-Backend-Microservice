package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.entities.Enrollment;
import com.example.course.enums.StatusEnum;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import com.example.course.repositories.EnrollmentRepositories;
import com.example.course.request.EnrollmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepositories enrollmentRepositories;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = Course.builder()
                .id(1)
                .title("Test Course")
                .status("AVAILABLE")
                .build();
    }

    /* ==================== addEnrollments ==================== */

    @Test
    void addEnrollments_validCourse_shouldReturnEnrollments() {
        // Arrange
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));

        EnrollmentRequest.Enrollment info = new EnrollmentRequest.Enrollment();
                info.setLearnerId("10");
        info.setStatus(StatusEnum.AVAILABLE);

        EnrollmentRequest request = new EnrollmentRequest();
        request.setEnrollments(List.of(info));

        Enrollment enrollment = Enrollment.builder()
                .learnerId(10)
                .status("AVAILABLE")
                .course(sampleCourse)
                .enrolledAt(OffsetDateTime.now())
                .build();

        when(enrollmentRepositories.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<Enrollment> result = enrollmentService.addEnrollments("1", request);

        // Assert
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getLearnerId());
        verify(enrollmentRepositories, times(1)).saveAll(anyList());
    }

    @Test
    void addEnrollments_invalidCourse_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        EnrollmentRequest request = new EnrollmentRequest();
        request.setEnrollments(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.addEnrollments("99", request));

        assertTrue(exception.getMessage().contains("Invalid course id provided 99"));
        verify(enrollmentRepositories, never()).saveAll(anyList());
    }

    /* ==================== getEnrollmentById ==================== */

    @Test
    void getEnrollmentById_validCourse_shouldReturnEnrollment() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));

        Enrollment enrollment = Enrollment.builder()
                .learnerId(10)
                .status("AVAILABLE")
                .course(sampleCourse)
                .enrolledAt(OffsetDateTime.now())
                .build();

        when(enrollmentRepositories.findByCouseAndLearnerId(sampleCourse, "10")).thenReturn(enrollment);

        Enrollment result = enrollmentService.getEnrollmentById("1", "10");

        assertNotNull(result);
        assertEquals(10, result.getLearnerId());
        verify(enrollmentRepositories, times(1)).findByCouseAndLearnerId(sampleCourse, "10");
    }

    @Test
    void getEnrollmentById_invalidCourse_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.getEnrollmentById("99", "10"));

        assertTrue(exception.getMessage().contains("Invalid course id provided 99"));
        verify(enrollmentRepositories, never()).findByCouseAndLearnerId(any(), anyString());
    }
}
