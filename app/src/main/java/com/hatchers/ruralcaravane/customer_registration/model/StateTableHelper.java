package com.hatchers.ruralcaravane.customer_registration.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hatchers.ruralcaravane.database.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by Nikam on 15/01/2018.
 */

public class StateTableHelper {

    public static boolean insertStateData(Context context, StateTable stateTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(StateTable.STATE_ID, stateTable.getStateId());
            values.put(StateTable.CITY_ID, stateTable.getCityId());
            values.put(StateTable.LATITUDE, stateTable.getLatitude());
            values.put(StateTable.LONGITUDE,stateTable.getLongitude());
            values.put(StateTable.STATENAME,stateTable.getStateName());


            if (db.insert(StateTable.STATE_TABLE, null, values) > 0)
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


    public static boolean updateStateData(Context context, StateTable stateTable)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(StateTable.STATE_ID,stateTable.getStateId());
            values.put(StateTable.CITY_ID,stateTable.getCityId());
            values.put(StateTable.STATENAME,stateTable.getStateName());
            values.put(StateTable.LATITUDE,stateTable.getLatitude());
            values.put(StateTable.LONGITUDE,stateTable.getLongitude()
            );


            // upadating Row
            if(db.update(StateTable.STATE_TABLE, values, StateTable.STATE_ID+"="+stateTable.getStateId(), null)>0)
            {
                Toast.makeText(context,"State data updated",Toast.LENGTH_LONG).show();
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


    public static ArrayList<StateTable> getStateDataList(Context context)
    {
        ArrayList<StateTable> stateTableArrayList = new ArrayList<StateTable>();
        SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ StateTable.STATE_TABLE,null);
        try
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                StateTable stateTable = new StateTable();

                stateTable.setStateId(cursor.getString(cursor.getColumnIndex(StateTable.STATE_ID)));
                stateTable.setCityId(cursor.getString(cursor.getColumnIndex(StateTable.CITY_ID)));
                stateTable.setStateName(cursor.getString(cursor.getColumnIndex(StateTable.STATENAME)));
                stateTable.setLatitude(cursor.getString(cursor.getColumnIndex(StateTable.LATITUDE)));
                stateTable.setLongitude(cursor.getString(cursor.getColumnIndex(StateTable.LONGITUDE)));

                stateTableArrayList.add(stateTable);
                cursor.moveToNext();
            }
            return stateTableArrayList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean deleteStateDataById(Context context, String state_id)
    {
        try {
            SQLiteDatabase db = new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + StateTable.STATE_TABLE +
                    " WHERE " + StateTable.STATE_ID + " = '" + state_id + "'");
            //delete all rows in titlebackground table

            Toast.makeText(context, "State Data Deleted Successfully", Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteStateData(Context context)
    {
        try {
            SQLiteDatabase db =  new DatabaseHandler(context).getWritableDatabase();

            db.execSQL("DELETE FROM " + StateTable.STATE_TABLE ); //delete all rows in titlebackground table


            Toast.makeText(context,"State Data Deleted Successfully",Toast.LENGTH_SHORT).show();


            db.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
