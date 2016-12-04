package com.example.badhri.huddle.fragments;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.activities.DashboardActivity;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.parseModels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private final String TO_DATE = "TO_DATE";
    private final String FROM_DATE = "FROM_DATE";
    private final String TO_TIME = "TO_TIME";
    private final String FROM_TIME= "FROM_TIME";

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

    @BindView(R.id.et_to_date_text)
    EditText etToDateText;

    @BindView(R.id.et_from_date_text)
    EditText etFromDateText;

    @BindView(R.id.et_from_time_text)
    EditText et_from_time_text;

    @BindView(R.id.et_to_time_text)
    EditText et_to_time_text;

    @BindView(R.id.tvContactLabel)
    TextView tvContactLabel;

    Fragment fg;

    private ArrayAdapter<String> adapter;
    private UserNonParse user;

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
        if (price != 0) {
            tvPrice.setText(String.valueOf(price));
        } else {
            tvPrice.setText("$$");
        }


        Float rating = getArguments().getFloat("rating");
        if (rating != 0.0) {
            tvRating.setText(String.valueOf(rating));
        } else {
            tvRating.setText("Take a chance.");
        }


        String phonenumber = getArguments().getString("phonenumber");
        if (phonenumber != null && phonenumber.length() > 0) {
            tvPhoneNumber.setText(phonenumber);
        } else {
            tvContactLabel.setText("");
        }

        populateInviteeList();

        fg = this;
        etToDateText.setCursorVisible(false);
        etFromDateText.setCursorVisible(false);

        // set calendar
        //etToDateText.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.check_box), null);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = getArguments().getParcelable("user");
        }
    }

    public void populateInviteeList() {
        ArrayList<String> invitees = getArguments().getStringArrayList("invitees");
        ParseQuery query = new ParseQuery("User");
        query.whereContainedIn("objectId", invitees);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (throwable == null) {
                    ArrayList<User> users = (ArrayList<User>) o;
                    ArrayList<String> names = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        names.add(users.get(i).getUsername());
                    }
                    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                    lvInvitees.setAdapter(adapter);
                }

            }
        });

    }

    @OnClick(R.id.send_invite)
    public void sendEventInvite() {
        if (etEventName.getText().length() == 0) {
            Toast.makeText(getActivity(), "please name the event", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("send out event!");
            // need to send invite, but otherwise
            Events event = new Events();
            if (etFromDateText.getText().length() > 0){
                event.setStartTime(new Date());
                event.setEventStartTime(etFromDateText.getText().toString());
            } else {
                event.setStartTime(new Date());
            }

            if (etToDateText.getText().length() > 0){
                event.setEndTime(new Date());
                event.setEventEndTime(etToDateText.getText().toString());
            } else {
                event.setStartTime(new Date());
            }


            event.setEventName(etEventName.getText().toString());
            event.setVenue(etEventName.getText().toString());
            UserNonParse u = getArguments().getParcelable("user");
            event.setOwner(u.getParseId());
            try {
                event.save();
                setAttendeesTable(event);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAttendeesTable(Events event) {
        Attendees attendees = new Attendees();
        attendees.setEvent(event.getObjectId());
        attendees.setUser(event.getOwner());
        attendees.setStatus("Attending");
        attendees.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
//                    Toast.makeText(getActivity(), "Successfully created the event and set attenting",
//                            Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(getActivity(), DashboardActivity.class);
                    k.putExtra("user", user);
                    startActivity(k);
                } else {
                    Log.e("DEBUG", "Failed to save message", e);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    @OnClick(R.id.et_to_date_text)
    public void openToCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) fg,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        FragmentManager fm = getActivity().getFragmentManager();
        dpd.show(fm, TO_DATE);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @OnClick(R.id.et_from_date_text)
    public void openFromCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) fg,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        FragmentManager fm = getActivity().getFragmentManager();
        dpd.show(fm, FROM_DATE);
    }


//
    @TargetApi(Build.VERSION_CODES.N)
    @OnClick(R.id.et_to_time_text)
    public void openTimeToPicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                (TimePickerDialog.OnTimeSetListener) fg,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE), false);
        FragmentManager fm = getActivity().getFragmentManager();
        tpd.show(fm, TO_TIME);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @OnClick(R.id.et_from_time_text)
    public void openTimeFromPicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                (TimePickerDialog.OnTimeSetListener) fg,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE), false);
        FragmentManager fm = getActivity().getFragmentManager();
        tpd.show(fm, FROM_TIME);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (view.getTag().equals(TO_DATE)) {
            etToDateText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        } else {
            etFromDateText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        }
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        Log.d("DEBUG", date.toString());

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (view.getTag().equals(TO_TIME)) {
            et_to_time_text.setText(hourOfDay+":"+minute);
        } else {
            et_from_time_text.setText(hourOfDay+":"+minute);
        }
        String time = "You picked the following time: "+hourOfDay+"h"+minute+"m"+second;
//        timeTextView.setText(time);
        Log.d("DEBUG", time.toString());

    }
}
