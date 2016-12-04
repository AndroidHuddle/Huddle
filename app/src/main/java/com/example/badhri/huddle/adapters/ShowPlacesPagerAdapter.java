package com.example.badhri.huddle.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.badhri.huddle.fragments.FriendsAroundMeFragment;
import com.example.badhri.huddle.fragments.FriendsFragment;
import com.example.badhri.huddle.fragments.PlacesFragment;
import com.example.badhri.huddle.models.UserNonParse;

public class ShowPlacesPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = {"Places", "Nearby", "Invites"};
    private UserNonParse user;

    //how the adapter gets the manager to insert/remove fragments from activity
    public ShowPlacesPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    //control order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        //once created, they will be automatically cached

        Bundle args;
        args = new Bundle();
        args.putParcelable("user", user);

        switch (position) {
            case 0:
                PlacesFragment pf = new PlacesFragment();
                pf.setArguments(args);
                return pf;
            case 1:
                FriendsAroundMeFragment fam = new FriendsAroundMeFragment();
                fam.setArguments(args);
                return fam;
            case 2:
                FriendsFragment ff = new FriendsFragment();
                ff.setArguments(args);
                return ff;
            default:
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
    public void setUser(UserNonParse u) {
        user = u;
    }

}
