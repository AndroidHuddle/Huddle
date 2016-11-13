package com.example.badhri.huddle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

// need to add annotations to the activity that uses the runtime permissions
// requesting for location requires runtime permissions
@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        YelpClient c = MainClientApp.getYelpRestClient();
        c.search("restaurants", "San Francisco", searchHandler());

        // not sure if this works correctly, i didn't get asked for permission
        // but it says it's true; i'm not sure how to turn off permissions for location
        if (checkPermission(this)){
            System.out.println("I have have permissions?");
        }
        MainActivityPermissionsDispatcher.checkLocationsPermissionWithCheck(this);

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

    // SCRATCH Work to figure out runtime permissions
    final String permission = Manifest.permission.ACCESS_COARSE_LOCATION;

//    @OnClick(R.id.btnLocationPermissions)
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void checkLocationsPermission() {
        // trigger check for location

        Toast.makeText(this,"checkLocationsPermission", Toast.LENGTH_LONG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//        Toast.makeText(this,"onRequestPermissionsResult", Toast.LENGTH_LONG);
    }

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
