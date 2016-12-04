package com.example.badhri.huddle.utils;

import com.google.android.gms.maps.model.LatLng;


public class FriendType {
    private String username;
    private LatLng latLng;

    public FriendType(String username, LatLng latLng) {
        this.username = username;
        this.latLng = latLng;
    }

    public double getLatitude() {
        return latLng.latitude;
    }

    public double getLongitude() {
        return latLng.longitude;
    }

    public String getUsername(){
        return username;
    }

}
