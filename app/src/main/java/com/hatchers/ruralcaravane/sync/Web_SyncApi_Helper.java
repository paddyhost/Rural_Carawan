package com.hatchers.ruralcaravane.sync;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Path;
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
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.CompleteConstructionActivity;
import com.hatchers.ruralcaravane.app.MyApplication;
import com.hatchers.ruralcaravane.constants.WebServiceUrls;
import com.hatchers.ruralcaravane.construction_team.ConstructionFragment;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentDetailsHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.image_utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.STEP2_PREFIX;
import static com.hatchers.ruralcaravane.payment_details.apihelper.Payment_WebApiHelper.uploadPaymentDataToServer;

/**
 * Created by Ashwin on 04-Feb-18.
 */

public class Web_SyncApi_Helper
{
    //upload customer and complete chulha construction to server
    public static void UploadCustomerAndCompletedKitchenDataToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
    {
        final KitchenTable kitchenDetailsData = KitchenTableHelper.getKitchenDetailsData(activity, customerTable.getUniqueIdValue());

                if(customerTable.getUpload_statusValue().equalsIgnoreCase("0"))
                {
                    addCustomerToServer(activity, sweetAlertDialog, customerTable);
                }
                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("1"))
                {
                        //kitchen added local
                    if (customerTable.getKitchen_added().equalsIgnoreCase(CustomerTable.LOCAL))
                    {
                        if (kitchenDetailsData != null) {

                            if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.KITCHEN_ADDED_LOCAL)) {
                                addKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                            } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.KITCHEN_UPLOADED_SERVER)) {
                                uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                            } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.TEAM_ADDED_LOCAL)) {
                                uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                            } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.TEAM_ADDED_SERVER)) {
                                updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                            } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.PHOTOS_ADDED_LOCAL)) {
                                updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                            }
                            else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.PHOTOS_ADDED_SERVER)) {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("Photo already Uploaded");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                            } else {
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
                }
                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("2"))
                {
                    if (customerTable.getCustomer_added().equalsIgnoreCase(CustomerTable.LOCAL))
                    {
                        addCustomerToServer(activity,sweetAlertDialog,customerTable);
                    }
                    else
                    {
                        //kitchen added local
                        if (customerTable.getKitchen_added().equalsIgnoreCase(CustomerTable.LOCAL))
                        {
                            if (kitchenDetailsData != null) {

                                if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.KITCHEN_ADDED_LOCAL)) {
                                    addKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                                } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.KITCHEN_UPLOADED_SERVER)) {
                                    uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                                } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.TEAM_ADDED_LOCAL)) {
                                    uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                                } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.TEAM_ADDED_SERVER)) {
                                    updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                                } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.PHOTOS_ADDED_LOCAL)) {
                                    updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData, customerTable);
                                } else if (kitchenDetailsData.getUpload_statusValue().equalsIgnoreCase(KitchenTable.PHOTOS_ADDED_SERVER)) {
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.setTitleText("Photo already Uploaded");
                                    sweetAlertDialog.setConfirmText("Ok");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                } else {
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
                    }
                }
                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("3"))
                {
                    //team added local
                    uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                }
                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("4"))
                {
                    if (customerTable.getCustomer_added().equalsIgnoreCase(CustomerTable.LOCAL))
                    {
                        addCustomerToServer(activity,sweetAlertDialog,customerTable);
                    }
                    else
                    {
                        //team added local
                        uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);
                    }
                }
                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("5"))
                {
                    //photos added local
                    updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData,customerTable);
                }

                else if(customerTable.getUpload_statusValue().equalsIgnoreCase("6"))
                {
                    if (customerTable.getCustomer_added().equalsIgnoreCase(CustomerTable.LOCAL))
                    {
                        addCustomerToServer(activity,sweetAlertDialog,customerTable);
                    }
                    else
                    {
                        //photos added local
                        updateKitchenToServer(activity, sweetAlertDialog, kitchenDetailsData,customerTable);
                    }
                }
    }

    private static void addCustomerToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
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
                            JSONObject result = responce.getJSONObject("result");

                            customerTable.setCustomerIdValue(result.getString("id"));
                            customerTable.setUploadDateValue(result.getString("uploaddate"));
                            customerTable.setUpdateDateValue(result.getString("updateDate"));
                            customerTable.setUpload_statusValue("1");
                            customerTable.setCustomer_added(CustomerTable.SERVER);
                            if(CustomerTableHelper.updateCustomerData(activity,customerTable))
                            {
                                Toast.makeText(activity,"Succefully uploaded customer",Toast.LENGTH_SHORT).show();
                                //customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_SUCCESS);
                                //  addNewCustomerToServer(activity,sweetAlertDialog);
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("Successfully Uploaded Customer");
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
                                Toast.makeText(activity,"upload failed",Toast.LENGTH_SHORT).show();
                                //  customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_FAILED);
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("update Failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                            }
                            // customerTable.setImagePathValue(result.getString("imagepath"));
                        }
                        else
                        {
                            //group.setCreatestatus(group.BROADCASTERROR);
                            //  EventBus.getDefault().post(group);
                            Toast.makeText(activity,"upload failed",Toast.LENGTH_SHORT).show();
                            //customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_FAILED);
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("upload Failed");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }
                    }
                    else
                    {
                        Toast.makeText(activity," response failed",Toast.LENGTH_SHORT).show();
                        //  customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_RESPONSE_FAILED);
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("upload Failed");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(activity,"Json error",Toast.LENGTH_SHORT).show();
                    //  customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_JSON_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Json error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(activity,"No connection error",Toast.LENGTH_SHORT).show();
                    // customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_NO_CONNECTION_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Check interner connection");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(activity,"Server error",Toast.LENGTH_SHORT).show();
                    //  customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_SERVER_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Server error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                }
                else if (error instanceof NetworkError)
                {
                    Toast.makeText(activity,"Network error",Toast.LENGTH_SHORT).show();
                    // customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_NEWORK_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Network Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(activity,"Parse error",Toast.LENGTH_SHORT).show();
                    //customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_PARSE_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Parse Error");
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
                    Toast.makeText(activity,"Unknown error",Toast.LENGTH_SHORT).show();
                    // customerTable.fireOnCustomerEvent(CustomerTable.CUSTOMER_ADD_UNKNOWN_ERROR);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Unknown Error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

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

                    byte[] byteArray = null;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if (customerTable.getImagePathValue() != null) {
                        File image = new File(customerTable.getImagePathValue());

                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        String name = customerTable.getImagePathValue().substring(customerTable.getImagePathValue().lastIndexOf("/") + 1);
                        if (name.indexOf(".") > 0)
                            name = name.substring(0, name.lastIndexOf("."));
                        params.put("ufile", new DataPart(name + ".jpg", byteArray, "image/jpeg"));
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }



                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
    }

    private static void addKitchenToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final KitchenTable kitchenTable, final CustomerTable customerTable)
    {

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
                            kitchenTable.setUpload_statusValue(KitchenTable.KITCHEN_UPLOADED_SERVER);

                            if(KitchenTableHelper.updateKitchenData(activity,kitchenTable))
                            {
                                customerTable.setUpload_statusValue("3");
                                CustomerTableHelper.updateCustomerStatus(activity,customerTable);
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
                    //http://www.hatchers.in/caravan/index.php/api/V1/addKitchenSuitability?format=json&housetype=Brick%20Stacked&addeddate=2018-01-22%2022:41:49&roofType=Asbestos%20Sheet&longitude=75.3615486&hieght=57&latitude=19.885418&geoaddress=N5-65-12,Aurangabad,%20Maharashtra,431003&kitchen_id=KIT_180122104110110&customerid=CU_180122103956156&costofculha=&state=N&mobile=9975294782&password=user@123
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

    }

    private static void uploadConstructionDataToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
    {
        final ConstructionTable constructionTable = ConstructionTableHelper.getUnUploadConstructionData(activity,customerTable.getUniqueIdValue());

        if (!(ConstructionTableHelper.getConstructionTeamList(activity,customerTable).size()>0))
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Team not added");
            sweetAlertDialog.setContentText("Please add team");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        if(constructionTable==null)
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("Team added");
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
        StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlUploadConstruction, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responce = new JSONObject(response);
                        if (responce.getString("status").equalsIgnoreCase("success")) {
                            if (responce.getString("message").equalsIgnoreCase("Cuntruction Details added successfully")) {


                                JSONObject jsonObject = responce.getJSONObject("result");

                                constructionTable.setTechnicianIdValue(jsonObject.getString("id"));
                                constructionTable.setKitchenIdValue(jsonObject.getString("kechain_id"));
                                constructionTable.setTechnicianIdValue(jsonObject.getString("technitionid"));
                                constructionTable.setCustomerIdValue(jsonObject.getString("customerid"));
                                constructionTable.setAddedByIdValue(jsonObject.getString("addedby_id"));
                                constructionTable.setUploadStatusValue(ConstructionTable.TEAM_ADDED_SERVER);

                                if (ConstructionTableHelper.updateConstructionTeamData(activity, constructionTable)) {
                                    customerTable.setUpload_statusValue("5");
                                    CustomerTableHelper.updateCustomerStatus(activity,customerTable);

                                    Toast.makeText(activity, "Construction Data Updated Successfully..", Toast.LENGTH_SHORT).show();
                                    uploadConstructionDataToServer(activity, sweetAlertDialog, customerTable);

                                } else {

                                    Toast.makeText(activity, "Construction Data Updation Failed ", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.setTitleText("Update failed");
                                    sweetAlertDialog.setConfirmText("Ok");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });

                                }


                            } else {
                                Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Upload failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                            }

                        } else {
                            Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Upload failed");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        Toast.makeText(activity, "JSON Error ", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("JSON Error");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(activity, "Timeout Error ", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Check internet connection");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    } else if (error instanceof ServerError) {
                        Toast.makeText(activity, "Server Error ", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Server Error");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    } else if (error instanceof NetworkError) {
                        Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Network Error");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    } else if (error instanceof ParseError) {
                        Toast.makeText(activity, "Parse Error", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Parse Error");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    } else {
                        Toast.makeText(activity, "Unknown Error", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Unkonwn error");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                    }

                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();

                    params.put("format", "json");
                    params.put("kechain_id", constructionTable.getKitchenIdValue());
                    if (constructionTable.getTechnicianUniqueIdValue() != null) {
                        params.put("technitionid", constructionTable.getTechnicianUniqueIdValue());
                    } else {
                        params.put("technitionid", "");
                    }
                    params.put("customerid", constructionTable.getCustomerIdValue());
                    params.put("addedby_id", constructionTable.getAddedByIdValue());
                    params.put("mobile", new PrefManager(activity).getMobile());
                    params.put("password", new PrefManager(activity).getPassword());
                    params.put("starttime", "");
                    params.put("endtime", "");

                    //returning parameters
                    return params;
                }

            };

            MyApplication.getInstance().addToRequestQueue(strReq);
        }

    }

    private static void updateKitchenToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final KitchenTable kitchenTable, final CustomerTable customerTable)
    {
        File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
        if (image != null) {
            if (!image.exists()) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Add Chulha photos");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            } else {
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebServiceUrls.urlUpdateKitchenToServer, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        Log.d("Result", resultResponse);

                        try {
                            JSONObject responce = new JSONObject(resultResponse);
                            if (responce.getString("status").equalsIgnoreCase("success")) {

                                if (responce.getString("message").equalsIgnoreCase("Kitchen Suitability updated successfully")) {
                                    // JSONArray resultArray = responce.getJSONArray("result");
                                    JSONObject result = responce.getJSONObject("result");
                                    kitchenTable.setKitchen_idValue(result.getString("id"));
                                    kitchenTable.setKitchenState(result.getString("state"));
                                    kitchenTable.setUpdateDateValue(result.getString("updatedate"));
                                    kitchenTable.setUpload_statusValue(KitchenTable.PHOTOS_ADDED_SERVER);

                                    if (KitchenTableHelper.updateKitchenData(activity, kitchenTable)) {
                                        customerTable.setUpload_statusValue("7");
                                        CustomerTableHelper.updateCustomerStatus(activity, customerTable);
                                        Toast.makeText(activity, "Kitchen Succefully Updated", Toast.LENGTH_SHORT).show();
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        sweetAlertDialog.setTitleText("Kitchen Successfully Updated");
                                        sweetAlertDialog.setConfirmText("Ok");
                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });

                                        //kitchenTable.fireOnKitchenEvent(KitchenTable.KITCHEN_ADD_SUCCESS);
                                    } else {
                                        Toast.makeText(activity, "update failed", Toast.LENGTH_SHORT).show();
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

                                } else {
                                    //group.setCreatestatus(group.BROADCASTERROR);
                                    //  EventBus.getDefault().post(group);
                                    Toast.makeText(activity, "upload failed", Toast.LENGTH_SHORT).show();
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
                            } else {
                                Toast.makeText(activity, " response failed", Toast.LENGTH_SHORT).show();
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
                        } catch (Exception e) {
                            Toast.makeText(activity, "Json error", Toast.LENGTH_SHORT).show();
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
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(activity,"Volley error",Toast.LENGTH_SHORT).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity, "No connection error", Toast.LENGTH_SHORT).show();
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
                        } else if (error instanceof ServerError) {
                            Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
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
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
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
                        } else if (error instanceof ParseError) {
                            Toast.makeText(activity, "Parse error", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(activity, "Unknown error", Toast.LENGTH_SHORT).show();
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
                        params.put("kitchen_id", kitchenTable.getKitchenUniqueIdValue());
                        params.put("customerid", kitchenTable.getCustomer_idValue());
                        params.put("stime", kitchenTable.getConstructionStartDateTimeValue());
                        params.put("endtime", kitchenTable.getConstructionEndDateTimeValue());
                        params.put("state", KitchenTable.STATE_C);
                        params.put("mobile", new PrefManager(activity).getMobile());
                        params.put("password", new PrefManager(activity).getPassword());
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView
                        try {

                            byte[] byteArray = null;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if (kitchenTable.getStep1_imageValue() != null) {
                                File image = new File(kitchenTable.getStep1_imageValue());

                                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byteArray = stream.toByteArray();

                                String name = kitchenTable.getStep1_imageValue().substring(kitchenTable.getStep1_imageValue().lastIndexOf("/") + 1);
                                if (name.indexOf(".") > 0)
                                    name = name.substring(0, name.lastIndexOf("."));
                                //not added kitchen image api
                                params.put("ufile", new DataPart(name + ".jpg", byteArray, "image/jpeg"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "step 1 Error", Toast.LENGTH_SHORT).show();
                        }

                        try {
                            byte[] byteArray = null;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if (kitchenTable.getStep2_imageValue() != null) {
                                File image = new File(kitchenTable.getStep2_imageValue());

                                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(image));
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byteArray = stream.toByteArray();

                                String name = kitchenTable.getStep2_imageValue().substring(kitchenTable.getStep2_imageValue().lastIndexOf("/") + 1);
                                if (name.indexOf(".") > 0)
                                    name = name.substring(0, name.lastIndexOf("."));
                                //not added kitchen image api
                                params.put("ufile2", new DataPart(name + ".jpg", byteArray, "image/jpeg"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "step 2 Error", Toast.LENGTH_SHORT).show();
                        }

                        return params;
                    }
                };

                MyApplication.getInstance().addToRequestQueue(multipartRequest);

            }
        }
        else
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Add Chulha photos");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }
    }










    //upload payments of customer to server
    public static void UploadPaymentsOfCustomerToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
    {

        if(customerTable.getUpload_statusValue().equalsIgnoreCase("0"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("1"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("2"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("3"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("4"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("5"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("6"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else if(customerTable.getUpload_statusValue().equalsIgnoreCase("7"))
    {
        checkNUploadPayments(activity, sweetAlertDialog, customerTable);
    }
    else  if (customerTable.getUpload_statusValue().equalsIgnoreCase("8"))
        {
            uploadHalfPaymentDataToServer(activity, sweetAlertDialog,customerTable);
        }
        else if (customerTable.getUpload_statusValue().equalsIgnoreCase("9"))
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("Half Payment Completed");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }
        else if (customerTable.getUpload_statusValue().equalsIgnoreCase("10"))
        {
            uploadCompletePaymentDataToServer(activity, sweetAlertDialog,customerTable);
        }
        else if (customerTable.getUpload_statusValue().equalsIgnoreCase("11"))
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("Payment Completed");
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
            sweetAlertDialog.setTitleText("Payment not added");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }
    }

    private static void checkNUploadPayments(Activity activity,SweetAlertDialog sweetAlertDialog,CustomerTable customerTable)
    {
        PaymentTable paymentTable = PaymentDetailsHelper.getAllPaymentDataByCustomer(activity, customerTable.getCustomerIdValue());
        if(paymentTable!=null) {
            if (paymentTable.getUpload_statusValue().equalsIgnoreCase(PaymentTable.PAYMENT_ADDED_LOCAL))
            {
                uploadHalfPaymentDataToServer(activity, sweetAlertDialog,customerTable);
            }
            else if(paymentTable.getUpload_statusValue().equalsIgnoreCase(PaymentTable.PAYMENT_ADDED_SERVER))
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Half Payment Completed");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }
            else if(paymentTable.getUpload_statusValue().equalsIgnoreCase(PaymentTable.PAYMENT_COMPLETED_LOCAL))
            {
                uploadCompletePaymentDataToServer(activity, sweetAlertDialog,customerTable);
            }
            else if(paymentTable.getUpload_statusValue().equalsIgnoreCase(PaymentTable.PAYMENT_COMPLETED_SERVER))
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Payment Completed");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }
        }
        else
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Payment not added");
            sweetAlertDialog.setContentText("Please add payment");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

    }
    private static void uploadHalfPaymentDataToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
    {
        final PaymentTable paymentTable= PaymentDetailsHelper.getUnUploadPaymentData(activity,customerTable.getUniqueIdValue());

        if(paymentTable!=null)
        {
                StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlUploadPayment, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responce = new JSONObject(response);
                            if (responce.getString("status").equalsIgnoreCase("success")) {
                                if (responce.getString("message").equalsIgnoreCase("Payment Details added successfully")) {

                                    JSONObject jsonObject = responce.getJSONObject("result");

                                    paymentTable.setPayment_idValue(jsonObject.getString("id"));
                                    paymentTable.setPaymentUniqueIdValue(jsonObject.getString("PAYMENT_ID"));
                                    paymentTable.setAmountValue(jsonObject.getString("AMOUNT"));
                                    paymentTable.setCustomerIdValue(jsonObject.getString("CUSTOMER_ID"));
                                    paymentTable.setDateOfPaymentValue(jsonObject.getString("DATE_OF_PAYMENT"));
                                    paymentTable.setKitchenIdValue(jsonObject.getString("KITCHEN_ID"));
                                    paymentTable.setDateOfPaymentValue(jsonObject.getString("DATE_OF_PAYMENT"));
                                    paymentTable.setPaymentTypeValue(jsonObject.getString("PAYMENT_TYPE"));
                                    paymentTable.setReceiptNoValue(jsonObject.getString("RECEIPT_NO"));
                                    paymentTable.setUploadDateValue(jsonObject.getString("UploadDate"));
                                    paymentTable.setUpload_statusValue(PaymentTable.PAYMENT_ADDED_SERVER);


                                    if (PaymentDetailsHelper.updatePaymentDetailsData(activity, paymentTable)) {
                                        customerTable.setUpload_statusValue("9");
                                        CustomerTableHelper.updateCustomerStatus(activity,customerTable);
                                        Toast.makeText(activity, "Payment Data Updated Successfully..", Toast.LENGTH_SHORT).show();
                                        uploadPaymentDataToServer(activity, sweetAlertDialog);
                                    } else {

                                        Toast.makeText(activity, "Payment Data Updation Failed ", Toast.LENGTH_SHORT).show();
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        sweetAlertDialog.setTitleText("Update failed");
                                        sweetAlertDialog.setConfirmText("Ok");
                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });

                                    }


                                } else {
                                    Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.setTitleText("Response failed");
                                    sweetAlertDialog.setConfirmText("Ok");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Response failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            Toast.makeText(activity, "JSON Error ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("JSON Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }
                    }
                }
                        , new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity, "Timeout Error ", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Check internet connection");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof ServerError) {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Server Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Network Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof ParseError) {
                            Toast.makeText(activity, "Parse Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Parse Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else {
                            Toast.makeText(activity, "Unknown Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Unkonwn error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }

                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<String, String>();
                /*format=json&mobile=9975294782&password=user@123&
                CUSTOMER_ID=2321&PAYMENT_ID=23214&AMOUNT=234124&
                DATE_OF_PAYMENT=a&KITCHEN_ID=asadx&
                PAYMENT_TYPE=asxasxas&RECEIPT_NO=asxasx*/
                        params.put("format", "json");
                        params.put("CUSTOMER_ID", paymentTable.getCustomerIdValue());
                        params.put("PAYMENT_ID", paymentTable.getPaymentUniqueIdValue());
                        params.put("AMOUNT", paymentTable.getTotalPaidValue());
                        params.put("DATE_OF_PAYMENT", paymentTable.getDateOfPaymentValue());
                        params.put("KITCHEN_ID", paymentTable.getKitchenIdValue());
                        params.put("PAYMENT_TYPE", paymentTable.getPaymentTypeValue());
                        params.put("RECEIPT_NO", paymentTable.getReceiptNoValue());
                        params.put("mobile", new PrefManager(activity).getMobile());
                        params.put("password", new PrefManager(activity).getPassword());


                        //returning parameters
                        return params;
                    }

                };

                MyApplication.getInstance().addToRequestQueue(strReq);

        }
        else
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Payment not added");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

    }

    private static void uploadCompletePaymentDataToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog, final CustomerTable customerTable)
    {
        final PaymentTable paymentTable= PaymentDetailsHelper.getCompletePaymentData(activity,customerTable.getUniqueIdValue());

        if(paymentTable!=null)
        {
                StringRequest strReq = new StringRequest(Request.Method.POST, WebServiceUrls.urlUploadPayment, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responce = new JSONObject(response);
                            if (responce.getString("status").equalsIgnoreCase("success")) {
                                if (responce.getString("message").equalsIgnoreCase("Payment Details added successfully")) {

                                    JSONObject jsonObject = responce.getJSONObject("result");

                                    paymentTable.setPayment_idValue(jsonObject.getString("id"));
                                    paymentTable.setPaymentUniqueIdValue(jsonObject.getString("PAYMENT_ID"));
                                    paymentTable.setAmountValue(jsonObject.getString("AMOUNT"));
                                    paymentTable.setCustomerIdValue(jsonObject.getString("CUSTOMER_ID"));
                                    paymentTable.setDateOfPaymentValue(jsonObject.getString("DATE_OF_PAYMENT"));
                                    paymentTable.setKitchenIdValue(jsonObject.getString("KITCHEN_ID"));
                                    paymentTable.setDateOfPaymentValue(jsonObject.getString("DATE_OF_PAYMENT"));
                                    paymentTable.setPaymentTypeValue(jsonObject.getString("PAYMENT_TYPE"));
                                    paymentTable.setReceiptNoValue(jsonObject.getString("RECEIPT_NO"));
                                    paymentTable.setUploadDateValue(jsonObject.getString("UploadDate"));
                                    paymentTable.setUpload_statusValue(PaymentTable.PAYMENT_COMPLETED_SERVER);


                                    if (PaymentDetailsHelper.updatePaymentDetailsData(activity, paymentTable)) {
                                        customerTable.setUpload_statusValue("11");
                                        CustomerTableHelper.updateCustomerStatus(activity,customerTable);
                                        Toast.makeText(activity, "Payment Data Updated Successfully..", Toast.LENGTH_SHORT).show();
                                        uploadPaymentDataToServer(activity, sweetAlertDialog);
                                    } else {

                                        Toast.makeText(activity, "Payment Data Updation Failed ", Toast.LENGTH_SHORT).show();
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        sweetAlertDialog.setTitleText("Update failed");
                                        sweetAlertDialog.setConfirmText("Ok");
                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });

                                    }


                                } else {
                                    Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.setTitleText("Response failed");
                                    sweetAlertDialog.setConfirmText("Ok");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(activity, "Response Failed ", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Response failed");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            Toast.makeText(activity, "JSON Error ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("JSON Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }
                    }
                }
                        , new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity, "Timeout Error ", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Check internet connection");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof ServerError) {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Server Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Network Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else if (error instanceof ParseError) {
                            Toast.makeText(activity, "Parse Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Parse Error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        } else {
                            Toast.makeText(activity, "Unknown Error", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Unkonwn error");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }

                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable<String, String>();
                /*format=json&mobile=9975294782&password=user@123&
                CUSTOMER_ID=2321&PAYMENT_ID=23214&AMOUNT=234124&
                DATE_OF_PAYMENT=a&KITCHEN_ID=asadx&
                PAYMENT_TYPE=asxasxas&RECEIPT_NO=asxasx*/
                        params.put("format", "json");
                        params.put("CUSTOMER_ID", paymentTable.getCustomerIdValue());
                        params.put("PAYMENT_ID", paymentTable.getPaymentUniqueIdValue());
                        params.put("AMOUNT", paymentTable.getTotalPaidValue());
                        params.put("DATE_OF_PAYMENT", paymentTable.getDateOfPaymentValue());
                        params.put("KITCHEN_ID", paymentTable.getKitchenIdValue());
                        params.put("PAYMENT_TYPE", paymentTable.getPaymentTypeValue());
                        params.put("RECEIPT_NO", paymentTable.getReceiptNoValue());
                        params.put("mobile", new PrefManager(activity).getMobile());
                        params.put("password", new PrefManager(activity).getPassword());


                        //returning parameters
                        return params;
                    }

                };

                MyApplication.getInstance().addToRequestQueue(strReq);
        }
        else
        {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Payment not Completed");
            sweetAlertDialog.setConfirmText("Ok");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

    }

}
