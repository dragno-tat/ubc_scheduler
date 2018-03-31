package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.Course;
import com.dragno.rest.service.model.CourseString;

import java.util.Set;

/**
 * Created by Anthony on 7/8/2017.
 */
public interface CourseRetriever {

    Set<Course> retrieve(CourseString course);
}
