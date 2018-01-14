package com.hatchers.ruralcaravane.customer_registration;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.apihelper.WebCustomer_ApiHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.customer_registration.listener.CityListner;
import com.hatchers.ruralcaravane.customer_registration.listener.VillageListner;
import com.hatchers.ruralcaravane.customer_registration.model.City;
import com.hatchers.ruralcaravane.customer_registration.model.CityVillageList;
import com.hatchers.ruralcaravane.customer_registration.model.Village;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.scaner.AdharScanner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.sax.SAXResult;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextView uniqueIdTxt;
    private CircleImageView profileImage;
    private TextInputEditText customer_name, customer_address, customer_mobileno, customer_age,aadhar_id;
    private Spinner citySpinner, villageSpinner;
    private ArrayList<City> cityArrayList;
    private ArrayList<Village> villageArrayList;
    private String villageId,cityid;
    private int RESULT_CANCELED;


    public AddCustomerFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add__customer, container, false);

        //call city list api
        CityVillageList cityList=new CityVillageList();
        WebCustomer_ApiHelper.getCityList(getActivity(),cityList);
        setCityEvent(cityList);
        initializations(view);
        generateUniqueId();
        onclicklisteners();
        setCitySpinnerList();
        citySelectedListner();
        setVillageSpinnerList();
        villageSelectedListner();
        setGender();

        return view;
    }

    private void initializations(View view)
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        cityArrayList =new ArrayList<City>();
        villageArrayList = new ArrayList<Village>();
        save = (Button) view.findViewById(R.id.saveBtn);
        customer_name = (TextInputEditText) view.findViewById(R.id.customer_name);
        customer_address = (TextInputEditText) view.findViewById(R.id.customer_address);
        customer_mobileno = (TextInputEditText) view.findViewById(R.id.customer_mobileno);
        customer_age = (TextInputEditText) view.findViewById(R.id.customer_age_txt);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.radio_gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        profileImage=(CircleImageView) view.findViewById(R.id.profileImage);
        uniqueIdTxt=(TextView)view.findViewById(R.id.uniqueIdTxt);
        ScanByAadhar=(Button)view.findViewById(R.id.ScanByAadhar);
        citySpinner = (Spinner)view.findViewById(R.id.city_spinner);
        villageSpinner = (Spinner)view.findViewById(R.id.village_spinner);
        aadhar_id=(TextInputEditText)view.findViewById(R.id.aadhar_id);
    }

    private void setCitySpinnerList()
    {
        ArrayAdapter<City> adapter =
                new ArrayAdapter<City>(getActivity(), R.layout.spinner_item2, cityArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        citySpinner.setAdapter(adapter);

    }

    private void citySelectedListner()
    {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                City city = cityArrayList.get(position);
                //call village list api
                CityVillageList cityVillageList=new CityVillageList();
                WebCustomer_ApiHelper.getVillageList(getActivity(),cityVillageList,city.getId());
                setVillageEvent(cityVillageList);
                citySpinner.setSelection(position);
                cityid=city.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void villageSelectedListner()
    {
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Village village=villageArrayList.get(i);
                villageId=village.getId();
                villageSpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setVillageSpinnerList()
    {
        ArrayAdapter<Village> adapter =
                new ArrayAdapter<Village>(getActivity(), R.layout.spinner_item2, villageArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        villageSpinner.setAdapter(adapter);
    }

    private void setCityEvent(final CityVillageList cityList)
    {
        cityList.setOnCityEvent(new CityListner() {
            @Override
            public void onCity_Add_Success()
            {
                cityArrayList = cityList.getCityListArrayList();
                setCitySpinnerList();
            }

            @Override
            public void onCity_Add_Failed() {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Response_Failed() {
                Toast.makeText(getActivity(),"Response Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Json_Error() {
                Toast.makeText(getActivity(),"Json Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_No_Connection_Error() {
                Toast.makeText(getActivity(),"Check net connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Server_Error() {
                Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Network_Error() {
                Toast.makeText(getActivity(),"Network error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Parse_Error() {
                Toast.makeText(getActivity(),"Parse error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Unknown_Error() {
                Toast.makeText(getActivity(),"Unknown Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVillageEvent(final CityVillageList villageList)
    {
        villageList.setOnVillageEvent(new VillageListner() {
            @Override
            public void onVillage_Add_Success() {
                villageArrayList = villageList.getVillageArrayList();
                setVillageSpinnerList();
            }

            @Override
            public void onVillage_Add_Failed() {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Response_Failed() {
                Toast.makeText(getActivity(),"Response Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Json_Error() {
                Toast.makeText(getActivity(),"Json Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_No_Connection_Error() {
                Toast.makeText(getActivity(),"Check net Connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Server_Error() {
                Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Network_Error() {
                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Parse_Error() {
                Toast.makeText(getActivity(),"Parse Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Unknown_Error() {
                Toast.makeText(getActivity(),"Unknown Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onclicklisteners()
    {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomerData();
                if(checkValidation()) {
                    SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait");
                    sweetAlertDialog.show();

                    FileHelper.savePNGImage(Folders.CUSTOMERFOLDER,custBitmap,customer_table.getImagePathValue());

                    if(CustomerTableHelper.insertCustomerData(getContext(), customer_table))
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Customer Data Added Successfully");
                        sweetAlertDialog.setConfirmText("Ok");
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
                                if( getActivity() instanceof CustomerRegistrationActivity) {
                                    ((CustomerRegistrationActivity) getActivity()).viewPager.setCurrentItem(0);
                                }

                            }
                        });

                    }
                    else
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Customer Data Add Failed");
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
        uniqueIdTxt.setText("CU"+datetime);
    }

    private void showPictureDialog()
    {
        final CharSequence[] options = {"Take Photo","Choose Photo from Gallery", "Cancel"};

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

                            case 1:
                                choosePhotoFromGallary();
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
        //customer_table.setVillageNameValue(villageSpinner.getSelectedItem().toString());
        customer_table.setCustomerAddressValue(customer_address.getText().toString());
        customer_table.setCustomerMobilenoValue(customer_mobileno.getText().toString());
        customer_table.setCustomerAgeValue(customer_age.getText().toString());
        customer_table.setCustomerGenderValue(selected_gender);
        customer_table.setUniqueIdValue(uniqueIdTxt.getText().toString());
        customer_table.setAadharIdValue(aadhar_id.getText().toString());
        customer_table.setImagePathValue("CU_"+customer_table.getUniqueIdValue());
        customer_table.setCityId(cityid);
        customer_table.setVillageIdValue(villageId);
        customer_table.setAddedDateValue(getCurrentDateTime());
        customer_table.setUpload_statusValue("0");

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

        if (aadhar_id.getText().toString().trim().length() == 0) {
            aadhar_id.setError("Please Enter Aadhar Number");
            response = false;
        } else {
            aadhar_id.setError(null);
        }

        if (customer_name.getText().toString().trim().length() == 0) {
            customer_name.setError("Please Enter Customer Name");
            response = false;
        } else {
            customer_name.setError(null);
        }

        if (customer_age.getText().toString().trim().length() == 0) {
            customer_age.setError("Please Enter Customer Age ");
            response = false;
        } else {
            customer_age.setError(null);
        }

        if (customer_address.getText().toString().trim().length() == 0) {
            customer_address.setError("Please Enter Customer Address ");
            response = false;
        } else {
            customer_address.setError(null);
        }

        if (customer_mobileno.getText().toString().trim().length() == 0) {
            customer_mobileno.setError("Please Enter Customer Mobile Number");
            response = false;
        } else {
            customer_mobileno.setError(null);
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
            response = false;
            // no radio buttons are checked
        } else {
            // one of the radio buttons is checked

        }

/*
        if (citySpinner.getSelectedItem().toString().trim().equalsIgnoreCase("City")) {

            View selectedView = citySpinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                if (citySpinner.getSelectedItemPosition() == 0) {
                    String errorString = "Please Select City";
                    selectedTextView.setError(errorString);

                } else {
                    selectedTextView.setError(null);
                }
            }
            response = false;
        }


        if (villageSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Village")) {

            View selectedView = villageSpinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                if (villageSpinner.getSelectedItemPosition() == 0) {
                    String errorString = "Please Select Village";
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
}
