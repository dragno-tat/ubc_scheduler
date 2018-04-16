package com.dragno.rest.service.scheduler.ssc;

import com.dragno.rest.service.model.*;
import com.dragno.rest.service.model.builder.CourseBuilder;
import com.google.common.collect.Sets;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalTime;
import java.util.Set;

/**
 * Created by Anthony on 7/8/2017.
 */
class SSCCourseDocumentParser {

    Set<CourseSection> parseDocument(CourseString courseString, Document document) {
        Set<CourseSection> sections = Sets.newHashSet();
        Elements trTags = document.getElementsByTag("tr");
        CourseSection previousSection = null;
        for (Element tr : trTags) {
            if (isTrForCourse(tr)) {
                previousSection = handleCourseParsing(courseString, sections, previousSection, tr);
            }
        }
        return sections;
    }

    private CourseSection handleCourseParsing(CourseString courseString, Set<CourseSection> sections, CourseSection previousSection,
                                       Element tr) {
        CourseBuilder builder = new CourseBuilder()
                .id(Integer.parseInt(courseString.getCourseId()))
                .dept(courseString.getDepartment());

        if (parseCourse(builder, tr)) {
            if (isPartOfPreviousSection(builder.getSection())){
                Schedule mergedSchedule = previousSection.getSchedule().or(builder.getSchedule());
                builder.schedule(mergedSchedule).section(previousSection.getSection());
                sections.remove(previousSection);
            }
            CourseSection section = builder.build();
            sections.add(section);
            previousSection = section;
        }
        return previousSection;
    }

    private boolean isTrForCourse(Element tr) {
        return tr.className().contains("section");
    }

    private boolean parseCourse(CourseBuilder builder, Element tr) {
        Elements tdTags = tr.getElementsByTag("td");
        try {
            builder.status(parseStatus(tdTags.get(0)))
                   .section(parseSection(tdTags.get(1)))
                   .activity(parseActivity(tdTags.get(2)))
                   .term(parseTerm(tdTags.get(3)))
                   .schedule(createSchedule(tdTags));
        } catch (Exception e) {
            //silently ignore a course that can't be parsed; TODO log?
            return false;
        }
        return true;
    }

    private String parseSection(Element element) {
        String[] split = element.text().split("\\s+");
        return split[split.length - 1];
    }

    private Status parseStatus(Element element) {
        return Status.fromStatusString(element.text());
    }

    private Activity parseActivity(Element element) {
        return Activity.fromActivityString(element.text());
    }

    private int parseTerm(Element element) {
        return Integer.parseInt(element.text());
    }

    private Schedule createSchedule(Elements tdTags) {
        return new Schedule(parseDays(tdTags.get(5)), parseTime(tdTags.get(6)), parseTime(tdTags.get(7)));
    }

    private Set<Day> parseDays(Element element) {
        Set<Day> days = Sets.newHashSet();
        String[] dayStrings = element.text().split("\\s+");
        for(String dayString : dayStrings){
            days.add(Day.fromDayString(dayString));
        }
        return days;
    }

    private LocalTime parseTime(Element element) {
        String[] strings = element.text().split(":");
        return LocalTime.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
    }

    private boolean isPartOfPreviousSection(String section) {
        return section.isEmpty();
    }
}
