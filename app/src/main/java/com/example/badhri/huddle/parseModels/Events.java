package com.example.badhri.huddle.parseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by badhri on 11/12/16.
 */

@ParseClassName("Events")
public class Events extends ParseObject {
    public static final String VENUE_KEY = "venue";
    public static final String EVENT_NAME_KEY = "eventName";
    public static final String START_TIME_KEY = "startTime";
    public static final String END_TIME_KEY = "endTime";
    public static final String OWNER_KEY = "owner";

    public String getVenue() {
        return getString(VENUE_KEY);
    }

    public void setVenue(String venue)  {
        put(VENUE_KEY, venue);
    }
    public String getEventName() {
        return getString(EVENT_NAME_KEY);
    }

    public void setEventName(String eventName)  {
        put(EVENT_NAME_KEY, eventName);
    }

    public Date getStartTime() {
        if (getDate(START_TIME_KEY) != null) {
            return getDate(START_TIME_KEY);
        }
        return new Date();
    }

    public void setStartTime(Date date)  {
        put(START_TIME_KEY, date);
    }

    public Date getEndTime() {
        if (getDate(END_TIME_KEY) != null) {
            return getDate(END_TIME_KEY);
        }
        return new Date();
    }

    public void setEndTime(Date date)  {
        put(END_TIME_KEY, date);
    }

    public String getOwner() {
        return getString(OWNER_KEY);
    }

    public void setOwner(String objectID) {
        put(OWNER_KEY, objectID);
    }
}
