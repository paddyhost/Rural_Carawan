package com.hatchers.ruralcaravane.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.TypedValue;

/**
 * Created by Ashwin on 26-Jan-18.
 */

public class Utility
{
    public static float getConvertFloatToDP(Activity activity,float value)
    {
      float dpValue;
      dpValue = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, value,activity.getResources().getDisplayMetrics());

        return dpValue;
    }

    public static int generateRandomNumber(Activity activity){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        final int lastNumber = sp.getInt("lastNumber", 0);
        int randomNumber = (int) (Math.random() * 7);
        while(randomNumber == lastNumber){
            randomNumber = (int) (Math.random() *7);
        }
        sp.edit().putInt("lastNumber", randomNumber).apply();
        return randomNumber;
    }
}
