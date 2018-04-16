package com.dragno.rest.service;

import com.dragno.rest.service.exception.NoValidScheduleException;
import com.dragno.rest.service.model.CourseSection;
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

import static com.google.common.base.Preconditions.checkNotNull;

public class CourseSchedulingService {

    private static CourseSchedulingService instance = new CourseSchedulingService();

    private Table<Integer,Character,HashMap<CourseString, Set<CourseSection>>> coursesCache;

    private ForkJoinPool pool;

    private CourseSchedulingService() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        coursesCache = ArrayTable.create(ImmutableSet.of(currentYear-1,currentYear),
                ImmutableSet.of('W', 'S'));
        pool = new ForkJoinPool();
    }

    public static CourseSchedulingService getInstance() {
        return instance;
    }

    public Set<CourseSection> scheduleCourses(ScheduleOptions options) {
        CourseScheduler scheduler = new SingleTermCourseScheduler(new MinimizeTimeAtSchoolScorer(), pool);
        PriorityQueue<Set<CourseSection>> courseSections = new PriorityQueue<>(Comparator.comparingInt(Set::size));
        CoursesFilterer filterer = createFilterer(options);

        for(String name : options.getCourses()) {
            getOrRetrieveCourses(options.getSessyr(), options.getSesscd(), new CourseString(name))
                    .stream()
                    .filter(c -> !isVantage(c))
                    .collect(Collectors.groupingBy(CourseSection::getActivity, Collectors.toSet()))
                    .values()
                    .forEach(set -> {
                        CourseSection section = checkNotNull(Iterables.getFirst(set, null));
                        String courseName = section.getDept() + " " + section.getId();
                        Set<CourseSection> filtered = filterer.filter(set);
                        if(filtered.isEmpty()) {
                            throw new NoValidScheduleException("No " + section.getActivity().toString().toLowerCase() +
                                    "s for " + courseName + " fit the criteria");
                        }
                        courseSections.add(filtered);
                    });
        }
        return scheduler.scheduleCourses(courseSections);
    }

    private CoursesFilterer createFilterer(ScheduleOptions options) {
        return new CoursesFilterer.Builder()
                .after(options.getStartTime())
                .before(options.getEndTime())
                .days(options.getDays())
                .statuses(getIncludedStatues(options.getExcludedStatuses()))
                .term(options.getTerm())
                .breaks(options.getBreaks())
                .build();
    }

    private Set<Status> getIncludedStatues(Set<Status> excludedStatues) {
        Set<Status> allStatuses = Sets.newHashSet(Status.values());
        allStatuses.removeAll(excludedStatues);
        return allStatuses;
    }

    private Set<CourseSection> getOrRetrieveCourses(int sessyr, char sesscd, CourseString courseName){
        HashMap<CourseString, Set<CourseSection>> coursesMap = coursesCache.get(sessyr,sesscd);
        if(coursesMap == null) {
            coursesMap = Maps.newHashMap();
            coursesCache.put(sessyr,sesscd,coursesMap);
        }
        Set<CourseSection> sections = coursesMap.get(courseName);
        if(sections == null) {
            sections = new SSCCourseRetriever(sessyr, sesscd).retrieve(courseName);
            coursesMap.put(courseName, sections);
        }
        if(sections.isEmpty()) {
            throw new NoValidScheduleException("No sections found for course: " + courseName.getCourse() + " in " +
                    sessyr + sesscd);
        }
        return sections;
    }

    private boolean isVantage(CourseSection course) {
        return course.getSection().startsWith("V");
    }
}
