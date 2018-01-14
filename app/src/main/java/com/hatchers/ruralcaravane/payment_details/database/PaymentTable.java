package com.hatchers.ruralcaravane.payment_details.database;


import android.os.Parcel;
import android.os.Parcelable;

public class PaymentTable implements Parcelable{

    public static final String PAYMENT_TABLE="PaymentTable";

    public static final String PAYMENT_ID="payment_id",PAYMENT_AMOUNT="payment_amount",TOTAL_PAID="total_paid",
                               REMAINING_AMOUNT="remaining_amount",UPLOAD_STATUS="upload_status",RECEIPT_IMAGE="receipt_image",
                               CUSTOMER_ID="customer_id",TECHNICIAN_ID="technician_id",KITCHEN_ID="kitchen_id",
                               INSTALLMENT_ID="installment_id",DATE_OF_PAYMENT="date_of_payment",TYPE="type",
                               PAYMENT_TYPE="payment_type",RECEIPT_NO="receipt_no",ISSUED_BY_ID="issued_by_id",
                               CHULLHA_ID="chullha_id",UPDATE_DATE="update_date",PAYMENT_UNIQUE_ID="payment_unique_id";

    public static final String CREATE_PAYMENT_TABLE="CREATE TABLE " + PAYMENT_TABLE +
            "("+PAYMENT_ID+" TEXT PRIMARY KEY ,"+PAYMENT_AMOUNT+" TEXT,"+TOTAL_PAID+" TEXT,"+REMAINING_AMOUNT+" TEXT, "+UPLOAD_STATUS+" TEXT, "
            +RECEIPT_IMAGE+" TEXT, "+CUSTOMER_ID+" TEXT, "+INSTALLMENT_ID+" TEXT, "+DATE_OF_PAYMENT+" TEXT, "+TECHNICIAN_ID+" TEXT, "+KITCHEN_ID+" TEXT , "
            +PAYMENT_TYPE+" TEXT, "+RECEIPT_NO+" TEXT, "+ISSUED_BY_ID+" TEXT, "+TYPE+" TEXT, "+CHULLHA_ID+" TEXT, "+UPDATE_DATE+" TEXT, "+PAYMENT_UNIQUE_ID+" TEXT)";

    private String payment_idValue,payment_amountValue,totalPaidValue,remaining_amountValue,upload_statusValue,customerIdValue,receiptImageValue,installmentIdValue,dateOfPaymentValue,technicianIdValue,kitchenIdValue,paymentTypeValue,receiptNoValue,issuedByIdValue,typeValue,chullhaIdValue,updateDateValue,paymentUniqueIdValue;

    public PaymentTable(String payment_idValue, String payment_amountValue, String totalPaidValue, String remaining_amountValue, String upload_statusValue,String customerIdValue,String receiptImageValue,String installmentIdValue,String dateOfPaymentValue,String technicianIdValue,String kitchenIdValue,String paymentTypeValue,String receiptNoValue,String issuedByIdValue,String typeValue,String chullhaIdValue,String updateDateValue,String paymentUniqueIdValue) {
        this.payment_idValue = payment_idValue;
        this.payment_amountValue = payment_amountValue;
        this.totalPaidValue = totalPaidValue;
        this.remaining_amountValue = remaining_amountValue;
        this.upload_statusValue = upload_statusValue;
        this.customerIdValue=customerIdValue;
        this.receiptImageValue=receiptImageValue;
        this.customerIdValue=customerIdValue;
        this.installmentIdValue=installmentIdValue;
        this.dateOfPaymentValue=dateOfPaymentValue;
        this.technicianIdValue=technicianIdValue;
        this.kitchenIdValue=kitchenIdValue;
        this.paymentTypeValue=paymentTypeValue;
        this.receiptNoValue=receiptNoValue;
        this.issuedByIdValue=issuedByIdValue;
        this.typeValue=typeValue;
        this.chullhaIdValue=chullhaIdValue;
        this.updateDateValue=updateDateValue;
        this.paymentUniqueIdValue=paymentUniqueIdValue;
    }

    public PaymentTable() {

    }

