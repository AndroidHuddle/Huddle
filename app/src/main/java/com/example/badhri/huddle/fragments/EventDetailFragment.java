package com.example.badhri.huddle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.UsersAdapter;
import com.example.badhri.huddle.models.EventNonParse;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {
    @BindView(R.id.rvAttendees)
    RecyclerView rvAttendees;

    @BindView(R.id.tvEventName)
    TextView tvEventName;

    @BindView(R.id.tvEventTime)
    TextView tvEventTime;

    @BindView(R.id.tvEventAddress)
    TextView tvEventAddress;

    @BindView(R.id.tvCost)
    TextView tvCost;

    private UserNonParse user;


    private ArrayList<User> users;
    private UsersAdapter usersAdapter;
    EventNonParse event;

    private Unbinder unbinder;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            event = getArguments().getParcelable("event");
        }
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(getActivity(), users);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvAttendees.setAdapter(usersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAttendees.setLayoutManager(linearLayoutManager);
//
//        for (int i = 0; i < 15; i++) {
//            usersAdapter.add(com.example.badhri.huddle.models.User.randomUser());
//        }

        tvEventName.setText(event.getEventName());
        tvEventTime.setText(event.getEndTime().toString());
        tvEventAddress.setText(event.getVenue());
    }

}