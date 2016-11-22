package com.example.badhri.huddle.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by victorhom on 11/21/16.
 */
public class EventActivityType implements Parcelable {
    public String getVenue() {
        return venue;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getEventType() {
        return eventType;
    }

    private String venue;
    private String eventName;
    private Date createdAt;
    private Date updatedAt;
    private String eventType;

    public EventActivityType(){}

    public static EventActivityType newInstance() {
        EventActivityType eAT = new EventActivityType();

        return eAT;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.venue);
        dest.writeString(this.eventName);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeString(this.eventType);
    }

    protected EventActivityType(Parcel in) {
        this.venue = in.readString();
        this.eventName = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.eventType = in.readString();
    }

    public static final Parcelable.Creator<EventActivityType> CREATOR = new Parcelable.Creator<EventActivityType>() {
        @Override
        public EventActivityType createFromParcel(Parcel source) {
            return new EventActivityType(source);
        }

        @Override
        public EventActivityType[] newArray(int size) {
            return new EventActivityType[size];
        }
    };
}
