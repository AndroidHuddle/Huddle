package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.FriendsTabFragmentAdapter;
import com.example.badhri.huddle.fragments.UserHeaderFragment;
import com.example.badhri.huddle.models.UserNonParse;

public class FriendListActivity extends AppCompatActivity {

    public static String USER_ARG = "userObjectID";
    private String userObjectId;
    UserNonParse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // objectId of the User
        user = getIntent().getParcelableExtra(USER_ARG);
        userObjectId = user.getParseId();
        if (savedInstanceState == null) {

            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(userObjectId);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flUserHeader, userHeaderFragment);
            ft2.commit();

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.friendsViewPager);
            viewPager.setAdapter(new FriendsTabFragmentAdapter(getSupportFragmentManager(), this, user));

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.friendsTab);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friendlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            // This is the up button
            case R.id.miAddFriend:
                i = new Intent(this, AddFriendActivity.class);
                i.putExtra("user", user);
                //i.putExtra(ProfileActivity.USER_ARG, "B2wtCIzbY2");
                startActivity(i);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
