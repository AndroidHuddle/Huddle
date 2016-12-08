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
    public static final String EVENT_DETAILS = "eventDetails";

//    taken from data received from Yelp if possible
    public static final String RATING = "rating";
    public static final String RATING_IMG_URL = "ratingImgUrl";
    public static final String MOBILE_URL = "mobileUrl";
    public static final String REVIEW_COUNT = "reviewCount";
    public static final String NAME_OF_LOCATION = "nameOfLocation";
    public static final String IMAGE_URL = "imageUrl";
    public static final String URL = "url";

    public static final String CROSS_STREET = "crossStreet";
    public static final String CITY = "city";
    public static final String DISPLAY_ADDRESS = "displayAddress";

    public String getCrossStreet() {return getString(CROSS_STREET);}
    public void setCrossStreet(String cs) {put(CROSS_STREET, cs);}

    public String getCity() {return getString(CITY);}
    public void setCity(String city ) { put(CITY, city);}

    public String getDisplayAddress() {return getString(DISPLAY_ADDRESS);}
    public void setDisplayAddress(String address) {put(DISPLAY_ADDRESS, address);}

    public double getRating() {return getDouble(RATING); }
    public void setRating( double rating) { put(RATING, rating); }

    public String getRatingImgUrl() {return getString(RATING_IMG_URL);}
    public void setRatingImgUrl(String url){ put(RATING_IMG_URL, url);}

    public String getMobileUrl() { return getString(MOBILE_URL);}
    public void setMobileUrl(String url){ put(MOBILE_URL, url);}

    public int getReviewCount() {return getInt(REVIEW_COUNT);}
    public void setReviewCount(int count) {put(REVIEW_COUNT, count);}

    public String getNameOfLocation() {return getString(NAME_OF_LOCATION);}
    public void setNameOfLocation(String name){put(NAME_OF_LOCATION, name);}

    public String getImageUrl() {return getString(IMAGE_URL);}
    public void setImageUrl(String url){put(IMAGE_URL, url);}

    public String getUrl() {return getString(URL);}
    public void setUrl(String url){put(URL, url);}

    public String getEventDetails() { return getString(EVENT_DETAILS); };
    public void setEventDetails(String details) { put(EVENT_DETAILS, details);}

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
    public String getStartDate() {return getString(EVENT_START_DATE);}

    public void setEndDate(String date) {put(EVENT_END_DATE, date);}
    public String getEndDate() {return getString(EVENT_END_DATE);}

    public void setEventStartTime(String time) {put(EVENT_START_TIME, time);}
    public String getEventStartTime() {return getString(EVENT_START_TIME);}

    public void setEventEndTime(String time) {put(EVENT_END_TIME, time);}
    public String getEventEndTime() {return getString(EVENT_END_TIME);}
}
