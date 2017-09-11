package com.dragno.model;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Anthony on 7/8/2017.
 */
class ScheduleTest {

    @Test
    public void setInvalidStartTime() {
        assertThrows(IllegalStateException.class, () -> new Schedule(ImmutableSet.of(Day.MON), LocalTime.of(7, 0),
                LocalTime.of(8, 0)));
        assertThrows(IllegalStateException.class, () -> new Schedule(ImmutableSet.of(Day.MON), LocalTime.of(9, 0),
                LocalTime.of(8, 0)));
    }

    @Test
    public void multiCourseSchedule() {
        Schedule schedule = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 0));
        schedule = new Schedule(schedule, ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertThat(schedule.cardinality()).isEqualTo(8);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.MON, Day.WED, Day.FRI))).isEqualTo(8);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.TUE, Day.THU))).isEqualTo(0);
        assertThat(Integer.toBinaryString(schedule.getDay(Day.MON))).isEqualTo("1100");
        assertThat(Integer.toBinaryString(schedule.getDay(Day.WED))).isEqualTo("11001100");
        assertThat(Integer.toBinaryString(schedule.getDay(Day.FRI))).isEqualTo("11000000");
    }

    @Test
    public void setEarliestTime() {
        Schedule schedule = new Schedule(ImmutableSet.of(Day.MON), LocalTime.of(Schedule.EARLIER_START_HOUR, 0),
                LocalTime.of(9, 30));
        assertThat(schedule.cardinality()).isEqualTo(3);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.MON))).isEqualTo(3);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.TUE, Day.WED, Day.THU, Day.FRI))).isEqualTo(0);
        assertThat(Integer.toBinaryString(schedule.getDay(Day.MON))).isEqualTo("111");
    }

    @Test
    public void setMonWedFri() {
        Schedule schedule = new Schedule(ImmutableSet.of(Day.MON, Day.WED, Day.FRI), LocalTime.of(10, 0), LocalTime.of
                (11, 30));
        assertThat(schedule.cardinality()).isEqualTo(9);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.MON, Day.WED, Day.FRI))).isEqualTo(9);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.TUE, Day.THU))).isEqualTo(0);
        assertThat(Integer.toBinaryString(schedule.getDay(Day.MON))).isEqualTo("1110000");
        assertThat(Integer.toBinaryString(schedule.getDay(Day.WED))).isEqualTo("1110000");
        assertThat(Integer.toBinaryString(schedule.getDay(Day.FRI))).isEqualTo("1110000");
    }

    @Test
    public void setTueThu() {
        Schedule schedule = new Schedule(ImmutableSet.of(Day.TUE, Day.THU), LocalTime.of(16, 30), LocalTime.of
                (18, 0));
        assertThat(schedule.cardinality()).isEqualTo(6);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.TUE, Day.THU))).isEqualTo(6);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.MON, Day.WED, Day.FRI))).isEqualTo(0);
        assertThat(Integer.toBinaryString(schedule.getDay(Day.TUE))).isEqualTo("11100000000000000000");
        assertThat(Integer.toBinaryString(schedule.getDay(Day.THU))).isEqualTo("11100000000000000000");
    }

    @Test
    public void setLatestTime() {
        Schedule schedule = new Schedule(ImmutableSet.of(Day.FRI), LocalTime.of(Schedule.LATEST_END_HOUR - 1, 0),
                LocalTime.of(Schedule.LATEST_END_HOUR, 0));
        assertThat(schedule.cardinality()).isEqualTo(2);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.FRI))).isEqualTo(2);
        assertThat(schedule.cardinality(ImmutableSet.of(Day.MON, Day.TUE, Day.WED, Day.THU))).isEqualTo(0);
        assertThat(Integer.toBinaryString(schedule.getDay(Day.FRI))).isEqualTo("1100000000000000000000");
    }

    @Test
    public void orWithOverLap(){
        Schedule schedule1 = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 30));
        Schedule schedule2 = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(10, 0), LocalTime.of(11, 0));
        Schedule combinedSchedule1 = schedule1.or(schedule2);
        Schedule combinedSchedule2 = schedule2.or(schedule1);
        assertThat(combinedSchedule1).isEqualTo(combinedSchedule2);
        assertThat(combinedSchedule1.cardinality()).isEqualTo(9);
        assertThat(Integer.toBinaryString(combinedSchedule1.getDay(Day.WED)))
                .isEqualTo("111100");
    }

    @Test
    public void orWithNoOverLap(){
        Schedule schedule1 = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 0));
        Schedule schedule2 = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(10, 0), LocalTime.of(11, 0));
        Schedule combinedSchedule1 = schedule1.or(schedule2);
        Schedule combinedSchedule2 = schedule2.or(schedule1);
        assertThat(combinedSchedule1).isEqualTo(combinedSchedule2);
        assertThat(combinedSchedule1.cardinality()).isEqualTo(8);
        assertThat(Integer.toBinaryString(combinedSchedule1.getDay(Day.WED)))
                .isEqualTo("111100");
    }

    @Test
    public void doesNotIntersect(){
        Schedule schedule1 = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 0));
        Schedule schedule2 = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertThat(schedule1.intersects(schedule2)).isFalse();
        assertThat(schedule2.intersects(schedule1)).isFalse();
    }

    @Test
    public void doesIntersect(){
        Schedule schedule1 = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 30));
        Schedule schedule2 = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertThat(schedule1.intersects(schedule2)).isTrue();
        assertThat(schedule2.intersects(schedule1)).isTrue();
    }

    @Test
    public void startsAfter(){
        Schedule schedule = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0),
                LocalTime.of(10, 30));
        assertThat(schedule.startsAfter(LocalTime.of(8, 0))).isTrue();
        assertThat(schedule.startsAfter(LocalTime.of(9, 0))).isTrue();
        assertThat(schedule.startsAfter(LocalTime.of(9, 30))).isFalse();
        assertThat(schedule.startsAfter(LocalTime.of(10, 0))).isFalse();
    }

    @Test
    public void endsBefore(){
        Schedule schedule = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(15, 30), LocalTime.of(16,
            30));
        assertThat(schedule.endsBefore(LocalTime.of(17, 0))).isTrue();
        assertThat(schedule.endsBefore(LocalTime.of(16, 30))).isTrue();
        assertThat(schedule.endsBefore(LocalTime.of(16, 0))).isFalse();
    }
}