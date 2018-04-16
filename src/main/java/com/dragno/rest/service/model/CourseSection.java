package com.dragno.rest.service.model;

import com.dragno.rest.util.ScheduleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;

/**
 * Created by Anthony on 7/8/2017.
 */
public class CourseSection {

    private String dept;
    private int id;
    private Status status;
    private String section;
    private Activity activity;
    private int term;
    @JsonSerialize(using = ScheduleSerializer.class)
    private Schedule schedule;

    public CourseSection(String dept, int id, Status status, String section,
                  Activity activity, int term, Schedule schedule) {
        this.dept = dept;
        this.id = id;
        this.status = status;
        this.section = section;
        this.activity = activity;
        this.term = term;
        this.schedule = schedule;
    }

    public String getDept() {
        return dept;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getSection() {
        return section;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getTerm() {
        return term;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseSection section = (CourseSection) o;

        if (id != section.id) {
            return false;
        }
        if (term != section.term) {
            return false;
        }
        if (!dept.equals(section.dept)) {
            return false;
        }
        if (status != section.status) {
            return false;
        }
        if (!this.section.equals(section.section)) {
            return false;
        }
        if (activity != section.activity) {
            return false;
        }
        return schedule.equals(section.schedule);
    }

    @Override
    public int hashCode() {
        int result = dept.hashCode();
        result = 31 * result + id;
        result = 31 * result + term;
        result = 31 * result + status.hashCode();
        result = 31 * result + section.hashCode();
        result = 31 * result + activity.hashCode();
        result = 31 * result + schedule.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("dept=", dept)
                          .add("id=", id)
                          .add("status=", status)
                          .add("section=", section)
                          .add("activity=", activity)
                          .add("term=", term)
                          .add("schedule=", schedule)
                          .toString();
    }
}
