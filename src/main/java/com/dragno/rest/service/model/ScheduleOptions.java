package com.dragno.rest.service.model;

import com.dragno.rest.util.LocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalTime;
import java.util.Set;

public class ScheduleOptions {

    private Set<String> courses;
    private int term;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;
    private Set<Status> excludedStatuses;
    private Set<Day> days;
    private int sessyr;
    private char sesscd;

    @JsonCreator
    public ScheduleOptions(@JsonProperty("courses") Set<String> courses, @JsonProperty("term") int term,
            @JsonProperty("startTime") LocalTime startTime, @JsonProperty("endTime") LocalTime endTime,
            @JsonProperty("excludedStatuses") Set<Status> excludedStatuses, @JsonProperty("days") Set<Day> days,
            @JsonProperty("sessyr") int sessyr, @JsonProperty("sesscd") char sesscd) {
        this.courses = courses;
        this.term = term;
        if(startTime == null) {
            this.startTime = LocalTime.of(Schedule.EARLIEST_START_HOUR, 0);
        } else {
            this.startTime = startTime;
        }
        if(startTime == null) {
            this.startTime = LocalTime.of(Schedule.LATEST_END_HOUR, 0);
        } else {
            this.endTime = endTime;
        }
        this.excludedStatuses = excludedStatuses;
        this.days = days;
        this.sessyr = sessyr;
        this.sesscd = sesscd;
    }

    public Set<String> getCourses() {
        return courses;
    }

    public int getTerm() {
        return term;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Set<Status> getExcludedStatuses() {
        return excludedStatuses;
    }

    public Set<Day> getDays() {
        return days;
    }

    public int getSessyr() {
        return sessyr;
    }

    public char getSesscd() {
        return sesscd;
    }
}
