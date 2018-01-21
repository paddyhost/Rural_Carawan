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
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.app.MyApplication;
import com.hatchers.ruralcaravane.constants.WebServiceUrls;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.apihelper.WebKitchen_ApiHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
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
    public static boolean addNewCustomerToServer(final Activity activity)
    {
        final CustomerTable customerTable =CustomerTableHelper.getUnUploadCustomerData(activity);
        if(customerTable==null)
        {
           // WebKitchen_ApiHelper.addKitchenServer(activity);
            return false;
        }
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
                            JSONObject result = responce.getJSONObject("result");

                            customerTable.setCustomerIdValue(result.getString("id"));
                            customerTable.setUploadDateValue(result.getString("uploaddate"));
                            customerTable.setUpdateDateValue(result.getString("updateDate"));
                            customerTable.setUpload_statusValue("1");
                            if(CustomerTableHelper.updateCustomerData(activity,customerTable))
                            {
                                Toast.makeText(activity,"Succefully uploaded",Toast.LENGTH_SHORT).show();
                                customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_SUCCESS);
                                addNewCustomerToServer(activity);
                            }
                            else
                            {
                                customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_FAILED);
                            }
                           // customerTable.setImagePathValue(result.getString("imagepath"));





                        }
                        else
                        {
                            //group.setCreatestatus(group.BROADCASTERROR);
                          //  EventBus.getDefault().post(group);
                            Toast.makeText(activity,"upload failed",Toast.LENGTH_SHORT).show();
                            customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_FAILED);
                        }
                    }
                    else
                    {
                        Toast.makeText(activity," response failed",Toast.LENGTH_SHORT).show();
                        customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_RESPONSE_FAILED);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(activity,"Json error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_JSON_ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(activity,"No connection error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(activity,"Server error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    Toast.makeText(activity,"Network error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(activity,"Parse error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_PARSE_ERROR);
                }
                else
                {
                    Toast.makeText(activity,"Unknown error",Toast.LENGTH_SHORT).show();
                    customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_UNKNOWN_ERROR);
                }

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
                params.put("cmobile", customerTable.getCustomerMobilenoValue());
                params.put("villageid", customerTable.getVillageIdValue());
                params.put("citi_id", customerTable.getCityId());
                params.put("added_date", customerTable.getAddedDateValue());
                params.put("addedby_id", new PrefManager(activity).getUserId());
                params.put("mobile",new PrefManager(activity).getMobile());
                params.put("password",new PrefManager(activity).getPassword());
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
                   File image = new File(customerTable.getImagePathValue());
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                  // Bitmap mBitmap = customerTable.getProfileBitmap();
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

