package com.dragno.util;

import com.dragno.model.Day;
import com.dragno.model.Schedule;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by Anthony on 7/11/2017.
 */
class MinimizeTimeAtSchoolScorerTest {

    private static MinimizeTimeAtSchoolScorer scorer;

    @BeforeAll
    public static void setUp() {
        scorer = new MinimizeTimeAtSchoolScorer();
    }

    @Test
    public void symmetricalSchedulesAreEqual() {
        Schedule middleSchedule = new Schedule(ImmutableSet.of(Day.WED), LocalTime.of(11, 0), LocalTime.of(12, 30));
        Schedule earlySchedule = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0), LocalTime.of(10,
                30));
        earlySchedule = earlySchedule.or(middleSchedule);
        Schedule laterSchedule = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(9, 0), LocalTime.of(10,
                30));
        laterSchedule = laterSchedule.or(middleSchedule);

        assertThat(scorer.score(earlySchedule)).isEqualTo(scorer.score(laterSchedule));
    }

    @Test
    public void lessBreaksBetterThanMoreBreaks() {
        Schedule scheduleToMerge = new Schedule(ImmutableSet.of(Day.WED), LocalTime.of(10, 30), LocalTime.of(12, 0));
        Schedule lessBreaks = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0), LocalTime.of(10,
                30));
        lessBreaks = lessBreaks.or(scheduleToMerge);
        Schedule moreBreaks = new Schedule(ImmutableSet.of(Day.WED, Day.FRI), LocalTime.of(13, 0), LocalTime.of(14,
                30));
        moreBreaks = moreBreaks.or(scheduleToMerge);

        assertThat(scorer.score(lessBreaks)).isLessThan(scorer.score(moreBreaks));
    }

    @Test
    public void lessDaysBetterThanMoreDays() {
        Schedule scheduleToMerge = new Schedule(ImmutableSet.of(Day.FRI), LocalTime.of(10, 30), LocalTime.of(12, 0));
        Schedule lessDays = new Schedule(ImmutableSet.of(Day.FRI), LocalTime.of(9, 0), LocalTime.of(10,
                30));
        lessDays = lessDays.or(scheduleToMerge);
        Schedule moreDays = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0), LocalTime.of(10,
                30));

        assertThat(scorer.score(lessDays)).isLessThan(scorer.score(moreDays));
    }

    @Test
    public void moreBreaksBetterThanMoreDays() {
        Schedule scheduleToMerge = new Schedule(ImmutableSet.of(Day.FRI), LocalTime.of(9, 30), LocalTime.of(11, 0));
        Schedule moreBreaks = new Schedule(ImmutableSet.of(Day.FRI), LocalTime.of(13, 0), LocalTime.of(14,
                30));
        moreBreaks = moreBreaks.or(scheduleToMerge);

        Schedule moreDays = new Schedule(ImmutableSet.of(Day.MON, Day.WED), LocalTime.of(9, 0), LocalTime.of(10,
                30));

        assertThat(scorer.score(moreBreaks)).isLessThan(scorer.score(moreDays));
    }

    @ParameterizedTest
    @MethodSource("dayAndExpectedProvider")
    public void calculateNumberOfBreakPeriods(int day, int expected) {
        assertThat(scorer.calculateNumberOfBreakPeriods(day)).isEqualTo(expected);
    }

    private static Stream<Arguments> dayAndExpectedProvider() {
        return Stream.of(Arguments.of(0, 0),
                Arguments.of(Integer.parseUnsignedInt("1001", 2), 2),
                Arguments.of(Integer.parseUnsignedInt("0010001000", 2), 3),
                Arguments.of(Integer.parseUnsignedInt("1111", 2), 0),
                Arguments.of(Integer.parseUnsignedInt("01001101110", 2), 3));
    }
}