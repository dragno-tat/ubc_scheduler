package com.dragno.util;

import com.dragno.model.Day;
import com.dragno.model.Schedule;
import com.google.common.annotations.VisibleForTesting;

/**
 * Created by Anthony on 7/9/2017.
 */
public class MinimizeTimeAtSchoolScorer implements ScheduleScorer {

    private static final int DAY_PENALTY = 10;

    @Override
    public int score(Schedule schedule) {
        int score = 0;
        for (Day day : Day.values()) {
            int scheduleDay = schedule.getDay(day);
            if (hasClassOnDay(scheduleDay)) {
                score += DAY_PENALTY + calculateNumberOfBreakPeriods(scheduleDay);
            }
        }
        return score;
    }

    @VisibleForTesting
    int calculateNumberOfBreakPeriods(int scheduleDay) {
        int sum = 0, count = 0;
        scheduleDay |= scheduleDay - 1; //remove trailing 0s
        while (scheduleDay != scheduleDay >> 1) {
            scheduleDay >>= 1;
            if ((scheduleDay & 1) == 1) { //prevent counting leading 0s
                sum += count;
                count = 0;
            } else {
                count++;
            }
        }
        return sum;
    }

    private boolean hasClassOnDay(int scheduleDay) {
        return Integer.bitCount(scheduleDay) != 0;
    }
}
