package com.dragno.rest.service.model;

/**
 * Created by Anthony on 7/8/2017.
 */
public enum Day {
    MON("Mon"), TUE("Tue"), WED("Wed"), THU("Thu"), FRI("Fri");

    private String day;

    Day(String day) {
        this.day = day;
    }

    public static Day fromDayString(String dayString) throws EnumConstantNotPresentException {
        for(Day day : values()){
            if(day.day.equalsIgnoreCase(dayString)){
                return day;
            }
        }
        throw new EnumConstantNotPresentException(Day.class, dayString);
    }
}
