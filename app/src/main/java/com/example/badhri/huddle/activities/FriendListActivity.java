package com.example.badhri.huddle.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.FriendsTabFragmentAdapter;
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

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.friendsViewPager);
            viewPager.setAdapter(new FriendsTabFragmentAdapter(getSupportFragmentManager(), this, userObjectId));

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.friendsTab);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
