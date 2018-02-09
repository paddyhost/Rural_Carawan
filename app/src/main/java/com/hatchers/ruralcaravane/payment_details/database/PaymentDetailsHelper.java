package com.hatchers.ruralcaravane.payment_details.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.database.DatabaseHandler;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;

import java.util.ArrayList;

import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class PaymentDetailsHelper {

    public static boolean insertPaymentDetailsData(Context context, PaymentTable paymentTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PaymentTable.PAYMENT_UNIQUE_ID,paymentTable.getPaymentUniqueIdValue());
            values.put(PaymentTable.AMOUNT,paymentTable.getAmountValue());
            values.put(PaymentTable.TOTAL_PAID,paymentTable.getTotalPaidValue());
            values.put(PaymentTable.BALANCE,paymentTable.getBalanceValue());
            values.put(PaymentTable.UPLOAD_STATUS,paymentTable.getUpload_statusValue());
            values.put(PaymentTable.RECEIPT_IMAGE,paymentTable.getReceiptImageValue());
            values.put(PaymentTable.CUSTOMER_ID,paymentTable.getCustomerIdValue());
            values.put(PaymentTable.KITCHEN_ID,paymentTable.getKitchenIdValue());
            values.put(PaymentTable.DATE_OF_PAYMENT,getCurrentDateTime());
            values.put(PaymentTable.RECEIPT_NO,paymentTable.getReceiptNoValue());
            values.put(PaymentTable.PAYMENT_TYPE,paymentTable.getPaymentTypeValue());
            values.put(PaymentTable.UPLOAD_DATE,paymentTable.getUploadDateValue());


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
            values.put(PaymentTable.PAYMENT_UNIQUE_ID,paymentTable.getPaymentUniqueIdValue());
            values.put(PaymentTable.AMOUNT,paymentTable.getAmountValue());
            values.put(PaymentTable.TOTAL_PAID,paymentTable.getTotalPaidValue());
            values.put(PaymentTable.BALANCE,paymentTable.getBalanceValue());
            values.put(PaymentTable.UPLOAD_STATUS,paymentTable.getUpload_statusValue());
            values.put(PaymentTable.RECEIPT_IMAGE,paymentTable.getReceiptImageValue());
            values.put(PaymentTable.CUSTOMER_ID,paymentTable.getCustomerIdValue());
            values.put(PaymentTable.KITCHEN_ID,paymentTable.getKitchenIdValue());
            values.put(PaymentTable.DATE_OF_PAYMENT,getCurrentDateTime());
            values.put(PaymentTable.RECEIPT_NO,paymentTable.getReceiptNoValue());
            values.put(PaymentTable.PAYMENT_TYPE,paymentTable.getPaymentTypeValue());
            values.put(PaymentTable.UPLOAD_DATE,paymentTable.getUploadDateValue());


            // upadating Row
            if(db.update(PaymentTable.PAYMENT_TABLE, values, PaymentTable.PAYMENT_UNIQUE_ID+"='"+paymentTable.getPaymentUniqueIdValue()+"'", null)>0)
            {
                PrefManager prefManager=new PrefManager(context);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    Toast.makeText(context, context.getResources().getString(R.string.payment_details_updated_marathi), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, context.getResources().getString(R.string.payment_details_updated_english), Toast.LENGTH_LONG).show();
                }
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
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE,null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                PaymentTable paymentTable = new PaymentTable();

                paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
                paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
                paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
                paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
                paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
                paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
                paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
                paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
                paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
                paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
                paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
                paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
                paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));


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

    public static int  getPaymentAmountByCustomerId(Context context, String customerId)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+PaymentTable.TOTAL_PAID+") as total FROM " + PaymentTable.PAYMENT_TABLE+" WHERE "+ PaymentTable.CUSTOMER_ID +"='"+customerId+"'",null);
        //Cursor cursor = db.rawQuery("SELECT FROM "+ PaymentTable.PAYMENT_TABLE+" WHERE "+ PaymentTable.CUSTOMER_ID +"='"+customerId+"'",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {



                return  cursor.getInt(cursor.getColumnIndex("total"));
            }
            return 0;
        }
        catch (Exception e)
        {
            return 0;
        }
    }


    public static ArrayList<PaymentTable> getPaymentDetailsList(Context context, String customeruniq_id)
    {
        ArrayList<PaymentTable> paymentTableArrayList = new ArrayList<PaymentTable>();
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+" WHERE "+PaymentTable.CUSTOMER_ID+" = '"+customeruniq_id+"'"+" ORDER BY "+ PaymentTable.DATE_OF_PAYMENT + " DESC",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                PaymentTable paymentTable=new PaymentTable();

                paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
                paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
                paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
                paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
                paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
                paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
                paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
                paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
                paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
                paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
                paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
                paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
                paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));

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

    public static PaymentTable getCompletePaymentData(Context context, String customerId)
    {
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+
                " WHERE " + PaymentTable.UPLOAD_STATUS +"='2' AND "
                        + PaymentTable.CUSTOMER_ID +"='"+customerId+"'",
                null);
        try
        {
            cursor.moveToFirst();
            PaymentTable paymentTable=new PaymentTable();

            paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
            paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
            paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
            paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
            paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
            paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
            paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
            paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
            paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
            paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
            paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
            paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
            paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));

            return paymentTable;
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public static PaymentTable getUnUploadPaymentData(Context context, String customerId)
    {
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+
                        " WHERE " + PaymentTable.UPLOAD_STATUS +"='0' AND "
                        + PaymentTable.CUSTOMER_ID +"='"+customerId+"'",
                null);
        try
        {
            cursor.moveToFirst();
            PaymentTable paymentTable=new PaymentTable();

            paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
            paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
            paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
            paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
            paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
            paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
            paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
            paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
            paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
            paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
            paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
            paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
            paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));

            return paymentTable;
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public static PaymentTable getAllPaymentData(Context context, String customerId)
    {
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+
                        " WHERE "/* + PaymentTable.UPLOAD_STATUS +"='2' AND "*/
                        + PaymentTable.CUSTOMER_ID +"='"+customerId+"'",
                null);
        try
        {
            cursor.moveToFirst();
            PaymentTable paymentTable=new PaymentTable();

            paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
            paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
            paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
            paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
            paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
            paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
            paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
            paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
            paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
            paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
            paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
            paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
            paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));

            return paymentTable;
        }

        catch (Exception e)
        {
            return null;
        }
    }



    public static PaymentTable getAllPaymentDataByCustomer(Context context, String customerId)
    {
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PaymentTable.PAYMENT_TABLE+" WHERE "+ PaymentTable.CUSTOMER_ID +"='"+customerId+"'",null);
        try
        {
            cursor.moveToFirst();
            PaymentTable paymentTable=new PaymentTable();

            paymentTable.setPayment_idValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_ID)));
            paymentTable.setPaymentUniqueIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_UNIQUE_ID)));
            paymentTable.setAmountValue(cursor.getString(cursor.getColumnIndex(PaymentTable.AMOUNT)));
            paymentTable.setTotalPaidValue(cursor.getString(cursor.getColumnIndex(PaymentTable.TOTAL_PAID)));
            paymentTable.setBalanceValue(cursor.getString(cursor.getColumnIndex(PaymentTable.BALANCE)));
            paymentTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_STATUS)));
            paymentTable.setCustomerIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.CUSTOMER_ID)));
            paymentTable.setReceiptImageValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_IMAGE)));
            paymentTable.setKitchenIdValue(cursor.getString(cursor.getColumnIndex(PaymentTable.KITCHEN_ID)));
            paymentTable.setDateOfPaymentValue(cursor.getString(cursor.getColumnIndex(PaymentTable.DATE_OF_PAYMENT)));
            paymentTable.setReceiptNoValue(cursor.getString(cursor.getColumnIndex(PaymentTable.RECEIPT_NO)));
            paymentTable.setPaymentTypeValue(cursor.getString(cursor.getColumnIndex(PaymentTable.PAYMENT_TYPE)));
            paymentTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(PaymentTable.UPLOAD_DATE)));

            return paymentTable;
        }

        catch (Exception e)
        {
            return null;
        }
    }

}
