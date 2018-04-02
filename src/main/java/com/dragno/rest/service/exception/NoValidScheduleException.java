package com.dragno.rest.service.exception;

/**
 * Created by Anthony on 7/9/2017.
 */
public class NoValidScheduleException extends RuntimeException {

    public NoValidScheduleException() {
        this("No valid schedule is possible for the criteria");
    }

    public NoValidScheduleException(String message) {
        super(message);
    }
}
