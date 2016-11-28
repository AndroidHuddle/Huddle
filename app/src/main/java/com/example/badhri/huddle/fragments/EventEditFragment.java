package com.example.badhri.huddle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Events;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventEditFragment extends Fragment {
    @BindView(R.id.etEventName)
    EditText etEventName;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvRating)
    TextView tvRating;

    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.lvInvitees)
    ListView lvInvitees;

    private ArrayAdapter<String> adapter;

    public EventEditFragment() {
        // Required empty public constructor
    }
    // need an area to set the date

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_event_edit, container, false);
        ButterKnife.bind(this, v);

        String name = getArguments().getString("name");
        etEventName.setText(name);

        String address = getArguments().getString("address");
        tvAddress.setText(address);

        Integer price = getArguments().getInt("price");
        tvPrice.setText(String.valueOf(price));

        Float rating = getArguments().getFloat("rating");
        tvRating.setText(String.valueOf(rating));

        String phonenumber = getArguments().getString("phonenumber");
        tvPhoneNumber.setText(phonenumber);

        ArrayList<String> invitees = getArguments().getStringArrayList("invitees");
        adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, invitees);
        lvInvitees.setAdapter(adapter);

        return v;
    }

    @OnClick(R.id.send_invite)
    public void sendEventInvite() {
        System.out.println("send out event!");
        // need to send invite, but otherwise
        Events event = new Events();
        event.setEventName(etEventName.getText().toString());
        event.setVenue(etEventName.getText().toString());
        UserNonParse u = getArguments().getParcelable("user");
        event.setOwner(u.getParseId());
        try {
            event.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // then transition back to the main page
    }

}
