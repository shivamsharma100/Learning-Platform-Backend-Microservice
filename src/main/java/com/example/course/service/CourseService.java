package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> addCourses(List<Course> courses) {
        return courseRepository.saveAll(courses);
    }
}
