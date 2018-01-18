package com.hatchers.ruralcaravane.kitchen_suitability.database;


import android.os.Parcel;
import android.os.Parcelable;

import com.hatchers.ruralcaravane.kitchen_suitability.listner.KitchenListener;

import java.util.ArrayList;
import java.util.List;

public class KitchenTable implements Parcelable{


    public static final String KITCHEN_TABLE="KitchenTable";

    public static final String KITCHEN_ID="kitchen_id",KITCHEN_UNIQUE_ID="unique_id",HOUSE_TYPE="house_type",
                               ROOF_TYPE="roof_type",KITCHEN_HEIGHT="kitchen_height",UPLOAD_STATUS="upload_status",
                               PLACE_IMAGE="place_image",LATITUDE="latitude",LONGITUDE="longitude",GEO_ADDRESS="geo_address",
                               UPLOAD_DATE="upload_date",COST_OF_CHULLHA="cost_of_chullha",CUSTOMER_ID = "customer_id",
                               STEP1_IMAGE="step1_image",STEP2_IMAGE="step2_image",ADDED_DATE="added_date",
                               USER_UNIQUE_ID="user_unique_id",ADDED_BY_ID="added_by_id", UPDATE_DATE="update_date",
                               CONSTRUCTION_START_DATETIME="construction_start_datetime",
                               CONSTRUCTION_END_DATETIME="construction_end_datetime", KITCHEN_STATE ="kitchen_state";

    public static final String CREATE_KITCHEN_TABLE="CREATE TABLE " + KITCHEN_TABLE +
            "("+KITCHEN_ID+" int PRIMARY KEY ,"+HOUSE_TYPE+" TEXT,"+ROOF_TYPE+" TEXT,"
            +KITCHEN_HEIGHT+" TEXT, "+UPLOAD_STATUS+" TEXT, "+CUSTOMER_ID+" TEXT, "
            +PLACE_IMAGE+" TEXT, "+KITCHEN_UNIQUE_ID+" TEXT, "+LATITUDE+" TEXT, "
            +LONGITUDE+" TEXT, "+GEO_ADDRESS+" TEXT, "+UPLOAD_DATE+" TEXT, "+STEP1_IMAGE+" TEXT, "
            +STEP2_IMAGE+" TEXT, " +COST_OF_CHULLHA+" TEXT, "+ADDED_DATE+" TEXT, "+USER_UNIQUE_ID+" TEXT, "
            +ADDED_BY_ID+" TEXT, "+UPDATE_DATE+" TEXT, "+CONSTRUCTION_START_DATETIME+" TEXT, "
            +KITCHEN_STATE+" TEXT, "+CONSTRUCTION_END_DATETIME+" TEXT)";

    private String kitchen_idValue,house_typeValue,roof_typeValue,kitchen_heightValue,
            upload_statusValue,customer_idValue,placeImageValue,kitchenUniqueIdValue,
            latitudeValue,longitudeValue,uploadDateValue,geoAddressValue,costOfChullhaValue,
            step1_imageValue,step2_imageValue,addedDateValue,userUniqueIdValue,addedByIdValue,
            updateDateValue,constructionStartDateTimeValue,constructionEndDateTimeValue,kitchenState;

    public static final String STATE_A="A", STATE_C="C", STATE_N="N", STATE_P="P";

    public KitchenTable() {
    }

    public KitchenTable(String kitchen_idValue, String house_typeValue, String roof_typeValue,
                        String kitchen_heightValue, String upload_statusValue,String customer_idValue,
                        String placeImageValue,String geoAddressValue,String costOfChullhaValue,String kitchenUniqueIdValue,
                        String latitudeValue,String longitudeValue,String uploadDateValue,String step1_imageValue,
                        String step2_imageValue,String addedDateValue,String userUniqueIdValue,String addedByIdValue,
                        String updateDateValue,String constructionStartDateTimeValue,String constructionEndDateTimeValue,
                        String kitchenState)
    {
        this.kitchen_idValue = kitchen_idValue;
        this.house_typeValue = house_typeValue;
        this.roof_typeValue = roof_typeValue;
        this.kitchen_heightValue = kitchen_heightValue;
        this.upload_statusValue=upload_statusValue;
        this.customer_idValue=customer_idValue;
        this.placeImageValue=placeImageValue;
        this.geoAddressValue=geoAddressValue;
        this.kitchenUniqueIdValue=kitchenUniqueIdValue;
        this.latitudeValue=latitudeValue;
        this.longitudeValue=longitudeValue;
        this.uploadDateValue=uploadDateValue;
        this.costOfChullhaValue=costOfChullhaValue;
        this.step1_imageValue=step1_imageValue;
        this.step2_imageValue=step2_imageValue;
        this.addedDateValue=addedDateValue;
        this.userUniqueIdValue=userUniqueIdValue;
        this.addedByIdValue=addedByIdValue;
        this.updateDateValue=updateDateValue;
        this.constructionStartDateTimeValue=constructionStartDateTimeValue;
        this.constructionEndDateTimeValue=constructionEndDateTimeValue;
        this.kitchenState=kitchenState;
    }

