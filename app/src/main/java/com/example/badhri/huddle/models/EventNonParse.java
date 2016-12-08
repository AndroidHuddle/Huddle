package com.example.badhri.huddle.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.badhri.huddle.parseModels.Events;

import java.util.Date;

/**
 * Created by victorhom on 11/20/16.
 */
// the purpose of this is to avoid passing parse objects around
// converting them to normal model objects
public class EventNonParse implements Parcelable {
    private String venue;
    private String eventName;
    private Date createdAt;
    private Date updatedAt;

    private String imageUrl;
    private String eventDetails;
    private String owner;
    private String crossStreet;
    private String city;
    private String displayAddress;

    public String getCrossStreet() {
        return crossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

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

    public Date getEndTime() {
        return endTime;
    }

    private Date endTime;

    public EventNonParse(){}

    public static EventNonParse fromEvent(Events parseEvent) {
        EventNonParse event = new EventNonParse();
        try {
            event.venue = parseEvent.getVenue();
        } catch (Exception e) {}

        try {
            event.eventName = parseEvent.getEventName();
        } catch (Exception e) {}

        try {
            event.createdAt = parseEvent.getCreatedAt();
        } catch (Exception e) {}

        try {
            event.updatedAt = parseEvent.getUpdatedAt();
        } catch (Exception e) {}

        try {
            event.endTime = parseEvent.getEndTime();
        } catch (Exception e) {}

        try {
            event.eventId = parseEvent.getObjectId();
        } catch (Exception e) {}

        try {
            event.imageUrl = parseEvent.getImageUrl();
        } catch (Exception e) {}

        try {
            event.eventDetails = parseEvent.getEventDetails();
        } catch (Exception e) {}

        try {
            event.owner = parseEvent.getOwner();
        } catch (Exception e) {}

        try {
            event.crossStreet = parseEvent.getCrossStreet();
        } catch (Exception e) {}

        try {
            event.city = parseEvent.getCity();
        } catch (Exception e) {}

        try {
            event.displayAddress = parseEvent.getDisplayAddress();
        } catch (Exception e) {}
        return event;
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
        dest.writeString(this.imageUrl);
        dest.writeString(this.eventDetails);
        dest.writeString(this.owner);
        dest.writeString(this.crossStreet);
        dest.writeString(this.city);
        dest.writeString(this.displayAddress);
        dest.writeString(this.eventId);
        dest.writeLong(this.endTime != null ? this.endTime.getTime() : -1);
    }

    protected EventNonParse(Parcel in) {
        this.venue = in.readString();
        this.eventName = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.imageUrl = in.readString();
        this.eventDetails = in.readString();
        this.owner = in.readString();
        this.crossStreet = in.readString();
        this.city = in.readString();
        this.displayAddress = in.readString();
        this.eventId = in.readString();
        long tmpEndTime = in.readLong();
        this.endTime = tmpEndTime == -1 ? null : new Date(tmpEndTime);
    }

    public static final Creator<EventNonParse> CREATOR = new Creator<EventNonParse>() {
        @Override
        public EventNonParse createFromParcel(Parcel source) {
            return new EventNonParse(source);
        }

        @Override
        public EventNonParse[] newArray(int size) {
            return new EventNonParse[size];
        }
    };
}