    protected PaymentTable(Parcel in) {
        payment_idValue = in.readString();
        payment_amountValue = in.readString();
        totalPaidValue = in.readString();
        remaining_amountValue = in.readString();
        upload_statusValue = in.readString();
        customerIdValue = in.readString();
        receiptImageValue = in.readString();
        installmentIdValue = in.readString();
        dateOfPaymentValue = in.readString();
        technicianIdValue = in.readString();
        kitchenIdValue = in.readString();
        paymentTypeValue = in.readString();
        receiptNoValue = in.readString();
        issuedByIdValue = in.readString();
        typeValue = in.readString();
        chullhaIdValue = in.readString();
        updateDateValue = in.readString();
        paymentUniqueIdValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payment_idValue);
        dest.writeString(payment_amountValue);
        dest.writeString(totalPaidValue);
        dest.writeString(remaining_amountValue);
        dest.writeString(upload_statusValue);
        dest.writeString(customerIdValue);
        dest.writeString(receiptImageValue);
        dest.writeString(installmentIdValue);
        dest.writeString(dateOfPaymentValue);
        dest.writeString(technicianIdValue);
        dest.writeString(kitchenIdValue);
        dest.writeString(paymentTypeValue);
        dest.writeString(receiptNoValue);
        dest.writeString(issuedByIdValue);
        dest.writeString(typeValue);
        dest.writeString(chullhaIdValue);
        dest.writeString(updateDateValue);
        dest.writeString(paymentUniqueIdValue);
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

    public String getPayment_amountValue() {
        return payment_amountValue;
    }

    public void setPayment_amountValue(String payment_amountValue) {
        this.payment_amountValue = payment_amountValue;
    }

    public String getAdvance_amountValue() {
        return totalPaidValue;
    }

    public void setAdvance_amountValue(String advance_amountValue) {
        this.totalPaidValue = advance_amountValue;
    }

    public String getRemaining_amountValue() {
        return remaining_amountValue;
    }

    public void setRemaining_amountValue(String remaining_amountValue) {
        this.remaining_amountValue = remaining_amountValue;
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

    public String getTotalPaidValue() {
        return totalPaidValue;
    }

    public void setTotalPaidValue(String totalPaidValue) {
        this.totalPaidValue = totalPaidValue;
    }

    public String getReceiptImageValue() {
        return receiptImageValue;
    }

    public void setReceiptImageValue(String receiptImageValue) {
        this.receiptImageValue = receiptImageValue;
    }

    public String getInstallmentIdValue() {
        return installmentIdValue;
    }

    public void setInstallmentIdValue(String installmentIdValue) {
        this.installmentIdValue = installmentIdValue;
    }

    public String getDateOfPaymentValue() {
        return dateOfPaymentValue;
    }

    public void setDateOfPaymentValue(String dateOfPaymentValue) {
        this.dateOfPaymentValue = dateOfPaymentValue;
    }

    public String getTechnicianIdValue() {
        return technicianIdValue;
    }

    public void setTechnicianIdValue(String technicianIdValue) {
        this.technicianIdValue = technicianIdValue;
    }

    public String getKitchenIdValue() {
        return kitchenIdValue;
    }

    public void setKitchenIdValue(String kitchenIdValue) {
        this.kitchenIdValue = kitchenIdValue;
    }

    public String getPaymentTypeValue() {
        return paymentTypeValue;
    }

    public void setPaymentTypeValue(String paymentTypeValue) {
        this.paymentTypeValue = paymentTypeValue;
    }

    public String getReceiptNoValue() {
        return receiptNoValue;
    }

    public void setReceiptNoValue(String receiptNoValue) {
        this.receiptNoValue = receiptNoValue;
    }

    public String getIssuedByIdValue() {
        return issuedByIdValue;
    }

    public void setIssuedByIdValue(String issuedByIdValue) {
        this.issuedByIdValue = issuedByIdValue;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getChullhaIdValue() {
        return chullhaIdValue;
    }

    public void setChullhaIdValue(String chullhaIdValue) {
        this.chullhaIdValue = chullhaIdValue;
    }

    public String getUpdateDateValue() {
        return updateDateValue;
    }

    public void setUpdateDateValue(String updateDateValue) {
        this.updateDateValue = updateDateValue;
    }

    public String getPaymentUniqueIdValue() {
        return paymentUniqueIdValue;
    }

    public void setPaymentUniqueIdValue(String paymentUniqueIdValue) {
        this.paymentUniqueIdValue = paymentUniqueIdValue;
    }
}
