package com.dragno.scheduler;

import com.dragno.model.Course;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Anthony on 7/15/2017.
 */
public class OptionsAwareCourseScheduler implements CourseScheduler{

    private final int maxCourses;
    private final CourseScheduler delegate;

    public OptionsAwareCourseScheduler(CourseScheduler scheduler, int maxCourses){
        if(scheduler instanceof OptionsAwareCourseScheduler){
            throw new IllegalStateException("Cannot have nested schedulers of the same class");
        }
        this.delegate = scheduler;
        this.maxCourses = maxCourses;
    }

    @Override
    public Set<Course> scheduleCourses(Set<Set<Course>> courses) throws InterruptedException {
        Set<Set<Course>> copy = Sets.newHashSet(courses);

        return delegate.scheduleCourses(copy);
    }
}
