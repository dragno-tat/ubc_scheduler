package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.Course;

import java.util.Set;

/**
 * Created by Anthony on 7/15/2017.
 */
public interface CourseScheduler {

    Set<Course> scheduleCourses(Set<Set<Course>> courses) throws InterruptedException;
}
