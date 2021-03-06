package com.hatchers.ruralcaravane.payment_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.CustomerListFragment;
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
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.MARATHI;
import static com.hatchers.ruralcaravane.constants.AppConstants.PAYMENT_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;

public class PaymentDetailsFragment extends Fragment {


    private int CAMERA = 1;
    private int RESULT_CANCELED;
    Bitmap payBitmap;
    private Toolbar payment_toolbar;
    private TextInputEditText payment_amount,paid_amount,remaining_amount,receipt_number;
    private ImageView receiptImageView;
    private Button savePayment;
    private String paymentUniqueIdTxt;
    PaymentTable paymentTable;

    PaymentTable oldPaymentTable;
    private PrefManager prefManager;
    private TextView text1;

    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    private CustomerTable customertable;
    public static PaymentDetailsFragment getInstance(CustomerTable customertable,PaymentTable paymentTable)
    {
        PaymentDetailsFragment fragment = new PaymentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        args.putParcelable(PaymentTable.PAYMENT_TABLE,paymentTable);
        fragment.setArguments(args);
        return fragment;

    }

    public static PaymentDetailsFragment getNewPaymentInstance(CustomerTable customertable)
    {
        PaymentDetailsFragment fragment = new PaymentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            customertable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
            oldPaymentTable =getArguments().getParcelable(PaymentTable.PAYMENT_TABLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment_details, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(payment_toolbar);
        initializations(view);
        setLanguageToUI();
        onClickListeners();
        addTextListner();
        setPaymentDetailsFromList();


        return view;
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            payment_toolbar.setTitle(getResources().getString(R.string.do_payment_marathi));

            receipt_number.setHint(getResources().getString(R.string.receipt_number_marathi));
            receipt_number.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            receipt_number.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            payment_amount.setHint(getResources().getString(R.string.cost_of_chullaha_marathi));
            payment_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            payment_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            paid_amount.setHint(getResources().getString(R.string.paid_amount_marathi));
            paid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            paid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            remaining_amount.setText(getResources().getString(R.string.remaining_payment_marathi));
            remaining_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            text1.setText(getResources().getString(R.string.upload_payment_receipt_marathi));
            text1.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            savePayment.setText(getResources().getString(R.string.save));
            savePayment.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


        }
        else
        {
            payment_toolbar.setTitle(getResources().getString(R.string.do_payment_english));

            receipt_number.setHint(getResources().getString(R.string.receipt_number_english));
            receipt_number.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            receipt_number.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            payment_amount.setText(getResources().getString(R.string.cost_of_chullaha_english));
            payment_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            payment_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            paid_amount.setHint(getResources().getString(R.string.paid_amount_english));
            paid_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            paid_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            remaining_amount.setText(getResources().getString(R.string.remaining_payment_english));
            remaining_amount.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            remaining_amount.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            text1.setText(getResources().getString(R.string.upload_payment_receipt_english));
            text1.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            savePayment.setText(getResources().getString(R.string.save1));
            savePayment.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

        }
    }

