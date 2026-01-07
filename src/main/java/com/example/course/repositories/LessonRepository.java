package com.example.course.repositories;

import com.example.course.entities.Course;
import com.example.course.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<List<Lesson>> findByCourse(Course course);
}
