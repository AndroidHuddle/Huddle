package com.example.badhri.huddle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.RequestsAdapter;
import com.example.badhri.huddle.parseModels.FriendRequest;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.DividerItemDecoration;
import com.example.badhri.huddle.utils.FriendRequestContainer;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by badhri on 11/28/16.
 */

public class RequestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_OBJECTID = "userObjectId";
    private static final String TAG = "FriendRequestTab";

    // TODO: Rename and change types of parameters
    private String userObjectId;

    @BindView(R.id.rvRequestsList)
    RecyclerView rvRequestsList;

    private Unbinder unbinder;

    //private OnFragmentInteractionListener mListener;

    ArrayList<FriendRequestContainer> mRequests;

    LinearLayoutManager linearLayoutManager;
    RequestsAdapter adapter;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userObjectId ObjectId of the user.
     * @return A new instance of fragment FriendListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsFragment newInstance(String userObjectId) {
        RequestsFragment fragment = new RequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_OBJECTID, userObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userObjectId = getArguments().getString(ARG_USER_OBJECTID);
        }
        mRequests = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request_list, container, false);
        unbinder = ButterKnife.bind(this, v);
        setUpRecyclerView();
        addRequests();
        return v;
    }

    private void setUpRecyclerView() {
        adapter = new RequestsAdapter(getActivity(), mRequests);
        rvRequestsList.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvRequestsList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        rvRequestsList.addItemDecoration(dividerItemDecoration);
        rvRequestsList.setItemAnimator(new DefaultItemAnimator());
    }

    private void addRequests() {
        // query the outgoing friend requests
        ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);
        query.whereEqualTo(FriendRequest.FROM_KEY, userObjectId);
        mRequests.clear();
        Log.i(TAG, "Add requests called");
        query.findInBackground(new FindCallback<FriendRequest>() {
            public void done(List<FriendRequest> friendRequestsList, ParseException e) {
                if (e == null) {
                    for (final FriendRequest request : friendRequestsList) {
                        ParseQuery<User> query = ParseQuery.getQuery(User.class);
                        Log.i(TAG, "Fetched requests");
                        query.getInBackground(request.getToUser(), new GetCallback<User>() {
                            public void done(User object, ParseException e) {
                                if (e == null) {
                                    // object will be your game score
                                    Log.i(TAG, "Fetched User");
                                    FriendRequestContainer friendRequestContainer = new FriendRequestContainer();
                                    friendRequestContainer.friendRequest = request;
                                    friendRequestContainer.user = object;
                                    friendRequestContainer.incoming = false;
                                    mRequests.add(friendRequestContainer);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    // something went wrong
                                }
                            }
                        });

                    }// update adapter
                    // Scroll to the bottom of the list on initial load
                    /*if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }*/
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });

        //Query the incoming friend requests
        ParseQuery<FriendRequest> query2 = ParseQuery.getQuery(FriendRequest.class);
        query2.whereEqualTo(FriendRequest.TO_KEY, userObjectId);
        query2.findInBackground(new FindCallback<FriendRequest>() {
            public void done(List<FriendRequest> friendRequestsList, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Fetched requests");
                    for (final FriendRequest request : friendRequestsList) {
                        ParseQuery<User> query = ParseQuery.getQuery(User.class);
                        query.getInBackground(request.getFromUser(), new GetCallback<User>() {
                            public void done(User object, ParseException e) {
                                if (e == null) {
                                    Log.i(TAG, "Fetched User");
                                    // object will be your game score
                                    FriendRequestContainer friendRequestContainer = new FriendRequestContainer();
                                    friendRequestContainer.friendRequest = request;
                                    friendRequestContainer.user = object;
                                    friendRequestContainer.incoming = true;
                                    mRequests.add(friendRequestContainer);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    // something went wrong
                                }
                            }
                        });

                    }// update adapter
                    // Scroll to the bottom of the list on initial load
                    /*if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }*/
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        addRequests();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}