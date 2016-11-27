package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.badhri.huddle.HuddleApplication;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.GPSTracker;
import com.example.badhri.huddle.utils.ParsePushHelper;
import com.parse.ParseException;
import com.parse.ParsePush;

public class
MainActivity extends AppCompatActivity {
    UserNonParse user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the user's current location and add to parse; this value should be updated
        // right before the create event activity
        GPSTracker gpsTracker = new GPSTracker(this);
        Double latitude = gpsTracker.getLatitude();
        Double longitude = gpsTracker.getLongitude();

        // note that the phone number on the shared preferences has a + as a prefix to the phone number
        user = getIntent().getParcelableExtra("user");
        if (user == null) {
            SharedPreferences mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
            String phonenumber = mSettings.getString("phoneNumber", null);
            String username = mSettings.getString("username", null);

            User u = new User();
            u.setPhoneNumber(Long.valueOf(phonenumber.substring(1)));
            u.setUsername(username);
            u.setLatitude(latitude);
            u.setLongitude(longitude);
            try {
                u.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            UserNonParse user = UserNonParse.fromUser(u);

        }

//        Log.d("DEBUG", "getting user in the main activity");




        Button post = (Button)findViewById(R.id.button);
        ParsePush.subscribeInBackground(HuddleApplication.CHANNEL_NAME);

        post.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
//                how do we want to handle this page
                // are we going to move it out or is there something in mind for this page
                // otherwise, in the previous screen that transitions to this, need to make sure we pass the user in
                Intent i = new Intent(MainActivity.this, DashBoard.class);
                i.putExtra("user", user);
                startActivity(i);

                ParsePushHelper.pushToUser("badhri", "You have a new freing request", "Parse");
                /*Events event = new Events();
                event.setVenue("new york" + new Random().nextInt(50) + 1);
                event.setEventName("test" + new Random().nextInt(50) + 1);
                event.setStartTime(new Date());
                event.setEndTime(new Date());
                event.setOwner("dzdUbFloUf");
                event.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(MainActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Badhri", "Failed to save message", e);
                        }
                    }
                }); */

                //{"Attending", "Not Responded", "Not Attending"};
                /*Attendees attendees = new Attendees();
                attendees.setEvent("avJiF7VvN9");
                attendees.setUser("dzdUbFloUf");
                attendees.setStatus("Attending");
                attendees.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(MainActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Badhri", "Failed to save message", e);
                        }
                    }
                });

              */


            }
        });
    }
}
