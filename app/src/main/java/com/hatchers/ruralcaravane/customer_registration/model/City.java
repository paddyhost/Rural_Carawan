package com.hatchers.ruralcaravane.customer_registration.model;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public class City
{
    private String id, cityname, latitude, longitude;

    public City(String id, String cityname, String latitude, String longitude)
    {
        this.id = id;
        this.cityname = cityname;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public City() {
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
