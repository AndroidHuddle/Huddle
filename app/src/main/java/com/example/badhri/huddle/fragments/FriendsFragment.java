package com.example.badhri.huddle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Friends;
import com.example.badhri.huddle.parseModels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FriendsFragment extends Fragment{
    private Unbinder unbinder;
    private UserNonParse user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        unbinder = ButterKnife.bind(this, v);

        ParseQuery query = new ParseQuery("Friends");
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
                    List<Friends> lf = (List<Friends>) o;
                    System.out.println(lf.size());
                    ArrayList<String> friendIds = new ArrayList<>();
                    // get friend ids;
                    // now get their names/username/ etc.
                    // this is the case where I would love to actually have their names
                    for (int i = 0; i < lf.size(); i++) {
                        if (user.getParseId().equals(lf.get(i).getUserId())) {
                            friendIds.add(lf.get(i).getFriendId());
                        }
                    }
                    Log.d("DEBUG", friendIds.toString());
                    queryUsers(friendIds);
                }
            }
        });
        return v;
    }

    private void queryUsers(ArrayList<String> friends) {
        ParseQuery query = new ParseQuery("User");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                List<User> lu = (List<User>) o;
                ArrayList<UserNonParse> lnp = new ArrayList<>();
                for (int i = 0; i < lu.size(); i++) {

                    User u = lu.get(i);
                    try {
                        System.out.println(u.getUsername());
                        System.out.println(String.valueOf(u.getPhoneNumber()));
                        UserNonParse unp = UserNonParse.fromUser(u);
                        lnp.add(unp);
                    } catch (Exception e){}

                }

                // take these users and mark as checkboxes on the friendsfragment page
            }
        });
    }

    @OnClick(R.id.btn_check_details)
    public void onClickFinalDetails() {
        Log.d("DEBUG", "checking for final details of invite");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            user = args.getParcelable("user");
            Log.d("DEBUG", "in friends fragment");
            Log.d("DEBUG", user.getPhoneNumber());
        }
        // query the friends table in parse
        // will also need to match the friends and get their current location
    }
}
