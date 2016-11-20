package com.example.badhri.huddle.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by victorhom on 11/12/16.
 * http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
 */
public class ParseJSONObject {

    private String loadJSONFromAsset(Context c, String filename) {
        String json = null;
        try {
            InputStream is = c.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public JSONObject getJSONObject(Context c, String filename) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(loadJSONFromAsset(c, filename));
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


}