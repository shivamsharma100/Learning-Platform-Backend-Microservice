package com.example.course.repositories;

import com.example.course.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
