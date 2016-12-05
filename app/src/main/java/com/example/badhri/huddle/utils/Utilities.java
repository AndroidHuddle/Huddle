package com.example.badhri.huddle.utils;


import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {


    static public float getHueFromHEX(String hexColor) {

        //ContextCompat.getColor(context, R.color.colorAccent)

        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(hexColor), hsv);

        return hsv[0];

    }


    static public String formatDate(Date date, String format){
//        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
//        return dateFormat.format(date);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}