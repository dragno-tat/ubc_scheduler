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

    private Predicate<CourseSection> filter;

    private CoursesFilterer(Predicate<CourseSection> filter) {
        this.filter = filter;
    }

    public Set<CourseSection> filter(Set<CourseSection> sections) {
        return sections.stream()
                      .filter(filter)
                      .collect(Collectors.toSet());
    }

    public static class Builder {

        private Set<Predicate<CourseSection>> predicates;

        public Builder() {
            this.predicates = newHashSet();
        }

        public Builder term(int term){
            predicates.add(section -> term == section.getTerm());
            return this;
        }

        public Builder statuses(Set<Status> statuses){
            predicates.add(section -> statuses.contains(section.getStatus()));
            return this;
        }

        public Builder activity(Activity activity){
            predicates.add(section -> activity == section.getActivity());
            return this;
        }

        public Builder days(Set<Day> days){
            Set<Day> undesiredDays = Sets.newHashSet(Day.values());
            undesiredDays.removeAll(days);
            predicates.add(section -> section.getSchedule().cardinality(undesiredDays) == 0);
            return this;
        }

        public Builder breaks(Set<Schedule> breaks) {
            predicates.add(section -> {
                for(Schedule b : breaks) {
                    if(section.getSchedule().intersects(b)) {
                        return false;
                    }
                }
                return true;
            });
            return this;
        }

        public Builder before(LocalTime endTime){
            predicates.add(section -> section.getSchedule().endsBefore(endTime));
            return this;
        }

        public Builder after(LocalTime startTime){
            predicates.add(section -> section.getSchedule().startsAfter(startTime));
            return this;
        }

        public CoursesFilterer build() {
            return new CoursesFilterer(predicates.stream().reduce(Predicate::and).orElse(p -> true));
        }
    }
}
