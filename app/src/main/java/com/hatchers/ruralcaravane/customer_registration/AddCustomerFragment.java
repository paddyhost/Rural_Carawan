package com.hatchers.ruralcaravane.customer_registration;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.AddKitchenSuitability;
import com.hatchers.ruralcaravane.locality.database.CityTable;
import com.hatchers.ruralcaravane.locality.database.CityTableHelper;
import com.hatchers.ruralcaravane.locality.database.StateTable;
import com.hatchers.ruralcaravane.locality.database.StateTableHelper;
import com.hatchers.ruralcaravane.locality.database.VillageTable;
import com.hatchers.ruralcaravane.locality.database.VillageTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.scaner.AdharScanner;
import com.hatchers.ruralcaravane.utils.Utility;
import com.hatchers.ruralcaravane.utils.validations.Validations;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hatchers.ruralcaravane.constants.AppConstants.CUSTOMER_PREFIX;
import static com.hatchers.ruralcaravane.constants.AppConstants.MARATHI;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;

public class AddCustomerFragment extends Fragment {

    private int CAMERA = 1,GALLERY=2,ADHARSCAN=3;;
    Bitmap custBitmap;
    CustomerTable customer_table;
    private String selected_gender = "";
    private Button save,ScanByAadhar;
    private FloatingActionButton fab;
    private RadioGroup radioGroupGender;
    private RadioButton male, female;
    private TextView uniqueIdTxt,txt_customerRegistration,uniqueIdText,stateTxt,cityTxt,villageTxt;
    private CircleImageView profileImage;
    private EditText customer_name, customer_address, customer_mobileno, customer_age,aadhar_id;
    private Spinner citySpinner, villageSpinner ,stateSpinner;
    private ArrayList<CityTable> cityArrayList;
    private ArrayList<VillageTable> villageArrayList;
    private ArrayList<StateTable> stateArrayList;
    private String villageId,cityid,stateId;
    private int RESULT_CANCELED;
    private Toolbar addCustomerToolbar;
    private PrefManager prefManager;
    private FragmentTransaction fragmentTransaction;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS=111;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA=112;

