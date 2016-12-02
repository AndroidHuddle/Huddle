package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.badhri.huddle.fragments.FriendListFragment;
import com.example.badhri.huddle.fragments.RequestsFragment;
import com.example.badhri.huddle.models.UserNonParse;

/**
 * Created by badhri on 11/28/16.
 */

public class FriendsTabFragmentAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "Friends", "Requests"};
    private Context context;
    private EventsAdapter eventsAdapter;
    private String userObjectId;
    private UserNonParse user;

    public FriendsTabFragmentAdapter(FragmentManager fm, Context context, UserNonParse user) {
        super(fm);
        this.context = context;
        this.userObjectId = user.getParseId();
        this.user = user;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FriendListFragment friendListFragment = FriendListFragment.newInstance(userObjectId);
                return friendListFragment;
            case 1:
                RequestsFragment requestsFragment = RequestsFragment.newInstance(userObjectId);
                return requestsFragment;
            default:
                return null;
        }
        //return EventsFragment.newInstance(filters[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
