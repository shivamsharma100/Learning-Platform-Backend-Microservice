package com.example.course.Integration;

import com.example.course.entities.Course;
import com.example.course.repositories.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestJwtConfig.class)
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    /* ===================== COURSES ===================== */

    @Test
    void postCourses_withAdminRole_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());
    }

    @Test
    void postCourses_withLearnerRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer learner-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postCourses_withoutJwt_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCourses_withoutJwt_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isForbidden());
    }

    /* ===================== ENROLLMENTS ===================== */

    @Test
    void postEnrollments_withAdminRole_shouldReturnOk() throws Exception {
        // Arrange: create a course
        Course course = Course.builder()
                .title("Test Course")
                .level("BEGINNER")
                .duration(8)
                .status("available")
                .build();

        Course savedCourse = courseRepository.save(course);

        // Act + Assert
        mockMvc.perform(post("/api/enrollment/courses/" + savedCourse.getId() + "/enrollments")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "enrollments": [
                        {
                          "learnerId": "1",
                          "status": "available",
                          "enrolledAt": "2026-01-08T10:15:30Z"
                        }
                      ]
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void postEnrollments_withLearnerRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/enrollment/courses/1/enrollments")
                        .header("Authorization", "Bearer learner-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    /* ===================== LESSONS ===================== */

    @Test
    void addLesson_withInstructorRole_shouldReturnOk() throws Exception {
        Course course = Course.builder()
                .title("Test Course")
                .level("BEGINNER")
                .duration(10)
                .status("ACTIVE")
                .build();

        Course saved = courseRepository.save(course);

        mockMvc.perform(post("/api/lesson/courses/" + saved.getId() + "/lessons")
                        .header("Authorization", "Bearer instructor-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "lessons": [
                        {
                          "title": "Intro",
                          "contentUrl": "http://video",
                          "orderNo": 1
                        }
                      ]
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void addLesson_withAdminRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/lesson/courses/1/lessons")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    /* ===================== PUBLIC ===================== */

    @Test
    void authEndpoint_shouldBePublic() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\": \"shivam.sharma\",\n" +
                                "    \"password\": \"password\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }
}

