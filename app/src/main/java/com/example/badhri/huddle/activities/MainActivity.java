package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.badhri.huddle.HuddleApplication;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.Utils.ParsePushHelper;
import com.parse.ParsePush;

public class
MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button post = (Button)findViewById(R.id.button);
        ParsePush.subscribeInBackground(HuddleApplication.CHANNEL_NAME);

        post.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DashBoard.class);
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
