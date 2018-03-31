package com.dragno.rest.controller;


import com.dragno.rest.service.CourseSchedulingService;
import com.dragno.rest.service.exception.NoValidScheduleException;
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
        if(options.getTerm() != 1 && options.getTerm() != 2) {
            errorMessage = "Course term must be 1 or 2";
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
            return Response.ok().entity(CourseSchedulingService.getInstance().scheduleCourses(options)).build();
        } catch (InterruptedException e) {
            return Response.status(500).entity(new SchedulingError("Scheduling was interrupted")).build();
        } catch (NoValidScheduleException e) {
            return Response.status(404).entity(new SchedulingError("No valid schedule is possible for the criteria"))
                    .build();
        }
    }
}
