package com.dragno.util;

import com.dragno.model.Course;
import com.dragno.model.CourseString;

import java.util.Set;

/**
 * Created by Anthony on 7/8/2017.
 */
public interface CourseRetriever {

    Set<Course> retrieve(CourseString course);
}
