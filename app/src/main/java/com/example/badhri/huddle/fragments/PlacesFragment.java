package com.example.badhri.huddle.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;


@RuntimePermissions
public class PlacesFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private int PLACE_PICKER_REQUEST = 1;

    private GoogleMap map;
    private Place place;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private Activity activity;
    private View v;
    private Marker selectedPlaceMarker;

    private static final float ZOOM = 14f;

    private OnCompleteEventClick mListener;
    private UserNonParse user;

    public interface OnCompleteEventClick {
        public abstract void onEventSelect(Place place);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlacesFragment.OnCompleteEventClick) {
            this.mListener = (OnCompleteEventClick) context;
        }
    }

    // Now we can fire the event when the user selects something in the fragment
//    public void onSomeClick(View v) {
//        // need to get the current place and lat and long to pass as an event back to SelectPlaceActivity
//        mListener.onEventPress(null);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            if (user != null) {
                Log.d("DEBUG", "in PlacesFragment");
                Log.d("DEBUG", user.getPhoneNumber());
            }
        }
    }


    private void createGoogleApiClient() {
        //Toast.makeText(this, "createGoogleApi()", Toast.LENGTH_SHORT).show();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(activity)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        try {
            v = inflater.inflate(R.layout.fragment_places, container, false);
        } catch (InflateException e) {

        }

        if (mapFragment == null) {
            mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        }


        //set up google API for Places
        createGoogleApiClient();

        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            if (user != null) {
                Log.d("DEBUG", "in PlacesFragment");
                Log.d("DEBUG", user.getPhoneNumber());
            }
        }


        //myPlaces();
        buildMap();

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button b = (Button) view.findViewById(R.id.button_placepicker);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPlaces();
            }
        });
    }

    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void myPlaces() {
        /**
         If you want to change the place picker's default behavior, you can use the builder to set the initial latitude and longitude bounds of the map displayed by the place picker.
         Call setLatLngBounds() on the builder, passing in a LatLngBounds to set the initial latitude and longitude bounds.
         These bounds define an area called the 'viewport'.
         By default, the viewport is centered on the device's location, with the zoom at city-block level.
         */
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            // Start the Intent by requesting a result, identified by a request code.
            this.startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                //remove previous markers
                if (selectedPlaceMarker != null) {
                    selectedPlaceMarker.remove();
                }

                place = PlacePicker.getPlace(activity, data);

                // I should force the place to be an event object and pass that back to the
                // select place activity as an object
//                Log.d("DEBUG", data.toString());
//                String toastMsg = String.format("You selected %s", place.getName());
//                Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show();

                mListener.onEventSelect(place);
                //draw this on the map:
                if (mapFragment != null) {
                    //buildMap();
                    centerMapUsingSelectedLocation();
                }
            }
        }
    }

    private void buildMap() {
        if (TextUtils.isEmpty(getResources().getString(R.string.google_places_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap != null) {

                    map = googleMap;

                    getLastKnownLocationAndCenter();

                    //some properties for the map:
//                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                    map.setTrafficEnabled(true);
                    map.setBuildingsEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);

                    map.clear();
                    //centerMapUsingSelectedLocation();
                    map.clear();
                } else {
                    Toast.makeText(activity, "Trouble loading map. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getLastKnownLocationAndCenter() {

        ParseQuery query = new ParseQuery("User");
        query.whereEqualTo("objectId", user.getParseId());
        query.findInBackground(new FindCallback() {

            @Override
            public void done(Object o, Throwable throwable) {
                if (o != null) {
                    List<User> lu = (List<User>) o;
                    if (lu.size() > 0) {
                        User user = lu.get(0);
                        LatLng lastKnownLocation = new LatLng(user.getLatitude(), user.getLongitude());

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastKnownLocation, ZOOM);
                        map.animateCamera(cameraUpdate);

                    }
                }
            }

            @Override
            public void done(List objects, ParseException e) {

            }
        });
    }


    public void centerMapUsingSelectedLocation() {
        final LatLng position = place.getLatLng();

        ParseQuery query = new ParseQuery("User");
        query.whereEqualTo("objectId", user.getParseId());
        query.findInBackground(new FindCallback() {

            @Override
            public void done(Object o, Throwable throwable) {
                if (o != null) {
                    List<User> lu = (List<User>) o;
                    if (lu.size() > 0) {
                        User user = lu.get(0);
                        if (user.getLatitude() == 0 && user.getLongitude() == 0) {
//                            Log.d("DEBUG", user.getObjectId());
                            user.setLatitude(position.latitude);
                            user.setLongitude(position.longitude);
                            try {
                                user.save();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }

            @Override
            public void done(List objects, ParseException e) {

            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to position
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //put a marker on the place selected:
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(place.getName().toString())
                .snippet(place.getAddress().toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        //.icon(BitmapDescriptorFactory.defaultMarker(Color.parseColor("#61cae6"))))

        selectedPlaceMarker = map.addMarker(markerOptions);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, "Error - connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if (googleApiClient != null) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

    }

}
