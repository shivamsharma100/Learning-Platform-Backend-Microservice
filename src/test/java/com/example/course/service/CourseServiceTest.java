package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = Course.builder()
                .id(1)
                .title("Test Course")
                .description("Original Description")
                .level("BEGINNER")
                .duration(10)
                .status("AVAILABLE")
                .build();
    }

    /* ==================== addCourses ==================== */

    @Test
    void addCourses_shouldReturnSavedCourses() {
        List<Course> courses = List.of(sampleCourse);
        when(courseRepository.saveAll(courses)).thenReturn(courses);
        courseService.addCourses(courses);

        verify(courseRepository, times(1)).saveAll(courses);
    }

    /* ==================== getCourse ==================== */

    @Test
    void getCourse_existingId_shouldReturnCourse() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));

        Course result = courseService.getCourse("1");

        assertNotNull(result);
        assertEquals("Test Course", result.getTitle());
    }

    @Test
    void getCourse_nonExistingId_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourse("99"));

        assertTrue(exception.getMessage().contains("Course not present with id99"));
    }

    /* ==================== getCourses ==================== */

    @Test
    void getCourses_whenCoursesExist_shouldReturnList() {
        when(courseRepository.findAll()).thenReturn(List.of(sampleCourse));

        List<Course> result = courseService.getCourses();

        assertEquals(1, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourses_whenNoCourses_shouldThrowException() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourses());

        assertTrue(exception.getMessage().contains("No courses available"));
    }

    /* ==================== updateCourse ==================== */

    @Test
    void updateCourse_existingCourse_shouldUpdateAndReturnMessage() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        String message = courseService.updateCourse("1", "Updated Description");

        assertEquals("course has been updated successfully", message);
        assertEquals("Updated Description", sampleCourse.getDescription());
        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void updateCourse_nonExistingCourse_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.updateCourse("99", "Desc"));

        assertTrue(exception.getMessage().contains("Course not found for courseId99"));
        verify(courseRepository, never()).save(any());
    }
}
