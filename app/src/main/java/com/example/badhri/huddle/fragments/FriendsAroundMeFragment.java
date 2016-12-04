package com.example.badhri.huddle.fragments;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Friends;
import com.example.badhri.huddle.parseModels.User;
import com.example.badhri.huddle.services.GeofenceService;
import com.example.badhri.huddle.utils.FriendType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class FriendsAroundMeFragment extends Fragment implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    private Context context;

    private GoogleApiClient googleApiClient;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private static final float ZOOM = 12f;
    private final int UPDATE_INTERVAL = 3 * 60 * 1000; // 3 minutes
    private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    private static final long GEO_DURATION = 60 * 60 * 1000;
    //private static final float GEOFENCE_RADIUS = 400.0f; // in meters
    private static final float MINIMUM_RADIUS = 100.0f; //500.0f
    private float geofence_radius = MINIMUM_RADIUS;
    private final int GEOFENCE_REQ_CODE = 0;
    private Location lastLocation;
    private PendingIntent geofencePendingIntent;
    private Circle geofenceLimits;
    private Marker geofenceMarker;
    private Marker myLocationMarker;
    private LocationRequest locationRequest;
    private View fragmentView;
    private UserNonParse user;
    private ArrayList<FriendType> myFriends;

    //private LatLng[] dummyFriends = new LatLng[]{new LatLng(42.360449, -71.103372), new LatLng(42.360223, -71.103774), new LatLng(42.361401, -71.101494)};//todo: remove!!


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();

        createGoogleApiClient();

        if (fragmentView != null) {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null)
                parent.removeView(fragmentView);
        }
        try {
            fragmentView = inflater.inflate(R.layout.fragment_friendsaroundme, container, false);

            if (mapFragment == null) {
                mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.friends_map));
            }

            return fragmentView;
        } catch (InflateException e) {

        }
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize user
        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            if (user != null) {
                Log.d("DEBUG", "in FriendsAroundMeFragment, user retrieved");
            }
        }

        initializeRadiusBar(view);

        initializeGoogleMaps();

        getMyFriendsLocation();

    }

    private void initializeRadiusBar(View view) {

        DiscreteSeekBar radiusSeekBar = (DiscreteSeekBar) view.findViewById(R.id.radiusSeekbar);
        radiusSeekBar.setMax(2000);
        radiusSeekBar.setProgress(50);

        radiusSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {

            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                geofence_radius = MINIMUM_RADIUS + value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                recreateGeofences();
            }
        });

    }


    private void createGoogleApiClient() {
        //Toast.makeText(this, "createGoogleApi()", Toast.LENGTH_SHORT).show();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initializeGoogleMaps() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });
    }


    private void createFriendGeofence(FriendType friend) {
        if (map != null) {

            //map.clear();// todo: remove all geofence markers in a more elegant way

/*            for (int i = 0; i < friends.length; i++) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(friends[i])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(friends[i].toString());*/




/*            for (int i = 0; i < myFriends.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(myFriends.get(i).getLatitude(), myFriends.get(i).getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(myFriends.get(i).getUsername());

                geofenceMarker = map.addMarker(markerOptions);

                Geofence geofence = createGeofence(geofenceMarker.getPosition(), GEOFENCE_RADIUS, names[i]);
                GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
                FriendsAroundMeFragmentPermissionsDispatcher.startGeofencingWithCheck(this, geofenceRequest);
            }*/


            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(friend.getLatitude(), friend.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    //.icon(BitmapDescriptorFactory.defaultMarker(Color.parseColor("#ef518f")))
                    .title(friend.getUsername());

            geofenceMarker = map.addMarker(markerOptions);

            Geofence geofence = createGeofence(geofenceMarker.getPosition(), geofence_radius, friend.getUsername());
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            FriendsAroundMeFragmentPermissionsDispatcher.startGeofencingWithCheck(this, geofenceRequest);

        } else {
            Toast.makeText(context, "Map not available", Toast.LENGTH_SHORT).show();
        }


    }


    private Geofence createGeofence(LatLng latLng, float radius, String geofenceReqID) {
        return new Geofence.Builder()
                .setRequestId(geofenceReqID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }


    @NeedsPermission({android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION})
    // Add the created GeofenceRequest to the device's monitoring list
    protected void startGeofencing(GeofencingRequest request) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (googleApiClient.isConnected()) {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
        }
    }

    private PendingIntent createGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceService.class);
        //intent.putExtra("friend", "Victor");
        return PendingIntent.getService(context, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FriendsAroundMeFragmentPermissionsDispatcher.getLastKnownLocationWithCheck(this);
    }


    @NeedsPermission({android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            Toast.makeText(context, "Actual Lat: " + lastLocation.getLatitude() + ", long: " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No location retrieved yet", Toast.LENGTH_SHORT).show();
        }
        FriendsAroundMeFragmentPermissionsDispatcher.startLocationUpdatesWithCheck(this);
    }


    @NeedsPermission({android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            drawGeofence();
        } else {
            Toast.makeText(context, "Error drawing geofence" + status, Toast.LENGTH_SHORT).show();
        }
    }


    // Draw Geofence circle on GoogleMap
    private void drawGeofence() {
        if (geofenceLimits != null) {
            geofenceLimits.remove();
        }

        CircleOptions circleOptions = new CircleOptions()
                .center(geofenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(geofence_radius);
        geofenceLimits = map.addCircle(circleOptions);
    }


    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

        recreateGeofences();

    }


    private void recreateGeofences() {

        //remove current geofences
        if (geofencePendingIntent != null) {
            LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofencePendingIntent);
        }

        for (int i = 0; i < myFriends.size(); i++) {
            createFriendGeofence(myFriends.get(i));
        }
    }


    private void markerLocation(LatLng latLng) {

        if (map != null) {
            // Remove the previous marker
            if (myLocationMarker != null) {
                myLocationMarker.remove();
            }

            String title = latLng.latitude + ", " + latLng.longitude;
            title = user.getUsername();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    //.icon(BitmapDescriptorFactory.defaultMarker(Color.parseColor("#ef518f")))
                    .title(title);

            myLocationMarker = map.addMarker(markerOptions);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM);
            map.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        FriendsAroundMeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    public void getMyFriendsLocation() {

        //get friends from friends table:
        ParseQuery<Friends> query = ParseQuery.getQuery(Friends.class);

        query.whereEqualTo("user", user.getParseId());
        //query.whereEqualTo("user", "B2wtCIzbY2"); //hardcode for now; todo: remove!!
        query.findInBackground(new FindCallback<Friends>() {
            public void done(List<Friends> friendList, ParseException e) {
                if (e == null) {
                    //initialize checking if any friends were retrieved
                    myFriends = friendList.size() > 0 ? new ArrayList<FriendType>() : null;
                    for (Friends friend : friendList) {
                        ParseQuery<User> query = ParseQuery.getQuery(User.class);
                        query.getInBackground(friend.getFriendId(), new GetCallback<User>() {
                            public void done(User user, ParseException e) {
                                if (e == null) {
                                    FriendType thisFriend = new FriendType(user.getUsername(), new LatLng(user.getLatitude(), user.getLongitude()));
                                    /*
                                    //todo: remove this block:
                                    int randomIndex = new Random().nextInt((dummyFriends.length));
                                    thisFriend = new FriendType(user.getUsername(), dummyFriends[randomIndex]);
                                    //todo: end of remove
                                    */
                                    myFriends.add(thisFriend);

                                    createFriendGeofence(thisFriend);

                                } else {
                                    // something went wrong
                                }
                            }
                        });
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });


    }

}
