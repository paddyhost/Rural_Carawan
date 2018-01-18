package com.hatchers.ruralcaravane.locality.database;

/**
 * Created by Nikam on 15/01/2018.
 */

public class StateTable {

    public static final String STATE_TABLE = "StateTable";

    public static final String CITY_ID = "city_id",STATE_ID="state_id",LATITUDE="latitude",LONGITUDE="longitude",STATENAME="statename";

    public static final String CREATE_STATE__TABLE="CREATE TABLE " + STATE_TABLE+
            "("+CITY_ID+" TEXT, "+STATE_ID+" int PRIMARY KEY, "+LATITUDE+" TEXT, "+LONGITUDE+" TEXT, "+STATENAME+" TEXT)";

    private String stateId, cityId, StateName, latitude, longitude;

    public StateTable() {
    }

    public StateTable(String stateId, String cityId, String stateName, String latitude, String longitude) {
        this.stateId = stateId;
        this.cityId = cityId;
        StateName = stateName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