    protected KitchenTable(Parcel in) {
        kitchen_idValue = in.readString();
        house_typeValue = in.readString();
        roof_typeValue = in.readString();
        kitchen_heightValue = in.readString();
        upload_statusValue = in.readString();
        customer_idValue = in.readString();
        placeImageValue = in.readString();
        kitchenUniqueIdValue = in.readString();
        latitudeValue = in.readString();
        longitudeValue = in.readString();
        uploadDateValue = in.readString();
        geoAddressValue = in.readString();
        costOfChullhaValue = in.readString();
        step1_imageValue=in.readString();
        step2_imageValue=in.readString();
        addedDateValue=in.readString();
        userUniqueIdValue=in.readString();
        addedByIdValue=in.readString();
        updateDateValue=in.readString();
        constructionStartDateTimeValue=in.readString();
        constructionEndDateTimeValue=in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kitchen_idValue);
        dest.writeString(house_typeValue);
        dest.writeString(roof_typeValue);
        dest.writeString(kitchen_heightValue);
        dest.writeString(upload_statusValue);
        dest.writeString(customer_idValue);
        dest.writeString(placeImageValue);
        dest.writeString(kitchenUniqueIdValue);
        dest.writeString(latitudeValue);
        dest.writeString(longitudeValue);
        dest.writeString(uploadDateValue);
        dest.writeString(geoAddressValue);
        dest.writeString(costOfChullhaValue);
        dest.writeString(step1_imageValue);
        dest.writeString(step2_imageValue);
        dest.writeString(addedDateValue);
        dest.writeString(userUniqueIdValue);
        dest.writeString(addedByIdValue);
        dest.writeString(updateDateValue);
        dest.writeString(constructionStartDateTimeValue);
        dest.writeString(constructionEndDateTimeValue);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KitchenTable> CREATOR = new Creator<KitchenTable>() {
        @Override
        public KitchenTable createFromParcel(Parcel in) {
            return new KitchenTable(in);
        }

        @Override
        public KitchenTable[] newArray(int size) {
            return new KitchenTable[size];
        }
    };

    public String getKitchen_idValue() {
        return kitchen_idValue;
    }

    public void setKitchen_idValue(String kitchen_idValue) {
        this.kitchen_idValue = kitchen_idValue;
    }

    public String getHouse_typeValue() {
        return house_typeValue;
    }

    public void setHouse_typeValue(String house_typeValue) {
        this.house_typeValue = house_typeValue;
    }

    public String getRoof_typeValue() {
        return roof_typeValue;
    }

    public void setRoof_typeValue(String roof_typeValue) {
        this.roof_typeValue = roof_typeValue;
    }

    public String getKitchen_heightValue() {
        return kitchen_heightValue;
    }

    public void setKitchen_heightValue(String kitchen_heightValue) {
        this.kitchen_heightValue = kitchen_heightValue;
    }

    public String getUpload_statusValue() {
        return upload_statusValue;
    }

    public void setUpload_statusValue(String upload_statusValue) {
        this.upload_statusValue = upload_statusValue;
    }

    public String getCustomer_idValue() {
        return customer_idValue;
    }

    public void setCustomer_idValue(String customer_idValue) {
        this.customer_idValue = customer_idValue;
    }

    public String getPlaceImageValue() {
        return placeImageValue;
    }

    public void setPlaceImageValue(String placeImageValue) {
        this.placeImageValue = placeImageValue;
    }

    public String getKitchenUniqueIdValue() {
        return kitchenUniqueIdValue;
    }

    public void setKitchenUniqueIdValue(String uniqueIdValue) {
        this.kitchenUniqueIdValue = uniqueIdValue;
    }

    public String getLatitudeValue() {
        return latitudeValue;
    }

    public void setLatitudeValue(String latitudeValue) {
        this.latitudeValue = latitudeValue;
    }

    public String getLongitudeValue() {
        return longitudeValue;
    }

    public void setLongitudeValue(String longitudeValue) {
        this.longitudeValue = longitudeValue;
    }

