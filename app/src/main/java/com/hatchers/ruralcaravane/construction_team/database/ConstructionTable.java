package com.hatchers.ruralcaravane.construction_team.database;


import android.os.Parcel;
import android.os.Parcelable;

public class ConstructionTable implements Parcelable {

    public static final String CONSTRUCTION_TEAM_TABLE = "Construction_Team_Table";

    public static final String TECHNICIAN_ID = "technician_id",TECHNICIAN_NAME="technician_name",
            TECHNICIAN_ADDRESS="technician_address",TECHNICIAN_MOBILENO="technician_mobileno",
            TECHNICIAN_AGE="technician_age", TECHNICIAN_GENDER="technician_gender",
            UPLOAD_STATUS="upload_status",TECHNICIAN_UNIQUE_ID="technician_unique_id",
            CUSTOMER_ID="customer_id",KITCHEN_ID="kitchen_id",DATETIME="date_time",
            KITCHEN_UNIQUE_ID="kitchen_unique_id", ADDED_BY_ID="added_by_id",
            ADDED_DATE="added_date",UPDATE_DATE="update_date";

    public static final String CREATE_CONSTRUCTION_TEAM_TABLE="CREATE TABLE " + CONSTRUCTION_TEAM_TABLE+
            "("+TECHNICIAN_ID+" TEXT PRIMARY KEY ,"+TECHNICIAN_NAME+" TEXT,"
            +TECHNICIAN_ADDRESS+" TEXT,"+TECHNICIAN_AGE+" TEXT,"+TECHNICIAN_GENDER+" TEXT,"
            +TECHNICIAN_MOBILENO+" TEXT, "+UPLOAD_STATUS+" TEXT, " +TECHNICIAN_UNIQUE_ID+" TEXT, "
            +CUSTOMER_ID+" TEXT, " +KITCHEN_ID+" TEXT, "+KITCHEN_UNIQUE_ID+" TEXT, "+DATETIME+" TEXT,"
            +ADDED_BY_ID+" TEXT, "+ADDED_DATE+" TEXT, "+UPDATE_DATE+" TEXT)";

    private  String technicianIdValue,technicianNameValue,
            technicianAddressValue,technicianMobileNoValue,
            technicianAgeValue,technicianGenderValue,
            uploadStatusValue, customerIdValue,kitchenIdValue,
            dateTimeValue,technicianUniqueIdValue,kitchentUniqueId,
            addedByIdValue,addedDateValue,updateDateValue;

    public ConstructionTable() {
    }

    public ConstructionTable(String technicianIdValue,String kitchentUniqueId, String technicianNameValue, String technicianAddressValue, String technicianMobileNoValue, String technicianAgeValue, String technicianGenderValue,String uploadStatusValue,String customerIdValue,String kitchenIdValue,String dateTimeValue,String technicianUniqueIdValue,String addedByIdValue,String addedDateValue,String updateDateValue) {
        this.technicianIdValue = technicianIdValue;
        this.technicianNameValue = technicianNameValue;
        this.technicianAddressValue = technicianAddressValue;
        this.technicianMobileNoValue = technicianMobileNoValue;
        this.technicianAgeValue = technicianAgeValue;
        this.technicianGenderValue = technicianGenderValue;
        this.uploadStatusValue=uploadStatusValue;
        this.customerIdValue=customerIdValue;
        this.kitchenIdValue=kitchenIdValue;
        this.dateTimeValue=dateTimeValue;
        this.technicianUniqueIdValue=technicianUniqueIdValue;
        this.kitchentUniqueId=kitchentUniqueId;
        this.addedByIdValue=addedByIdValue;
        this.addedDateValue=addedDateValue;
        this.updateDateValue=updateDateValue;
    }


