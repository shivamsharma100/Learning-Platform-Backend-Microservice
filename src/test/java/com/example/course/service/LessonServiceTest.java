package com.example.course.service;


import com.example.course.entities.Course;
import com.example.course.entities.Lesson;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import com.example.course.repositories.LessonRepository;
import com.example.course.request.LessonRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    LessonService lessonService;

    @Mock
    LessonRepository lessonRepository;

    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = Course.builder()
                .id(1)
                .title("Test Course")
                .status("AVAILABLE")
                .build();
    }

    @Test
    void addLessons_validCourse_shouldReturnTrue() {
        // Arrange
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));

        LessonRequest.Lesson lessonInfo = new LessonRequest.Lesson();
        lessonInfo.setTitle("Intro");
        lessonInfo.setOrderNo("1");
        lessonInfo.setContentUrl("http://video");


        LessonRequest request = new LessonRequest();
        request.setLessons(List.of(lessonInfo));

        when(lessonRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        Boolean result = lessonService.addLessons(request, "1");

        // Assert
        assertTrue(result);
        verify(lessonRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addLessons_invalidCourse_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        LessonRequest request = new LessonRequest();
        request.setLessons(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.addLessons(request, "99"));

        assertTrue(exception.getMessage().contains("Invalid course id provided99"));
        verify(lessonRepository, never()).saveAll(anyList());
    }

    @Test
    void getLessons_validCourse_shouldReturnLessons() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));

        Lesson lesson = Lesson.builder()
                .title("Intro")
                .orderNo(1)
                .contentUrl("http://video")
                .course(sampleCourse)
                .build();

        when(lessonRepository.findByCourse(sampleCourse)).thenReturn(Optional.of(List.of(lesson)));

        List<Lesson> result = lessonService.getLessons("1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Intro", result.get(0).getTitle());
        verify(lessonRepository, times(1)).findByCourse(sampleCourse);
    }

    @Test
    void getLessons_invalidCourse_shouldThrowException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.getLessons("99"));

        assertTrue(exception.getMessage().contains("Invalid course id provided99"));
        verify(lessonRepository, never()).findByCourse(any());
    }

    @Test
    void getLessons_noLessonsForCourse_shouldThrowException() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));
        when(lessonRepository.findByCourse(sampleCourse)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.getLessons("1"));

        assertTrue(exception.getMessage().contains("Lesson not available for this course id1"));
        verify(lessonRepository, times(1)).findByCourse(sampleCourse);
    }
}
