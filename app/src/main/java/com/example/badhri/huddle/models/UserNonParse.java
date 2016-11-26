package com.example.badhri.huddle.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.badhri.huddle.parseModels.User;

/**
 * Created by victorhom on 11/25/16.
 */
public class UserNonParse implements Parcelable {

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    private String phoneNumber;

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    // no form currently to collect a user email
    private String email;

    public UserNonParse() {

    }

    public static UserNonParse fromUser(User user) {
        UserNonParse u = new UserNonParse();
        try {
            u.phoneNumber = user.getUsername();
        } catch (Exception e) {}

        try {
            u.username = user.getUsername();
        } catch (Exception e) {}

        try {
            u.email = user.getEmail();
        } catch (Exception e) {}

        return u;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phoneNumber);
        dest.writeString(this.username);
        dest.writeString(this.email);
    }

    protected UserNonParse(Parcel in) {
        this.phoneNumber = in.readString();
        this.username = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<UserNonParse> CREATOR = new Parcelable.Creator<UserNonParse>() {
        @Override
        public UserNonParse createFromParcel(Parcel source) {
            return new UserNonParse(source);
        }

        @Override
        public UserNonParse[] newArray(int size) {
            return new UserNonParse[size];
        }
    };
}
