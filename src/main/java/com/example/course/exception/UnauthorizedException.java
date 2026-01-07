package com.example.course.exception;

public class UnauthorizedException extends RuntimeException{
    UnauthorizedException(String message){super(message);}
}
