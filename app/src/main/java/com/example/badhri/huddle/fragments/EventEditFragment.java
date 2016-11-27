package com.example.badhri.huddle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventEditFragment extends Fragment {


    public EventEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_event_edit, container, false);
        ButterKnife.bind(this, v);
        String name = getArguments().getString("name");
        System.out.println("in fragment");
        System.out.println(name);
        return v;
    }

}