    public String getUploadDateValue() {
        return uploadDateValue;
    }

    public void setUploadDateValue(String uploadDateValue) {
        this.uploadDateValue = uploadDateValue;
    }

    public String getGeoAddressValue() {
        return geoAddressValue;
    }

    public void setGeoAddressValue(String geoAddressValue) {
        this.geoAddressValue = geoAddressValue;
    }

    public String getCostOfChullhaValue() {
        return costOfChullhaValue;
    }

    public void setCostOfChullhaValue(String costOfChullhaValue) {
        this.costOfChullhaValue = costOfChullhaValue;
    }

    public String getStep1_imageValue() {
        return step1_imageValue;
    }

    public void setStep1_imageValue(String step1_imageValue) {
        this.step1_imageValue = step1_imageValue;
    }

    public String getStep2_imageValue() {
        return step2_imageValue;
    }

    public void setStep2_imageValue(String step2_imageValue) {
        this.step2_imageValue = step2_imageValue;
    }

    public String getAddedDateValue() {
        return addedDateValue;
    }

    public void setAddedDateValue(String addedDateValue) {
        this.addedDateValue = addedDateValue;
    }

    public String getUserUniqueIdValue() {
        return userUniqueIdValue;
    }

    public void setUserUniqueIdValue(String userUniqueIdValue) {
        this.userUniqueIdValue = userUniqueIdValue;
    }

    public String getAddedByIdValue() {
        return addedByIdValue;
    }

    public void setAddedByIdValue(String addedByIdValue) {
        this.addedByIdValue = addedByIdValue;
    }

    public String getUpdateDateValue() {
        return updateDateValue;
    }

    public void setUpdateDateValue(String updateDateValue) {
        this.updateDateValue = updateDateValue;
    }

    public String getConstructionStartDateTimeValue() {
        return constructionStartDateTimeValue;
    }

    public void setConstructionStartDateTimeValue(String constructionStartDateTimeValue) {
        this.constructionStartDateTimeValue = constructionStartDateTimeValue;
    }

    public String getConstructionEndDateTimeValue() {
        return constructionEndDateTimeValue;
    }

    public void setConstructionEndDateTimeValue(String constructionEndDateTimeValue) {
        this.constructionEndDateTimeValue = constructionEndDateTimeValue;
    }

    ///events
    static final public int KITCHEN_ADD_SUCCESS=0,KITCHEN_ADD_FAILED=1,KITCHEN_ADD_RESPONSE_FAILED=2,
            KITCHEN_ADD_JSON_ERROR=3,KITCHEN_ADD_NO_CONNECTION_ERROR=4,KITCHEN_ADD_SERVER_ERROR=5,
            KITCHEN_ADD_NEWORK_ERROR=6,KITCHEN_ADD_PARSE_ERROR=7, KITCHEN_ADD_UNKNOWN_ERROR=8;


    private List<KitchenListener> kitchenEvent = new ArrayList<KitchenListener>();

    public void setOnKitchenEvent(KitchenListener toAdd) {

        kitchenEvent.add(toAdd);
    }
    public void fireOnKitchenEvent(int event) {

        for (KitchenListener hl : kitchenEvent) {

            if(event==KITCHEN_ADD_SUCCESS)
            {
                hl.onKitchen_Add_Success();
            }
            else if(event==KITCHEN_ADD_FAILED)
            {
                hl.onKitchen_Add_Failed();
            }
            else if(event==KITCHEN_ADD_RESPONSE_FAILED)
            {
                hl.onKitchen_Add_Response_Failed();
            }
            else if(event==KITCHEN_ADD_JSON_ERROR)
            {
                hl.onKitchen_Add_Json_Error();
            }
            else if(event==KITCHEN_ADD_NO_CONNECTION_ERROR)
            {
                hl.onKitchen_Add_No_Connection_Error();
            }
            else if(event==KITCHEN_ADD_SERVER_ERROR)
            {
                hl.onKitchen_Add_Server_Error();
            }
            else if(event==KITCHEN_ADD_NEWORK_ERROR)
            {
                hl.onKitchen_Add_Network_Error();
            }
            else if(event==KITCHEN_ADD_PARSE_ERROR)
            {
                hl.onKitchen_Add_Parse_Error();
            }
            else if(event==KITCHEN_ADD_UNKNOWN_ERROR)
            {
                hl.onKitchen_Add_Unknown_Error();
            }


        }
    }

    public String getKitchenState() {
        return kitchenState;
    }

    public void setKitchenState(String kitchenState) {
        this.kitchenState = kitchenState;
    }
}

