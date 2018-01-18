package com.hatchers.ruralcaravane.locality.database;

/**
 * Created by Nikam on 15/01/2018.
 */

public class StateTable {

    public static final String STATE_TABLE = "StateTable";

    public static final String STATE_ID="state_id",LATITUDE="latitude",LONGITUDE="longitude",STATENAME="statename";

    public static final String CREATE_STATE__TABLE="CREATE TABLE " + STATE_TABLE+
            "("+STATE_ID+" int PRIMARY KEY, "+LATITUDE+" TEXT, "+LONGITUDE+" TEXT, "+STATENAME+" TEXT)";

    private String stateId, StateName, latitude, longitude;

    @Override
    public String toString() {
        return StateName;
    }

    public StateTable() {
    }

    public StateTable(String stateId, String stateName, String latitude, String longitude) {
        this.stateId = stateId;
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
