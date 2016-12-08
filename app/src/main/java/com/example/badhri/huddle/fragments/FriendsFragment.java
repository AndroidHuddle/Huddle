package com.example.badhri.huddle.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Friends;
import com.example.badhri.huddle.parseModels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FriendsFragment extends Fragment {
    private Unbinder unbinder;
    private UserNonParse user;
    @BindView(R.id.cb_friends_to_invite)
    LinearLayout cbFriendsToInvite;
    private OnCheckCompleteDetails mListeners;

    public interface OnCheckCompleteDetails {
        public abstract void getInviteList(ArrayList<String> invitees);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCheckCompleteDetails) {
            this.mListeners = (OnCheckCompleteDetails) context;
        }
    }

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
//                    System.out.println(lf.size());
                    ArrayList<String> friendIds = new ArrayList<>();
                    // get friend ids;
                    // now get their names/username/ etc.
                    // this is the case where I would love to actually have their names
                    for (int i = 0; i < lf.size(); i++) {
                        if (user != null) {
                            if (user.getParseId().equals(lf.get(i).getUserId())) {
                                friendIds.add(lf.get(i).getFriendId());
                            }
                        }
                    }
//                    Log.d("DEBUG", friendIds.toString());
                    queryUsers(friendIds);
                }
            }
        });
        return v;
    }

    private void queryUsers(final ArrayList<String> friends) {
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
                    if (friends.contains(u.getObjectId())) {
                        try {
                            UserNonParse unp = UserNonParse.fromUser(u);
                            lnp.add(unp);
                            CheckBox checkBox = new CheckBox(getContext());
                            checkBox.setText(unp.getUsername());
                            // checkBox.getTag() to get the parse id
                            checkBox.setTag(unp.getParseId().toString());
                            cbFriendsToInvite.addView(checkBox);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_check_details)
    public void onClickFinalDetails() {
        ArrayList<String> inviteeList = new ArrayList<>();
        for (int i = 0; i < cbFriendsToInvite.getChildCount(); i++) {
            CheckBox cb = (CheckBox) cbFriendsToInvite.getChildAt(i);
            if (cb.isChecked()) {
                inviteeList.add(cb.getTag().toString());
            }
        }
        // you shouldn't have an event where there is no invite, so no invite - do nothing
        // now that logins work, the user should have friends to invite
        // they should invite in this fragment
//        if (inviteeList.size() > 0) {
            Log.d("DEBUG", inviteeList.toString());
            mListeners.getInviteList(inviteeList);
            Log.d("DEBUG", "checking for final details of invite");
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            if (user != null) {
                Log.d("DEBUG", "in friends fragment");
                Log.d("DEBUG", user.getPhoneNumber());
            }
        }
        // query the friends table in parse
        // will also need to match the friends and get their current location
    }
}
