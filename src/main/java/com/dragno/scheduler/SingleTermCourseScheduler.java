package com.dragno.scheduler;

import com.dragno.exception.NoValidScheduleException;
import com.dragno.model.Course;
import com.dragno.model.IntermediateSchedule;
import com.dragno.util.ScheduleScorer;
import com.google.common.collect.Queues;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Anthony on 7/9/2017.
 */
public class SingleTermCourseScheduler implements CourseScheduler {

    private final ForkJoinPool pool;
    private final ScheduleScorer scorer;

    public SingleTermCourseScheduler(ScheduleScorer scorer) {
        this.scorer = scorer;
        pool = new ForkJoinPool();
    }

    @Override
    public Set<Course> scheduleCourses(Set<Set<Course>> courses) throws InterruptedException {
        ConcurrentLinkedQueue<IntermediateSchedule> results = Queues.newConcurrentLinkedQueue();
        PriorityQueue<Set<Course>> availableCourses = new PriorityQueue<>(Comparator.comparingInt(Set::size));

        addCoursesByActivity(availableCourses, courses);
        executeAndAwaitCompletion(availableCourses, results);
        return getBestSchedule(results);
    }

    private void addCoursesByActivity(PriorityQueue<Set<Course>> availableCourses, Set<Set<Course>> courses) {
        courses.forEach(courseSet -> availableCourses.addAll(
                courseSet.stream()
                         .collect(Collectors.groupingBy(Course::getActivity, Collectors.toSet()))
                         .values()));
    }

    private void executeAndAwaitCompletion(PriorityQueue<Set<Course>> availableCourses,
            ConcurrentLinkedQueue<IntermediateSchedule> results) throws InterruptedException {
        pool.execute(new CourseSchedulingTask(availableCourses, scorer, results));
        pool.shutdown();
        pool.awaitTermination(180, TimeUnit.SECONDS);
    }

    private Set<Course> getBestSchedule(ConcurrentLinkedQueue<IntermediateSchedule> results) {
        Optional<IntermediateSchedule> schedule = results.stream().min((schedule1, schedule2) -> scorer.compare
                (schedule1.getSchedule(), schedule2.getSchedule()));
        return schedule.orElseThrow(NoValidScheduleException::new).getCourses();
    }
}
