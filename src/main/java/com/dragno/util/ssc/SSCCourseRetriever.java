package com.dragno.util.ssc;

import com.dragno.model.Course;
import com.dragno.model.CourseString;
import com.dragno.model.builder.SSCParametersDataBuilder;
import com.dragno.util.CourseRetriever;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.text.MessageFormat.format;

/**
 * Created by Anthony on 7/8/2017.
 */
public class SSCCourseRetriever implements CourseRetriever {

    private static final String BASE_URL = "https://courses.students.ubc.ca/cs/main";

    private String sessyr;

    private String sesscd;

    public SSCCourseRetriever(String sessyr, String sesscd) {
        this.sessyr = sessyr;
        this.sesscd = sesscd;
    }

    @Override
    public Set<Course> retrieve(CourseString course) {
        Document document = retrieveDocument(course);
        return new SSCCourseDocumentParser().parseDocument(course, document);
    }

    private Document retrieveDocument(CourseString course) {
        try {
            return Jsoup.connect(BASE_URL).data(getParameterData(course)).timeout(5000).get();
        } catch (IOException e) {
            throw new RuntimeException(format("Failed to connect to {0}", BASE_URL), e);
        }
    }

    private Map<String, String> getParameterData(CourseString course) {
        return new SSCParametersDataBuilder()
                .dept(course.getDepartment())
                .courseId(course.getCourseId())
                .sessyr(sessyr)
                .sesscd(sesscd)
                .build();
    }

}
