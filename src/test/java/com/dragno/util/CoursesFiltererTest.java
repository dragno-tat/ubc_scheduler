package com.dragno.util;

import com.dragno.model.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by Anthony on 7/8/2017.
 */
class CoursesFiltererTest {

    @Test
    public void filterBeforeTime() {
        List<Course> createdCourses = Lists.newArrayList();
        CoursesFilterer filterer = setUpCoursesFilterer(i -> {
            Course course = mock(Course.class);
            Schedule schedule = new Schedule(Sets.newHashSet(Day.MON), LocalTime.of(i+8,0), LocalTime.of(i+9,30));
            when(course.getSchedule()).thenReturn(schedule);
            createdCourses.add(course);
            return course;
        });

        Set<Course> actualCourses = filterer.before(LocalTime.of(10, 0)).filter();

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses)).isEqualTo(createdCourses.get(0));
    }

    @Test
    public void filterAfterTime() {
        List<Course> createdCourses = Lists.newArrayList();
        CoursesFilterer filterer = setUpCoursesFilterer(i -> {
            Course course = mock(Course.class);
            Schedule schedule = new Schedule(Sets.newHashSet(Day.MON), LocalTime.of(i+8,0), LocalTime.of(i+9,30));
            when(course.getSchedule()).thenReturn(schedule);
            createdCourses.add(course);
            return course;
        });

        Set<Course> actualCourses = filterer.after(LocalTime.of(10, 0)).filter();

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
        CoursesFilterer filterer = new CoursesFilterer(Sets.newHashSet(originalCourses));

        Set<Course> actualCourses = filterer.days(Sets.newHashSet(days[0], days[1])).filter();

        assertThat(actualCourses).hasSize(2);
        assertThat(actualCourses).contains(originalCourses.get(0)).contains(originalCourses.get(1));
    }

    @Test
    public void filterTerm() {
        CoursesFilterer filterer = setUpCoursesFilterer(i -> {
            Course course = mock(Course.class);
            when(course.getTerm()).thenReturn(i);
            return course;
        });

        Set<Course> actualCourses = filterer.term(1).filter();

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getTerm()).isEqualTo(1);
    }

    @Test
    public void filterStatus() {
        Status[] statuses = Status.values();
        CoursesFilterer filterer = setUpCoursesFilterer(i -> {
            Course course = mock(Course.class);
            when(course.getStatus()).thenReturn(statuses[i]);
            return course;
        });

        Set<Course> actualCourses = filterer.status(statuses[0]).filter();

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getStatus()).isEqualTo(statuses[0]);
    }

    @Test
    public void filterActivity() {
        Activity[] activities = Activity.values();
        CoursesFilterer filterer = setUpCoursesFilterer(i -> {
            Course course = mock(Course.class);
            when(course.getActivity()).thenReturn(activities[i]);
            return course;
        });

        Set<Course> actualCourses = filterer.activity(activities[0]).filter();

        assertThat(actualCourses).hasSize(1);
        assertThat(Iterables.getOnlyElement(actualCourses).getActivity()).isEqualTo(activities[0]);
    }

    private CoursesFilterer setUpCoursesFilterer(Function<Integer, Course> courseProvider){
        Set<Course> originalCourses = Sets.newHashSet();
        for (int i = 0; i < 3; i++) {
            originalCourses.add(courseProvider.apply(i));
        }
        return new CoursesFilterer(originalCourses);
    }
}