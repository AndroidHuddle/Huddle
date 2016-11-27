package com.example.badhri.huddle.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.badhri.huddle.fragments.FriendsFragment;
import com.example.badhri.huddle.fragments.PlacesFragment;
import com.example.badhri.huddle.models.UserNonParse;

public class ShowPlacesPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Places", "Friends"};
    private UserNonParse user;

    //how the adapter gets the manager to insert/remove fragments from activity
    public ShowPlacesPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    //control order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        //once created, they will be automatically cached
        if (position == 0) {
            return new PlacesFragment();
        } else if (position == 1) {
            // bundle it
            FriendsFragment ff = new FriendsFragment();
            Bundle args = new Bundle();
            args.putParcelable("user", user);
            ff.setArguments(args);
            return ff;
        } else {
            return null;
        }
    }

    //how many fragments there are to swipe between
    @Override
    public int getCount() {
        return tabTitles.length;
    }


    //return the tab title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    // now the two fragments can have access to the UserNonParse object
    public void setUser(UserNonParse u){
        user = u;
    }

}
