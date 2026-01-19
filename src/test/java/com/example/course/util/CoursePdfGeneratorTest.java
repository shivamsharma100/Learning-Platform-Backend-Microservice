package com.example.course.util;

import com.example.course.entities.Course;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoursePdfGeneratorTest {

    @Test
    void generateCoursePdf_shouldReturnNonEmptyPdf_whenCoursesPresent() {

        List<Course> courses = List.of(
                Course.builder()
                        .id(1)
                        .title("Java Basics")
                        .description("Intro to Java")
                        .level("Beginner")
                        .duration(10)
                        .build(),
                Course.builder()
                        .id(2)
                        .title("Spring Boot")
                        .description("REST APIs with Spring")
                        .level("Intermediate")
                        .duration(15)
                        .build()
        );

        byte[] pdfBytes = CoursePdfGenerator.generateCoursePdf(courses);

        assertNotNull(pdfBytes, "Generated PDF should not be null");
        assertTrue(pdfBytes.length > 0, "Generated PDF should not be empty");
    }

    @Test
    void generateCoursePdf_shouldGeneratePdfEvenForSingleCourse() {

        List<Course> courses = List.of(
                Course.builder()
                        .id(1)
                        .title("Docker Fundamentals")
                        .description("Containers explained")
                        .level("Beginner")
                        .duration(8)
                        .build()
        );

        byte[] pdfBytes = CoursePdfGenerator.generateCoursePdf(courses);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generateCoursePdf_shouldThrowException_whenCoursesListIsNull() {

        Exception exception = assertThrows(
                NullPointerException.class,
                () -> CoursePdfGenerator.generateCoursePdf(null)
        );

        assertNotNull(exception);
    }
}
