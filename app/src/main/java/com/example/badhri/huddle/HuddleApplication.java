package com.example.badhri.huddle;

import android.app.Application;

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
                .clientKey("jargon")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://androidhuddle.herokuapp.com/parse/").build());
        //http://yourappname.herokuapp.com/parse
        //http://androidhuddle.herokuapp.com/parse
        //Ignoring header X-Parse-Client-Key because its value was null.
    }
}
