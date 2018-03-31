package com.dragno.rest.service.model;

/**
 * Created by Anthony on 7/8/2017.
 */
public enum Status {
    NA("N/A"), FULL("Full"), RESTRICTED("Restricted"), STT("STT");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public static Status fromStatusString(String statusString){
        for(Status status : values()){
            if(status.status.equalsIgnoreCase(statusString)){
                return status;
            }
        }
        return NA;
    }
}
