package com.hatchers.ruralcaravane.customer_registration.database;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.hatchers.ruralcaravane.customer_registration.listener.Customer_Listener;

import java.util.ArrayList;
import java.util.List;

public class CustomerTable implements Parcelable{

    public static final String CUSTOMER_TABLE = "CustomerTable";

    public static final String CUSTOMER_ID = "customer_id",CUSTOMER_NAME="customer_name",
            VILLAGE_NAME="village_name",CUSTOMER_ADDRESS="customer_address",
            CUSTOMER_MOBILENO="customer_mobileno",CUSTOMER_AGE="customer_age",
            CUSTOMER_GENDER="customer_gender",UPLOAD_STATUS="upload_status",
            UNIQUE_ID="unique_id",IMAGE_PATH="image_path",AADHAR_ID="aadhar_id",
            VILLAGE_ID="village_id",ADDED_DATE="added_date",CITY_ID="city_id",
            ADDED_BY_ID="added_by_id",UPLOAD_DATE="upload_date",UPDATE_DATE="update_date",
            CUSTOMER_ADDED = "customer_added", KITCHEN_ADDED = "kitchen_added", TEAM_ADDED = "team_added",
            CHULHA_PHOTO_ADDED = "chulha_photo_added", CONSTRUCTION_COMPLETE = "construction_complete",
            PAYMENT_ADDED = "payment_added", PAYMENT_COMPLETED = "payment_completed";

    public static final String CREATE_CUSTOMER_TABLE="CREATE TABLE " + CUSTOMER_TABLE+
            "("+CUSTOMER_ID+" int PRIMARY KEY ,"+CUSTOMER_NAME+" TEXT,"+CUSTOMER_ADDRESS+" TEXT,"
            +CUSTOMER_AGE+" TEXT,"+CUSTOMER_GENDER+" TEXT,"+CUSTOMER_MOBILENO+" TEXT,"+VILLAGE_NAME+" TEXT,"
            +UPLOAD_STATUS+" TEXT, "+UNIQUE_ID+" TEXT, "+IMAGE_PATH+" TEXT, "+AADHAR_ID+" TEXT, "
            +VILLAGE_ID+" TEXT, "+CITY_ID+" TEXT, "+ADDED_DATE+" TEXT, "+ADDED_BY_ID+" TEXT, "
            +CUSTOMER_ADDED+" TEXT, "+KITCHEN_ADDED+" TEXT, " +TEAM_ADDED+" TEXT, "
            +CHULHA_PHOTO_ADDED +" TEXT, " +CONSTRUCTION_COMPLETE +" TEXT, " +PAYMENT_ADDED +" TEXT, "
            +PAYMENT_COMPLETED +" TEXT, " +UPLOAD_DATE+" TEXT, "+UPDATE_DATE+" TEXT )";


    public static final String LOCAL="local", SERVER="server", NOT_ADDED="not_added";

    private String customerIdValue,customerNameValue,villageNameValue,customerAddressValue,
            customerMobilenoValue,customerAgeValue,customerGenderValue,upload_statusValue,
            uniqueIdValue,imagePathValue,aadharIdValue,villageIdValue,addedDateValue,cityId,
            addedByIdValue,uploadDateValue,updateDateValue,
            customer_added, kitchen_added, team_added, chulha_photo_added, payment_added,
            payment_completed,constructionComplete;

        //  add customer = 0;
        //  upload customer = 1;
        //  add kitchen = 2;
        //  upload kitchen = 3;
        //  add team = 4;
        //  upload team = 5;
        //  add photos = 6;
        //  upload photos = 7;
        //  add payment = 8;
        //  upload payment = 9;
        //  complete payment = 10;
        //  upload complete payment = 11;

    public CustomerTable() {
    }

    public CustomerTable(String customerIdValue, String customerNameValue, String villageNameValue,
                         String customerAddressValue, String customerMobilenoValue, String customerAgeValue,
                         String customerGenderValue,String upload_statusValue, String uniqueIdValue,
                         String imagePathValue,String aadharIdValue, String villageIdValue,String addedDateValue,
                         String cityId,String addedByIdValue,String uploadDateValue, String updateDateValue,
                         String customer_added, String kitchen_added, String team_added, String chulha_photo_added,
                         String team_add_complete, String payment_added, String payment_completed)
    {
        this.customerIdValue = customerIdValue;
        this.customerNameValue = customerNameValue;
        this.villageNameValue = villageNameValue;
        this.customerAddressValue = customerAddressValue;
        this.customerMobilenoValue = customerMobilenoValue;
        this.customerAgeValue = customerAgeValue;
        this.customerGenderValue = customerGenderValue;
        this.upload_statusValue=upload_statusValue;
        this.uniqueIdValue=uniqueIdValue;
        this.imagePathValue=imagePathValue;
        this.aadharIdValue=aadharIdValue;
        this.villageIdValue=villageIdValue;
        this.addedDateValue=addedDateValue;
        this.cityId=cityId;
        this.addedByIdValue=addedByIdValue;
        this.uploadDateValue=uploadDateValue;
        this.updateDateValue=updateDateValue;
        this.customer_added=customer_added;
        this.kitchen_added=kitchen_added;
        this.team_added=team_added;
        this.chulha_photo_added=chulha_photo_added;
        this.constructionComplete=team_add_complete;
        this.payment_added=payment_added;
        this.payment_completed=payment_completed;
    }

