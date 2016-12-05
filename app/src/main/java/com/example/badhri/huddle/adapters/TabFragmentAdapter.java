package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.badhri.huddle.fragments.EventsFragment;
import com.example.badhri.huddle.models.UserNonParse;

/**
 * Created by badhri on 11/13/16.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Yes", "Undecided", "No" };
    private String filters[] = new String [] {"Attending", "Not Responded", "Not Attending"};
    private Context context;
    private EventsAdapter eventsAdapter;
    private UserNonParse user;

    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        EventsFragment ef = EventsFragment.newInstance(filters[position]);
        ef.setUser(user);
        return ef;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public void setUser(UserNonParse u) {
        user = u;
    }
}
