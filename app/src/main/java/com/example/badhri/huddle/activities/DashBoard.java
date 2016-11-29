package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.TabFragmentAdapter;
import com.example.badhri.huddle.fragments.EventsFragment;
import com.example.badhri.huddle.models.EventNonParse;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Events;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DashBoard extends AppCompatActivity implements EventsFragment.OnCompleteEventClick{

    private UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getParcelableExtra("user");
        // check that the user is not null, it shouldn't be able to get this far as being null.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch 'create event' part
                Intent i = new Intent(DashBoard.this, SelectPlaceActivity.class);
                // pass the user in the oncreate methods;
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            // This is the up button
            case R.id.miProfile:
                i = new Intent(this, ProfileActivity.class);
                i.putExtra(ProfileActivity.USER_ARG, "B2wtCIzbY2");
                startActivity(i);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            case R.id.miFriends:
                i = new Intent(this, FriendListActivity.class);
                i.putExtra(ProfileActivity.USER_ARG, "B2wtCIzbY2");
                startActivity(i);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // get the event up to the activity level for best practice of communication
    @Override
    public void onEventPress(int tabIndex, Events event) {
        Log.d("DEBUG", event.toString());
        Log.d("DEBUG", String.valueOf(tabIndex));
        Log.d("DEBUG", event.getVenue());

        // open up the event details activity
        EventNonParse eventNonParse = EventNonParse.fromEvent(event);
        Intent i = new Intent(DashBoard.this, EventDetailActivity.class);
        i.putExtra("event", eventNonParse);
        startActivity(i);
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
