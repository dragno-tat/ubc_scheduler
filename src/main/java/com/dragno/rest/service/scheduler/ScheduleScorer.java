package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.Schedule;

import java.util.Comparator;

/**
 * Created by Anthony on 7/9/2017.
 */

/*
 * A lower score indicates a better schedule than a higher score
 */
public interface ScheduleScorer extends Comparator<Schedule> {

    int score(Schedule schedule);

    @Override
    default int compare(Schedule schedule1, Schedule schedule2) {
        return Integer.compare(score(schedule1), score(schedule2));
    }
}
