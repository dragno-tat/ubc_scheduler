package com.dragno.scheduler;

import com.dragno.model.Course;
import com.dragno.model.IntermediateSchedule;
import com.dragno.model.Schedule;
import com.dragno.util.ScheduleScorer;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RecursiveAction;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Anthony on 7/9/2017.
 */
public class CourseSchedulingTask extends RecursiveAction {

    private static final int PARALLEL_SIZE_LIMIT = 1;

    private ScheduleScorer scorer;
    private ConcurrentLinkedQueue<IntermediateSchedule> results;
    private PriorityQueue<Set<Course>> availableCourses;
    private IntermediateSchedule intermediateSchedule;

    public CourseSchedulingTask(PriorityQueue<Set<Course>> availableCourses, ScheduleScorer scorer,
            ConcurrentLinkedQueue<IntermediateSchedule> results) {
        this(availableCourses, new IntermediateSchedule(), scorer, results);
    }

    private CourseSchedulingTask(PriorityQueue<Set<Course>> availableCourses, IntermediateSchedule
            intermediateSchedule, ScheduleScorer scorer, ConcurrentLinkedQueue<IntermediateSchedule> results) {
        this.availableCourses = new PriorityQueue<>(availableCourses);
        this.intermediateSchedule = intermediateSchedule;
        this.scorer = scorer;
        this.results = results;
    }

    @Override
    protected void compute() {
        if (availableCourses.size() <= PARALLEL_SIZE_LIMIT) {
            computeSequentially();
        } else {
            forkTasks();
        }
    }

    private void computeSequentially() {
        checkState(availableCourses.size() == 1); //method needs to be modified if limit increased
        int bestScore = Integer.MAX_VALUE;
        Schedule bestFullSchedule = null;
        Course bestCourse = null;
        for (Course course : availableCourses.remove()) {
            if (fitsSchedule(course)) {
                Schedule fullSchedule = intermediateSchedule.getSchedule().or(course.getSchedule());
                int score = scorer.score(fullSchedule);
                if (score < bestScore) {
                    bestScore = score;
                    bestFullSchedule = fullSchedule;
                    bestCourse = course;
                }
            }
        }
        if (bestFullSchedule != null) {
            results.add(new IntermediateSchedule(intermediateSchedule, bestFullSchedule, bestCourse));
        }
    }

    private boolean fitsSchedule(Course course) {
        return !course.getSchedule().intersects(intermediateSchedule.getSchedule());
    }

    private void forkTasks() {
        for (Course course : availableCourses.remove()) {
            if (fitsSchedule(course)) {
                createSubTask(course).fork();
            }
        }
    }

    private CourseSchedulingTask createSubTask(Course course) {
        Schedule mergedSchedule = intermediateSchedule.getSchedule().or(course.getSchedule());
        return new CourseSchedulingTask(availableCourses,
                new IntermediateSchedule(intermediateSchedule, mergedSchedule, course),
                scorer, results);
    }

}
