package com.dragno.rest.service.model.builder;

import com.dragno.rest.service.model.Activity;
import com.dragno.rest.service.model.CourseSection;
import com.dragno.rest.service.model.Schedule;
import com.dragno.rest.service.model.Status;

/**
 * Created by Anthony on 7/8/2017.
 */
public class CourseSectionBuilder {

    private String dept;
    private int id;
    private Status status;
    private String section;
    private Activity activity;
    private int term;
    private Schedule schedule;

    public CourseSectionBuilder dept(String dept) {
        this.dept = dept;
        return this;
    }

    public CourseSectionBuilder id(int id) {
        this.id = id;
        return this;
    }

    public CourseSectionBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public CourseSectionBuilder section(String section) {
        this.section = section;
        return this;
    }

    public CourseSectionBuilder activity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public CourseSectionBuilder term(int term) {
        this.term = term;
        return this;
    }

    public CourseSectionBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getSection() {
        return section;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public CourseSection build() {
        validateFields();
        return new CourseSection(dept, id, status, section, activity, term, schedule);
    }

    private void validateFields() {
        if (dept == null || id == 0 || status == null || section == null || activity == null || term == 0 ||
                schedule == null) {
            throw new IllegalStateException("All fields must be set to build a CourseSection");
        }
    }
}
