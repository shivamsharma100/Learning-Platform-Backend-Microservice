package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> addCourses(List<Course> courses) {
        return courseRepository.saveAll(courses);
    }

    public Course getCourse(String courseId){
        Optional<Course> course = Optional.ofNullable(courseRepository.findById(Long.parseLong(courseId)).orElseThrow(() -> new RuntimeException("Course not present with id" + courseId)));
       return course.orElse(null);
    }

    public List<Course> getCourses(){
        Optional<List<Course>> courses = Optional.of(courseRepository.findAll());
        return courses.orElse(null);
    }

    public String updateCourse(String courseId, String description){
        courseRepository.findById(Long.parseLong(courseId)).ifPresentOrElse(
                course -> {
                    course.setDescription(description);
                    courseRepository.save(course);
                },()->{
                    throw new RuntimeException("Course not found for courseId"+ courseId);
                }

        );
        return "course has been updated successfully";
    }
}
