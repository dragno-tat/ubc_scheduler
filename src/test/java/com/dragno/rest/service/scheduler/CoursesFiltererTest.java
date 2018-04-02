package com.dragno.rest.service.scheduler;

import com.dragno.rest.service.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoursesFiltererTest {

    @Test
    public void filterBeforeTime() {
        List<Course> createdCourses = Lists.newArrayList();
        createCourses(createdCourses);

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .before(LocalTime.of(10, 0))
                .build()
                .filter(Sets.newHashSet(createdCourses));

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses)).isEqualTo(createdCourses.get(0));
    }

    @Test
    public void filterAfterTime() {
        List<Course> createdCourses = Lists.newArrayList();
        createCourses(createdCourses);

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .after(LocalTime.of(10, 0))
                .build()
                .filter(Sets.newHashSet(createdCourses));

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses)).isEqualTo(createdCourses.get(createdCourses.size() - 1));
    }

    @Test
    public void filterDays() {
        Day[] days = Day.values();
        List<Course> originalCourses = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Course course = mock(Course.class);
            Schedule schedule = new Schedule(Sets.newHashSet(Arrays.copyOfRange(days, 0,i+1)), LocalTime.of(8,0),
                    LocalTime.of(9,0));
            when(course.getSchedule()).thenReturn(schedule);
            originalCourses.add(course);
        }
        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .days(Sets.newHashSet(days[0], days[1]))
                .build()
                .filter(Sets.newHashSet(originalCourses));

        assertThat(actualCourses).hasSize(2);
        assertThat(actualCourses).contains(originalCourses.get(0)).contains(originalCourses.get(1));
    }

    @Test
    public void filterBreaks() {
        List<Course> createdCourses = Lists.newArrayList();
        createCourses(createdCourses);

        Set<Schedule> breaks = Sets.newHashSet();
        breaks.add(new Schedule(Sets.newHashSet(Day.MON), LocalTime.of(9,0), LocalTime.of(10,0)));
        breaks.add(new Schedule(Sets.newHashSet(Day.WED), LocalTime.of(10,0), LocalTime.of(11,0)));

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .breaks(breaks)
                .build()
                .filter(Sets.newHashSet(createdCourses));

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses)).isEqualTo(createdCourses.get(createdCourses.size() - 1));
    }

    private void createCourses(List<Course> createdCourses) {
        for(int i = 0; i < 3; i++) {
            Course course = mock(Course.class);
            Schedule schedule = new Schedule(Sets.newHashSet(Day.MON), LocalTime.of(i+8,0), LocalTime.of(i+9,30));
            when(course.getSchedule()).thenReturn(schedule);
            createdCourses.add(course);
        }
    }

    @Test
    public void filterTerm() {
        Set<Course> originalCourses = Sets.newHashSet();
        for (int i = 0; i < 2; i++) {
            Course course = mock(Course.class);
            when(course.getTerm()).thenReturn(i);
            originalCourses.add(course);
        }

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .term(1)
                .build()
                .filter(originalCourses);

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getTerm()).isEqualTo(1);
    }

    @Test
    public void filterStatus() {
        Set<Course> originalCourses = Sets.newHashSet();
        Status[] statuses = Status.values();
        for (int i = 0; i < 3; i++) {
            Course course = mock(Course.class);
            when(course.getStatus()).thenReturn(statuses[i]);
            originalCourses.add(course);
        }

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .statuses(ImmutableSet.of(statuses[0]))
                .build()
                .filter(originalCourses);

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getStatus()).isEqualTo(statuses[0]);
    }

    @Test
    public void filterActivity() {
        Set<Course> originalCourses = Sets.newHashSet();
        Activity[] activities = Activity.values();
        for (int i = 0; i < 3; i++) {
            Course course = mock(Course.class);
            when(course.getActivity()).thenReturn(activities[i]);
            originalCourses.add(course);
        };

        Set<Course> actualCourses = new CoursesFilterer.Builder()
                .activity(activities[0])
                .build()
                .filter(originalCourses);

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getActivity()).isEqualTo(activities[0]);
    }
}