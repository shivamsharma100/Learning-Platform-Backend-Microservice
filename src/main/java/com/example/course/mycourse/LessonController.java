package com.example.course.mycourse;

import com.example.course.entities.Course;
import com.example.course.entities.Lesson;
import com.example.course.request.LessonRequest;
import com.example.course.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
@Tag(name = "Lesson Controller", description = "Operations related to Lessons")
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @Operation(summary = "Add new Lessons for a course")
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Boolean> addCourse(@RequestBody LessonRequest lessonRequests,
                                             @PathVariable String courseId) {
        Boolean response = lessonService.addLessons(lessonRequests, courseId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Add new Lessons for a course")
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> addCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(lessonService.getLessons(courseId));
    }
}
