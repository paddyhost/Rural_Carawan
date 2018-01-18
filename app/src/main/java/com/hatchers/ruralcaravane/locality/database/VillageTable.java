package com.hatchers.ruralcaravane.locality.database;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public class VillageTable
{


    public static final String VILLAGE_TABLE = "VillageTable";

    public static final String CITY_ID = "city_id",ID="id",LATITUDE="latitude",LONGITUDE="longitude",VILLAGENAME="villagename";

    public static final String CREATE_VILLAGE__TABLE="CREATE TABLE " + VILLAGE_TABLE+
            "("+CITY_ID+" TEXT, "+ID+" int PRIMARY KEY, "+LATITUDE+" TEXT, "+LONGITUDE+" TEXT, "+VILLAGENAME+" TEXT)";


    private String id, cityId, villageName, latitude, longitude;

    public VillageTable(String id, String cityId,String villageName, String latitude, String longitude)
    {
        this.id = id;
        this.cityId = cityId;
        this.villageName = villageName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public VillageTable() {
    }

    @Override
    public String toString() {
        return villageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }


}
