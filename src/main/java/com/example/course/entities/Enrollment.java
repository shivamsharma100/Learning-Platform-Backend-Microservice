package com.example.course.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK to Course (owned by this service)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // External reference (NO relationship)
    @Column(name = "learner_id", nullable = false)
    private Integer learnerId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime enrolledAt;
}
