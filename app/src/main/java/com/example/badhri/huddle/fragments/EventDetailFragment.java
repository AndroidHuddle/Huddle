package com.example.badhri.huddle.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.activities.DashboardActivity;
import com.example.badhri.huddle.adapters.UsersAdapter;
import com.example.badhri.huddle.models.EventNonParse;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Attendees;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {
    // matching with the exact text in strings.xml
    public String ATTEND = "Attend";
    public String MAYBE = "Maybe";
    public String NOPE = "Nope";

    private String filter;

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

    @BindView(R.id.rbAttend)
    RadioButton rbAtten;

    @BindView(R.id.rbMaybe)
    RadioButton rbMaybe;

    @BindView(R.id.rbNoAttend)
    RadioButton rbNoAttend;

    @BindView(R.id.rgRadioAttendOption)
    RadioGroup rbRadioAttendOption;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.tvDetails)
    TextView tvDetails;


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
            user = getArguments().getParcelable("user");
        }
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(getActivity(), users);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvAttendees.setAdapter(usersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAttendees.setLayoutManager(linearLayoutManager);

        tvEventName.setText(event.getEventName());
        if (event.getEndTime() != null) {
            tvEventTime.setText(event.getEndTime().toString());
        }

        tvEventAddress.setText(event.getDisplayAddress());

        if (event.getEventDetails() != null && event.getEventDetails().length() > 0) {
            tvDetails.setText(event.getEventDetails());
        }


        addUsers();
    }

    private void addUsers() {
        ParseQuery<Attendees> query = ParseQuery.getQuery(Attendees.class);
        query.whereEqualTo("event", event.getEventId());
        query.whereEqualTo("status", "Attending");
        users.clear();
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
                        users.add(objects.get(i));
                        usersAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @OnClick(R.id.btnSave)
    public void radioOptionNotify() {
        // get selected radio button from radioGroup
        int selectedId = rbRadioAttendOption.getCheckedRadioButtonId();

        RadioButton btn = (RadioButton) getView().findViewById(selectedId);
//        Log.d("DEBUG", btn.getText().toString());
        String decision = btn.getText().toString();

        // need to update the Attendees table
        // need the event id
        // the new status (from the radio btn selection)
        // the user id
        markStatus(decision);
    }

    private void markStatus(final String decision) {
        ParseQuery query = new ParseQuery("Attendees");
        query.whereEqualTo("event", event.getEventId());
        query.whereEqualTo("user", user.getParseId());
        query.findInBackground(new FindCallback() {

            @Override
            public void done(Object o, Throwable throwable) {
                if (o != null) {
                    ArrayList<Attendees> al = (ArrayList<Attendees>) o;
                    if (al.size() > 0) {
                        Attendees a = al.get(0);
                        if (decision.equals(ATTEND)) {
                           a.setStatus("Attending");
                        } else if (decision.equals(MAYBE)) {
                            a.setStatus("Not Responded");
                        } else {
                            a.setStatus("Not Attending");
                        }
                        try {
                            a.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        // pass the user in the oncreate methods;
                        i.putExtra("user", user);
                        // in case I want to mark some sort of feedback back in the Dashboard
                        // maybe something like coloring the tab
                        //
                        i.putExtra("eventId", a.getEvent());
                        i.putExtra("status", decision);
                        startActivity(i);
                    }
                }

            }

            @Override
            public void done(List objects, ParseException e) {

            }
        });
    }
}