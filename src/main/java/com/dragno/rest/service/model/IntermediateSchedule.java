package com.dragno.rest.service.model;

import com.google.common.collect.Sets;

import java.util.Set;

public class IntermediateSchedule {

    private Schedule schedule;
    private Set<Course> courses;

    public IntermediateSchedule(){
        this.schedule = new Schedule();
        this.courses = Sets.newHashSet();
    }

    public IntermediateSchedule(IntermediateSchedule otherWithCoursesToCopy, Schedule newSchedule, Course courseToAdd) {
        this.schedule = newSchedule;
        this.courses = Sets.newHashSet(otherWithCoursesToCopy.courses);
        this.courses.add(courseToAdd);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Set<Course> getCourses() {
        return Sets.newHashSet(courses);
    }

}