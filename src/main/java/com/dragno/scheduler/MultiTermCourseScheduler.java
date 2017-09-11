package com.dragno.scheduler;

import com.dragno.model.Course;

import java.util.Set;

/**
 * Created by Anthony on 7/15/2017.
 */
public class MultiTermCourseScheduler implements CourseScheduler{

    private final int firstTermCourses;
    private final int secondTermCourses;

    private final CourseScheduler delegate;

    public MultiTermCourseScheduler(int firstTermCourses, int secondTermCourses,
            CourseScheduler scheduler) {
        if(scheduler instanceof MultiTermCourseScheduler){
            throw new IllegalStateException("Cannot have nested schedulers of the same class");
        }
        this.firstTermCourses = firstTermCourses;
        this.secondTermCourses = secondTermCourses;
        this.delegate = scheduler;
    }

    @Override
    public Set<Course> scheduleCourses(Set<Set<Course>> courses) throws InterruptedException {
        return null;
    }
}
