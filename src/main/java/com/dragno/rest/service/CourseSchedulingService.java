package com.dragno.rest.service;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class CourseSchedulingService {

    private static CourseSchedulingService instance;

    private CourseScheduler scheduler;

    private Table<Integer,Character,HashMap<CourseString, Set<Course>>> coursesCache;

    private CourseSchedulingService(CourseScheduler scheduler) {
        this.scheduler = scheduler;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        coursesCache = ArrayTable.create(ImmutableSet.of(currentYear-1,currentYear),
                ImmutableSet.of('W', 'S'));
    }

    public static CourseSchedulingService getInstance() {
        if(instance == null) {
            instance = new CourseSchedulingService(new SingleTermCourseScheduler(new MinimizeTimeAtSchoolScorer()));
        }
        return instance;
    }

    public Set<Course> scheduleCourses(ScheduleOptions options) throws InterruptedException {
        Set<Set<Course>> allSelectedCourses = Sets.newHashSet();
        CoursesFilterer filterer = createFilterer(options);
        options.getCourses().forEach(name -> allSelectedCourses.add(filterer.filter(getOrRetrieveCourses(
                options.getSessyr(), options.getSesscd(), new CourseString(name)))));
        return scheduler.scheduleCourses(allSelectedCourses);
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
}
