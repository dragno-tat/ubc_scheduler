package com.dragno.rest.service.model.builder;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by Anthony on 7/8/2017.
 */
public class SSCParametersDataBuilder {

    private String pname;

    private String tname;

    private String req;

    private String dept;

    private String course;

    private int sessyr;

    private char sesscd;

    public SSCParametersDataBuilder dept(String dept) {
        this.dept = dept;
        return this;
    }

    public SSCParametersDataBuilder courseId(String courseId) {
        this.course = courseId;
        return this;
    }

    public SSCParametersDataBuilder sessyr(int sessyr) {
        this.sessyr = sessyr;
        return this;
    }

    public SSCParametersDataBuilder sesscd(char sesscd) {
        this.sesscd = sesscd;
        return this;
    }

    public Map<String, String> build() {
        return new ImmutableMap.Builder<String, String>()
                .put("pname", "subjarea")
                .put("tname", "subjarea")
                .put("req", "3")
                .put("dept", dept)
                .put("course", course)
                .put("sessyr", Integer.toString(sessyr))
                .put("sesscd", Character.toString(sesscd))
                .build();
    }
}
