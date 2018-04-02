package com.dragno.rest.service;

import com.dragno.rest.service.exception.NoValidScheduleException;
import com.dragno.rest.service.model.Course;
import com.dragno.rest.service.model.CourseString;
import com.dragno.rest.service.model.ScheduleOptions;
import com.dragno.rest.service.model.Status;
import com.dragno.rest.service.scheduler.CourseScheduler;
import com.dragno.rest.service.scheduler.CoursesFilterer;
import com.dragno.rest.service.scheduler.MinimizeTimeAtSchoolScorer;
import com.dragno.rest.service.scheduler.SingleTermCourseScheduler;
import com.dragno.rest.service.scheduler.ssc.SSCCourseRetriever;
import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class CourseSchedulingService {

    private static CourseSchedulingService instance;

    private Table<Integer,Character,HashMap<CourseString, Set<Course>>> coursesCache;

    private ForkJoinPool pool;

    private CourseSchedulingService() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        coursesCache = ArrayTable.create(ImmutableSet.of(currentYear-1,currentYear),
                ImmutableSet.of('W', 'S'));
        pool = new ForkJoinPool();
    }

    public static CourseSchedulingService getInstance() {
        if(instance == null) {
            instance = new CourseSchedulingService();
        }
        return instance;
    }

    public Set<Course> scheduleCourses(ScheduleOptions options) {
        CourseScheduler scheduler = new SingleTermCourseScheduler(new MinimizeTimeAtSchoolScorer(), pool);
        PriorityQueue<Set<Course>> courses = new PriorityQueue<>(Comparator.comparingInt(Set::size));
        CoursesFilterer filterer = createFilterer(options);

        for(String name : options.getCourses()) {
            getOrRetrieveCourses(options.getSessyr(), options.getSesscd(), new CourseString(name))
                    .stream()
                    .filter(c -> !isVantage(c))
                    .collect(Collectors.groupingBy(Course::getActivity, Collectors.toSet()))
                    .values()
                    .forEach(set -> {
                        Set<Course> filtered = filterer.filter(set);
                        if(filtered.isEmpty()) {
                            throw new NoValidScheduleException("Some course activities were filtered out by the " +
                                    "criteria");
                        }
                        courses.add(filtered);
                    });
        }
        if(courses.isEmpty()) {
            throw new NoValidScheduleException("No courses were found that matched the criteria");
        }
        return scheduler.scheduleCourses(courses);
    }

    private CoursesFilterer createFilterer(ScheduleOptions options) {
        return new CoursesFilterer.Builder()
                .after(options.getStartTime())
                .before(options.getEndTime())
                .days(options.getDays())
                .statuses(getIncludedStatues(options.getExcludedStatuses()))
                .term(options.getTerm())
                .build();
    }

    private Set<Status> getIncludedStatues(Set<Status> excludedStatues) {
        Set<Status> allStatuses = Sets.newHashSet(Status.values());
        allStatuses.removeAll(excludedStatues);
        return allStatuses;
    }

    private Set<Course> getOrRetrieveCourses(int sessyr, char sesscd, CourseString courseName){
        HashMap<CourseString, Set<Course>> coursesMap = coursesCache.get(sessyr,sesscd);
        if(coursesMap == null) {
            coursesMap = Maps.newHashMap();
            coursesCache.put(sessyr,sesscd,coursesMap);
        }
        Set<Course> courses = coursesMap.get(courseName);
        if(courses == null) {
            courses = new SSCCourseRetriever(sessyr, sesscd).retrieve(courseName);
            coursesMap.put(courseName, courses);
        }
        return courses;
    }

    private boolean isVantage(Course course) {
        return course.getSection().startsWith("V");
    }
}
