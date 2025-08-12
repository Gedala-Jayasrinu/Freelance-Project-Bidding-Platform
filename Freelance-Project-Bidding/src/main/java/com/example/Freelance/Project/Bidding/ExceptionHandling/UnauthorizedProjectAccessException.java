package com.example.Freelance.Project.Bidding.ExceptionHandling;



public class UnauthorizedProjectAccessException extends RuntimeException {
    public UnauthorizedProjectAccessException(String message) {
        super(message);
    }
}
