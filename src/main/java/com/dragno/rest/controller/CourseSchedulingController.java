package com.dragno.rest.controller;


import com.dragno.rest.service.CourseSchedulingService;
import com.dragno.rest.service.exception.NoValidScheduleException;
import com.dragno.rest.service.model.CourseResults;
import com.dragno.rest.service.model.ScheduleOptions;
import com.dragno.rest.service.model.SchedulingError;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("scheduler")
public class CourseSchedulingController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response scheduleCourses(ScheduleOptions options) {
        String errorMessage = "";
        if(options.getCourses().isEmpty()) {
            errorMessage = "No courses selected";
        }
        if(options.getTerm() != 1 && options.getTerm() != 2) {
            errorMessage = "CourseSection term must be 1 or 2";
        }
        if(options.getDays().isEmpty()) {
            errorMessage = "No days selected";
        }
        if(options.getSesscd() != 'W' && options.getSesscd() != 'S') {
            errorMessage = "Sesscd must be W or S";
        }
        if(!errorMessage.isEmpty()) {
            return Response.status(400).entity(new SchedulingError(errorMessage)).build();
        }
        try {
            CourseResults results = new CourseResults(options.getSessyr(), options.getSesscd(),
                    CourseSchedulingService.getInstance().scheduleCourses(options));
            return Response.ok().entity(results).build();
        } catch (NoValidScheduleException | IllegalStateException e) {
            return Response.status(404).entity(new SchedulingError(e.getMessage())).build();
        }
    }
}
