package com.example.badhri.huddle.utils;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by victorhom on 11/28/16.
 */
public class LocationCalculator {

    // the range is given in feet
    // if you want to convert to feet for ease
    public static double feetToMeter(int value) {
        double feetDouble = Double.valueOf(value);
        double meters = feetDouble/3.2808;
        return meters;
    }

    public static Boolean inRange(Place l, String latitude, String longitude, int range) {
        LatLng center = l.getLatLng();
        float[] results = new float[1];
        Location.distanceBetween(center.latitude,center.longitude, Double.valueOf(latitude), Double.valueOf(longitude), results);
        float distanceInMeters = results[0];
        boolean isInRange = distanceInMeters < range;
        return isInRange;
    }

}
