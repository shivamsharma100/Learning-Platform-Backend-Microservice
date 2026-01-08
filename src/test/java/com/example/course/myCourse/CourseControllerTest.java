package com.example.course.myCourse;

import com.example.course.entities.Course;
import com.example.course.mycourse.CourseController;
import com.example.course.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void addCourse_withAdminRole_shouldReturnOk() throws Exception {
        Mockito.when(courseService.addCourses(any()))
                .thenReturn(List.of(new Course()));

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"title\":\"Spring\",\"level\":\"BEGINNER\",\"duration\":10,\"status\":\"ACTIVE\"}]"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCourse_withAdminRole_shouldReturnOk() throws Exception {
        Mockito.when(courseService.updateCourse(any(), any()))
                .thenReturn("Updated Successfully");

        mockMvc.perform(put("/api/courses/1")
                        .param("description", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"title\":\"Spring\",\"level\":\"BEGINNER\",\"duration\":10,\"status\":\"ACTIVE\"}]"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourses_withLearnerRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/courses/course/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllCourses_withLearnerRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk());
    }

}
