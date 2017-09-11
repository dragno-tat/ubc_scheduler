package com.dragno;

import com.dragno.model.Course;
import com.dragno.model.CourseString;
import com.dragno.scheduler.SingleTermCourseScheduler;
import com.dragno.util.MinimizeTimeAtSchoolScorer;
import com.dragno.util.ssc.SSCCourseRetriever;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;

/**
 * Created by Anthony on 7/8/2017.
 */
public class TestMain {

    public static Phaser phaser = new Phaser();

    public static void main(String[] args) {

        List<String> courseNames = Lists.newArrayList("CPSC 313", "CPSC 310", "STAT 241", "ECON 310");

        System.out.println(Instant.now());
        Set<Set<Course>> allCourses = Sets.newHashSet();
        for(String name : courseNames){
            Set<Course> courses = retrieveCourses(name).stream().filter(course -> course.getTerm() == 1).collect(
                    Collectors.toSet());
            allCourses.add(courses);
        }
        System.out.println(Instant.now());
        SingleTermCourseScheduler scheduler = new SingleTermCourseScheduler(new MinimizeTimeAtSchoolScorer());

        try {
            System.out.println(Instant.now());
            scheduler.scheduleCourses(allCourses).forEach(System.out::println);
            System.out.println(Instant.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Set<Course> retrieveCourses(String name){
        return new SSCCourseRetriever("2017", "W").retrieve(new CourseString(name));
    }
}