    private void initializations(View view)
    {
        prefManager = new PrefManager(getActivity());
        payment_toolbar = (Toolbar) view.findViewById(R.id.payment_toolbar);
        payment_amount = (TextInputEditText) view.findViewById(R.id.payment_amount);
        paid_amount = (TextInputEditText) view.findViewById(R.id.paid_amount);
        remaining_amount = (TextInputEditText) view.findViewById(R.id.remaining_amount);
        receipt_number=(TextInputEditText)view.findViewById(R.id.receipt_number);
        receiptImageView = (ImageView) view.findViewById(R.id.takePhoto);
        savePayment = (Button) view.findViewById(R.id.savePayment);
        text1=(TextView)view .findViewById(R.id.text1);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window =getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void onClickListeners()
    {
        payment_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

                    FileHelper.savePNGImage(Folders.PAYMENTFOLDER,payBitmap,PAYMENT_PREFIX+paymentTable.getPaymentUniqueIdValue());
                    File image = FileHelper.createfile(Folders.PAYMENTFOLDER,PAYMENT_PREFIX+paymentTable.getPaymentUniqueIdValue(), FileType.PNG);
                    if(image!=null)
                    {
                        paymentTable.setReceiptImageValue(image.getAbsolutePath());

                        if(image.exists())
                        {
                            SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
                            }
                            else
                            {
                                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
                            }
                            sweetAlertDialog.show();

                            if(PaymentDetailsHelper.insertPaymentDetailsData(getContext(), paymentTable))
                            {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                    sweetAlertDialog.setTitleText(getResources().getString(R.string.payment_details_added_successfully_marathi));
                                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                                }
                                else
                                {
                                    sweetAlertDialog.setTitleText(getResources().getString(R.string.payment_details_added_successfully_english));
                                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                                }
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();

                                        payment_amount.setText("");
                                        paid_amount.setText("");
                                        remaining_amount.setText("");
                                        receiptImageView.setImageResource(R.drawable.capture_area);
                                        payBitmap=null;


                                    }
                                });
                            }
                            else
                            {

                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                    sweetAlertDialog.setTitleText(getResources().getString(R.string.payment_details_add_failed_marathi));
                                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                                }
                                else
                                {
                                    sweetAlertDialog.setTitleText(getResources().getString(R.string.payment_details_add_failed_marathi));
                                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                                }
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
                            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_receipt_image_marathi), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_receipt_image_english), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    else
                    {
                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_receipt_image_marathi), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_receipt_image_english), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    private void showPictureDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_marathi), getResources().getString(R.string.cancel_marathi)};
            builder.setTitle(getResources().getString(R.string.add_photo_marathi));
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
        }
        else
        {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_english), getResources().getString(R.string.cancel_english)};
            builder.setTitle(getResources().getString(R.string.add_photo_english));
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
        }
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

    private void setPaymentDetailsData()
    {
        paymentTable=new PaymentTable();

        paymentTable.setPayment_idValue("");
        paymentTable.setAmountValue(payment_amount.getText().toString());
        paymentTable.setTotalPaidValue(paid_amount.getText().toString());
        paymentTable.setBalanceValue(remaining_amount.getText().toString());
        paymentTable.setCustomerIdValue(customertable.getUniqueIdValue());
        paymentTable.setDateOfPaymentValue(getCurrentDateTime());
        paymentTable.setUpload_statusValue("0");
        paymentTable.setPaymentUniqueIdValue(PAYMENT_PREFIX+generateUniqueId());
        paymentTable.setReceiptNoValue(receipt_number.getText().toString());
        paymentTable.setPaymentTypeValue(paymentTable.getPaymentTypeValue());
        paymentTable.setUploadDateValue(getCurrentDateTime());



    }

    private void calculateRemainingAmount()
    {
        int remainingAmount;

            if (payment_amount.getText().toString().equalsIgnoreCase("") ||
                    paid_amount.getText().toString().equalsIgnoreCase(""))
        {
                remainingAmount = 0;
                remaining_amount.setText(String.valueOf(remainingAmount));
            } else
                {
                    remainingAmount = Integer.parseInt(payment_amount.getText().toString()) - Integer.parseInt(paid_amount.getText().toString());
                    if(remainingAmount<0)
                    {
                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_marathi));
                        }
                        else
                        {
                            paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_english));
                        }
                    }
                    else
                    {
                        remaining_amount.setText(String.valueOf(remainingAmount));
                    }
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
                    if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                        paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_marathi));
                    }
                    else
                    {
                        paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_english));
                    }
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
                    if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                        paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_marathi));
                    }
                    else
                    {
                        paid_amount.setError(getResources().getString(R.string.amount_must_be_less_than_total_amount_english));
                    }
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

    private String generateUniqueId()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        paymentUniqueIdTxt=PAYMENT_PREFIX+datetime;

        return paymentUniqueIdTxt;

    }

    private boolean checkValidation()
    {
        boolean response = true;

        if (payment_amount.getText().toString().trim().length() == 0) {
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                payment_amount.setError(getResources().getString(R.string.cost_of_chullaha_marathi));
            }
            else
            {
                payment_amount.setError(getResources().getString(R.string.cost_of_chullaha_english));
            }

            response = false;
        } else {
            payment_amount.setError(null);
        }

        if (paid_amount.getText().toString().trim().length() == 0) {
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                paid_amount.setError(getResources().getString(R.string.please_enter_advance_amount_marathi));
            }
            else
            {
                paid_amount.setError(getResources().getString(R.string.please_enter_advance_amount_english));
            }
            response = false;
        } else {
            paid_amount.setError(null);
        }

        if (remaining_amount.getText().toString().trim().length() == 0) {
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                remaining_amount.setError(getResources().getString(R.string.remaining_payment_marathi));
            }
            else
            {
                remaining_amount.setError(getResources().getString(R.string.remaining_payment_english));
            }
            response = false;
        } else {
            remaining_amount.setError(null);
        }

        return response;
    }

    private void setPaymentDetailsFromList()
    {
        if(oldPaymentTable!=null)
        {
            payment_amount.setText(oldPaymentTable.getAmountValue());
            paid_amount.setText(oldPaymentTable.getTotalPaidValue());
            remaining_amount.setText(oldPaymentTable.getBalanceValue());
            payment_amount.setFocusable(false);
            paid_amount.setFocusable(false);
            receipt_number.setText(oldPaymentTable.getReceiptNoValue());
            receipt_number.setFocusable(false);

            File image = FileHelper.createfile(Folders.PAYMENTFOLDER,PAYMENT_PREFIX+oldPaymentTable.getPaymentUniqueIdValue(), FileType.PNG);
            if(image!=null)
            {
                if(image.exists())
                {
                    Glide.with(getActivity())
                            .load(image.getAbsolutePath())
                            .error(R.drawable.capture_area)
                            .into(receiptImageView);
                }
            }

        }
    }

}
