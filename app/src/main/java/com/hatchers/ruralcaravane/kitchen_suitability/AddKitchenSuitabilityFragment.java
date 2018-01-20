package com.hatchers.ruralcaravane.kitchen_suitability;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.apihelper.WebKitchen_ApiHelper;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.runtime_permissions.RuntimePermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.KITCHEN_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class AddKitchenSuitabilityFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    private int CAMERA = 1;

    Bitmap kitBitmap;
    private Toolbar kitchen_toolbar;
    private Spinner house_type, roof_type;
    private TextInputEditText kitchen_height;
    private ImageView place_image;
    private int RESULT_CANCELED;
    private Button upload;
    KitchenTable kitchen_table;
    FrameLayout layout;
    private GoogleApiClient client;
    private TextView kitchenUniqueIdText;



    public AddKitchenSuitabilityFragment() {
        // Required empty public constructor
    }


    private CustomerTable customertable;
    public static AddKitchenSuitabilityFragment getInstance(CustomerTable customertable)
    {
        AddKitchenSuitabilityFragment fragment = new AddKitchenSuitabilityFragment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            customertable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_kitchen__suitability, container, false);

        initializations(view);
        onclicklisteners();
        generateUniqueId();

        return view;
    }


    private void initializations(View view)
    {

        kitchen_toolbar = (Toolbar) view.findViewById(R.id.kitchen_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(kitchen_toolbar);
        house_type = (Spinner) view.findViewById(R.id.house_type_survey);
        roof_type = (Spinner) view.findViewById(R.id.roof_type);
        kitchen_height = (TextInputEditText) view.findViewById(R.id.kitchen_height);
        place_image = (ImageView) view.findViewById(R.id.placeImage);
        upload = (Button) view.findViewById(R.id.upload);
        kitchenUniqueIdText=(TextView)view.findViewById(R.id.kitchenUniqueIdText);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API).build();

        house_type.setOnItemSelectedListener(this);
        roof_type.setOnItemSelectedListener(this);
        layout = (FrameLayout)view. findViewById(R.id.AddreFramelauout);

        ArrayAdapter<CharSequence> house_survey_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.House_Type, android.R.layout.simple_spinner_item);
        house_survey_adapter.setDropDownViewResource(R.layout.spinner_item);
        house_type.setAdapter(house_survey_adapter);

        ArrayAdapter<CharSequence> roof_type_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Roof_Type, android.R.layout.simple_spinner_item);
        roof_type_adapter.setDropDownViewResource(R.layout.spinner_item);
        roof_type.setAdapter(roof_type_adapter);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }


    }

    private void onclicklisteners()
    {
        kitchen_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        place_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setKitchenData();
                if(checkValidation()) {

                    FileHelper.savePNGImage(Folders.CHULHAFOLDER,kitBitmap,KITCHEN_PREFIX+kitchen_table.getKitchenUniqueIdValue());
                    File image = FileHelper.createfile(Folders.CHULHAFOLDER, KITCHEN_PREFIX+kitchen_table.getKitchenUniqueIdValue(), FileType.PNG);

                    if(image!=null)
                    {
                        kitchen_table.setPlaceImageValue(image.getAbsolutePath());

                        if(image.exists())
                        {
                            SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                                    .setTitleText("Please wait");
                            sweetAlertDialog.show();

                            if(KitchenTableHelper.insertKitchenData(getContext(), kitchen_table))
                            {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("Kitchen Data Added Successfully");
                                sweetAlertDialog.setConfirmText("Ok");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();

                                        house_type.setAdapter(null);
                                        roof_type.setAdapter(null);
                                        kitchen_height.setText("");
                                        place_image.setImageResource(R.mipmap.chullha);
                                        kitBitmap=null;

                                        if(RuntimePermissions.isNetworkConnectionAvailable(getActivity())) {

                                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            AddKitchenAddress addKitchenAddress = AddKitchenAddress.getInstance(kitchen_table);
                                            fragmentTransaction.replace(R.id.frame_layout, addKitchenAddress).commit();
                                        }
                                        else
                                        {
                                            android.support.v7.app.AlertDialog.Builder builder =new android.support.v7.app.AlertDialog.Builder(getActivity());
                                            builder.setTitle("No internet Connection");
                                            builder.setMessage("Please turn on internet connection to continue");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    getActivity().onBackPressed();
                                                }
                                            });
                                            android.support.v7.app.AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    }
                                });
                            }

                            else
                            {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Kitchen Data Add Failed");
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
                            Toast.makeText(getActivity(), "Please Upload Planned Area Image....!", Toast.LENGTH_SHORT).show();

                        }
                    }
                   else
                    {
                        Toast.makeText(getActivity(), "Please Upload Planned Area Image....!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }


    private void setKitchenData()
    {
        kitchen_table = new KitchenTable();
        kitchen_table.setHouse_typeValue(house_type.getSelectedItem().toString());
        kitchen_table.setRoof_typeValue(roof_type.getSelectedItem().toString());
        kitchen_table.setKitchen_heightValue(kitchen_height.getText().toString());
        kitchen_table.setKitchenUniqueIdValue(kitchenUniqueIdText.getText().toString());
        kitchen_table.setCustomer_idValue(customertable.getUniqueIdValue());
        kitchen_table.setAddedDateValue(getCurrentDateTime());
        kitchen_table.setUpload_statusValue("0");
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        kitchen_table.setUploadDateValue(formatter.format(date));

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
        AlertDialog alert = builder.create();
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
            place_image.setImageBitmap(thumbnail);
            kitBitmap=thumbnail;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private boolean checkValidation()
    {
        boolean response = true;

        if (kitchen_height.getText().toString().trim().length() == 0) {
            kitchen_height.setError("Please Enter Kitchen Height");
            response = false;
        } else {
            kitchen_height.setError(null);
        }

        if (house_type.getSelectedItem().toString().trim().equalsIgnoreCase("Please Select House Type")) {

            View selectedView = house_type.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                if (house_type.getSelectedItemPosition() == 0) {
                    String errorString = "Please Select House Type";
                    selectedTextView.setError(errorString);

                } else {
                    selectedTextView.setError(null);
                }
            }
            response = false;
        }


            if (roof_type.getSelectedItem().toString().trim().equalsIgnoreCase("Please Enter Roof Type")) {

               View selectedView1 = roof_type.getSelectedView();
                if (selectedView1 != null && selectedView1 instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView1;
                    if (roof_type.getSelectedItemPosition() == 0) {
                        String errorString = "Please Enter Roof Type";
                        selectedTextView.setError(errorString);

                    } else {
                        selectedTextView.setError(null);
                    }
                }

                response = false;
            }



        return response;
    }

    private void generateUniqueId()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        kitchenUniqueIdText.setText(datetime);
    }


}
