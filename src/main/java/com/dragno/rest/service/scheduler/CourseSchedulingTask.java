package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.CourseSection;
import com.dragno.rest.service.model.IntermediateSchedule;
import com.dragno.rest.service.model.Schedule;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.RecursiveAction;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Anthony on 7/9/2017.
 */
public class CourseSchedulingTask extends RecursiveAction {

    private static final int PARALLEL_SIZE_LIMIT = 1;

    private ScheduleScorer scorer;
    private ConcurrentLinkedQueue<IntermediateSchedule> results;
    private PriorityQueue<Set<CourseSection>> availableSections;
    private IntermediateSchedule intermediateSchedule;
    private Phaser phaser;

    public CourseSchedulingTask(PriorityQueue<Set<CourseSection>> availableSections, ScheduleScorer scorer,
            ConcurrentLinkedQueue<IntermediateSchedule> results, Phaser phaser) {
        this(availableSections, new IntermediateSchedule(), scorer, results, phaser);
    }

    private CourseSchedulingTask(PriorityQueue<Set<CourseSection>> availableSections, IntermediateSchedule
            intermediateSchedule, ScheduleScorer scorer, ConcurrentLinkedQueue<IntermediateSchedule> results,
            Phaser phaser) {
        this.availableSections = new PriorityQueue<>(availableSections);
        this.intermediateSchedule = intermediateSchedule;
        this.scorer = scorer;
        this.results = results;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    protected void compute() {
        if (availableSections.size() <= PARALLEL_SIZE_LIMIT) {
            computeSequentially();
        } else {
            forkTasks();
        }
        phaser.arriveAndDeregister();
    }

    private void computeSequentially() {
        checkState(availableSections.size() == 1); //method needs to be modified if limit increased
        int bestScore = Integer.MAX_VALUE;
        Schedule bestFullSchedule = null;
        CourseSection bestSection = null;
        for (CourseSection section : availableSections.remove()) {
            if (fitsSchedule(section)) {
                Schedule fullSchedule = intermediateSchedule.getSchedule().or(section.getSchedule());
                int score = scorer.score(fullSchedule);
                if (score < bestScore) {
                    bestScore = score;
                    bestFullSchedule = fullSchedule;
                    bestSection = section;
                }
            }
        }
        if (bestFullSchedule != null) {
            results.add(new IntermediateSchedule(intermediateSchedule, bestFullSchedule, bestSection));
        }
    }

    private boolean fitsSchedule(CourseSection section) {
        return !section.getSchedule().intersects(intermediateSchedule.getSchedule());
    }

    private void forkTasks() {
        for (CourseSection section : availableSections.remove()) {
            if (fitsSchedule(section)) {
                createSubTask(section).fork();
            }
        }
    }

    private CourseSchedulingTask createSubTask(CourseSection section) {
        Schedule mergedSchedule = intermediateSchedule.getSchedule().or(section.getSchedule());
        return new CourseSchedulingTask(availableSections,
                new IntermediateSchedule(intermediateSchedule, mergedSchedule, section),
                scorer, results, phaser);
    }

}
