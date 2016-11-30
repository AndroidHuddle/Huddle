package com.example.badhri.huddle.parseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by badhri on 11/28/16.
 */

@ParseClassName("FriendRequest")
public class FriendRequest extends ParseObject {
    public static final String FROM_KEY = "from";
    public static final String TO_KEY = "to";
    public static final String STATUS_KEY = "status";

    public String getFromUser() {
        return getString(FROM_KEY);
    }

    public void setFromUser(String userObjectId)  {
        put(FROM_KEY, userObjectId);
    }

    public String getToUser() {
        return getString(TO_KEY);
    }

    public void setToUser(String friendObjectId )  {
        put(TO_KEY, friendObjectId);
    }

    public String getStatus() {
        return getString(STATUS_KEY);
    }

    public void setStatus(String friendObjectId )  {
        put(STATUS_KEY, friendObjectId);
    }
}