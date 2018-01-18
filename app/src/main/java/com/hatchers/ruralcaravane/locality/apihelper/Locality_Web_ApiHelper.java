package com.hatchers.ruralcaravane.locality.apihelper;

import android.app.Activity;

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
import com.hatchers.ruralcaravane.locality.database.CityTable;
import com.hatchers.ruralcaravane.locality.database.CityTableHelper;
import com.hatchers.ruralcaravane.locality.database.StateTable;
import com.hatchers.ruralcaravane.locality.database.StateTableHelper;
import com.hatchers.ruralcaravane.locality.database.VillageTable;
import com.hatchers.ruralcaravane.locality.database.VillageTableHelper;
import com.hatchers.ruralcaravane.locality.model.Locality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;



public class Locality_Web_ApiHelper
{

    public static void getStateList(final Activity activity, final Locality locality)
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlGetStateList,new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject responce = new JSONObject(response);
                    if (responce.getString("status").equalsIgnoreCase("Success"))
                    {
                        if(responce.getString("message").equalsIgnoreCase("Result foud")) {
                            JSONArray result = responce.getJSONArray("result");
                            ArrayList<StateTable> stateTableArrayList =new ArrayList<StateTable>();
                            if(result.length()>0) {
                                for (int i = 0; i < result.length(); i++) {
                                    StateTable stateTable = new StateTable();
                                    JSONObject jsonObject = result.getJSONObject(i);

                                    stateTable.setStateName(jsonObject.getString("name"));
                                    stateTable.setStateId(jsonObject.getString("id"));
                                    stateTable.setLatitude(jsonObject.getString("lat"));
                                    stateTable.setLongitude(jsonObject.getString("lang"));

                                    stateTableArrayList.add(stateTable);

                                    if (StateTableHelper.insertStateData(activity, stateTable)) {
                                        if (i + 1 == result.length()) {
                                            // end of loop
                                            locality.setStateArrayList(stateTableArrayList);
                                            locality.fireOnStateEvent(Locality.STATE_ADD_SUCCESS);
                                        }
                                    } else {
                                        locality.fireOnStateEvent(Locality.STATE_ADD_FAILED);
                                    }
                                }
                            }
                            else
                            {
                                locality.fireOnStateEvent(Locality.STATE_ADD_FAILED);
                            }

                        }
                        else
                        {
                            locality.fireOnStateEvent(Locality.STATE_ADD_FAILED);
                        }

                    }
                    else
                    {
                        locality.fireOnStateEvent(Locality.STATE_ADD_RESPONSE_FAILED);
                    }
                } catch (JSONException e)
                {
                    locality.fireOnStateEvent(Locality.STATE_ADD_JSON_ERROR);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    locality.fireOnStateEvent(Locality.STATE_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    locality.fireOnStateEvent(Locality.STATE_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    locality.fireOnStateEvent(Locality.STATE_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    locality.fireOnStateEvent(Locality.STATE_ADD_PARSE_ERROR);
                }
                else
                {
                    locality.fireOnStateEvent(Locality.STATE_ADD_UNKNOWN_ERROR);
                }

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("format","json");
                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public static void getCityList(final Activity activity, final Locality cityList)
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlGetCityList,new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject responce = new JSONObject(response);
                    if (responce.getString("status").equalsIgnoreCase("Success"))
                    {
                        if(responce.getString("message").equalsIgnoreCase("Result foud")) {
                            JSONArray result = responce.getJSONArray("result");
                            ArrayList<CityTable> cities =new ArrayList<CityTable>();
                            if(result.length()>0) {
                                for (int i = 0; i < result.length(); i++) {
                                    CityTable city = new CityTable();
                                    JSONObject jsonObject = result.getJSONObject(i);

                                    city.setCityname(jsonObject.getString("name"));
                                    city.setId(jsonObject.getString("id"));
                                    city.setLatitude(jsonObject.getString("lat"));
                                    city.setLongitude(jsonObject.getString("lang"));

                                    cities.add(city);
                                    if (CityTableHelper.insertCityData(activity, city)) {
                                        if (i + 1 == result.length()) {
                                            // end of loop
                                            cityList.setCityListArrayList(cities);
                                            cityList.fireOnCityEvent(Locality.CITY_ADD_SUCCESS);
                                        }
                                    } else {
                                        cityList.fireOnCityEvent(Locality.CITY_ADD_FAILED);
                                    }
                                }
                            }
                            else
                            {
                                cityList.fireOnCityEvent(Locality.CITY_ADD_FAILED);
                            }
                        }
                        else
                        {
                            cityList.fireOnCityEvent(Locality.CITY_ADD_FAILED);
                        }

                    }
                    else
                    {
                        cityList.fireOnCityEvent(Locality.CITY_ADD_RESPONSE_FAILED);
                    }
                } catch (JSONException e) {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_JSON_ERROR);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_SERVER_ERROR);
                    //TODO
                }
                else if (error instanceof NetworkError)
                {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_PARSE_ERROR);
                }
                else
                {
                    cityList.fireOnCityEvent(Locality.CITY_ADD_UNKNOWN_ERROR);
                }

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("format","json");
                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public static void getVillageList(final Activity activity, final Locality locality)
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlGetVillageList,new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject responce = new JSONObject(response);
                    if (responce.getString("status").equalsIgnoreCase("Success"))
                    {
                        if(responce.getString("message").equalsIgnoreCase("Result foud")) {
                            JSONArray result = responce.getJSONArray("result");
                            ArrayList<VillageTable> villages =new ArrayList<VillageTable>();
                            if(result.length()>0) {
                                for (int i = 0; i < result.length(); i++) {
                                    VillageTable village = new VillageTable();
                                    JSONObject jsonObject = result.getJSONObject(i);

                                    village.setVillageName(jsonObject.getString("name"));
                                    village.setCityId(jsonObject.getString("city_id"));
                                    village.setId(jsonObject.getString("id"));
                                    village.setLatitude(jsonObject.getString("lat"));
                                    village.setLongitude(jsonObject.getString("lang"));

                                    villages.add(village);

                                    if (VillageTableHelper.insertVillageData(activity, village)) {
                                        if (i + 1 == result.length()) {
                                            // end of loop
                                            locality.setVillageArrayList(villages);
                                            locality.fireOnVillageEvent(Locality.VILLAGE_ADD_SUCCESS);
                                        }
                                    } else {
                                        locality.fireOnVillageEvent(Locality.VILLAGE_ADD_FAILED);
                                    }

                                }
                            }
                            else
                            {
                                locality.fireOnVillageEvent(Locality.VILLAGE_ADD_FAILED);
                            }
                        }
                        else
                        {
                            locality.fireOnVillageEvent(Locality.VILLAGE_ADD_FAILED);
                        }

                    }
                    else
                    {
                        locality.fireOnVillageEvent(Locality.VILLAGE_ADD_RESPONSE_FAILED);
                    }
                } catch (JSONException e) {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_JSON_ERROR);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_PARSE_ERROR);
                }
                else
                {
                    locality.fireOnVillageEvent(Locality.VILLAGE_ADD_UNKNOWN_ERROR);
                }

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("format","json");
                params.put("city_id","1");
                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);

    }

}
