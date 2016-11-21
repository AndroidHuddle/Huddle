package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.fragments.UserDetailsFragment;
import com.example.badhri.huddle.fragments.UserHeaderFragment;

public class ProfileActivity extends AppCompatActivity {

    public static String USER_ARG = "userObjectID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // objectId of the User
        String userObjectId = getIntent().getStringExtra(USER_ARG);
        if (savedInstanceState == null) {

            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(userObjectId);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flUserHeader, userHeaderFragment);
            ft2.commit();

            UserDetailsFragment userDetailsFragment = UserDetailsFragment.newInstance(userObjectId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fUserDetails, userDetailsFragment);
            ft.commit();
        }
    }
}
