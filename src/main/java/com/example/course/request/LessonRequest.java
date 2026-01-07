package com.example.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest{

    public List<Lesson> lessons;

    @Data
    public static class Lesson {

        String title;

        String contentUrl;

        String orderNo;
    }

}
