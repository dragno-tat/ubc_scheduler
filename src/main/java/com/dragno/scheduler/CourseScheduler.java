package com.dragno.scheduler;

import com.dragno.model.Course;

import java.util.Set;

/**
 * Created by Anthony on 7/15/2017.
 */
public interface CourseScheduler {

    Set<Course> scheduleCourses(Set<Set<Course>> courses) throws InterruptedException;
}
