package com.example.badhri.huddle;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.badhri.huddle.activities.DashboardActivity;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.networks.YelpClient;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.parseModels.FriendRequest;
import com.example.badhri.huddle.parseModels.Friends;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.GPSTracker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.interceptors.ParseLogInterceptor;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by badhri on 11/12/16.
 */


public class HuddleApplication extends Application {
    private AuthCallback authCallback;
    public static Context context;
    final public static String TAG = "Huddle";
    final public static  String CHANNEL_NAME = "huddle";

    private String phonenumber;
    private UserNonParse user;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HuddleApplication.context = this;
        //Register all Parse models here
        ParseObject.registerSubclass(Events.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Attendees.class);
        ParseObject.registerSubclass(Friends.class);
        ParseObject.registerSubclass(FriendRequest.class);
        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("androidhuddle") // should correspond to APP_ID env variable
                .clientKey("jargon")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://androidhuddle.herokuapp.com/parse").build());


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
//                Toast.makeText(getBaseContext(), "login successful", Toast.LENGTH_LONG).show();

                // storing phone numbers and username
                SharedPreferences mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor = mSettings.edit();
                phonenumber = phoneNumber.toString();
                editor.putString("phoneNumber", phoneNumber);
                editor.apply();

                // We are using the fields in the installation instance to bypass the parse
                // authentication. So the user gets identified using the phoneNumber
                // registered here.
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                // This SHOULD BE in sync with the phoneNumber format in the User table.
                installation.put("phoneNumber", phonenumber.substring(1));
                installation.saveInBackground();
                // at this point, the username hasn't been picked out yet

                // need to set up the user before starting the new activity
                // but setUpUser is asynchronous
                setUpUser();
                // a user should be set for sure before passing further
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Toast.makeText(getBaseContext(), "login NOT successful", Toast.LENGTH_LONG).show();
                //Log.d("Digits", "Sign in with Digits failure", exception);
            }
        };
    }

    private void launchDashboard() {
        Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
        intent.putExtra("user", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//I don't really get why this is needed
        startActivity(intent);

    }

    // find user if user exists, otherwise, set up a new record for user on Parse
    public void setUpUser() {
        ParseQuery query = new ParseQuery("User");
        query.whereEqualTo("phoneNumber", Long.valueOf(phonenumber.substring(1)));
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (throwable == null) {
                    List<User> users = (List<User>) o;
                    for (int i = 0; i < users.size(); i++) {
                        String parsePhoneNumber = String.valueOf(users.get(i).getPhoneNumber());
                        if (parsePhoneNumber.equals(phonenumber.substring(1))) {
                            // we know of this user
                            // turn the parseModel to a UserNonParse instance to pass to next activity
                            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                            Double latitude = gpsTracker.getLatitude();
                            Double longitude = gpsTracker.getLongitude();
                            // updating coordinates for the existing user
                            users.get(i).setLatitude(latitude);
                            users.get(i).setLongitude(longitude);

                            user = UserNonParse.fromUser(users.get(i));
//                            System.out.println("get the parse user id: ");
//                            System.out.println(user.getParseId());
                            try {
                                users.get(i).save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // did not find user in parse
                    // create a row for user in parse and update backend
                    // create a UserNonParse instance
                    // this will be performed in the main activity
                    launchDashboard();
                } else {
                    Log.d("DEBUG", throwable.toString());
                }
            }
        });
    }


    public AuthCallback getAuthCallback(){
        return authCallback;
    }

    public static YelpClient getYelpRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, HuddleApplication.context);
    }
}
