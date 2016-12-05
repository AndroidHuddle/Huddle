package com.example.badhri.huddle.services;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.activities.SelectPlaceActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceService extends IntentService {

    private static final String TAG = GeofenceService.class.getSimpleName();
    private static final int GEOFENCE_NOTIFICATION_ID = 0;
    private static int index = 0;
    private NotificationManager notificationMngr;

    public GeofenceService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        //String friend = intent.getStringExtra("friend");
        //Toast.makeText(getApplicationContext(), friend, Toast.LENGTH_SHORT).show();

        notificationMngr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();

        // Check if the transition type
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofence that was triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

//            //remove all previous notifications:
//            for (int i = 0; i < index; i++) {
//                notificationMngr.cancel(i);
//            }
//            index = 0;

//            // Create a detail message with Geofences received
//            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);
//
//            // Send notification details as a String
//            sendNotification(geofenceTransitionDetails);
            // get the ID of each geofence triggered
            for (Geofence geofence : triggeringGeofences) {
                sendNotification(geofence.getRequestId());
                index++;
            }

        }
    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {

        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }

/*        String status = null;
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "Entering ";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "Exiting ";
        //return status + TextUtils.join(", ", triggeringGeofencesList);*/

        return TextUtils.join(", ", triggeringGeofencesList);
    }


    // Send a notification
    private void sendNotification(String msg) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), SelectPlaceActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(SelectPlaceActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create and send Notification

        //notificationMng.notify(GEOFENCE_NOTIFICATION_ID, createNotification(msg, notificationPendingIntent));
        notificationMngr.notify(index, createNotification(msg, notificationPendingIntent));
    }


    // Create a notification
    private Notification createNotification(String friendsName, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.mipmap.icon_huddle)
                //.setColor(Color.RED)
                .setContentTitle("Friend alert")
                .setContentText(friendsName + " nearby!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error";
        }
    }
}
