package com.dragno.rest.service.model;

import com.google.common.collect.Sets;

import java.util.Set;

public class IntermediateSchedule {

    private Schedule schedule;
    private Set<CourseSection> courseSections;

    public IntermediateSchedule(){
        this.schedule = new Schedule();
        this.courseSections = Sets.newHashSet();
    }

    public IntermediateSchedule(IntermediateSchedule otherWithCoursesToCopy, Schedule newSchedule, CourseSection sectionToAdd) {
        this.schedule = newSchedule;
        this.courseSections = Sets.newHashSet(otherWithCoursesToCopy.courseSections);
        this.courseSections.add(sectionToAdd);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Set<CourseSection> getCourseSections() {
        return Sets.newHashSet(courseSections);
    }

}