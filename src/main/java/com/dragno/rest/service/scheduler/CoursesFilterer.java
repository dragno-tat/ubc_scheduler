package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.*;
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

    private Predicate<Course> filter;

    private CoursesFilterer(Predicate<Course> filter) {
        this.filter = filter;
    }

    public Set<Course> filter(Set<Course> courses) {
        return courses.stream()
                      .filter(filter)
                      .collect(Collectors.toSet());
    }

    public static class Builder {

        private Set<Predicate<Course>> predicates;

        public Builder() {
            this.predicates = newHashSet();
        }

        public Builder term(int term){
            predicates.add(course -> term == course.getTerm());
            return this;
        }

        public Builder statuses(Set<Status> statuses){
            predicates.add(course -> statuses.contains(course.getStatus()));
            return this;
        }

        public Builder activity(Activity activity){
            predicates.add(course -> activity == course.getActivity());
            return this;
        }

        public Builder days(Set<Day> days){
            Set<Day> undesiredDays = Sets.newHashSet(Day.values());
            undesiredDays.removeAll(days);
            predicates.add(course -> course.getSchedule().cardinality(undesiredDays) == 0);
            return this;
        }

        public Builder breaks(Set<Schedule> breaks) {
            predicates.add(course -> {
                for(Schedule b : breaks) {
                    if(course.getSchedule().intersects(b)) {
                        return false;
                    }
                }
                return true;
            });
            return this;
        }

        public Builder before(LocalTime endTime){
            predicates.add(course -> course.getSchedule().endsBefore(endTime));
            return this;
        }

        public Builder after(LocalTime startTime){
            predicates.add(course -> course.getSchedule().startsAfter(startTime));
            return this;
        }

        public CoursesFilterer build() {
            return new CoursesFilterer(predicates.stream().reduce(Predicate::and).orElse(p -> true));
        }
    }
}
