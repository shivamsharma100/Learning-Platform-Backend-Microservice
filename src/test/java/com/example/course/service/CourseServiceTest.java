package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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
    void getCourses_whenCoursesExist_shouldReturnPage() {
        // Create a Page with 1 sample course
        Page<Course> page = new PageImpl<>(List.of(sampleCourse));
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Course> result = courseService.getCourses(0, 10); // page=0, size=10

        assertEquals(1, result.getContent().size());
        assertEquals(sampleCourse, result.getContent().get(0));
        verify(courseRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getCourses_whenNoCourses_shouldThrowException() {
        // Empty page
        Page<Course> emptyPage = new PageImpl<>(Collections.emptyList());
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourses(0, 10));

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