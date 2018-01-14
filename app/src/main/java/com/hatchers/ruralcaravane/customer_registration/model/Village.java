package com.hatchers.ruralcaravane.customer_registration.model;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public class Village
{
    private String id,cityId,  villageName, latitude, longitude;

    public Village(String id, String villageName, String latitude, String longitude)
    {
        this.id = id;
        this.villageName = villageName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Village() {
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
