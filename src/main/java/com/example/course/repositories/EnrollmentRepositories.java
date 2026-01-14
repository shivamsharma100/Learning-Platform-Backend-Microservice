package com.example.course.repositories;

import com.example.course.entities.Course;
import com.example.course.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepositories extends JpaRepository<Enrollment, Integer> {
    Enrollment findByCourseAndLearnerId(Course course, Integer learnerId);
}