    public AddCustomerFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add__customer, container, false);

        initializations(view);
        setLanguageToUI();
        generateUniqueId();
        onclicklisteners();
        setStateSpinnerList();
        stateSelectedListener();
        setGender();

        //RuntimePermissions.checkCameraPermission(getActivity());
        //RuntimePermissions.checkReadExternalStoragePermission(getActivity());
        //RuntimePermissions.checkWriteExternalStoragePermission(getActivity());

        checkAndRequestPermissions();
        return view;
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            addCustomerToolbar.setTitle(getResources().getString(R.string.new_customer));

            txt_customerRegistration.setText(getResources().getString(R.string.customer_registration));
            txt_customerRegistration.setTextSize(Utility.getConvertFloatToDP(getActivity(),15));

            ScanByAadhar.setText(getResources().getString(R.string.scan_aadhar_card));
            ScanByAadhar.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

            uniqueIdText.setText(getResources().getString(R.string.unique_id));
            uniqueIdText.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            uniqueIdTxt.setText(getResources().getString(R.string.unique_id));
            uniqueIdTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            stateTxt.setText(getResources().getString(R.string.state));
            stateTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            cityTxt.setText(getResources().getString(R.string.city));
            cityTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            villageTxt.setText(getResources().getString(R.string.village));
            villageTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            aadhar_id.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            aadhar_id.setHint(getResources().getString(R.string.enter_aadhar_number));
            aadhar_id.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_name.setHint(getResources().getString(R.string.enter_customer_name));
            customer_name.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_name.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_address.setHint(getResources().getString(R.string.enter_customer_address));
            customer_address.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_address.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_mobileno.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_mobileno.setHint(getResources().getString(R.string.enter_customer_mobile_number));
            customer_mobileno.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_age.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_age.setHint(getResources().getString(R.string.enter_customer_age));
            customer_age.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            male.setText(getResources().getString(R.string.male));
            male.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            female.setText(getResources().getString(R.string.female));
            female.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            save.setText(getResources().getString(R.string.save));
            save.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

        }
        else
        {
            addCustomerToolbar.setTitle(getResources().getString(R.string.customer_information1));

            txt_customerRegistration.setText("Customer Registration");
            txt_customerRegistration.setTextSize(Utility.getConvertFloatToDP(getActivity(),15));

            ScanByAadhar.setText(getResources().getString(R.string.scan_aadhar_card1));
            ScanByAadhar.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            uniqueIdText.setText(getResources().getString(R.string.unique_id1));
            uniqueIdText.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            uniqueIdTxt.setText(getResources().getString(R.string.unique_id1));
            uniqueIdTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            stateTxt.setText(getResources().getString(R.string.state1));
            stateTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            cityTxt.setText(getResources().getString(R.string.city1));
            cityTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            villageTxt.setText(getResources().getString(R.string.village1));
            villageTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            aadhar_id.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            aadhar_id.setHint(getResources().getString(R.string.enter_aadhar_number1));
            aadhar_id.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            customer_name.setHint(getResources().getString(R.string.enter_customer_name1));
            customer_name.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_name.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_address.setHint(getResources().getString(R.string.enter_customer_address1));
            customer_address.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_address.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_mobileno.setHint(getResources().getString(R.string.enter_customer_mobile_number1));
            customer_mobileno.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_mobileno.setHintTextColor(getResources().getColor(R.color.DarkGrey));


            customer_age.setHint(getResources().getString(R.string.enter_customer_age1));
            customer_age.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            customer_age.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            male.setText(getResources().getString(R.string.male1));
            male.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            female.setText(getResources().getString(R.string.female1));
            female.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            save.setText(getResources().getString(R.string.save1));
            save.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
        }
    }

    private void initializations(View view)
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        prefManager = new PrefManager(getActivity());
        addCustomerToolbar=(Toolbar)view.findViewById(R.id.addCustomer_toolbar);
        cityArrayList =new ArrayList<CityTable>();
        villageArrayList = new ArrayList<VillageTable>();
        stateArrayList=new ArrayList<StateTable>();
        save = (Button) view.findViewById(R.id.saveBtn);
        customer_name = (EditText) view.findViewById(R.id.customer_name);
        customer_address = (EditText) view.findViewById(R.id.customer_address);
        customer_mobileno = (EditText) view.findViewById(R.id.customer_mobileno);
        customer_age = (EditText) view.findViewById(R.id.customer_age_txt);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.radio_gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        profileImage=(CircleImageView) view.findViewById(R.id.profileImage);
        uniqueIdTxt=(TextView)view.findViewById(R.id.uniqueIdTxt);
        ScanByAadhar=(Button)view.findViewById(R.id.ScanByAadhar);
        citySpinner = (Spinner)view.findViewById(R.id.city_spinner);
        villageSpinner = (Spinner)view.findViewById(R.id.village_spinner);
        stateSpinner=(Spinner)view.findViewById(R.id.state_spinner);
        aadhar_id=(EditText)view.findViewById(R.id.aadhar_id);
        txt_customerRegistration=(TextView)view.findViewById(R.id.customerRegistration_txt);
        uniqueIdText=(TextView)view.findViewById(R.id.uniqueId_Text);
        uniqueIdTxt=(TextView)view.findViewById(R.id.uniqueId_Txt);
        stateTxt=(TextView)view.findViewById(R.id.state_txt);
        cityTxt=(TextView)view.findViewById(R.id.city_txt);
        villageTxt=(TextView)view.findViewById(R.id.village_txt);
    }

    private void setStateSpinnerList()
    {
        stateArrayList = StateTableHelper.getStateDataList(getContext());
        ArrayAdapter<StateTable> adapter =
                new ArrayAdapter<StateTable>(getActivity(), R.layout.spinner_item2, stateArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        stateSpinner.setAdapter(adapter);
    }

    private void stateSelectedListener()
    {
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StateTable state=stateArrayList.get(i);

                stateId=state.getStateId();
                stateSpinner.setSelection(i);

                setCitySpinnerList();
                citySelectedListner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCitySpinnerList()
    {
        cityArrayList = CityTableHelper.getCityDataList(getContext(),stateId);
        ArrayAdapter<CityTable> adapter =
                new ArrayAdapter<CityTable>(getActivity(), R.layout.spinner_item2, cityArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        citySpinner.setAdapter(adapter);

    }

    private void citySelectedListner()
    {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                CityTable city = cityArrayList.get(position);
                citySpinner.setSelection(position);
                cityid=city.getId();

                setVillageSpinnerList();
                villageSelectedListener();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setVillageSpinnerList()
    {
        villageArrayList = VillageTableHelper.getVillageDataList(getContext(),cityid);
        ArrayAdapter<VillageTable> adapter = new ArrayAdapter<VillageTable>(getActivity(), R.layout.spinner_item2, villageArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        villageSpinner.setAdapter(adapter);
    }

    private void villageSelectedListener()
    {
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                VillageTable village=villageArrayList.get(i);
                villageId=village.getId();
                villageSpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onclicklisteners()
    {

        addCustomerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setCustomerData();
                File image =FileHelper.savePNGImage(Folders.CUSTOMERFOLDER,custBitmap,CUSTOMER_PREFIX+customer_table.getUniqueIdValue());
                FileHelper.createfile(Folders.CUSTOMERFOLDER, CUSTOMER_PREFIX+customer_table.getUniqueIdValue(), FileType.PNG);

                if(image!=null)
                {
                    customer_table.setImagePathValue(image.getAbsolutePath());
                }

                if(checkValidation())
                {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {

                        sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
                    }
                    else
                    {
                        sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
                    }
                    sweetAlertDialog.show();

                    if(CustomerTableHelper.insertCustomerData(getContext(), customer_table))
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            sweetAlertDialog.setTitleText(getResources().getString(R.string.customer_added_successfully_marathi));
                            sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                        }
                        else
                        {
                            sweetAlertDialog.setTitleText(getResources().getString(R.string.customer_added_successfully_english));
                            sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                        }

                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                profileImage.setImageResource(R.drawable.user_profile);
                                custBitmap=null;
                                customer_name.setText("");
                                customer_address.setText("");
                                customer_mobileno.setText("");
                                customer_age.setText("");
                                uniqueIdTxt.setText("");
                                aadhar_id.setText("");
                                male.setChecked(false);
                                female.setChecked(false);
                                generateUniqueId();
                                if( getActivity() instanceof CustomerRegistrationActivity)
                                {
                                    CustomerRegistrationActivity.viewPager.setCurrentItem(0);
                                    ((CustomerRegistrationActivity)getActivity()).customerListFragment.setData();

                                }
                                getActivity().onBackPressed();
                      /*          AddKitchenSuitability addKitchenSuitability = AddKitchenSuitability.getInstance(customer_table);
                                fragmentTransaction.replace(R.id.frame_layout, addKitchenSuitability).addToBackStack(null).commit();
*/
                            }
                        });

                    }
                    else
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);

                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            sweetAlertDialog.setTitleText(getResources().getString(R.string.customer_data_add_failed_marathi));
                            sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                        }

                        else
                        {
                            sweetAlertDialog.setTitleText(getResources().getString(R.string.customer_data_add_failed_english));
                            sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                        }
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    }
                }
            }


        });

        ScanByAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getActivity(),AdharScanner.class);
                startActivityForResult(intent, ADHARSCAN);// Activity is started with requestCode 2
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
    }

    private void generateUniqueId()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        uniqueIdTxt.setText(CUSTOMER_PREFIX+datetime);
        String randomNumber= String.valueOf(Utility.generateRandomNumber(getActivity()));

        uniqueIdTxt.setText(CUSTOMER_PREFIX+datetime+"_"+randomNumber);
    }


    private void showPictureDialog()
    {
        //final CharSequence[] options = {"Take Photo","Choose Photo from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_marathi), getResources().getString(R.string.choose_photo_from_gallery_marathi),getResources().getString(R.string.cancel_marathi)};
            builder.setTitle(getResources().getString(R.string.add_photo_marathi));

            builder.setItems(options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;

                            case 1:
                                choosePhotoFromGallary();
                                break;

                            case 2:
                                dialog.dismiss();
                        }
                    }
                });

        }
        else
        {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_english), getResources().getString(R.string.choose_photo_from_gallery_english),getResources().getString(R.string.cancel_english)};
            builder.setTitle(getResources().getString(R.string.add_photo_english));

            builder.setItems(options,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    takePhotoFromCamera();
                                    break;

                                case 1:
                                    choosePhotoFromGallary();
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

    public void choosePhotoFromGallary()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if(requestCode == ADHARSCAN)
        {
            String adharData=data.getStringExtra("MESSAGE");


            //try
            //{

                    parseQRCode(adharData);
                   /* AadhaarCard newCard = new AdharXMLParser().parse(adharData);
                  0  customer_name.setText(newCard.name);
                    customer_address.setText(newCard.getAddress());
                    customer_age.setText(newCard.yob);*/

           // }
            /*catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }*/

            }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);

                   // Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    profileImage.setImageBitmap(bitmap);
                    custBitmap=bitmap;
                   // diagonalView.setImageBitmap(bitmap);
                    //diagonalView.setBitmap(bitmap);

                } catch (IOException e) {
                    //e.printStackTrace();
                   // Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(thumbnail);
            custBitmap=thumbnail;
           // diagonalView.setImageBitmap(thumbnail);
            //diagonalView.setBitmap(thumbnail);
  }
    }

    void parseQRCode(String qrResponse)
    {
        String newWord;
        String substr=qrResponse.substring(0,5);

        if(substr.contains("/"))
        {
            newWord = qrResponse.substring(0,1)+qrResponse.substring(2);
        }
        else
        {
            newWord=qrResponse;
        }

       /*
       </?xml version="1.0" encoding="UTF-8"?>
       <PrintLetterBarcodeData uid="679503416602" name="Gopika Narayan Nikam" gender="FEMALE" yob="1994" co="null" lm="near kumavat mangal karyalay" loc="suyong colony,padampura" vtc="Aurangabad" po="Kranti Chowk" dist="Aurangabad" state="Maharashtra" pc="431005" dob="30-07-1994"/>
       */


       //</?xml version="1.0" encoding="UTF-8"?>
        // <PrintLetterBarcodeData uid="679503416602" name="Gopika Narayan Nikam" gender="FEMALE" yob="1994" co="null" lm="near kumavat mangal karyalay" loc="suyong colony,padampura" vtc="Aurangabad" po="Kranti Chowk" dist="Aurangabad" state="Maharashtra" pc="431005" dob="30-07-1994"/>
       XmlPullParser parser = null;
        InputStream stream = null;
        try {
            parser = Xml.newPullParser();
            stream = new ByteArrayInputStream(newWord.getBytes("UTF-8"));
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            if (!parser.getName().equals("PrintLetterBarcodeData"))
            {
                // not an Aadhaar QR Code
                // mIntentIntegrator.initiateScan();
                return;
            }
            String uid = parser.getAttributeValue(null, "uid");
            String name = parser.getAttributeValue(null, "name");
            String pincode = parser.getAttributeValue(null, "pc");
            String gender = parser.getAttributeValue(null,"gender");
            String year = parser.getAttributeValue(null,"yob");
            String location = parser.getAttributeValue(null,"loc");
            String vtc = parser.getAttributeValue(null,"vtc");
            String houseName = parser.getAttributeValue(null,"co");
            String dist = parser.getAttributeValue(null,"dist");
            String state = parser.getAttributeValue(null,"state");
            String lm = parser.getAttributeValue(null,"lm");

            int currentyear=Calendar.getInstance().get(Calendar.YEAR);
            int age=currentyear-Integer.parseInt(year);

            //Toast.makeText(getActivity(),name,Toast.LENGTH_LONG).show();
            customer_name.setText(name);
            customer_name.setFocusable(false);
            customer_address.setText( houseName +" "+location+" "+lm+" "+ vtc+ " "+ pincode+ ""+dist+" "+state);
            customer_address.setFocusable(false);
            customer_mobileno.setText("");
            customer_age.setText(String.valueOf(age));
            customer_age.setFocusable(false);
            aadhar_id.setText(uid);
            aadhar_id.setFocusable(false);
            if(gender.equalsIgnoreCase("M"))
            {male.setChecked(true);}
        else{
        female.setChecked(true);
    }

        } catch(XmlPullParserException xppe) {

        } catch (IOException ioe) {

        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ioe)
            {


            }

        }
    }

    private void setCustomerData()
    {
        customer_table = new CustomerTable();

        customer_table.setCustomerNameValue(customer_name.getText().toString());
        customer_table.setCustomerAddressValue(customer_address.getText().toString());
        customer_table.setCustomerMobilenoValue(customer_mobileno.getText().toString());
        customer_table.setCustomerAgeValue(customer_age.getText().toString());
        customer_table.setCustomerGenderValue(selected_gender);
        customer_table.setUniqueIdValue(uniqueIdTxt.getText().toString());
        customer_table.setAadharIdValue(aadhar_id.getText().toString());
        customer_table.setCityId(cityid);
        customer_table.setVillageIdValue(villageId);
        customer_table.setVillageNameValue(villageSpinner.getSelectedItem().toString());
        customer_table.setAddedDateValue(getCurrentDateTime());
        customer_table.setUpload_statusValue("0");
        customer_table.setCustomer_added(CustomerTable.LOCAL);
        customer_table.setAddedByIdValue(new PrefManager(getActivity()).getUserId());

        customer_table.setKitchen_added(CustomerTable.NOT_ADDED);
        customer_table.setTeam_added(CustomerTable.NOT_ADDED);
        customer_table.setChulha_photo_added(CustomerTable.NOT_ADDED);
        customer_table.setFiredPhotoAdded(CustomerTable.NOT_ADDED);
        customer_table.setconstructionComplete(CustomerTable.NOT_ADDED);
        customer_table.setPayment_added(CustomerTable.NOT_ADDED);
        customer_table.setPayment_completed(CustomerTable.NOT_ADDED);

    }

    public void setGender()
    {
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    selected_gender = "M";
                } else if (checkedId == R.id.female) {
                    selected_gender = "F";
                }
            }
        });

    }

    private boolean checkValidation()
    {
        boolean response = true;

        if(!Validations.isValidAadharNumber(aadhar_id.getText().toString()))
        {
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {

                aadhar_id.setError(getResources().getString(R.string.please_enter_valid_aadhar_number_marathi));
            }
            else
            {
                aadhar_id.setError(getResources().getString(R.string.please_enter_valid_aadhar_number_english));
            }
            response = false;
        }
        else
        {
            aadhar_id.setError(null);
        }

        if(!Validations.isValidName(customer_name.getText().toString()))
        {
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI))
            {
                customer_name.setError(getResources().getString(R.string.please_enter_customer_name_marathi));
            }
            else
            {
                customer_name.setError(getResources().getString(R.string.please_enter_customer_name_english));
            }
            response = false;
        }
        else
        {
            customer_name.setError(null);
        }

        if (customer_age.getText().toString().trim().length() == 0) {

            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                customer_age.setError(getResources().getString(R.string.please_enter_customer_age_marathi));
            }
            else
            {
                customer_age.setError(getResources().getString(R.string.please_enter_customer_age_english));
            }
                response = false;
        }
        else
        {
            customer_age.setError(null);
        }

        if (customer_address.getText().toString().trim().length() == 0) {

            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                customer_age.setError(getResources().getString(R.string.please_enter_customer_address_marathi));
            }
            else
            {
                customer_age.setError(getResources().getString(R.string.please_enter_customer_address_english));
            }
            response = false;
        } else {
            customer_address.setError(null);
        }


        if(!Validations.isValidPhoneNumber(customer_mobileno.getText().toString()))
        {
            //customer_mobileno.setError("Please Enter Customer Mobile Number");
            if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                customer_mobileno.setError(getResources().getString(R.string.please_enter_valid_mobile_number_marathi));
            }
            else
            {
                customer_mobileno.setError(getResources().getString(R.string.please_enter_valid_mobile_number_english));
            }
            response = false;
        }
        else
        {
            customer_mobileno.setError(null);
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                Toast.makeText(getActivity(),getResources().getString(R.string.please_select_customer_gender_marathi), Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.please_select_customer_gender_english), Toast.LENGTH_SHORT).show();
            }
            response = false;
            // no radio buttons are checked
        } else {
            // one of the radio buttons is checked

        }

/*
        if (citySpinner.getSelectedItem().toString().trim().equalsIgnoreCase("CityTable")) {

            View selectedView = citySpinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                if (citySpinner.getSelectedItemPosition() == 0) {
                    String errorString = "Please Select CityTable";
                    selectedTextView.setError(errorString);

                } else {
                    selectedTextView.setError(null);
                }
            }
            response = false;
        }


        if (villageSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("VillageTable")) {

            View selectedView = villageSpinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                if (villageSpinner.getSelectedItemPosition() == 0) {
                    String errorString = "Please Select VillageTable";
                    selectedTextView.setError(errorString);

                } else {
                    selectedTextView.setError(null);
                }
            }
            response = false;
        }
*/

        return response;
    }


    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);


        int storagePermission = ContextCompat.checkSelfPermission(getActivity(),


                Manifest.permission.READ_EXTERNAL_STORAGE);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }
}