    protected CustomerTable(Parcel in) {
        customerIdValue = in.readString();
        customerNameValue = in.readString();
        villageNameValue = in.readString();
        customerAddressValue = in.readString();
        customerMobilenoValue = in.readString();
        customerAgeValue = in.readString();
        customerGenderValue = in.readString();
        upload_statusValue = in.readString();
        uniqueIdValue = in.readString();
        imagePathValue = in.readString();
        aadharIdValue = in.readString();
        villageIdValue = in.readString();
        addedDateValue = in.readString();
        cityId = in.readString();
        addedByIdValue = in.readString();
        uploadDateValue = in.readString();
        updateDateValue = in.readString();
        customer_added = in.readString();
        kitchen_added = in.readString();
        team_added = in.readString();
        chulha_photo_added = in.readString();
        constructionComplete = in.readString();
        payment_added = in.readString();
        payment_completed = in.readString();
    }

    public static final Creator<CustomerTable> CREATOR = new Creator<CustomerTable>() {
        @Override
        public CustomerTable createFromParcel(Parcel in) {
            return new CustomerTable(in);
        }

        @Override
        public CustomerTable[] newArray(int size) {
            return new CustomerTable[size];
        }
    };

    public String getCustomerIdValue() {
        return customerIdValue;
    }

    public void setCustomerIdValue(String customerIdValue) {
        this.customerIdValue = customerIdValue;
    }

    public String getCustomerNameValue() {
        return customerNameValue;
    }

    public void setCustomerNameValue(String customerNameValue) {
        this.customerNameValue = customerNameValue;
    }

    public String getVillageNameValue() {
        return villageNameValue;
    }

    public void setVillageNameValue(String villageNameValue) {
        this.villageNameValue = villageNameValue;
    }

    public String getCustomerAddressValue() {
        return customerAddressValue;
    }

    public void setCustomerAddressValue(String customerAddressValue) {
        this.customerAddressValue = customerAddressValue;
    }

    public String getCustomerMobilenoValue() {
        return customerMobilenoValue;
    }

    public void setCustomerMobilenoValue(String customerMobilenoValue) {
        this.customerMobilenoValue = customerMobilenoValue;
    }

    public String getCustomerAgeValue() {
        return customerAgeValue;
    }

    public void setCustomerAgeValue(String customerAgeValue) {
        this.customerAgeValue = customerAgeValue;
    }

    public String getCustomerGenderValue() {
        return customerGenderValue;
    }

    public void setCustomerGenderValue(String customerGenderValue) {
        this.customerGenderValue = customerGenderValue;
    }

    public String getUpload_statusValue() {
        return upload_statusValue;
    }

    public void setUpload_statusValue(String upload_statusValue) {
        this.upload_statusValue = upload_statusValue;
    }

    public String getUniqueIdValue() {
        return uniqueIdValue;
    }

    public void setUniqueIdValue(String uniqueIdValue) {
        this.uniqueIdValue = uniqueIdValue;
    }

    public String getImagePathValue() {
        return imagePathValue;
    }

    public void setImagePathValue(String imagePathValue) {
        this.imagePathValue = imagePathValue;
    }

    public String getAadharIdValue() {
        return aadharIdValue;
    }

    public void setAadharIdValue(String aadharIdValue) {
        this.aadharIdValue = aadharIdValue;
    }

    public String getVillageIdValue() {
        return villageIdValue;
    }

    public void setVillageIdValue(String villageIdValue) {
        this.villageIdValue = villageIdValue;
    }

    public String getAddedDateValue() {
        return addedDateValue;
    }

