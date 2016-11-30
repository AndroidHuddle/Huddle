package com.example.badhri.huddle.utils;

import android.util.Log;

import com.example.badhri.huddle.HuddleApplication;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

/**
 * Created by badhri on 11/20/16.
 */

public class ParsePushHelper {
    public static void pushToUser(String phoneNumber, String message, String title) {
        HashMap<String, String> data = new HashMap<>();
        data.put("phoneNumber", phoneNumber);
        data.put("message", message);
        data.put("titleString", title);
        ParseCloud.callFunctionInBackground("sendToUser", data, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null)
                    Log.d(HuddleApplication.TAG, "push succeeded");
                else {
                    Log.d(HuddleApplication.TAG, Integer.toString(e.getCode()));
                    e.printStackTrace();
                    Log.d(HuddleApplication.TAG, e.getMessage());
                }
            }
        });
    }


    public static void pushToChannel(String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", message);
        ParseCloud.callFunctionInBackground("sendToChannel", data, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null)
                    Log.d(HuddleApplication.TAG, "push succeeded");
                else {
                    Log.d(HuddleApplication.TAG, Integer.toString(e.getCode()));
                    e.printStackTrace();
                    Log.d(HuddleApplication.TAG, e.getMessage());
                }
            }
        });

    }
}
