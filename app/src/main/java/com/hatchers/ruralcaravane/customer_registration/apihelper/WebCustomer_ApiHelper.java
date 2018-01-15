package com.hatchers.ruralcaravane.customer_registration.apihelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.model.City;
import com.hatchers.ruralcaravane.customer_registration.model.CityVillageList;
import com.hatchers.ruralcaravane.customer_registration.model.Village;
import com.hatchers.ruralcaravane.app.MyApplication;
import com.hatchers.ruralcaravane.constants.WebServiceUrls;
import com.hatchers.ruralcaravane.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public class WebCustomer_ApiHelper
{
    public static void getCityList(final Activity activity, final CityVillageList cityList)
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
                            ArrayList<City> cities =new ArrayList<City>();
                            for (int i =0;i<result.length();i++)
                            {
                                City city = new City();
                                JSONObject jsonObject = result.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String cityname = jsonObject.getString("name");
                                String longitude = jsonObject.getString("lang");
                                String lattitude = jsonObject.getString("lat");

                                city.setCityname(cityname);
                                city.setId(id);
                                city.setLatitude(lattitude);
                                city.setLongitude(longitude);

                                cities.add(city);

                                if (i + 1 == result.length())
                                {
                                    // end of loop
                                    cityList.setCityListArrayList(cities);
                                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_SUCCESS);
                                }

                            }

                        }
                        else
                        {
                            cityList.fireOnCityEvent(CityVillageList.CITY_ADD_FAILED);
                        }

                    }
                    else
                    {
                        cityList.fireOnCityEvent(CityVillageList.CITY_ADD_RESPONSE_FAILED);
                    }
                } catch (JSONException e) {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_JSON_ERROR);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_SERVER_ERROR);
                    //TODO
                }
                else if (error instanceof NetworkError)
                {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_PARSE_ERROR);
                }
                else
                {
                    cityList.fireOnCityEvent(CityVillageList.CITY_ADD_UNKNOWN_ERROR);
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

    public static void getVillageList(final Activity activity, final CityVillageList cityVillageList, final String cityId)
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
                            ArrayList<Village> villages =new ArrayList<Village>();
                            for (int i =0;i<result.length();i++)
                            {
                                Village village = new Village();
                                JSONObject jsonObject = result.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String villagename = jsonObject.getString("name");
                                String longitude = jsonObject.getString("lang");
                                String lattitude = jsonObject.getString("lat");

                                village.setVillageName(villagename);
                                village.setCityId(jsonObject.getString("city_id"));
                                village.setId(id);
                                village.setLatitude(lattitude);
                                village.setLongitude(longitude);

                                villages.add(village);

                                if (i + 1 == result.length())
                                {
                                    // end of loop
                                    cityVillageList.setVillageArrayList(villages);
                                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_SUCCESS);
                                }

                            }

                        }
                        else
                        {
                            cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_FAILED);
                        }

                    }
                    else
                    {
                        cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_RESPONSE_FAILED);
                    }
                } catch (JSONException e) {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_JSON_ERROR);
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_PARSE_ERROR);
                }
                else
                {
                    cityVillageList.fireOnVillageEvent(CityVillageList.VILLAGE_ADD_UNKNOWN_ERROR);
                }

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("format","json");
                params.put("city_id",cityId);
                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public static boolean addNewCustomer (final Activity activity, final CustomerTable customerTable)
    {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServiceUrls.urlAddCustomer, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Result",resultResponse);

                try
                {
                    JSONObject responce = new JSONObject(resultResponse);
                    if(responce.getString("status").equalsIgnoreCase("success"))
                    {
                        //{"status":"error","count":0,"type":"addNewCustomerInquery","result":[],"message":"autontication failed"}

                        if(responce.getString("message").equalsIgnoreCase("Customer added successfully")) {
                           // JSONArray resultArray = responce.getJSONArray("result");
                            //JSONObject result = resultArray.getJSONObject((0));
                            Toast.makeText(activity,"Succefully uploaded",Toast.LENGTH_SHORT).show();



                        }
                        else
                        {
                            //group.setCreatestatus(group.BROADCASTERROR);
                          //  EventBus.getDefault().post(group);
                            Toast.makeText(activity,"failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(activity,"failed",Toast.LENGTH_SHORT).show();}
                }
                catch (Exception e)
                {
                    Toast.makeText(activity,"Json error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //http://www.hatchers.in/caravan/index.php/api/V1/addNewCustomerInquery?
                // customerid=vbcvv&
                // adharid=1576&name=hhdhd&address=sgdhgd&age=24&gender=F&mobile=9975294782&villageid=1&
                // citi_id=2&added_date=12-12-2018&addedby_id=1&format=json&mobile=9975294782&password=user@123&ufile=image
                params.put("format", "json");
                params.put("customerid",customerTable.getUniqueIdValue());
                params.put("adharid", customerTable.getAadharIdValue());
                params.put("name", customerTable.getCustomerNameValue());
                params.put("address", customerTable.getCustomerAddressValue());
                params.put("age", customerTable.getCustomerAgeValue());
                params.put("gender", customerTable.getCustomerGenderValue());
                params.put("mobile", customerTable.getCustomerMobilenoValue());
                params.put("villageid", customerTable.getVillageIdValue());
                params.put("citi_id", customerTable.getCityId());
                params.put("added_date", customerTable.getAddedDateValue());
                params.put("addedby_id", customerTable.getAddedByIdValue());

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView


                try {

                    byte[] byteArray=null;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(customerTable.getImagePathValue()));
                   Bitmap mBitmap = customerTable.getProfileBitmap();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    String name=  customerTable.getImagePathValue().substring(customerTable.getImagePathValue().lastIndexOf("/")+1);
                    if (name.indexOf(".") > 0)
                        name = name.substring(0, name.lastIndexOf("."));
                    params.put("ufile", new DataPart(name+".jpg",byteArray,"image/jpeg"));


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
                }



                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
        return  true;


    }


}

