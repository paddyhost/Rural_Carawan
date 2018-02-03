package com.hatchers.ruralcaravane.payment_details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.CompleteConstructionActivity;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentDetailsHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.PAYMENT_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;
import static com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper.getKitchenDetailsData;
import static com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper.updateUpdateCost;
import static com.hatchers.ruralcaravane.payment_details.database.PaymentDetailsHelper.getPaymentAmountByCustomerId;


public class GetPayment extends Fragment {
    private int CAMERA = 1;
    private int RESULT_CANCELED;
    Bitmap payBitmap;
    private Toolbar paymentToolbar;
    private TextInputEditText payment_amount,paid_amount,remaining_amount,receipt_number,allpaid_amount;
    private ImageView receiptImageView;
    private Button savePayment;
    private String paymentUniqueIdTxt;
    PaymentTable paymentTable;
    PaymentTable oldPaymentTable;
    CustomerTable customerTable;
    KitchenTable kitchen;
    int totalcost;
    PrefManager prefManager;
    private TextView labelUploadReceipt;
    public GetPayment() {

    }


    public static GetPayment newInstance(CustomerTable customerTable) {
        GetPayment fragment = new GetPayment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE,customerTable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerTable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);

            totalcost= getPaymentAmountByCustomerId(getActivity(), customerTable.getUniqueIdValue());
            kitchen= getKitchenDetailsData(getActivity(),customerTable.getUniqueIdValue());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_get_payment, container, false);
        initializations(view);
        setLanguageToUI();
        onClickListeners();
        
        addTextListner();

        if(kitchen.getCostOfChullhaValue()!=null&&totalcost>=0)
        {
            paid_amount.setText("0");
            allpaid_amount.setText(totalcost+"");
            payment_amount.setText(kitchen.getCostOfChullhaValue()+"");
            payment_amount.setFocusable(false);
            allpaid_amount.setFocusable(false);

        }
        else {
            allpaid_amount.setText( "0");
            allpaid_amount.setFocusable(false);
        }
        return view;
    }

    private void initializations(View view)
    {

        prefManager=new PrefManager(getActivity());
        paymentToolbar = (Toolbar) view.findViewById(R.id.payment_toolbar);
        payment_amount = (TextInputEditText) view.findViewById(R.id.payment_amount);
        paid_amount = (TextInputEditText) view.findViewById(R.id.paid_amount);
        remaining_amount = (TextInputEditText) view.findViewById(R.id.remaining_amount);
        receipt_number=(TextInputEditText)view.findViewById(R.id.receipt_number);
        receiptImageView = (ImageView) view.findViewById(R.id.takePhoto);
        savePayment = (Button) view.findViewById(R.id.savePayment);
        allpaid_amount= (TextInputEditText) view.findViewById(R.id.allpaid_amount);
        labelUploadReceipt=(TextView)view.findViewById(R.id.label_upload_receipt);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window =getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }



    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            paymentToolbar.setTitle(getResources().getString(R.string.do_payment_marathi));

            receipt_number.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            receipt_number.setHint(getResources().getString(R.string.receipt_number_marathi));
            receipt_number.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            payment_amount.setHint(getResources().getString(R.string.cost_of_chullaha_marathi));
            payment_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            payment_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            allpaid_amount.setHint(getResources().getString(R.string.paid_amount_marathi));
            allpaid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            allpaid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            paid_amount.setHint(getResources().getString(R.string.remaining_payment_marathi));
            paid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            paid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            remaining_amount.setHint(getResources().getString(R.string.remaining_payment_marathi));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            remaining_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            labelUploadReceipt.setText(getResources().getString(R.string.upload_payment_receipt_marathi));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            savePayment.setText(getResources().getString(R.string.save));
            savePayment.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

        }
        else
        {
            paymentToolbar.setTitle(getResources().getString(R.string.do_payment_english));

            receipt_number.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            receipt_number.setHint(getResources().getString(R.string.receipt_number_english));
            receipt_number.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            payment_amount.setHint(getResources().getString(R.string.cost_of_chullaha_english));
            payment_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            payment_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            allpaid_amount.setHint(getResources().getString(R.string.paid_amount_english));
            allpaid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            allpaid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            paid_amount.setHint(getResources().getString(R.string.remaining_payment_english));
            paid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            paid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            remaining_amount.setHint(getResources().getString(R.string.remaining_payment_english));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            remaining_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            labelUploadReceipt.setText(getResources().getString(R.string.upload_payment_receipt_english));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            savePayment.setText(getResources().getString(R.string.save1));
            savePayment.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

        }
    }


    private void onClickListeners()
    {
        paymentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        receiptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        savePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setPaymentDetailsData();
                if(checkValidation()) {
                    if(updateUpdateCost(getActivity(),paymentTable.getAmountValue(),kitchen.getKitchenUniqueIdValue()))
                    {
                        FileHelper.savePNGImage(Folders.PAYMENTFOLDER,payBitmap,PAYMENT_PREFIX+paymentTable.getPaymentUniqueIdValue());
                        File image = FileHelper.createfile(Folders.PAYMENTFOLDER,PAYMENT_PREFIX+paymentTable.getPaymentUniqueIdValue(), FileType.PNG);
                        if(image!=null)
                        {
                            paymentTable.setReceiptImageValue(image.getAbsolutePath());

                            if(image.exists())
                            {
                                SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                                        .setTitleText("Please wait");

                                sweetAlertDialog.show();

                                if(PaymentDetailsHelper.insertPaymentDetailsData(getContext(), paymentTable))
                                {
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.setTitleText("Payment Details Added Successfully");
                                    sweetAlertDialog.setConfirmText("Ok");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();

                                            payment_amount.setText("");
                                            paid_amount.setText("");
                                            remaining_amount.setText("");
                                            receiptImageView.setImageResource(R.drawable.capture_area);
                                            payBitmap=null;
                                            getActivity().onBackPressed();
                                        }
                                    });
                                }
                                else
                                {

                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.setTitleText("Payment Details Add Failed");
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
                                Toast.makeText(getActivity(), "Please Upload Receipt Image....!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(getActivity(), "Please Upload Receipt Area Image....!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }



            }
        });

    }
    private void showPictureDialog()
    {
        final CharSequence[] options = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;

                            case 2:
                                dialog.dismiss();
                        }
                    }
                });
        AlertDialog alert=builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void takePhotoFromCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            receiptImageView.setImageBitmap(thumbnail);
            payBitmap=thumbnail;

        }
    }
    private void addTextListner()
    {
        payment_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateRemainingAmount();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateRemainingAmount();

            }
        });

        paid_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int before) {
                int totalcost=0;
                if (payment_amount.getText().toString().equalsIgnoreCase(""))
                {
                    totalcost = 0;
                }
                else
                {
                    totalcost = Integer.parseInt(payment_amount.getText().toString());
                }

                if(count>totalcost)
                {
                    paid_amount.setError("Amount must be less than total cost.");
                }
                else
                {
                    paid_amount.setError(null);
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                calculateRemainingAmount();
                int totalcost=0;
                if (payment_amount.getText().toString().equalsIgnoreCase(""))
                {
                    totalcost = 0;
                }
                else
                {
                    totalcost = Integer.parseInt(payment_amount.getText().toString());
                }

                if(count>totalcost)
                {
                    paid_amount.setError("Amount must be less than total cost.");
                }
                else
                {
                    paid_amount.setError(null);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateRemainingAmount();

            }
        });
    }

    private void calculateRemainingAmount()
    {
        int remainingAmount;
        int paidcost=  0;
        try{
            paidcost=  Integer.parseInt(allpaid_amount.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (payment_amount.getText().toString().equalsIgnoreCase("") ||
                paid_amount.getText().toString().equalsIgnoreCase(""))
        {
            remainingAmount = 0;
            remaining_amount.setText(String.valueOf(remainingAmount));
        } else
        {

            int cost=(Integer.parseInt(payment_amount.getText().toString()));
            remainingAmount = (cost-paidcost)- Integer.parseInt(paid_amount.getText().toString());

            if(remainingAmount<0)
            {
                paid_amount.setError("Amount must be less than total amount.");
            }
            else
            {
                remaining_amount.setText(String.valueOf(remainingAmount));
            }
        }

    }

    private String generateUniqueId()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        paymentUniqueIdTxt=PAYMENT_PREFIX+datetime;

        return paymentUniqueIdTxt;

    }

    private void setPaymentDetailsData()
    {
        paymentTable=new PaymentTable();

        paymentTable.setAmountValue(payment_amount.getText().toString());
        paymentTable.setTotalPaidValue(paid_amount.getText().toString());
        paymentTable.setBalanceValue(remaining_amount.getText().toString());
        paymentTable.setCustomerIdValue(customerTable.getUniqueIdValue());
        paymentTable.setDateOfPaymentValue(getCurrentDateTime());
        paymentTable.setUpload_statusValue("0");
        paymentTable.setPaymentUniqueIdValue(PAYMENT_PREFIX+generateUniqueId());
        paymentTable.setReceiptNoValue(receipt_number.getText().toString());
        paymentTable.setPaymentTypeValue(paymentTable.getPaymentTypeValue());
        paymentTable.setUploadDateValue(getCurrentDateTime());
        paymentTable.setKitchenIdValue(kitchen.getKitchenUniqueIdValue());
        paymentTable.setPaymentTypeValue("Cash");

    }
    private boolean checkValidation()
    {
        boolean response = true;

        if (payment_amount.getText().toString().trim().length() == 0) {
            payment_amount.setError("Please Enter Payment Amount");
            response = false;
        } else {
            payment_amount.setError(null);
        }

        if (paid_amount.getText().toString().trim().length() == 0) {
            paid_amount.setError("Please Enter Advance Amount");
            response = false;
        } else {
            paid_amount.setError(null);
        }

        if (remaining_amount.getText().toString().trim().length() == 0) {
            remaining_amount.setError("Please Enter Remaining Amount");
            response = false;
        } else {
            remaining_amount.setError(null);
        }

        return response;
    }
}
