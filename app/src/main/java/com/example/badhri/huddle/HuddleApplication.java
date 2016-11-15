package com.example.badhri.huddle;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.example.badhri.huddle.activities.MainActivity;
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
    private AuthCallback authCallback;

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
        setupCallbackforLogin();
    }

    private void setupCallbackforLogin() {
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY,
                BuildConfig.CONSUMER_SECRET);

        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());


        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session
                Toast.makeText(getBaseContext(), "login successful", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//I don't really get why this is needed
                startActivity(intent);
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Toast.makeText(getBaseContext(), "login NOT successful", Toast.LENGTH_LONG).show();
                //Log.d("Digits", "Sign in with Digits failure", exception);
            }
        };
    }


    public AuthCallback getAuthCallback(){
        return authCallback;
    }
}
