package com.example.course.myCourse;

import com.example.course.mycourse.EnrollmentController;
import com.example.course.request.EnrollmentRequest;
import com.example.course.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    void addEnrollments_withAdminRole_shouldReturnOk() throws Exception {
        EnrollmentRequest request = new EnrollmentRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/enrollment/courses/" + 1 + "/enrollments")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getEnrollments_withAdminRole_shouldReturnOk() throws Exception {
        EnrollmentRequest request = new EnrollmentRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(get("/api/enrollment/courses/" + 1 + "/enrollments")
                        .header("Authorization", "Bearer admin-token")
                        .param("learnerID","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}
