package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.fragments.FriendListFragment;
import com.example.badhri.huddle.fragments.UserHeaderFragment;

public class FriendListActivity extends AppCompatActivity {

    public static String USER_ARG = "userObjectID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // objectId of the User
        String userObjectId = getIntent().getStringExtra(USER_ARG);
        if (savedInstanceState == null) {

            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(userObjectId);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flUserHeader, userHeaderFragment);
            ft2.commit();

            FriendListFragment friendListFragment = FriendListFragment.newInstance(userObjectId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fFriendList, friendListFragment);
            ft.commit();
        }
    }
}
