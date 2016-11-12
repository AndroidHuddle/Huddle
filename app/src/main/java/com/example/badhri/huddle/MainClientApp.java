package com.example.badhri.huddle;

import android.app.Application;
import android.content.Context;

import com.example.badhri.huddle.networks.YelpClient;

/**
 * Created by victorhom on 11/12/16.
 */
public class MainClientApp extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MainClientApp.context = this;

    }

    public static YelpClient getYelpRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, MainClientApp.context);
    }
}
