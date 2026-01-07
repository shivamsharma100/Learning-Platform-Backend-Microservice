package com.example.course.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){super(message);}
}
