package com.example.badhri.huddle.parseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by badhri on 11/13/16.
 */


@ParseClassName("Attendees")
public class Attendees extends ParseObject {
    public static final String EVENT_KEY = "event";
    public static final String  USER_KEY = "user";
    public static final String STATUS_KEY = "status";

    public String getEvent() {
        return getString(EVENT_KEY);
    }

    public void setEvent(String eventid)  {
        put(EVENT_KEY, eventid);
    }
    public String getUser() {
        return getString(USER_KEY);
    }

    public void setUser(String userid) {
        put(USER_KEY, userid);
    }

    public String getStatus() {
        return getString(STATUS_KEY);
    }

    public void setStatus(String status) {
        put(STATUS_KEY, status);
    }
}
