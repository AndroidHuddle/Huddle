package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.badhri.huddle.HuddleApplication;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.TabFragmentAdapter;
import com.example.badhri.huddle.fragments.EventsFragment;
import com.example.badhri.huddle.models.EventNonParse;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.GPSTracker;
import com.example.badhri.huddle.utils.ParsePushHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DashboardActivity extends AppCompatActivity implements EventsFragment.OnCompleteEventClick {

    private UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getParcelableExtra("user");

        // check that the user is not null, user shouldn't be able to get this far
        if (user != null) {

            initialize();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //launch 'create event' part
                    Intent i = new Intent(DashboardActivity.this, SelectPlaceActivity.class);
                    // pass the user in the oncreate methods;
                    i.putExtra("user", user);
                    startActivity(i);
                }
            });

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            TabFragmentAdapter tfa = new TabFragmentAdapter(getSupportFragmentManager(), this);
            if (user != null) {
                tfa.setUser(user);
            }
            viewPager.setAdapter(tfa);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
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
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    private void initialize() {
        // get the user's current location and add to parse; this value should be updated
        // right before the create event activity
        GPSTracker gpsTracker = new GPSTracker(this);
        final Double latitude = gpsTracker.getLatitude();
        final Double longitude = gpsTracker.getLongitude();

        // note that the phone number on the shared preferences has a + as a prefix to the phone number

        SharedPreferences mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
        final String phonenumber = mSettings.getString("phoneNumber", null);
        final String username = mSettings.getString("username", null);

        ParseQuery query = new ParseQuery("User");
        query.whereEqualTo("phoneNumber", Long.valueOf(phonenumber.substring(1)));
        query.findInBackground(new FindCallback() {

            @Override
            public void done(Object o, Throwable throwable) {
                if (throwable == null) {
                    List<User> users = (List<User>) o;
                    User u = users.get(0);
                    u.setPhoneNumber(Long.valueOf(phonenumber.substring(1)));
                    u.setUsername(username);
                    u.setLatitude(latitude);
                    u.setLongitude(longitude);
                    try {
                        u.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    user = UserNonParse.fromUser(u);

                    ParsePush.subscribeInBackground(HuddleApplication.CHANNEL_NAME);

                    ParsePushHelper.pushToUser("badhri", "You have a new friend request", "Parse");
                }
            }

            @Override
            public void done(List objects, ParseException e) {

            }
        });
//        User u = new User();

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
                            Toast.makeText(LoginActivity.this, "Successfully created message on Parse",
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
                            Toast.makeText(LoginActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Badhri", "Failed to save message", e);
                        }
                    }
                });

              */
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            // This is the up button
            case R.id.miProfile:
                i = new Intent(this, ProfileActivity.class);
                i.putExtra(ProfileActivity.USER_ARG, user);
                startActivity(i);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            case R.id.miFriends:
                i = new Intent(this, FriendListActivity.class);
                i.putExtra(ProfileActivity.USER_ARG, user);
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
//        Log.d("DEBUG", event.toString());
//        Log.d("DEBUG", String.valueOf(tabIndex));
//        Log.d("DEBUG", event.getVenue());

        // open up the event details activity
        EventNonParse eventNonParse = EventNonParse.fromEvent(event);
        Intent i = new Intent(DashboardActivity.this, EventDetailActivity.class);
        i.putExtra("user", user);
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
//                    Log.d("DEBUG", businessesJson.toString());
//                    ArrayList<Business> businesses = Business.fromJSONArray(businessesJson);
//                    Log.d("DEBUG", String.valueOf(statusCode));
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString());
                Log.e("ERROR", statusCode + "");
                Log.e("ERROR", headers.toString());
                Log.e("ERROR", throwable.toString());
            }

        };
    }
}
