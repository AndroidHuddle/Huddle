package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.TabFragmentAdapter;
import com.example.badhri.huddle.fragments.EventsFragment;
import com.example.badhri.huddle.parseModels.Events;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DashBoard extends AppCompatActivity implements EventsFragment.OnCompleteEventClick{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch 'create event' part
                Intent i = new Intent(DashBoard.this, CreateEventActivity.class);
                startActivity(i);
            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Want to see if it works
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                YelpClient c = HuddleApplication.getYelpRestClient();
//                c.search("restaurants", "San Francisco", searchHandler());
//            }
//        }, 4000);
    }


    // get the event up to the activity level for best practice of communication
    @Override
    public void onEventPress(int tabIndex, Events event) {
        Log.d("DEBUG", event.toString());
        Log.d("DEBUG", String.valueOf(tabIndex));
        Log.d("DEBUG", event.getVenue());
    }

    // this is mainly to show how it works
    private JsonHttpResponseHandler searchHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray businessesJson = response.getJSONArray("businesses");
                    Log.d("DEBUG", businessesJson.toString());
//                    ArrayList<Business> businesses = Business.fromJSONArray(businessesJson);
                    Log.d("DEBUG", String.valueOf(statusCode));
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString());
                Log.e("ERROR", statusCode+"");
                Log.e("ERROR", headers.toString());
                Log.e("ERROR", throwable.toString());
            }

        };
    }
}
