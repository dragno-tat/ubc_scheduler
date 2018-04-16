package com.dragno.rest.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class CourseResults {

    @JsonProperty("sessyr")
    private int sessyr;

    @JsonProperty("sesscd")
    private char sesscd;

    @JsonProperty("courses")
    private Set<CourseSection> courses;



    public CourseResults(int sessyr, char sesscd, Set<CourseSection> courses) {
        this.sessyr = sessyr;
        this.sesscd = sesscd;
        this.courses = courses;
    }
}
