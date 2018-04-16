package com.dragno.rest.service.model;

import com.google.common.base.Objects;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkState;
import static java.text.MessageFormat.format;

/**
 * Created by Anthony on 7/8/2017.
 */
public class CourseString {

    private static final String REGEX_VALIDATOR = "^[a-zA-Z]{2,4}(\\s)?(\\d){3}[a-zA-Z]?$";

    private String course;

    public CourseString(String course){
        String trimmed = course.trim();
        validateString(trimmed);
        this.course = normalize(trimmed);
    }

    private void validateString(String course) {
        checkState(course.matches(REGEX_VALIDATOR), format("CourseSection {0} is in an invalid format", course));
    }

    private String normalize(String course) {
        int spaceIndex = course.indexOf(" ");
        if(spaceIndex == -1) {
            course = insertSpace(course);
        }
        return course.toUpperCase(Locale.US);
    }

    private String insertSpace(String course) {
        Matcher matcher = Pattern.compile("\\d").matcher(course);
        if(!matcher.find()){
            throw new IllegalStateException(format("CourseSection {0} is in an invalid format", course));
        }
        int firstIntIndex = matcher.start();
        course = new StringBuilder(course).insert(firstIntIndex, " ").toString();
        return course;
    }

    public String getCourse(){
        return course;
    }

    public String getDepartment(){
        return course.split("\\s")[0];
    }

    public String getCourseId(){
        return course.split("\\s")[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseString that = (CourseString) o;
        return Objects.equal(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(course);
    }
}
