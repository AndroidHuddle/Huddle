package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.fragments.EventDetailFragment;
import com.example.badhri.huddle.models.EventNonParse;
import com.example.badhri.huddle.models.UserNonParse;

public class EventDetailActivity extends AppCompatActivity {

    private UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // placeholder - the idea is that the previous activity (home screen) will pass
        // both the event and attendees object here
        // Event event = (Event) getIntent().getParcelableExtra("EVENT");
        // Attendees  attendees = (Attendees) getIntent().getParcelableExtra("ATTENDEES");

        // add the event details fragment
        // note: will need data from event and attendees object;
        // this data should be received here and taken to pass into the
        //
        EventNonParse event = getIntent().getParcelableExtra("event");
        UserNonParse user = getIntent().getParcelableExtra("user");
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putParcelable("user", user);
        EventDetailFragment edf = new EventDetailFragment();
        edf.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.event_details, edf);
        ft.commit();
    }

}
