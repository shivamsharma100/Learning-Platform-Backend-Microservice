package com.example.course.repositories;

import com.example.course.entities.Course;
import com.example.course.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface EnrollmentRepositories extends JpaRepository<Enrollment, Integer> {

    @Query("SELECT e.id, e.course, e.learnerId, e.status, e.enrolledAt FROM Enrollment e " +
            "WHERE e.course = :course AND " +
            " e.learnerId = :learnerId"
    )
    Enrollment findByCouseAndLearnerId(Course course, String learnerId);
}
