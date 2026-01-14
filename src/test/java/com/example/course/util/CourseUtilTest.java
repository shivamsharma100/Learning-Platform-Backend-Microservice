package com.example.course.util;

import com.example.course.entities.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CourseUtilTest {

    @Test
    void testCheckIfCourseIsAvailable() {
        Course course = new Course();
        course.setStatus("Not_Available");
        assertTrue(CourseUtil.checkIfCourseIsAvailable(course));
    }
}
