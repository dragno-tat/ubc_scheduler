package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.CourseSection;
import com.dragno.rest.service.model.CourseString;

import java.util.Set;

/**
 * Created by Anthony on 7/8/2017.
 */
public interface CourseRetriever {

    Set<CourseSection> retrieve(CourseString course);
}
