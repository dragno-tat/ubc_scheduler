package com.dragno.util;

import com.dragno.model.Activity;
import com.dragno.model.Course;
import com.dragno.model.Day;
import com.dragno.model.Status;
import com.google.common.collect.Sets;

import java.time.LocalTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Created by Anthony on 7/8/2017.
 */
public class CoursesFilterer {

    private Set<Course> courses;
    private Set<Predicate<Course>> predicates;

    public CoursesFilterer(Set<Course> courses) {
        this.courses = courses;
        this.predicates = newHashSet();
    }

    public CoursesFilterer term(int term){
        predicates.add(course -> term == course.getTerm());
        return this;
    }

    public CoursesFilterer status(Status status){
        predicates.add(course -> status == course.getStatus());
        return this;
    }

    public CoursesFilterer activity(Activity activity){
        predicates.add(course -> activity == course.getActivity());
        return this;
    }

    public CoursesFilterer days(Set<Day> days){
        Set<Day> undesiredDays = Sets.newHashSet(Day.values());
        undesiredDays.removeAll(days);
        predicates.add(course -> course.getSchedule().cardinality(undesiredDays) == 0);
        return this;
    }

    public CoursesFilterer before(LocalTime endTime){
        predicates.add(course -> course.getSchedule().endsBefore(endTime));
        return this;
    }

    public CoursesFilterer after(LocalTime startTime){
        predicates.add(course -> course.getSchedule().startsAfter(startTime));
        return this;
    }

    public Set<Course> filter() {
        return courses.stream()
                      .filter(predicates.stream().reduce(Predicate::and).orElse(p -> true))
                      .collect(Collectors.toSet());
    }
}
