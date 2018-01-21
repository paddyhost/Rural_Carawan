package com.hatchers.ruralcaravane.construction_team.apihelper;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hatchers.ruralcaravane.app.MyApplication;
import com.hatchers.ruralcaravane.constants.WebServiceUrls;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Nikam on 20/01/2018.
 */

public class Construction_WebApiHelper {

    public static boolean getConstructionDataToServer(final Activity activity)
    {

        final ConstructionTable constructionTable =ConstructionTableHelper.getUnUploadConstructionData(activity);
        if(constructionTable==null)
        {
            return false;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlUploadConstruction,new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject responce = new JSONObject(response);
                    if (responce.getString("status").equalsIgnoreCase("success"))
                    {
                        if(responce.getString("message").equalsIgnoreCase("Cuntruction Details added successfully")) {


                                    ConstructionTable constructionTable = new ConstructionTable();
                                    JSONObject jsonObject = responce.getJSONObject("result");

                                    constructionTable.setTechnicianIdValue(jsonObject.getString("id"));
                                    constructionTable.setKitchenIdValue(jsonObject.getString("kechain_id"));
                                    constructionTable.setTechnicianIdValue(jsonObject.getString("technitionid"));
                                    constructionTable.setCustomerIdValue(jsonObject.getString("customerid"));
                                    constructionTable.setAddedByIdValue(jsonObject.getString("addedby_id"));
                                    constructionTable.setUploadStatusValue("1");



                                    if (ConstructionTableHelper.updateConstructionTeamData(activity, constructionTable)) {

                                        Toast.makeText(activity,"Construction Data Updated Successfully..",Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(activity,"Construction Data Updation Failed ",Toast.LENGTH_SHORT).show();
                                    }


                        }
                        else
                        {
                            Toast.makeText(activity,"Response Failed ",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(activity,"Response Failed ",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e)
                {
                    Toast.makeText(activity,"JSON Error ",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(activity,"Timeout Error ",Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(activity,"Server Error ",Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof NetworkError)
                {
                    Toast.makeText(activity,"Network Error",Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(activity,"Parse Error",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity,"Unknown Error",Toast.LENGTH_SHORT).show();
                }

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("format","json");
                params.put("kechain_id",constructionTable.getKitchenIdValue());
                if(constructionTable.getTechnicianIdValue()!=null) {
                    params.put("technitionid", constructionTable.getTechnicianIdValue());
                }
                else
                {
                    params.put("technitionid", "");
                }
                params.put("customerid",constructionTable.getCustomerIdValue());
                params.put("addedby_id",constructionTable.getAddedByIdValue());
                params.put("mobile",new PrefManager(activity).getMobile());
                params.put("password",new PrefManager(activity).getPassword());


                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);
        return true;
    }

   }
