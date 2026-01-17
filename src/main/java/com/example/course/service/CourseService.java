package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> addCourses(List<Course> courses) {
        return courseRepository.saveAll(courses);
    }

    public Course getCourse(String courseId) {
        Optional<Course> course = Optional.ofNullable(courseRepository.findById(Integer.parseInt(courseId)).orElseThrow(() -> new ResourceNotFoundException("Course not present with id" + courseId)));
        return course.orElse(null);
    }

    public Page<Course> getCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // page starts from 0
        Page<Course> coursesPage = courseRepository.findAll(pageable);

        if (coursesPage.isEmpty()) {
            throw new ResourceNotFoundException("No courses available at this time");
        }

        return coursesPage;
    }


    public String updateCourse(String courseId, String description) {
        courseRepository.findById(Integer.parseInt(courseId)).ifPresentOrElse(
                course -> {
                    course.setDescription(description);
                    courseRepository.save(course);
                }, () -> {
                    throw new ResourceNotFoundException("Course not found for courseId" + courseId);
                }

        );
        return "course has been updated successfully";
    }
}
