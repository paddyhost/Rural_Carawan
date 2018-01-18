package com.hatchers.ruralcaravane.locality.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hatchers.ruralcaravane.database.DatabaseHandler;

import java.util.ArrayList;


public class CityTableHelper {

    public static boolean insertCityData(Context context, CityTable cityTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(CityTable.CITY_ID, cityTable.getId());
            values.put(CityTable.CITYNAME, cityTable.getCityname());
            values.put(CityTable.LATITUDE, cityTable.getLatitude());
            values.put(CityTable.LONGITUDE,cityTable.getLongitude());
            values.put(CityTable.STATE_ID,cityTable.getStateId());


            if (db.insert(CityTable.CITY_TABLE, null, values) > 0)
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

    public static boolean updateCityData(Context context, CityTable cityTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(CityTable.CITY_ID,cityTable.getId());
            values.put(CityTable.CITYNAME,cityTable.getCityname());
            values.put(CityTable.LATITUDE,cityTable.getLatitude());
            values.put(CityTable.LONGITUDE,cityTable.getLongitude());
            values.put(CityTable.STATE_ID,cityTable.getStateId());


            // upadating Row
            if(db.update(CityTable.CITY_TABLE, values, CityTable.CITY_ID+"="+cityTable.getId(), null)>0)
            {
                Toast.makeText(context,"City data updated",Toast.LENGTH_LONG).show();
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

    public static ArrayList<CityTable> getCityDataList(Context context,String stateId)
    {
        ArrayList<CityTable> cityTableArrayList = new ArrayList<CityTable>();
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();
         Cursor cursor = db.rawQuery("SELECT * FROM " + CityTable.CITY_TABLE, null);
       // Cursor cursor = db.rawQuery("SELECT * FROM "+ CityTable.CITY_TABLE + " WHERE "+CityTable.STATE_ID+"='"+stateId+"'",null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                CityTable cityTable = new CityTable();

                cityTable.setId(cursor.getString(cursor.getColumnIndex(CityTable.CITY_ID)));
                cityTable.setCityname(cursor.getString(cursor.getColumnIndex(CityTable.CITYNAME)));
                cityTable.setLatitude(cursor.getString(cursor.getColumnIndex(CityTable.LATITUDE)));
                cityTable.setLongitude(cursor.getString(cursor.getColumnIndex(CityTable.LONGITUDE)));
                cityTable.setStateId(cursor.getString(cursor.getColumnIndex(CityTable.STATE_ID)));

                cityTableArrayList.add(cityTable);
                cursor.moveToNext();
            }
            return cityTableArrayList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean deleteCityDataById(Context context, String city_id)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + CityTable.CITY_TABLE +
                    " WHERE " + CityTable.CITY_ID + " = '" + city_id + "'");
            //delete all rows in titlebackground table

            Toast.makeText(context, "City Data Deleted Successfully", Toast.LENGTH_SHORT).show();


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

            db.execSQL("DELETE FROM " + CityTable.CITY_TABLE ); //delete all rows in titlebackground table


            Toast.makeText(context,"City Data Deleted Successfully",Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
