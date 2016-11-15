package com.example.badhri.huddle;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.badhri.huddle.networks.YelpClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by victorhom on 11/12/16.
 */
public class MainClientApp extends Application {
    private static Context context;
    private AuthCallback authCallback;


    @Override
    public void onCreate() {
        super.onCreate();
        MainClientApp.context = this;

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




    public static YelpClient getYelpRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, MainClientApp.context);
    }


}
