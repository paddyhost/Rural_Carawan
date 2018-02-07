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
import com.hatchers.ruralcaravane.payment_details.apihelper.Payment_WebApiHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentDetailsHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Nikam on 20/01/2018.
 */

public class Construction_WebApiHelper {

    public static boolean uploadConstructionDataToServer(final Activity activity, final SweetAlertDialog sweetAlertDialog)
    {

       /* final ConstructionTable constructionTable =ConstructionTableHelper.getUnUploadConstructionData(activity);
        if(constructionTable==null)
        {

            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText("Please wait");
                    PaymentTable paymentTable= PaymentDetailsHelper.getUnUploadPaymentData(activity);
                    if(paymentTable!=null)
                    {
                        Payment_WebApiHelper.uploadPaymentDataToServer(activity,sweetAlertDialog);
                    }
                    else
                    {
                        PaymentTable paymentTable1= PaymentDetailsHelper.getUnUploadPaymentData1(activity);
                        if(paymentTable1!=null)
                        {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Payment Done");
                            sweetAlertDialog.setContentText("Payment uploaded successfully.");
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
                            sweetAlertDialog.setTitleText("Payment Not Done");
                            sweetAlertDialog.setContentText("Please complete Payment.");
                            sweetAlertDialog.setConfirmText("Ok");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });

                        }

                    }

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


                                    JSONObject jsonObject = responce.getJSONObject("result");

                                    constructionTable.setTechnicianIdValue(jsonObject.getString("id"));
                                    constructionTable.setKitchenIdValue(jsonObject.getString("kechain_id"));
                                    constructionTable.setTechnicianIdValue(jsonObject.getString("technitionid"));
                                    constructionTable.setCustomerIdValue(jsonObject.getString("customerid"));
                                    constructionTable.setAddedByIdValue(jsonObject.getString("addedby_id"));
                                    constructionTable.setUploadStatusValue("1");

                                    if (ConstructionTableHelper.updateConstructionTeamData(activity, constructionTable)) {

                                        Toast.makeText(activity,"Construction Data Updated Successfully..",Toast.LENGTH_SHORT).show();
                                        uploadConstructionDataToServer(activity,sweetAlertDialog);

                                    } else {

                                        Toast.makeText(activity,"Construction Data Updation Failed ",Toast.LENGTH_SHORT).show();
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


                        }
                        else
                        {
                            Toast.makeText(activity,"Response Failed ",Toast.LENGTH_SHORT).show();
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

                    }
                    else
                    {
                        Toast.makeText(activity,"Response Failed ",Toast.LENGTH_SHORT).show();
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
                } catch (JSONException e)
                {
                    Toast.makeText(activity,"JSON Error ",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity,"Timeout Error ",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Check internet connection");
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
                    Toast.makeText(activity,"Server Error ",Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Server Error");
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
                    Toast.makeText(activity,"Network Error",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity,"Parse Error",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity,"Unknown Error",Toast.LENGTH_SHORT).show();
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

                params.put("format","json");
                params.put("kechain_id",constructionTable.getKitchenIdValue());
                if(constructionTable.getTechnicianUniqueIdValue()!=null) {
                    params.put("technitionid", constructionTable.getTechnicianUniqueIdValue());
                }
                else
                {
                    params.put("technitionid", "");
                }
                params.put("customerid",constructionTable.getCustomerIdValue());
                params.put("addedby_id",constructionTable.getAddedByIdValue());
                params.put("mobile",new PrefManager(activity).getMobile());
                params.put("password",new PrefManager(activity).getPassword());
                params.put("starttime","");
                params.put("endtime","");

                //returning parameters
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(strReq);*/
        return true;
    }

   }
