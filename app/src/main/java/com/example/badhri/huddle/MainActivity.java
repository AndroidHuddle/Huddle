package com.example.badhri.huddle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.badhri.huddle.activities.EventDetailActivity;
import com.example.badhri.huddle.models.Business;
import com.example.badhri.huddle.networks.YelpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        YelpClient c = MainClientApp.getYelpRestClient();
        c.search("restaurants", "San Francisco", searchHandler());

    }

    // this is mainly to show how it works
    private JsonHttpResponseHandler searchHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray businessesJson = response.getJSONArray("businesses");
                    ArrayList<Business> businesses = Business.fromJSONArray(businessesJson);
                    Log.d("DEBUG", String.valueOf(statusCode));
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString());
            }

        };
    }

    @OnClick(R.id.btnOpenEventDetails)
    public void openEventDetails() {
        Intent eventIntent = new Intent(this, EventDetailActivity.class);
        this.startActivity(eventIntent);
    }
}
