package com.example.course.myCourse;

import com.example.course.entities.Lesson;
import com.example.course.mycourse.LessonController;
import com.example.course.request.LessonRequest;
import com.example.course.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    private LessonController lessonController;

    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lessonController = new LessonController(lessonService);

        lesson1 = new Lesson();
        lesson1.setId(1);

        lesson2 = new Lesson();
        lesson2.setId(2);
    }

    @Test
    void testAddLessons_success() {
        LessonRequest request = new LessonRequest();

        when(lessonService.addLessons(request, "c1")).thenReturn(true);

        ResponseEntity<Boolean> response = lessonController.addCourse(request, "c1");

        assertNotNull(response);
        assertTrue(response.getBody());

        verify(lessonService, times(1)).addLessons(request, "c1");
    }

    @Test
    void testGetLessonsByCourse_success() {
        when(lessonService.getLessons("c1")).thenReturn(List.of(lesson1, lesson2));

        ResponseEntity<List<Lesson>> response = lessonController.addCourse("c1");

        assertNotNull(response);
        assertEquals(2, response.getBody().size());

        verify(lessonService, times(1)).getLessons("c1");
    }
}
