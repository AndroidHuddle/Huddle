package com.example.badhri.huddle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FriendsFragment extends Fragment{
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        unbinder = ButterKnife.bind(this, v);

        ParseQuery query = new ParseQuery("User");
        query.findInBackground(new FindCallback() {
            // this version isn't the one that is used
            @Override
            public void done(List objects, ParseException e) {
                if (e == null) {
                    //Log.d("DEBUG", objects.toString());
                }
            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (throwable == null) {
                    Log.d("DEBUG", o.toString());
                }
            }
        });
        return v;
    }







}
