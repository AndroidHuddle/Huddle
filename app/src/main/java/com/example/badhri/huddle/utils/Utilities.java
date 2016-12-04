package com.example.badhri.huddle.utils;


import android.graphics.Color;

public class Utilities {


    static public float getHueFromHEX(String hexColor) {

        //ContextCompat.getColor(context, R.color.colorAccent)

        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(hexColor), hsv);

        return hsv[0];

    }


}