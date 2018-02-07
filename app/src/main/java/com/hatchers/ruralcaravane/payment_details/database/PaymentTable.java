package com.hatchers.ruralcaravane.payment_details.database;


import android.os.Parcel;
import android.os.Parcelable;

public class PaymentTable implements Parcelable{

    public static final String PAYMENT_TABLE="PaymentTable";

    public static final String PAYMENT_ID="payment_id",AMOUNT="amount",TOTAL_PAID="total_paid",
                               BALANCE="balance",UPLOAD_STATUS="upload_status",RECEIPT_IMAGE="receipt_image",
                               CUSTOMER_ID="customer_id",KITCHEN_ID="kitchen_id",DATE_OF_PAYMENT="date_of_payment",
                               RECEIPT_NO="receipt_no",PAYMENT_UNIQUE_ID="payment_unique_id", PAYMENT_TYPE="payment_type",
                               UPLOAD_DATE="upload_date";

    public static final String CREATE_PAYMENT_TABLE="CREATE TABLE " + PAYMENT_TABLE +
            "("+PAYMENT_ID+" int PRIMARY KEY ,"+AMOUNT+" TEXT,"+TOTAL_PAID+" TEXT,"+BALANCE+" TEXT, "+UPLOAD_STATUS+" TEXT, "
            +RECEIPT_IMAGE+" TEXT, "+CUSTOMER_ID+" TEXT, "+DATE_OF_PAYMENT+" TEXT, "+KITCHEN_ID+" TEXT , "+RECEIPT_NO+" TEXT, "
            +PAYMENT_UNIQUE_ID+" TEXT, "+PAYMENT_TYPE+" TEXT, "+UPLOAD_DATE+" TEXT)";

    public static final String PAYMENT_ADDED_LOCAL="0", PAYMENT_ADDED_SERVER="1", PAYMENT_COMPLETED_LOCAL="2",
                                PAYMENT_COMPLETED_SERVER="3";

    private String payment_idValue,amountValue,totalPaidValue,balanceValue,upload_statusValue,customerIdValue,receiptImageValue,dateOfPaymentValue,kitchenIdValue,receiptNoValue,paymentUniqueIdValue,paymentTypeValue,uploadDateValue;

    public PaymentTable(String payment_idValue, String amountValue, String totalPaidValue, String balanceValue, String upload_statusValue,String customerIdValue,String receiptImageValue,String dateOfPaymentValue,String kitchenIdValue,String receiptNoValue,String paymentUniqueIdValue ,String paymentTypeValue,String uploadDateValue) {
        this.payment_idValue = payment_idValue;
        this.amountValue = amountValue;
        this.totalPaidValue = totalPaidValue;
        this.balanceValue = balanceValue;
        this.upload_statusValue = upload_statusValue;
        this.customerIdValue=customerIdValue;
        this.receiptImageValue=receiptImageValue;
        this.customerIdValue=customerIdValue;
        this.dateOfPaymentValue=dateOfPaymentValue;
        this.kitchenIdValue=kitchenIdValue;
        this.receiptNoValue=receiptNoValue;
        this.paymentUniqueIdValue=paymentUniqueIdValue;
        this.paymentTypeValue=paymentTypeValue;
        this.uploadDateValue=uploadDateValue;
    }

    public PaymentTable() {

    }

    protected PaymentTable(Parcel in) {
        payment_idValue = in.readString();
        amountValue = in.readString();
        totalPaidValue = in.readString();
        balanceValue = in.readString();
        upload_statusValue = in.readString();
        customerIdValue = in.readString();
        receiptImageValue = in.readString();
        dateOfPaymentValue = in.readString();
        kitchenIdValue = in.readString();
        receiptNoValue = in.readString();
        paymentUniqueIdValue = in.readString();
        paymentTypeValue=in.readString();
        uploadDateValue=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payment_idValue);
        dest.writeString(amountValue);
        dest.writeString(totalPaidValue);
        dest.writeString(balanceValue);
        dest.writeString(upload_statusValue);
        dest.writeString(customerIdValue);
        dest.writeString(receiptImageValue);
        dest.writeString(dateOfPaymentValue);
        dest.writeString(kitchenIdValue);
        dest.writeString(receiptNoValue);
        dest.writeString(paymentUniqueIdValue);
        dest.writeString(paymentTypeValue);
        dest.writeString(uploadDateValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentTable> CREATOR = new Creator<PaymentTable>() {
        @Override
        public PaymentTable createFromParcel(Parcel in) {
            return new PaymentTable(in);
        }

        @Override
        public PaymentTable[] newArray(int size) {
            return new PaymentTable[size];
        }
    };

    public String getPayment_idValue() {
        return payment_idValue;
    }

    public void setPayment_idValue(String payment_idValue) {
        this.payment_idValue = payment_idValue;
    }

    public String getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
    }

    public String getTotalPaidValue() {
        return totalPaidValue;
    }

    public void setTotalPaidValue(String totalPaidValue) {
        this.totalPaidValue = totalPaidValue;
    }

    public String getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
    }

    public String getUpload_statusValue() {
        return upload_statusValue;
    }

    public void setUpload_statusValue(String upload_statusValue) {
        this.upload_statusValue = upload_statusValue;
    }

    public String getCustomerIdValue() {
        return customerIdValue;
    }

    public void setCustomerIdValue(String customerIdValue) {
        this.customerIdValue = customerIdValue;
    }

    public String getReceiptImageValue() {
        return receiptImageValue;
    }

    public void setReceiptImageValue(String receiptImageValue) {
        this.receiptImageValue = receiptImageValue;
    }

    public String getDateOfPaymentValue() {
        return dateOfPaymentValue;
    }

    public void setDateOfPaymentValue(String dateOfPaymentValue) {
        this.dateOfPaymentValue = dateOfPaymentValue;
    }

    public String getKitchenIdValue() {
        return kitchenIdValue;
    }

    public void setKitchenIdValue(String kitchenIdValue) {
        this.kitchenIdValue = kitchenIdValue;
    }

    public String getReceiptNoValue() {
        return receiptNoValue;
    }

    public void setReceiptNoValue(String receiptNoValue) {
        this.receiptNoValue = receiptNoValue;
    }

    public String getPaymentUniqueIdValue() {
        return paymentUniqueIdValue;
    }

    public void setPaymentUniqueIdValue(String paymentUniqueIdValue) {
        this.paymentUniqueIdValue = paymentUniqueIdValue;
    }

    public String getPaymentTypeValue() {
        return paymentTypeValue;
    }

    public void setPaymentTypeValue(String paymentTypeValue) {
        this.paymentTypeValue = paymentTypeValue;
    }

    public String getUploadDateValue() {
        return uploadDateValue;
    }

    public void setUploadDateValue(String uploadDateValue) {
        this.uploadDateValue = uploadDateValue;
    }
}
