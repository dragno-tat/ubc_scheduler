package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.exception.NoValidScheduleException;
import com.dragno.rest.service.model.Course;
import com.dragno.rest.service.model.IntermediateSchedule;
import com.google.common.collect.Queues;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anthony on 7/9/2017.
 */
public class SingleTermCourseScheduler implements CourseScheduler {

    private final ForkJoinPool pool;
    private final ScheduleScorer scorer;

    public SingleTermCourseScheduler(ScheduleScorer scorer, ForkJoinPool pool) {
        this.scorer = scorer;
        this.pool = pool;
    }

    @Override
    public Set<Course> scheduleCourses(PriorityQueue<Set<Course>> courses) throws InterruptedException {
        ConcurrentLinkedQueue<IntermediateSchedule> results = Queues.newConcurrentLinkedQueue();
        executeAndAwaitCompletion(courses, results);
        return getBestSchedule(results);
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
