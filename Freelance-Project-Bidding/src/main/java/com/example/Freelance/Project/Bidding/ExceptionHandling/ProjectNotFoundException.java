package com.example.Freelance.Project.Bidding.ExceptionHandling;


public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
