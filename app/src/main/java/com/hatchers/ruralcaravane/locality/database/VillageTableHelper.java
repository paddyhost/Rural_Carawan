package com.hatchers.ruralcaravane.locality.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hatchers.ruralcaravane.database.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by Nikam on 14/01/2018.
 */

public class VillageTableHelper {

    public static boolean insertVillageData(Context context, VillageTable villageTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(VillageTable.ID, villageTable.getId());
            values.put(VillageTable.CITY_ID, villageTable.getCityId());
            values.put(VillageTable.LATITUDE, villageTable.getLatitude());
            values.put(VillageTable.LONGITUDE,villageTable.getLongitude());
            values.put(VillageTable.VILLAGENAME,villageTable.getVillageName());


            if (db.insert(VillageTable.VILLAGE_TABLE, null, values) > 0)
            {

                //Toast.makeText(context,"Customer data inserted",Toast.LENGTH_LONG).show();
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

    public static boolean updateVillageData(Context context, VillageTable villageTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(VillageTable.ID,villageTable.getId());
            values.put(VillageTable.CITY_ID,villageTable.getCityId());
            values.put(VillageTable.VILLAGENAME,villageTable.getVillageName());
            values.put(VillageTable.LATITUDE,villageTable.getLatitude());
            values.put(VillageTable.LONGITUDE,villageTable.getLongitude()
            );


            // upadating Row
            if(db.update(VillageTable.VILLAGE_TABLE, values, VillageTable.ID+"="+villageTable.getId(), null)>0)
            {
                Toast.makeText(context,"Village data updated",Toast.LENGTH_LONG).show();
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

    public static ArrayList<VillageTable> getVillageDataList(Context context)
    {
        ArrayList<VillageTable> villageTableArrayList = new ArrayList<VillageTable>();
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ VillageTable.VILLAGE_TABLE,null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                VillageTable villageTable = new VillageTable();

                villageTable.setId(cursor.getString(cursor.getColumnIndex(VillageTable.ID)));
                villageTable.setCityId(cursor.getString(cursor.getColumnIndex(VillageTable.CITY_ID)));
                villageTable.setVillageName(cursor.getString(cursor.getColumnIndex(VillageTable.VILLAGENAME)));
                villageTable.setLatitude(cursor.getString(cursor.getColumnIndex(VillageTable.LATITUDE)));
                villageTable.setLongitude(cursor.getString(cursor.getColumnIndex(VillageTable.LONGITUDE)));

                villageTableArrayList.add(villageTable);
                cursor.moveToNext();
            }
            return villageTableArrayList;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static boolean deleteVillageDataById(Context context, String village_id)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + VillageTable.VILLAGE_TABLE +
                    " WHERE " + VillageTable.ID + " = '" + village_id + "'");
            //delete all rows in titlebackground table

            Toast.makeText(context, "Village Data Deleted Successfully", Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteCityData(Context context)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + VillageTable.VILLAGE_TABLE ); //delete all rows in titlebackground table


            Toast.makeText(context,"Village Data Deleted Successfully",Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
