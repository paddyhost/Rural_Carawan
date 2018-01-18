package com.hatchers.ruralcaravane.locality.database;



public class CityTable
{


    public static final String CITY_TABLE = "CityTable";

    public static final String CITY_ID = "city_id",CITYNAME="city_name",LATITUDE="latitude",LONGITUDE="longitude";

    public static final String CREATE_CITY_TABLE="CREATE TABLE " + CITY_TABLE+
            "("+CITY_ID+" int PRIMARY KEY , "+CITYNAME+" TEXT, "+LATITUDE+" TEXT, "+LONGITUDE+" TEXT)";

    private String id, cityname, latitude, longitude;

    public CityTable() {
    }


    public CityTable(String id, String cityname, String latitude, String longitude)
    {
        this.id = id;
        this.cityname = cityname;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return cityname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
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
