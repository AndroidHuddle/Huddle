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

    public static final String EVENT_START_DATE = "eventStartDate";
    public static final String EVENT_END_DATE = "eventStartDate";
    public static final String EVENT_START_TIME = "eventStartTime";
    public static final String EVENT_END_TIME = "eventEndTime";

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
        return getDate(START_TIME_KEY);
    }

    public void setStartTime(Date date)  {
        put(START_TIME_KEY, date);
    }

    public Date getEndTime() {
        return getDate(END_TIME_KEY);
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

    public void setStartDate(String date) {put(EVENT_START_DATE, date);}
    public void getStartDate() {getString(EVENT_START_DATE);}

    public void setEndDate(String date) {put(EVENT_END_DATE, date);}
    public void getEndDate() {getString(EVENT_END_DATE);}

    public void setEventStartTime(String time) {put(EVENT_START_TIME, time);}
    public void getEventStartTime() {getString(EVENT_START_TIME);}

    public void setEventEndTime(String time) {put(EVENT_END_TIME, time);}
    public void getEventEndTime() {getString(EVENT_END_TIME);}
}
