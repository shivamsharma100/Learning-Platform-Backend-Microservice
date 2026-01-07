package com.example.course.myCourse;

import com.example.course.entities.Enrollment;
import com.example.course.mycourse.EnrollmentController;
import com.example.course.request.EnrollmentRequest;
import com.example.course.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    private EnrollmentController enrollmentController;

    private Enrollment enrollment1;
    private Enrollment enrollment2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enrollmentController = new EnrollmentController(enrollmentService);

        enrollment1 = new Enrollment();
        enrollment1.setId(1L);
        enrollment1.setLearnerId(1L);

        enrollment2 = new Enrollment();
        enrollment2.setId(2L);
        enrollment2.setLearnerId(2L);
    }

    @Test
    void testAddEnrollments_success() {
        EnrollmentRequest request = new EnrollmentRequest();

        when(enrollmentService.addEnrollments("c1", request)).thenReturn(List.of(enrollment1, enrollment2));

        ResponseEntity<List<Enrollment>> response = enrollmentController.addEnrollments(request, "c1");
        verify(enrollmentService, times(1)).addEnrollments("c1", request);
    }

    @Test
    void testGetEnrollments_success() {
        when(enrollmentService.getEnrollmentById("c1", "l1")).thenReturn(enrollment1);

        ResponseEntity<Enrollment> response = enrollmentController.getEnrollments("l1", "c1");

        assertNotNull(response);
        assertEquals(1L, response.getBody().getId());
        assertEquals(1l, response.getBody().getLearnerId());

        verify(enrollmentService, times(1)).getEnrollmentById("c1", "l1");
    }
}
