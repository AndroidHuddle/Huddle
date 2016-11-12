package com.example.badhri.huddle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.badhri.huddle.models.Business;
import com.example.badhri.huddle.networks.YelpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YelpClient c = MainClientApp.getYelpRestClient();
        c.search("restaurants", "37.7867703362929,-122.399958372115", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(String.valueOf(statusCode));
                try {
                    JSONArray businessesJson = response.getJSONArray("businesses");
                    ArrayList<Business> businesses = Business.fromJSONArray(businessesJson);
                    Log.d("ERROR", businesses.toString());
                } catch (Exception e) {
                    Log.d("ERROR", e.toString());
                    Log.e("ERROR", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println(String.valueOf(statusCode));
                Log.d("ERROR", String.valueOf(statusCode));
                Log.e("ERROR", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println(String.valueOf(statusCode));
            }
        });
    }
}
