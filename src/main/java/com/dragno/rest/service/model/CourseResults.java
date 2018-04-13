package com.dragno.rest.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class CourseResults {

    @JsonProperty("sessyr")
    private int sessyr;

    @JsonProperty("sesscd")
    private char sesscd;

    @JsonProperty("courses")
    private Set<Course> courses;



    public CourseResults(int sessyr, char sesscd, Set<Course> courses) {
        this.sessyr = sessyr;
        this.sesscd = sesscd;
        this.courses = courses;
    }
}