    public void setAddedDateValue(String addedDateValue) {
        this.addedDateValue = addedDateValue;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddedByIdValue() {
        return addedByIdValue;
    }

    public void setAddedByIdValue(String addedByIdValue) {
        this.addedByIdValue = addedByIdValue;
    }

    public String getUploadDateValue() {
        return uploadDateValue;
    }

    public void setUploadDateValue(String uploadDateValue) {
        this.uploadDateValue = uploadDateValue;
    }

    public String getUpdateDateValue() {
        return updateDateValue;
    }

    public void setUpdateDateValue(String updateDateValue) {
        this.updateDateValue = updateDateValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(customerIdValue);
        parcel.writeString(customerNameValue);
        parcel.writeString(villageNameValue);
        parcel.writeString(customerAddressValue);
        parcel.writeString(customerMobilenoValue);
        parcel.writeString(customerAgeValue);
        parcel.writeString(customerGenderValue);
        parcel.writeString(upload_statusValue);
        parcel.writeString(uniqueIdValue);
        parcel.writeString(imagePathValue);
        parcel.writeString(aadharIdValue);
        parcel.writeString(villageIdValue);
        parcel.writeString(addedDateValue);
        parcel.writeString(cityId);
        parcel.writeString(addedByIdValue);
        parcel.writeString(uploadDateValue);
        parcel.writeString(updateDateValue);
        parcel.writeString(customer_added);
        parcel.writeString(kitchen_added);
        parcel.writeString(team_added);
        parcel.writeString(chulha_photo_added);
        parcel.writeString(constructionComplete);
        parcel.writeString(payment_added);
        parcel.writeString(payment_completed);
    }

    ///events
    static final public int CUSTOMER_ADD_SUCCESS=0,CUSTOMER_ADD_FAILED=1,CUSTOMER_ADD_RESPONSE_FAILED=2,
            CUSTOMER_ADD_JSON_ERROR=3,CUSTOMER_ADD_NO_CONNECTION_ERROR=4,CUSTOMER_ADD_SERVER_ERROR=5,
            CUSTOMER_ADD_NEWORK_ERROR=6,CUSTOMER_ADD_PARSE_ERROR=7, CUSTOMER_ADD_UNKNOWN_ERROR=8;


    private List<Customer_Listener> customerEvents = new ArrayList<Customer_Listener>();

    public void setOnCustomerEvent(Customer_Listener toAdd) {

        customerEvents.add(toAdd);
    }
    public void fireOnCustomerEvent(int event) {

        for (Customer_Listener hl : customerEvents) {

            if(event==CUSTOMER_ADD_SUCCESS)
            {
                hl.onCustomer_Add_Success();
            }
            else if(event==CUSTOMER_ADD_FAILED)
            {
                hl.onCustomer_Add_Failed();
            }
            else if(event==CUSTOMER_ADD_RESPONSE_FAILED)
            {
                hl.onCustomer_Add_Response_Failed();
            }
            else if(event==CUSTOMER_ADD_JSON_ERROR)
            {
                hl.onCustomer_Add_Json_Error();
            }
            else if(event==CUSTOMER_ADD_NO_CONNECTION_ERROR)
            {
                hl.onCustomer_Add_No_Connection_Error();
            }
            else if(event==CUSTOMER_ADD_SERVER_ERROR)
            {
                hl.onCustomer_Add_Server_Error();
            }
            else if(event==CUSTOMER_ADD_NEWORK_ERROR)
            {
                hl.onCustomer_Add_Network_Error();
            }
            else if(event==CUSTOMER_ADD_PARSE_ERROR)
            {
                hl.onCustomer_Add_Parse_Error();
            }
            else if(event==CUSTOMER_ADD_UNKNOWN_ERROR)
            {
                hl.onCustomer_Add_Unknown_Error();
            }


        }
    }

    public String getCustomer_added() {
        return customer_added;
    }

    public void setCustomer_added(String customer_added) {
        this.customer_added = customer_added;
    }

    public String getKitchen_added() {
        return kitchen_added;
    }

    public void setKitchen_added(String kitchen_added) {
        this.kitchen_added = kitchen_added;
    }

    public String getTeam_added() {
        return team_added;
    }

    public void setTeam_added(String team_added) {
        this.team_added = team_added;
    }

    public String getChulha_photo_added() {
        return chulha_photo_added;
    }

    public void setChulha_photo_added(String chulha_photo_added) {
        this.chulha_photo_added = chulha_photo_added;
    }

    public String getconstructionComplete() {
        return constructionComplete;
    }

    public void setconstructionComplete(String constructionComplete) {
        this.constructionComplete = constructionComplete;
    }

    public String getPayment_added() {
        return payment_added;
    }

    public void setPayment_added(String payment_added) {
        this.payment_added = payment_added;
    }

    public String getPayment_completed() {
        return payment_completed;
    }

    public void setPayment_completed(String payment_completed) {
        this.payment_completed = payment_completed;
    }
}
