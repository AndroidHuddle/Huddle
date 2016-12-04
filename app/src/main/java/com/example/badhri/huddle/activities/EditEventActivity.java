package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.fragments.EventEditFragment;
import com.example.badhri.huddle.models.UserNonParse;

import java.util.ArrayList;

public class EditEventActivity extends AppCompatActivity  {

    private String name;
    private String latitude;
    private String longitude;
    private String address;
    private int price;
    private float rating;
    private String phonenumber;
    private String weburi;
    private ArrayList<String> invitees;
    private UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");
        address = i.getStringExtra("address");
        price = i.getIntExtra("price", 0);
        rating = i.getFloatExtra("rating", 0);
        phonenumber = i.getStringExtra("phonenumber");
//        weburi = i.getStringExtra("weburi");
        invitees = i.getStringArrayListExtra("invitees");
        // as reminder, the user is the signed in user
        user = i.getParcelableExtra("user");

        // get yelp review if possible
        // still need to figure out the other user distances
        // make it a form with area to create the event the users for the invite list

        // Begin the transaction
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);
        bundle.putString("address", address);
        bundle.putInt("price", price);
        bundle.putFloat("rating", rating);
        bundle.putString("phonenumber", phonenumber);
//        bundle.putString("weburi", weburi);
        bundle.putStringArrayList("invitees", invitees);
        bundle.putParcelable("user", user);
        EventEditFragment eef = new EventEditFragment();
        eef.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.edit_event_confirmation, eef);
        ft.commit();
    }


}
