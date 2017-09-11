package com.dragno.exception;

/**
 * Created by Anthony on 7/9/2017.
 */
public class NoValidScheduleException extends RuntimeException {

    public NoValidScheduleException() {
        super();
    }

    public NoValidScheduleException(String message) {
        super(message);
    }
}
