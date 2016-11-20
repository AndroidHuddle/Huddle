package com.example.badhri.huddle;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.badhri.huddle.activities.MainActivity;
import com.example.badhri.huddle.networks.YelpClient;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.parseModels.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by badhri on 11/12/16.
 */

public class HuddleApplication extends Application {
    private AuthCallback authCallback;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        HuddleApplication.context = this;
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

                // storing phone numbers and username
                SharedPreferences mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("phoneNumber", phoneNumber);
                editor.apply();

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

    public static YelpClient getYelpRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, HuddleApplication.context);
    }
}
