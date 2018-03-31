package com.dragno.rest.service.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseStringTest {

    @ParameterizedTest
    @ValueSource(strings = {"CPSC 110", "AB 111", "ANSC 459A", "CHEM121"})
    public void validCourseString(String course){
        new CourseString(course);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "C 110", "ABCDE 111", "ANSC", "ANSC 459AB", "123 ABC"})
    public void invalidCourseString(String course){
        assertThrows(IllegalStateException.class, () -> new CourseString(course));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CPSC 110", "aB111", "ansc459a", "CHEM121"})
    public void normalizeCourseString(String course){
        CourseString courseString = new CourseString(course);
        String string = courseString.getCourse();
        assertThat(string).containsOnlyOnce(" ");
        String[] parts = string.split("\\s");
        assertThat(parts).hasSize(2);
        assertThat(parts[0]).isEqualTo(courseString.getDepartment());
        assertThat(parts[1]).isEqualTo(courseString.getCourseId());
        assertThat(parts[0]).containsPattern("^[A-Z]{2,4}$");
        assertThat(parts[1]).containsPattern("^(\\d){3}[A-Z]?$");
    }

    @Test
    public void doNothing(){
        //workaround to run parameterized tests with IntelliJ UI
    }
}