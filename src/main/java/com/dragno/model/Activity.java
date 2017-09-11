package com.dragno.model;

/**
 * Created by Anthony on 7/8/2017.
 */
public enum Activity {
    LECTURE("Lecture"), LAB("Laboratory"), TUTORIAL("Tutorial"), DISCUSSION("Discussion");

    private String activity;

    Activity(String activity) {
        this.activity = activity;
    }

    public static Activity fromActivityString(String activityString) throws EnumConstantNotPresentException {
        for(Activity activity : values()){
            if(activity.activity.equalsIgnoreCase(activityString)){
                return activity;
            }
        }
        throw new EnumConstantNotPresentException(Activity.class, activityString);
    }
}
