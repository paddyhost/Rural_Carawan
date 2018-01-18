package com.hatchers.ruralcaravane.kitchen_suitability.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.database.DatabaseHandler;

import java.util.ArrayList;

import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class KitchenTableHelper {


    public static boolean insertKitchenData(Context context, KitchenTable kitchenTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

                values.put(KitchenTable.HOUSE_TYPE,kitchenTable.getHouse_typeValue());
                values.put(KitchenTable.ROOF_TYPE,kitchenTable.getRoof_typeValue());
                values.put(KitchenTable.KITCHEN_HEIGHT,kitchenTable.getKitchen_heightValue());
                values.put(KitchenTable.UPLOAD_STATUS,"0");
                values.put(KitchenTable.CUSTOMER_ID,kitchenTable.getCustomer_idValue());
                values.put(KitchenTable.PLACE_IMAGE,kitchenTable.getPlaceImageValue());
                values.put(KitchenTable.KITCHEN_UNIQUE_ID,kitchenTable.getKitchenUniqueIdValue());
                values.put(KitchenTable.LATITUDE,kitchenTable.getLatitudeValue());
                values.put(KitchenTable.LONGITUDE,kitchenTable.getLongitudeValue());
                values.put(KitchenTable.GEO_ADDRESS,kitchenTable.getGeoAddressValue());
                values.put(KitchenTable.COST_OF_CHULLHA,kitchenTable.getCostOfChullhaValue());
                values.put(KitchenTable.STEP1_IMAGE,kitchenTable.getStep1_imageValue());
                values.put(KitchenTable.STEP2_IMAGE,kitchenTable.getStep2_imageValue());
                values.put(KitchenTable.ADDED_DATE,getCurrentDateTime());
                values.put(KitchenTable.ADDED_BY_ID,kitchenTable.getAddedByIdValue());
                values.put(KitchenTable.USER_UNIQUE_ID,kitchenTable.getUserUniqueIdValue());
                values.put(KitchenTable.UPDATE_DATE,kitchenTable.getUpdateDateValue());
                values.put(KitchenTable.CONSTRUCTION_START_DATETIME,kitchenTable.getConstructionStartDateTimeValue());
                values.put(KitchenTable.CONSTRUCTION_END_DATETIME,kitchenTable.getConstructionEndDateTimeValue());

            long i=   db.insert(KitchenTable.KITCHEN_TABLE, null, values);
            if ( i> 0)
            {
                kitchenTable.setKitchen_idValue(String.valueOf(i));
               // Toast.makeText(context,"KItchen data inserted",Toast.LENGTH_LONG).show();
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

    public static boolean updateKitchenData(Context context, KitchenTable kitchen_table)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();


            values.put(KitchenTable.KITCHEN_HEIGHT,kitchen_table.getKitchen_heightValue());
            values.put(KitchenTable.HOUSE_TYPE,kitchen_table.getHouse_typeValue());
            values.put(KitchenTable.ROOF_TYPE,kitchen_table.getRoof_typeValue());
            values.put(KitchenTable.CUSTOMER_ID,kitchen_table.getCustomer_idValue());
            values.put(KitchenTable.PLACE_IMAGE,kitchen_table.getPlaceImageValue());
            values.put(KitchenTable.KITCHEN_UNIQUE_ID,kitchen_table.getKitchenUniqueIdValue());
            values.put(KitchenTable.LATITUDE,kitchen_table.getLatitudeValue());
            values.put(KitchenTable.LONGITUDE,kitchen_table.getLongitudeValue());
            values.put(KitchenTable.UPLOAD_DATE,kitchen_table.getUploadDateValue());
            values.put(KitchenTable.GEO_ADDRESS,kitchen_table.getGeoAddressValue());
            values.put(KitchenTable.COST_OF_CHULLHA,kitchen_table.getCostOfChullhaValue());
            values.put(KitchenTable.STEP1_IMAGE,kitchen_table.getStep1_imageValue());
            values.put(KitchenTable.STEP2_IMAGE,kitchen_table.getStep2_imageValue());
            values.put(KitchenTable.ADDED_DATE,getCurrentDateTime());
            values.put(KitchenTable.ADDED_BY_ID,kitchen_table.getAddedByIdValue());
            values.put(KitchenTable.USER_UNIQUE_ID,kitchen_table.getUserUniqueIdValue());
            values.put(KitchenTable.UPDATE_DATE,getCurrentDateTime());
            values.put(KitchenTable.UPLOAD_STATUS,"1");
            values.put(KitchenTable.CONSTRUCTION_START_DATETIME,kitchen_table.getConstructionStartDateTimeValue());
            values.put(KitchenTable.CONSTRUCTION_END_DATETIME,kitchen_table.getConstructionEndDateTimeValue());


            // upadating Row
            if(db.update(KitchenTable.KITCHEN_TABLE, values, KitchenTable.KITCHEN_UNIQUE_ID+"='"+kitchen_table.getKitchenUniqueIdValue()+"'", null)>0)
            {
                Toast.makeText(context,"Kitchen data updated",Toast.LENGTH_LONG).show();
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

    public static ArrayList<KitchenTable> getKitchenDataList(Context context, CustomerTable customerTable)
    {
        ArrayList<KitchenTable> customerTableArrayList = new ArrayList<KitchenTable>();
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ KitchenTable.KITCHEN_TABLE+" WHERE "+KitchenTable.CUSTOMER_ID+"='"+customerTable.getUniqueIdValue()+"'",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                KitchenTable kitchen_table=new KitchenTable();

                kitchen_table.setKitchen_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_ID)));
                kitchen_table.setHouse_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.HOUSE_TYPE)));
                kitchen_table.setRoof_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ROOF_TYPE)));
                kitchen_table.setKitchen_heightValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_HEIGHT)));
                kitchen_table.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_STATUS)));
                kitchen_table.setCustomer_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CUSTOMER_ID)));
                kitchen_table.setPlaceImageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.PLACE_IMAGE)));
                kitchen_table.setKitchenUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_UNIQUE_ID)));
                kitchen_table.setLatitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LATITUDE)));
                kitchen_table.setLongitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LONGITUDE)));
                kitchen_table.setUploadDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_DATE)));
                kitchen_table.setGeoAddressValue(cursor.getString(cursor.getColumnIndex(KitchenTable.GEO_ADDRESS)));
                kitchen_table.setCostOfChullhaValue(cursor.getString(cursor.getColumnIndex(KitchenTable.COST_OF_CHULLHA)));
                kitchen_table.setStep1_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP1_IMAGE)));
                kitchen_table.setStep2_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP2_IMAGE)));
                kitchen_table.setAddedByIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_BY_ID)));
                kitchen_table.setAddedDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_DATE)));
                kitchen_table.setUserUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.USER_UNIQUE_ID)));
                kitchen_table.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPDATE_DATE)));
                kitchen_table.setConstructionStartDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_START_DATETIME)));
                kitchen_table.setConstructionEndDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_END_DATETIME)));


                customerTableArrayList.add(kitchen_table);
                cursor.moveToNext();
            }
            return customerTableArrayList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static KitchenTable getKitchenDetailsData(Context context, String custUniqueId)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ KitchenTable.KITCHEN_TABLE+" WHERE "+ KitchenTable.CUSTOMER_ID+"='"+custUniqueId+"'",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                KitchenTable kitchen_table = new KitchenTable();

                kitchen_table.setKitchen_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_ID)));
                kitchen_table.setHouse_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.HOUSE_TYPE)));
                kitchen_table.setRoof_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ROOF_TYPE)));
                kitchen_table.setKitchen_heightValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_HEIGHT)));
                kitchen_table.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_STATUS)));
                kitchen_table.setCustomer_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CUSTOMER_ID)));
                kitchen_table.setPlaceImageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.PLACE_IMAGE)));
                kitchen_table.setKitchenUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_UNIQUE_ID)));
                kitchen_table.setLatitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LATITUDE)));
                kitchen_table.setLongitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LONGITUDE)));
                kitchen_table.setUploadDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_DATE)));
                kitchen_table.setGeoAddressValue(cursor.getString(cursor.getColumnIndex(KitchenTable.GEO_ADDRESS)));
                kitchen_table.setCostOfChullhaValue(cursor.getString(cursor.getColumnIndex(KitchenTable.COST_OF_CHULLHA)));
                kitchen_table.setStep1_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP1_IMAGE)));
                kitchen_table.setStep2_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP2_IMAGE)));
                kitchen_table.setAddedByIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_BY_ID)));
                kitchen_table.setAddedDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_DATE)));
                kitchen_table.setUserUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.USER_UNIQUE_ID)));
                kitchen_table.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPDATE_DATE)));
                kitchen_table.setConstructionStartDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_START_DATETIME)));
                kitchen_table.setConstructionEndDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_END_DATETIME)));

                cursor.moveToNext();
                return  kitchen_table;
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean deleteKitchenById(Context context, String kitchen_id)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + KitchenTable.KITCHEN_TABLE +
                    " WHERE " + KitchenTable.KITCHEN_ID + " = '" + kitchen_id + "'");
            //delete all rows in titlebackground table

            Toast.makeText(context, "Kitchen Data Deleted Successfully", Toast.LENGTH_SHORT).show();

            db.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteKitchenData(Context context)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + KitchenTable.KITCHEN_TABLE ); //delete all rows in titlebackground table

            Toast.makeText(context,"Kitchen data Deleted Successfully",Toast.LENGTH_SHORT).show();

            db.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean updateUploadStatus(Context context, KitchenTable kitchen_table)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();


            values.put(KitchenTable.UPLOAD_STATUS,"0");



            // upadating Row
            if(db.update(KitchenTable.KITCHEN_TABLE, values, KitchenTable.KITCHEN_UNIQUE_ID+"='"+kitchen_table.getKitchen_idValue()+"'", null)>0)
            {
                Toast.makeText(context,"Kitchen data updated",Toast.LENGTH_LONG).show();
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


    public static KitchenTable getUnUploadKitchenData(Context context)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ KitchenTable.KITCHEN_TABLE+" WHERE  "+ KitchenTable.UPLOAD_STATUS+"='1'",null);
        try
        {

            cursor.moveToFirst();
            KitchenTable kitchenTable = new KitchenTable();
            kitchenTable.setKitchen_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_ID)));
            kitchenTable.setHouse_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.HOUSE_TYPE)));
            kitchenTable.setRoof_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ROOF_TYPE)));
            kitchenTable.setKitchen_heightValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_HEIGHT)));
            kitchenTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_STATUS)));
            kitchenTable.setCustomer_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CUSTOMER_ID)));
            kitchenTable.setPlaceImageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.PLACE_IMAGE)));
            kitchenTable.setKitchenUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_UNIQUE_ID)));
            kitchenTable.setLatitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LATITUDE)));
            kitchenTable.setLongitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LONGITUDE)));
            kitchenTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_DATE)));
            kitchenTable.setGeoAddressValue(cursor.getString(cursor.getColumnIndex(KitchenTable.GEO_ADDRESS)));
            kitchenTable.setCostOfChullhaValue(cursor.getString(cursor.getColumnIndex(KitchenTable.COST_OF_CHULLHA)));
            kitchenTable.setStep1_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP1_IMAGE)));
            kitchenTable.setStep2_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP2_IMAGE)));
            kitchenTable.setAddedByIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_BY_ID)));
            kitchenTable.setAddedDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_DATE)));
            kitchenTable.setUserUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.USER_UNIQUE_ID)));
            kitchenTable.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPDATE_DATE)));
            kitchenTable.setConstructionStartDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_START_DATETIME)));
            kitchenTable.setConstructionEndDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_END_DATETIME)));


            return kitchenTable;


        }
        catch (Exception e)

        {
            return null;
        }
    }

    public static KitchenTable getKitchenDetalBuUniqId(Context context, String uninqId)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
        // Cursor cursor = db.rawQuery("SELECT * FROM " + Message_Table.TABLE_MESSAGE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ KitchenTable.KITCHEN_TABLE+" WHERE  "+ KitchenTable.KITCHEN_UNIQUE_ID+"='"+uninqId+"'",null);
        try
        {

            cursor.moveToFirst();
            KitchenTable kitchenTable = new KitchenTable();
            kitchenTable.setKitchen_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_ID)));
            kitchenTable.setHouse_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.HOUSE_TYPE)));
            kitchenTable.setRoof_typeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ROOF_TYPE)));
            kitchenTable.setKitchen_heightValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_HEIGHT)));
            kitchenTable.setUpload_statusValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_STATUS)));
            kitchenTable.setCustomer_idValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CUSTOMER_ID)));
            kitchenTable.setPlaceImageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.PLACE_IMAGE)));
            kitchenTable.setKitchenUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.KITCHEN_UNIQUE_ID)));
            kitchenTable.setLatitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LATITUDE)));
            kitchenTable.setLongitudeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.LONGITUDE)));
            kitchenTable.setUploadDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPLOAD_DATE)));
            kitchenTable.setGeoAddressValue(cursor.getString(cursor.getColumnIndex(KitchenTable.GEO_ADDRESS)));
            kitchenTable.setCostOfChullhaValue(cursor.getString(cursor.getColumnIndex(KitchenTable.COST_OF_CHULLHA)));
            kitchenTable.setStep1_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP1_IMAGE)));
            kitchenTable.setStep2_imageValue(cursor.getString(cursor.getColumnIndex(KitchenTable.STEP2_IMAGE)));
            kitchenTable.setAddedByIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_BY_ID)));
            kitchenTable.setAddedDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.ADDED_DATE)));
            kitchenTable.setUserUniqueIdValue(cursor.getString(cursor.getColumnIndex(KitchenTable.USER_UNIQUE_ID)));
            kitchenTable.setUpdateDateValue(cursor.getString(cursor.getColumnIndex(KitchenTable.UPDATE_DATE)));
            kitchenTable.setConstructionStartDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_START_DATETIME)));
            kitchenTable.setConstructionEndDateTimeValue(cursor.getString(cursor.getColumnIndex(KitchenTable.CONSTRUCTION_END_DATETIME)));


            return kitchenTable;


        }
        catch (Exception e)

        {
            return null;
        }
    }

}
