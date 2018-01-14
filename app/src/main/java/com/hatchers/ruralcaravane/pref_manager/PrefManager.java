package com.hatchers.ruralcaravane.pref_manager;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "rural_caravane";

    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String MOBILE = "mobile";
    public static final String USER_ID = "id";
    public static final String VILLAGE_NAME = "villagename";
    public static final String VILLAGE_ID = "vilage_id";
    public static final String TQ_ID = "tq_id";
    public static final String BIRTHDAY = "birthday";
    public static final String TYPE = "type";
    public static final String CITY_ID ="city_id";
    public static final String STATE_ID="state_id";
    public static final String GENDER="gender";
    public static final String FNAME="fname";
    public static final String LNAME="lname";
    public static final String CITYNAME="cityname";





    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    public PrefManager(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedpreferences.edit();
    }


    public void createLogin(String mobile) {
        editor.putString(MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return sharedpreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public void setLogOut()
    {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return sharedpreferences.getString(USER_NAME, null);
    }


    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getPassword() {
        return sharedpreferences.getString(PASSWORD, null);
    }



    public void setMobile(String mobile) {
        editor.putString(MOBILE, mobile);
        editor.commit();
    }

    public String getMobile() {
        return sharedpreferences.getString(MOBILE, null);
    }


    public void setUserId(String user_id) {
        editor.putString(USER_ID, user_id);
        editor.commit();
    }

    public String getUserId() {
        return sharedpreferences.getString(USER_ID, null);
    }


    public void setVillageName(String villageName) {
        editor.putString(VILLAGE_NAME, villageName);
        editor.commit();
    }

    public String getVillageName() {
        return sharedpreferences.getString(VILLAGE_NAME, null);
    }


    public void setVillageId(String villageId) {
        editor.putString(VILLAGE_ID, villageId);
        editor.commit();
    }

    public String getVillageId() {
        return sharedpreferences.getString(VILLAGE_ID, null);
    }


    public void setTqId(String tqId) {
        editor.putString(TQ_ID, tqId);
        editor.commit();
    }


    public String getTqId() {
        return sharedpreferences.getString(TQ_ID, null);
    }


    public void setBirthday(String birthday) {
        editor.putString(BIRTHDAY, birthday);
        editor.commit();
    }

    public String getBirthday() {
        return sharedpreferences.getString(BIRTHDAY, null);
    }


    public String getType() {
        return sharedpreferences.getString(TYPE, null);
    }


    public void setType(String type) {
        editor.putString(TYPE, type);
        editor.commit();
    }

    public String getCityId() {
        return sharedpreferences.getString(CITY_ID, null);
    }


    public void setCityId(String cityId) {
        editor.putString(CITY_ID, cityId);
        editor.commit();
    }


    public String getStateId() {
        return sharedpreferences.getString(STATE_ID, null);
    }


    public void setStateId(String stateId) {
        editor.putString(STATE_ID, stateId);
        editor.commit();
    }

    public String getGender() {
        return sharedpreferences.getString(GENDER, null);
    }


    public void setGender(String gender) {
        editor.putString(GENDER, gender);
        editor.commit();
    }

    public String getFname() {
        return sharedpreferences.getString(FNAME, null);
    }


    public void setFname(String fname) {
        editor.putString(FNAME, fname);
        editor.commit();
    }

    public String getLname() {
        return sharedpreferences.getString(LNAME, null);
    }


    public void setLname(String lname) {
        editor.putString(LNAME, lname);
        editor.commit();
    }

    public String getCityname() {
        return sharedpreferences.getString(CITYNAME, null);
    }


    public void setCityname(String cityname) {
        editor.putString(CITYNAME, cityname);
        editor.commit();
    }

}
