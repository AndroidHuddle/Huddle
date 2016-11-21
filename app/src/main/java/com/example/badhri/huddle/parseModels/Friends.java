package com.example.badhri.huddle.parseModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by badhri on 11/20/16.
 */

@ParseClassName("Friends")
public class Friends extends ParseObject {
    public static final String USER_KEY = "user";
    public static final String FRIEND_KEY = "friend";

    public String getUserId() {
        return getString(USER_KEY);
    }

    public void setUserId(String userObjectId)  {
        put(USER_KEY, userObjectId);
    }

    public String getFriendId() {
        return getString(FRIEND_KEY);
    }

    public void setFriendId(String friendObjectId )  {
        put(FRIEND_KEY, friendObjectId);
    }
}
