package com.hatchers.ruralcaravane.kitchen_suitability.apihelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hatchers.ruralcaravane.app.MyApplication;
import com.hatchers.ruralcaravane.constants.WebServiceUrls;
import com.hatchers.ruralcaravane.construction_team.apihelper.Construction_WebApiHelper;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.image_utils.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ashwin on 18-Jan-18.
 */

public class WebKitchen_ApiHelper
{
    public static boolean addKitchenToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog)
    {
        final KitchenTable kitchenTable = KitchenTableHelper.getUnUploadKitchenData0(activity);
        if(kitchenTable==null)
        {
            KitchenTable kitchenTable1 = KitchenTableHelper.getUnUploadKitchenData2(activity);
            KitchenTable kitchenTable2 = KitchenTableHelper.getUnUploadKitchenData1(activity);
            KitchenTable kitchenTable3 = KitchenTableHelper.getUnUploadKitchenData3(activity);
            if(kitchenTable1!=null)
            {
                updateKitchenToServer(activity,sweetAlertDialog);
            }
            else if(kitchenTable3!=null)
            {
                Construction_WebApiHelper.uploadConstructionDataToServer(activity,sweetAlertDialog);
            }
            else if(kitchenTable2!=null)
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Kitchen Construction Not Completed");
                sweetAlertDialog.setContentText("Please complete kitchen construction.");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }
            else
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Kitchen Not Added");
                sweetAlertDialog.setContentText("Please add kitchen.");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }


            return false;
        }
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServiceUrls.urlAddKitchenToServer, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Result",resultResponse);

                try
                {
                    JSONObject responce = new JSONObject(resultResponse);
                    if(responce.getString("status").equalsIgnoreCase("success"))
                    {
                        //{"status":"success","count":1,"type":"addKitchenSuitability","result":{"id":"7","kitchen_id":"3","roofType":"hhdhd","housetype":"sgdhgd","hieght":"1","longitude":"445445","latitude":"9975294782","geoaddress":"1","place_image":"","addeddate":"0000-00-00 00:00:00","costofculha":"33","customer_id":null,"state":"A","step1image":"","step2image":"","updatedate":"2018-01-18 14:52:16","addedbyid":"9975294782","stime":"0000-00-00 00:00:00","endtime":"0000-00-00 00:00:00","adminactiondate":"0000-00-00 00:00:00","comment":null},"message":"Kitchen Suitability added successfully"}

                        if(responce.getString("message").equalsIgnoreCase("Kitchen Suitability added successfully"))
                        {
                            // JSONArray resultArray = responce.getJSONArray("result");
                            JSONObject result = responce.getJSONObject("result");

                            kitchenTable.setKitchen_idValue(result.getString("id"));
                            kitchenTable.setKitchenState(result.getString("state"));
                            kitchenTable.setUpdateDateValue(result.getString("updatedate"));
                            kitchenTable.setUpload_statusValue("1");

                            if(KitchenTableHelper.updateKitchenData(activity,kitchenTable))
                            {
                                Toast.makeText(activity,"Kitchen Succefully uploaded",Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("Successfully uploaded");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });



                               kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_SUCCESS);
                            }
                            else
                            {
                                Toast.makeText(activity,"update failed",Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("update Failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                                kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_FAILED);
                            }
                            // customerTable.setImagePathValue(result.getString("imagepath"));

                        }
                        else
                        {
                            //group.setCreatestatus(group.BROADCASTERROR);
                            //  EventBus.getDefault().post(group);
                            Toast.makeText(activity,"upload failed",Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("update Failed");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                            kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_FAILED);
                        }
                    }
                    else
                    {
                        Toast.makeText(activity," response failed",Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(" Failed");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                        kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_RESPONSE_FAILED);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(activity,"Json error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Json Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_JSON_ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(activity,"No connection error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Check internet Connection");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(activity,"Server error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Server Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    Toast.makeText(activity,"Network error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Check internet Connection");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(activity,"Parse error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Parse Eroor");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_PARSE_ERROR);
                }
                else
                {
                    Toast.makeText(activity,"Unknown error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Unknown Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_UNKNOWN_ERROR);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //http://www.hatchers.in/caravan/index.php/api/V1/addKitchenSuitability?
                // kitchen_id=3&roofType=hhdhd&housetype=sgdhgd&hieght=1&longitude=445445
                // &latitude=9975294782&geoaddress=1&addeddate=12-12-2018&costofculha=33
                // &state=A&format=json&mobile=9975294782&password=user@123

                params.put("format", "json");
                params.put("kitchen_id",kitchenTable.getKitchenUniqueIdValue());
                params.put("roofType", kitchenTable.getRoof_typeValue());
                params.put("housetype", kitchenTable.getHouse_typeValue());
                params.put("hieght", kitchenTable.getKitchen_heightValue());
                if(kitchenTable.getLatitudeValue()==null)
                {
                    params.put("latitude", "");
                }
                else
                {
                    params.put("latitude", kitchenTable.getLatitudeValue());
                }
                if(kitchenTable.getLongitudeValue()==null)
                {
                    params.put("longitude", "");
                }
                else
                {
                    params.put("longitude", kitchenTable.getLongitudeValue());
                }
                if(kitchenTable.getGeoAddressValue()==null)
                {
                    params.put("geoaddress", "");
                }
                else
                {
                    params.put("geoaddress", kitchenTable.getGeoAddressValue());
                }
                params.put("addeddate", kitchenTable.getAddedDateValue());
                params.put("customerid",kitchenTable.getCustomer_idValue());
                params.put("costofculha","");
                params.put("state", KitchenTable.STATE_N);
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
                    if(kitchenTable.getPlaceImageValue()!=null) {
                    File image = new File(kitchenTable.getPlaceImageValue());

                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        String name = kitchenTable.getPlaceImageValue().substring(kitchenTable.getPlaceImageValue().lastIndexOf("/") + 1);
                        if (name.indexOf(".") > 0)
                            name = name.substring(0, name.lastIndexOf("."));
                        //not added kitchen image api
                           params.put("ufile", new DataPart(name+".jpg",byteArray,"image/jpeg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"Error", Toast.LENGTH_SHORT).show();
                }

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
        return  true;

    }

    public static boolean updateKitchenToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog)
    {
        final KitchenTable kitchenTable = KitchenTableHelper.getUnUploadKitchenData2(activity);
        if(kitchenTable==null)
        {
            KitchenTable kitchenTable1 = KitchenTableHelper.getUnUploadKitchenData3(activity);
            if(kitchenTable1!=null)
            {
                Construction_WebApiHelper.uploadConstructionDataToServer(activity,sweetAlertDialog);
            }
            else
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Kitchen Construction not completed");
                sweetAlertDialog.setContentText("Please complete kitchen construction.");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }
            return false;
        }
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServiceUrls.urlUpdateKitchenToServer, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Result",resultResponse);

                try
                {
                    JSONObject responce = new JSONObject(resultResponse);
                    if(responce.getString("status").equalsIgnoreCase("success"))
                    {

                        if(responce.getString("message").equalsIgnoreCase("Kitchen Suitability updated successfully")) {
                            // JSONArray resultArray = responce.getJSONArray("result");
                            JSONObject result = responce.getJSONObject("result");
                            kitchenTable.setKitchen_idValue(result.getString("id"));
                            kitchenTable.setKitchenState(result.getString("state"));
                            kitchenTable.setUpdateDateValue(result.getString("updatedate"));
                            kitchenTable.setUpload_statusValue("3");

                            if(KitchenTableHelper.updateKitchenData(activity,kitchenTable))
                            {
                                Toast.makeText(activity,"Kitchen Succefully updated",Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("Successfully uploaded");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                                //kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_SUCCESS);
                            }
                            else
                            {
                                Toast.makeText(activity,"update failed",Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Update failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                                //kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_FAILED);
                            }
                            // customerTable.setImagePathValue(result.getString("imagepath"));

                        }
                        else
                        {
                            //group.setCreatestatus(group.BROADCASTERROR);
                            //  EventBus.getDefault().post(group);
                            Toast.makeText(activity,"upload failed",Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Update failed");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                            //  kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_FAILED);
                        }
                    }
                    else
                    {
                        Toast.makeText(activity," response failed",Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Response failed");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                        //kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_RESPONSE_FAILED);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(activity,"Json error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Json Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    //   kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_JSON_ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //   Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    Toast.makeText(activity,"No connection error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Check internet connection");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    // kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_NO_CONNECTION_ERROR);
                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(activity,"Server error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Server Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    // kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_SERVER_ERROR);
                }
                else if (error instanceof NetworkError)
                {
                    Toast.makeText(activity,"Network error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Network Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    // kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_NEWORK_ERROR);
                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(activity,"Parse error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Parse Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    //  kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_PARSE_ERROR);
                }
                else
                {
                    Toast.makeText(activity,"Unknown error",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Update failed");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    //  kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_UNKNOWN_ERROR);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//http://www.hatchers.in/caravan/index.php/api/V1/addKitchenSuitability?format=json&kitchen_id=180120052626126&customerid=180120052514114&stime=2018-01-20%2017:27:32&endtime=2018-01-20%2017:27:43&state=C&mobile=9975294782&password=user@123
                //http://hatchers.in/caravan/index.php/api/V1/updateKitchenSuitability?customerid=adfdfafddsd&kitchen_id=11111
                // &state=C&format=json&mobile=9975294782&password=user@123&stime=2018-01-16%2018:31:58&endtime=2018-01-16%2018:31:58
                params.put("format", "json");
                params.put("kitchen_id",kitchenTable.getKitchenUniqueIdValue());
                params.put("customerid", kitchenTable.getCustomer_idValue());
                params.put("stime", kitchenTable.getConstructionStartDateTimeValue());
                params.put("endtime", kitchenTable.getConstructionEndDateTimeValue());
                params.put("state", KitchenTable.STATE_C);
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
                    if(kitchenTable.getStep1_imageValue()!=null) {
                        File image = new File(kitchenTable.getStep1_imageValue());

                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        String name = kitchenTable.getStep1_imageValue().substring(kitchenTable.getStep1_imageValue().lastIndexOf("/") + 1);
                        if (name.indexOf(".") > 0)
                            name = name.substring(0, name.lastIndexOf("."));
                        //not added kitchen image api
                        params.put("ufile", new DataPart(name+".jpg",byteArray,"image/jpeg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"step 1 Error", Toast.LENGTH_SHORT).show();
                }

                try {
                    byte[] byteArray=null;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(kitchenTable.getStep2_imageValue()!=null) {
                        File image = new File(kitchenTable.getStep2_imageValue());

                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        String name = kitchenTable.getStep2_imageValue().substring(kitchenTable.getStep2_imageValue().lastIndexOf("/") + 1);
                        if (name.indexOf(".") > 0)
                            name = name.substring(0, name.lastIndexOf("."));
                        //not added kitchen image api
                        params.put("ufile2", new DataPart(name+".jpg",byteArray,"image/jpeg"));
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(activity,"step 2 Error", Toast.LENGTH_SHORT).show();
                }

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
        return  true;

    }

}
