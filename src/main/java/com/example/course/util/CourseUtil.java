package com.example.course.util;

import com.example.course.entities.Course;

import static com.example.course.AppConstants.COURSE_AVAILABILITY;

public class CourseUtil {

    public static boolean checkIfCourseIsAvailable(Course course){
        return !course.getStatus().equalsIgnoreCase(COURSE_AVAILABILITY);
    }
}
