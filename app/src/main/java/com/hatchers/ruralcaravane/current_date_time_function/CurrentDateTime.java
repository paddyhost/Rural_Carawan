package com.hatchers.ruralcaravane.current_date_time_function;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CurrentDateTime {


    public static String getCurrentDateTime()
    {
        String currentDateTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = sdf.format(new Date());

        return currentDateTime;
    }
}
