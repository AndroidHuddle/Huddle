package com.example.badhri.huddle.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by victorhom on 11/13/16.
 */
public class User implements Parcelable {

    private String name;

    public User(){}

    private static User fromJson(JSONObject jsonObject) {
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
        } catch(Exception e){}

        return u;
    }

    public static User randomUser(){
        User u = new User();
        u.name = "Random Name";
        return u;
    }

    public static ArrayList<User> fromJSONArray(JSONArray users) {
        ArrayList<User> u = new ArrayList<>();
        int length = users.length();
        for (int i = 0; i < length; i++) {
            try {
                u.add(fromJson(users.getJSONObject(i)));
            } catch(Exception e) {
                continue;
            }
        }
        return u;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected User(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
