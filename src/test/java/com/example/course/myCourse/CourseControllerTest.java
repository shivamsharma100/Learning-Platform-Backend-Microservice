package com.example.course.myCourse;

import com.example.course.entities.Course;
import com.example.course.mycourse.CourseController;
import com.example.course.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    private CourseController courseController;

    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseController = new CourseController(courseService);

        course1 = new Course();
        course1.setId(1);
        course1.setDescription("Desc 1");

        course2 = new Course();
        course2.setId(2);
        course2.setDescription("Desc 2");
    }

    @Test
    void testAddCourse_success() {
        List<Course> courses = List.of(course1, course2);
        when(courseService.addCourses(courses)).thenReturn(courses);

        ResponseEntity<List<Course>> response = courseController.addCourse(courses);
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(courseService, times(1)).addCourses(courses);
    }

    @Test
    void testGetCourseById_success() {
        when(courseService.getCourse("1")).thenReturn(course1);

        ResponseEntity<Course> response = courseController.getCourse("1");

        assertNotNull(response);
        assertEquals(1, response.getBody().getId());
        verify(courseService, times(1)).getCourse("1");
    }

    @Test
    void testGetCourses_success() {
        when(courseService.getCourses()).thenReturn(List.of(course1, course2));

        ResponseEntity<List<Course>> response = courseController.getCourses();
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(courseService, times(1)).getCourses();
    }

    @Test
    void testUpdateCourse_success() {
        when(courseService.updateCourse("1", "New Desc")).thenReturn("Updated");

        ResponseEntity<String> response = courseController.putCourse("1", "New Desc");
        assertNotNull(response);
        assertEquals("Updated", response.getBody());
        verify(courseService, times(1)).updateCourse("1", "New Desc");
    }
}
