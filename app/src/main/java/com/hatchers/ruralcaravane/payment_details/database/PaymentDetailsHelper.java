package com.hatchers.ruralcaravane.payment_details.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.database.DatabaseHandler;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;

import java.util.ArrayList;

import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class PaymentDetailsHelper {

    public static boolean insertPaymentDetailsData(Context context, PaymentTable paymentTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();


            values.put(PaymentTable.PAYMENT_AMOUNT,paymentTable.getPayment_amountValue());
            values.put(PaymentTable.TOTAL_PAID,paymentTable.getTotalPaidValue());
            values.put(PaymentTable.REMAINING_AMOUNT,paymentTable.getRemaining_amountValue());
            values.put(PaymentTable.UPLOAD_STATUS,"0");
            values.put(PaymentTable.RECEIPT_IMAGE,paymentTable.getReceiptImageValue());
            values.put(PaymentTable.CUSTOMER_ID,paymentTable.getCustomerIdValue());
            values.put(PaymentTable.TECHNICIAN_ID,paymentTable.getTechnicianIdValue());
            values.put(PaymentTable.KITCHEN_ID,paymentTable.getKitchenIdValue());
            values.put(PaymentTable.INSTALLMENT_ID,paymentTable.getInstallmentIdValue());
            values.put(PaymentTable.DATE_OF_PAYMENT,getCurrentDateTime());
            values.put(PaymentTable.TYPE,paymentTable.getTypeValue());
            values.put(PaymentTable.PAYMENT_TYPE,paymentTable.getPaymentTypeValue());
            values.put(PaymentTable.RECEIPT_NO,paymentTable.getReceiptNoValue());
            values.put(PaymentTable.ISSUED_BY_ID,paymentTable.getIssuedByIdValue());
            values.put(PaymentTable.CHULLHA_ID,paymentTable.getChullhaIdValue());
            values.put(PaymentTable.UPDATE_DATE,getCurrentDateTime());


            if (db.insert(PaymentTable.PAYMENT_TABLE, null, values) > 0)
            {
                //Toast.makeText(context,"Payment details inserted",Toast.LENGTH_LONG).show();
                db.close();
                return true;
            }
            else
            {
                db.close();
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePaymentDetailsData(Context context, PaymentTable paymentTable)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PaymentTable.PAYMENT_ID,paymentTable.getPayment_idValue());
            values.put(PaymentTable.PAYMENT_AMOUNT,paymentTable.getPayment_amountValue());
            values.put(PaymentTable.TOTAL_PAID,paymentTable.getAdvance_amountValue());
            values.put(PaymentTable.REMAINING_AMOUNT,paymentTable.getRemaining_amountValue());
            values.put(PaymentTable.UPLOAD_STATUS,"1");
            values.put(PaymentTable.RECEIPT_IMAGE,paymentTable.getReceiptImageValue());
            values.put(PaymentTable.CUSTOMER_ID,paymentTable.getCustomerIdValue());
            values.put(PaymentTable.TECHNICIAN_ID,paymentTable.getTechnicianIdValue());
            values.put(PaymentTable.KITCHEN_ID,paymentTable.getKitchenIdValue());
            values.put(PaymentTable.INSTALLMENT_ID,paymentTable.getInstallmentIdValue());
            values.put(PaymentTable.DATE_OF_PAYMENT,getCurrentDateTime());
            values.put(PaymentTable.TYPE,paymentTable.getTypeValue());
            values.put(PaymentTable.PAYMENT_TYPE,paymentTable.getPaymentTypeValue());
            values.put(PaymentTable.RECEIPT_NO,paymentTable.getReceiptNoValue());
            values.put(PaymentTable.ISSUED_BY_ID,paymentTable.getIssuedByIdValue());
            values.put(PaymentTable.CHULLHA_ID,paymentTable.getChullhaIdValue());
            values.put(PaymentTable.UPDATE_DATE,getCurrentDateTime());


            // upadating Row
            if(db.update(PaymentTable.PAYMENT_TABLE, values, PaymentTable.PAYMENT_ID+"="+paymentTable.getPayment_idValue(), null)>0)
            {
                Toast.makeText(context,"Payment deatails updated",Toast.LENGTH_LONG).show();
                db.close();
                return true;
            }
            else
            {
                db.close();
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }


    public static PaymentTable getPaymentDetailsData(Context context)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+" WHERE "+ PaymentTable.TECHNICIAN_ID,null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                PaymentTable paymentTable = new PaymentTable();

                paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
                paymentTable.setPayment_amountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_AMOUNT)));
                paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
                paymentTable.setRemaining_amountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.REMAINING_AMOUNT)));
                paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
                paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
                paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
                paymentTable.setTechnicianIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TECHNICIAN_ID)));
                paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
                paymentTable.setInstallmentIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.INSTALLMENT_ID)));
                paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
                paymentTable.setTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TYPE)));
                paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
                paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
                paymentTable.setIssuedByIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.ISSUED_BY_ID)));
                paymentTable.setChullhaIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CHULLHA_ID)));
                paymentTable.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPDATE_DATE)));


                cursor.moveToNext();
                return  paymentTable;
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static ArrayList<PaymentTable> getPaymentDetailsList(Context context, String customeruniq_id)
    {
        ArrayList<PaymentTable> paymentTableArrayList = new ArrayList<PaymentTable>();
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+" WHERE "+PaymentTable.CUSTOMER_ID+" = '"+customeruniq_id+"' ORDER BY "+PaymentTable.DATE_OF_PAYMENT+ " DESC ",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                PaymentTable paymentTable=new PaymentTable();

                paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
                paymentTable.setPayment_amountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_AMOUNT)));
                paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
                paymentTable.setRemaining_amountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.REMAINING_AMOUNT)));
                paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
                paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
                paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
                paymentTable.setTechnicianIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TECHNICIAN_ID)));
                paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
                paymentTable.setInstallmentIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.INSTALLMENT_ID)));
                paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
                paymentTable.setTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TYPE)));
                paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
                paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
                paymentTable.setIssuedByIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.ISSUED_BY_ID)));
                paymentTable.setChullhaIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CHULLHA_ID)));
                paymentTable.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPDATE_DATE)));

                paymentTableArrayList.add(paymentTable);
                cursor.moveToNext();
            }
            return paymentTableArrayList;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static boolean deletePaymentDetailsById(Context context, String payment_id)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + PaymentTable.PAYMENT_TABLE +
                    " WHERE " + PaymentTable.PAYMENT_ID + " = '" + payment_id + "'");
            //delete all rows in titlebackground table

            Toast.makeText(context, "Payment Details Deleted Successfully", Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deletePaymentDetailsData(Context context)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + PaymentTable.PAYMENT_TABLE ); //delete all rows in titlebackground table


            Toast.makeText(context,"Payment Details Deleted Successfully",Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