    protected ConstructionTable(Parcel in) {
        technicianIdValue = in.readString();
        technicianNameValue = in.readString();
        technicianAddressValue = in.readString();
        technicianMobileNoValue = in.readString();
        technicianAgeValue = in.readString();
        technicianGenderValue = in.readString();
        uploadStatusValue = in.readString();
        customerIdValue = in.readString();
        kitchenIdValue = in.readString();
        dateTimeValue = in.readString();
        technicianUniqueIdValue = in.readString();
        kitchentUniqueId = in.readString();
        addedByIdValue = in.readString();
        addedDateValue = in.readString();
        updateDateValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(technicianIdValue);
        dest.writeString(technicianNameValue);
        dest.writeString(technicianAddressValue);
        dest.writeString(technicianMobileNoValue);
        dest.writeString(technicianAgeValue);
        dest.writeString(technicianGenderValue);
        dest.writeString(uploadStatusValue);
        dest.writeString(customerIdValue);
        dest.writeString(kitchenIdValue);
        dest.writeString(dateTimeValue);
        dest.writeString(technicianUniqueIdValue);
        dest.writeString(kitchentUniqueId);
        dest.writeString(addedByIdValue);
        dest.writeString(addedDateValue);
        dest.writeString(updateDateValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConstructionTable> CREATOR = new Creator<ConstructionTable>() {
        @Override
        public ConstructionTable createFromParcel(Parcel in) {
            return new ConstructionTable(in);
        }

        @Override
        public ConstructionTable[] newArray(int size) {
            return new ConstructionTable[size];
        }
    };

    public String getTechnicianIdValue() {
        return technicianIdValue;
    }

    public void setTechnicianIdValue(String technicianIdValue) {
        this.technicianIdValue = technicianIdValue;
    }

    public String getTechnicianNameValue() {
        return technicianNameValue;
    }

    public void setTechnicianNameValue(String technicianNameValue) {
        this.technicianNameValue = technicianNameValue;
    }

    public String getTechnicianAddressValue() {
        return technicianAddressValue;
    }

    public void setTechnicianAddressValue(String technicianAddressValue) {
        this.technicianAddressValue = technicianAddressValue;
    }

    public String getTechnicianMobileNoValue() {
        return technicianMobileNoValue;
    }

    public void setTechnicianMobileNoValue(String technicianMobileNoValue) {
        this.technicianMobileNoValue = technicianMobileNoValue;
    }

    public String getTechnicianAgeValue() {
        return technicianAgeValue;
    }

    public void setTechnicianAgeValue(String technicianAgeValue) {
        this.technicianAgeValue = technicianAgeValue;
    }

    public String getTechnicianGenderValue() {
        return technicianGenderValue;
    }

    public void setTechnicianGenderValue(String technicianGenderValue) {
        this.technicianGenderValue = technicianGenderValue;
    }

    public String getUploadStatusValue() {
        return uploadStatusValue;
    }

    public void setUploadStatusValue(String uploadStatusValue) {
        this.uploadStatusValue = uploadStatusValue;
    }

    public String getCustomerIdValue() {
        return customerIdValue;
    }

    public void setCustomerIdValue(String customerIdValue) {
        this.customerIdValue = customerIdValue;
    }

    public String getKitchenIdValue() {
        return kitchenIdValue;
    }

    public void setKitchenIdValue(String kitchenIdValue) {
        this.kitchenIdValue = kitchenIdValue;
    }

    public String getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(String dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    public String getTechnicianUniqueIdValue() {
        return technicianUniqueIdValue;
    }

    public void setTechnicianUniqueIdValue(String technicianUniqueIdValue) {
        this.technicianUniqueIdValue = technicianUniqueIdValue;
    }

    public String getKitchentUniqueId() {
        return kitchentUniqueId;
    }

    public void setKitchentUniqueId(String kitchentUniqueId) {
        this.kitchentUniqueId = kitchentUniqueId;
    }

    public String getAddedByIdValue() {
        return addedByIdValue;
    }

    public void setAddedByIdValue(String addedByIdValue) {
        this.addedByIdValue = addedByIdValue;
    }

    public String getAddedDateValue() {
        return addedDateValue;
    }

    public void setAddedDateValue(String addedDateValue) {
        this.addedDateValue = addedDateValue;
    }

    public String getUpdateDateValue() {
        return updateDateValue;
    }

    public void setUpdateDateValue(String updateDateValue) {
        this.updateDateValue = updateDateValue;
    }
}
