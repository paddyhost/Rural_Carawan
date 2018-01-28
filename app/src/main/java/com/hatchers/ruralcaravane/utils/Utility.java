package com.hatchers.ruralcaravane.utils;

import android.app.Activity;
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
}
