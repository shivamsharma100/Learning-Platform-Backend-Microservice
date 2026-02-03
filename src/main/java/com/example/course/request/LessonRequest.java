package com.example.course.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest{

    @NotEmpty(message = "Lessons list cannot be empty")
    @jakarta.validation.Valid
    public List<Lesson> lessons;

    @Data
    public static class Lesson {

        @NotBlank(message = "Title cannot be blank")
        String title;

        @NotBlank(message = "content URL cannot be blank")
        String contentUrl;

        @NotBlank(message = "Order Number cannot be blank")
        String orderNo;
    }

}
