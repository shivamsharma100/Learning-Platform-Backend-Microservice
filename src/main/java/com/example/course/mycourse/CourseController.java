package com.example.course.mycourse;

import com.example.course.entities.Course;
import com.example.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Course Controller", description = "Operations related to Courses")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Create new Courses")
    @PostMapping
    public ResponseEntity<List<Course>> addCourse(@RequestBody List<Course>  courses) {
        List<Course> savedCourse = courseService.addCourses(courses);
        return ResponseEntity.ok(savedCourse);
    }
}
