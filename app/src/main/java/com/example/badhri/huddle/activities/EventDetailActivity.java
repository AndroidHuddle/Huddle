package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.fragments.EventDetailFragment;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // placeholder - the idea is that the previous activity (home screen) will pass
        // both the event and attendees object here
        // Event event = (Event) getIntent().getParcelableExtra("EVENT");
        // Attendees  attendees = (Attendees) getIntent().getParcelableExtra("ATTENDEES");

        // add the event details fragment
        // note: will need data from event and attendees object;
        // this data should be received here and taken to pass into the
        //


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.event_details, new EventDetailFragment());
        ft.commit();
    }

}
