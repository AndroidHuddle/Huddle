package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.ShowPlacesPagerAdapter;
import com.example.badhri.huddle.fragments.FriendsFragment;
import com.example.badhri.huddle.fragments.PlacesFragment;
import com.example.badhri.huddle.models.UserNonParse;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

// the event location and the user invite list has to bubble back up to SelectPlaceActivity
// to send to the final details page
public class SelectPlaceActivity extends AppCompatActivity implements PlacesFragment.OnCompleteEventClick, FriendsFragment.OnCheckCompleteDetails{

    private ViewPager vpPager;
    private UserNonParse user;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // in the current version, the UserNonParse doesn't have support to hold lat long
        // but that is easy to fix
        // or you can just use the helper method
        user = getIntent().getParcelableExtra("user");
        setupPagedFragments();
    }

    private void setupPagedFragments() {
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        ShowPlacesPagerAdapter a = new ShowPlacesPagerAdapter(getSupportFragmentManager());
        a.setUser(user);

        //Set number of pages that should be retained to either side of the current page in the view hierarchy in an idle state.
        vpPager.setOffscreenPageLimit(1);

        vpPager.setAdapter(a);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabstrip);
        tabStrip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        tabStrip.setTextSize(45);
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public void onEventSelect(Place place) {
        // this will register when an event location is picked
        // will update to be an actual event object
        // if the location isn't actually a place that has a name
        // the resulting string will be like so 40°43'11.7"N 73°59'13.4"W
        // that isn't ideal so I should get an event object back with some street address
        Log.d("DEBUG", "just checking things are clicked");
        Log.d("DEBUG", place.toString());
        this.place = place;
    }

    @Override
    public void getInviteList(ArrayList<String> invitees) {
        // this is what the user will hit when they want to verify the details of the event
        // open up a new activity with the final details
        Log.d("DEBUG", "inside the select place activity");
        Log.d("DEBUG", invitees.toString());
        Intent i = new Intent(SelectPlaceActivity.this, EditEventActivity.class);
        // should pass more than just the place
        try {i.putExtra("name", place.getName()); } catch (Exception e){}
        try {i.putExtra("latitude", String.valueOf(place.getLatLng().latitude));} catch (Exception e){}
        try {i.putExtra("longitude", String.valueOf(place.getLatLng().longitude));} catch (Exception e){}
        try {i.putExtra("address", place.getAddress());} catch (Exception e){}
        try {i.putExtra("price", place.getPriceLevel());} catch (Exception e){}
        try {i.putExtra("rating", place.getRating());} catch (Exception e){}
        try {i.putExtra("phonenumber", place.getPhoneNumber());} catch (Exception e){}
//        i.putExtra("weburi", place.getWebsiteUri().toString());
        try {i.putExtra("invitees", invitees);} catch (Exception e){}
        // as reminder, the user is the signed in user
        try {i.putExtra("user", user);} catch (Exception e){}
        startActivity(i);
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
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



}
