package com.example.badhri.huddle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.UsersAdapter;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.utils.DividerItemDecoration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListAttendingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_OBJECTID = "userObjectId";
    private static final String EVENT_ID = "eventId";

    // TODO: Rename and change types of parameters
    private String userObjectId;
    private String eventId;

    @BindView(R.id.rvFriendList)
    RecyclerView rvFriendList;

    private Unbinder unbinder;

    //private OnFragmentInteractionListener mListener;

    ArrayList<User> mFriends;

    LinearLayoutManager linearLayoutManager;
    UsersAdapter adapter;

    public FriendListAttendingFragment() {
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
    public static FriendListAttendingFragment newInstance(String userObjectId, String eventId) {
        FriendListAttendingFragment fragment = new FriendListAttendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_OBJECTID, userObjectId);
        args.putString(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userObjectId = getArguments().getString(ARG_USER_OBJECTID);
            eventId = getArguments().getString(EVENT_ID);
        }
        mFriends = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friend_list_attending, container, false);
        unbinder = ButterKnife.bind(this, v);
        setUpRecyclerView();
        addUsers();
        return v;
    }

    private void setUpRecyclerView() {
        adapter = new UsersAdapter(getActivity(), mFriends);
        rvFriendList.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFriendList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.HORIZONTAL);
        rvFriendList.addItemDecoration(dividerItemDecoration);
        rvFriendList.setItemAnimator(new DefaultItemAnimator());
    }

    private void addUsers() {
        ParseQuery<Attendees> query = ParseQuery.getQuery(Attendees.class);
        query.whereEqualTo("event", eventId);
        query.whereEqualTo("status", "Attending");
        mFriends.clear();
        query.findInBackground(new FindCallback() {

            @Override
            public void done(Object o, Throwable throwable) {
                ArrayList<Attendees> attendees = (ArrayList<Attendees>) o;
                ArrayList<String> userIds = new ArrayList<String>();
                for (int i = 0; i < attendees.size(); i++) {
                    userIds.add(attendees.get(i).getUser());
                }
                ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
                userQuery.whereContainedIn("objectId", userIds);
                populateAttendingUsers(userQuery);
            }

            @Override
            public void done(List objects, ParseException e) {

            }
        });
    }

    private void populateAttendingUsers(ParseQuery<User> userQuery) {
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (objects.size() > 0) {
                    for (int i = 0; i < objects.size(); i++) {
                        mFriends.add(objects.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
