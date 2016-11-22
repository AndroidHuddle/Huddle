package com.example.badhri.huddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.EventActivityTypeAdapter;
import com.example.badhri.huddle.models.EventActivityType;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {
    ArrayList<EventActivityType> eventActivityTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        RecyclerView rvEventActivityTypes = (RecyclerView) findViewById(R.id.rvEventActivityTypes);
        eventActivityTypes = new ArrayList<>();

        // hard code initial list of options
        EventActivityType restaurant = EventActivityType.newInstance();
        restaurant.setEventType("Restaurant");
        EventActivityType show = EventActivityType.newInstance();
        show.setEventType("Shows");
        EventActivityType custom = EventActivityType.newInstance();
        custom.setEventType("Make My Own");
        eventActivityTypes.add(show);
        eventActivityTypes.add(restaurant);
        eventActivityTypes.add(custom);
        Log.d("DEBUG", String.valueOf(eventActivityTypes.size()));

        EventActivityTypeAdapter eventActivityTypeAdapter = new EventActivityTypeAdapter(this, eventActivityTypes);
        rvEventActivityTypes.setAdapter(eventActivityTypeAdapter);
        rvEventActivityTypes.setLayoutManager(new LinearLayoutManager(this));
        eventActivityTypeAdapter.notifyDataSetChanged();

        Button button_create = (Button) findViewById(R.id.button_select_place);
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch 'select place' part
                Intent i = new Intent(CreateEventActivity.this, SelectPlaceActivity.class);
                startActivity(i);
            }
        });
    }
}
