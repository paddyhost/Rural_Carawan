package com.hatchers.ruralcaravane.utils.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikam on 04/02/2018.
 */

public class Validations {

   public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public static boolean isValidName(String name) {
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(name);
        return ms.matches();
    }
}
