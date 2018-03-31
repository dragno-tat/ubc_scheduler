package com.dragno.rest.service.model;

public class SchedulingError {

    private String message;

    public SchedulingError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
