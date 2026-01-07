package com.example.course.service;

import com.example.course.entities.Course;
import com.example.course.entities.Lesson;
import com.example.course.exception.ResourceNotFoundException;
import com.example.course.repositories.CourseRepository;
import com.example.course.repositories.LessonRepository;
import com.example.course.request.LessonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public Boolean addLessons(LessonRequest lessonRequest, String courseId){
        Course course  = courseRepository.findById(Long.parseLong(courseId)).orElseThrow(()->{
            throw new ResourceNotFoundException("Invalid course id provided"+ courseId);
        });
        List<Lesson> lessons = lessonRequest.getLessons().stream().map(lesson -> Lesson.builder().title(lesson.getTitle())
                .orderNo(Integer.parseInt(lesson.getOrderNo()))
                .contentUrl(lesson.getContentUrl())
                .course(course)
                .build()

        ).collect(Collectors.toList());
        lessonRepository.saveAll(lessons);
        return true;

    }

    public List<Lesson> getLessons(String courseId){
        Course course  = courseRepository.findById(Long.parseLong(courseId)).orElseThrow(()->{
            throw new ResourceNotFoundException("Invalid course id provided"+ courseId);
        });

        return lessonRepository.findByCourse(course).orElseThrow(()->{
            throw new ResourceNotFoundException("Lesson not available for this course id"+ courseId);
        });
    }


}
