package com.example.badhri.huddle;

import android.app.Application;
import android.content.Context;

import com.example.badhri.huddle.networks.YelpClient;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.parseModels.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by badhri on 11/12/16.
 */

public class HuddleApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //Register all Parse models here
        ParseObject.registerSubclass(Events.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Attendees.class);
        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("androidhuddle") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://androidhuddle.herokuapp.com/parse").build());
        HuddleApplication.context = this;
    }

    public static YelpClient getYelpRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, HuddleApplication.context);
    }
}
