package com.example.badhri.huddle.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

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

    //public static Boolean inRange(double latitude1, double longitude1, double latitude2, double longitude2, float range) {
    public static Boolean inRange(LatLng position1, LatLng position2, float range) {

/*        float[] results = new float[3];
        Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, results);
        float distanceInMeters = results[0];
        boolean isInRange = distanceInMeters < range;
        return isInRange;*/

        return SphericalUtil.computeDistanceBetween(position1, position2) < range;
        //return true;
    }

}
