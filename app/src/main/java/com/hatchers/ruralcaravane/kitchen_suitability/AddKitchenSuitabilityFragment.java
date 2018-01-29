package com.hatchers.ruralcaravane.kitchen_suitability;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.KITCHEN_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class AddKitchenSuitabilityFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private int CAMERA = 1;
    Bitmap kitBitmap;
    private Toolbar kitchen_toolbar;
    private Spinner house_type, roof_type;
    private TextInputEditText kitchen_height;
    private ImageView place_image;
    private Button upload;
    KitchenTable kitchen_table;
    private TextView kitchenUniqueIdText,uniqueId_Txt,text1;
    ArrayAdapter<CharSequence> house_survey_adapter,roof_type_adapter;
    private Button btnGetLocation;
    private CustomerTable customertable;

    private double lattitude, longitude;
    //get current location
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener locationListener;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;
    private PrefManager prefManager;


    public AddKitchenSuitabilityFragment()
    {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_kitchen__suitability, container, false);

        initializations(view);
        setLanguageToUI();
        toolbarClickListener();
        placeImageClickListener();
        saveKitchenClickListener();
        getLocationClickListener();
        generateUniqueId();

        return view;
    }


    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            kitchen_toolbar.setTitle(getResources().getString(R.string.kitchen_information));

            uniqueId_Txt.setText(getResources().getString(R.string.unique_id));
            uniqueId_Txt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            kitchenUniqueIdText.setText(getResources().getString(R.string.unique_id));
            kitchenUniqueIdText.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            kitchen_height.setHint(getResources().getString(R.string.kitchen_height));
            kitchen_height.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            kitchen_height.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            btnGetLocation.setText(getResources().getString(R.string.get_geo_location));
            btnGetLocation.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            text1.setText(getResources().getString(R.string.take_image_of_planned_area));
            text1.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            setHouseTypeSpinner(R.array.House_Type_Marathi);

            setRoof_typeSpinner(R.array.Roof_Type_Marathi);

            upload.setText(getResources().getString(R.string.save));
            upload.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


        }
        else
        {
            kitchen_toolbar.setTitle(getResources().getString(R.string.kitchen_information1));

            uniqueId_Txt.setText(getResources().getString(R.string.unique_id1));
            uniqueId_Txt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            kitchenUniqueIdText.setText(getResources().getString(R.string.unique_id1));
            kitchenUniqueIdText.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            kitchen_height.setHint(getResources().getString(R.string.kitchen_height1));
            kitchen_height.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            kitchen_height.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            btnGetLocation.setText(getResources().getString(R.string.get_geo_location1));
            btnGetLocation.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            text1.setText(getResources().getString(R.string.take_image_of_planned_area1));
            text1.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            upload.setText(getResources().getString(R.string.save1));
            upload.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            setHouseTypeSpinner(R.array.House_Type_English);

            setRoof_typeSpinner(R.array.Roof_Type_English);
        }
    }

    private void initializations(View view)
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        kitchen_table = new KitchenTable();

        prefManager = new PrefManager(getActivity());
        kitchen_toolbar = (Toolbar) view.findViewById(R.id.kitchen_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(kitchen_toolbar);
        house_type = (Spinner) view.findViewById(R.id.house_type_survey);
        roof_type = (Spinner) view.findViewById(R.id.roof_type);
        kitchen_height = (TextInputEditText) view.findViewById(R.id.kitchen_height);
        place_image = (ImageView) view.findViewById(R.id.placeImage);
        upload = (Button) view.findViewById(R.id.upload);
        kitchenUniqueIdText=(TextView)view.findViewById(R.id.kitchenUniqueIdText);
        btnGetLocation = (Button)view.findViewById(R.id.get_location);
        uniqueId_Txt=(TextView)view.findViewById(R.id.uniqueIdTxt);
        text1=(TextView)view.findViewById(R.id.text1);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void getCurrentLocation()
    {
        checkLocationPermission();

        toggleGPSUpdates();

        setLocationListner();
    }

    private void setHouseTypeSpinner(int houseTypeArray)
    {
        house_type.setOnItemSelectedListener(this);

        house_survey_adapter = ArrayAdapter.createFromResource(getContext(),
                houseTypeArray, android.R.layout.simple_spinner_item);
        house_survey_adapter.setDropDownViewResource(R.layout.spinner_item);
        house_type.setAdapter(house_survey_adapter);
    }

    private void setRoof_typeSpinner(int roofTypeArray)
    {
        roof_type.setOnItemSelectedListener(this);

        roof_type_adapter = ArrayAdapter.createFromResource(getContext(),
                roofTypeArray, android.R.layout.simple_spinner_item);
        roof_type_adapter.setDropDownViewResource(R.layout.spinner_item);
        roof_type.setAdapter(roof_type_adapter);
    }

    private void toolbarClickListener()
    {
        kitchen_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void placeImageClickListener()
    {
        place_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    private void saveKitchenClickListener()
    {
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

                                        getActivity().onBackPressed();

                                        /*if(RuntimePermissions.isNetworkConnectionAvailable(getActivity())) {

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
                                        }*/

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

    private void getLocationClickListener()
    {
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getCurrentLocation();

                SweetAlertDialog locationDialog;
                if(lattitude==0.0 || longitude ==0.0)
                {
                    locationDialog =new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Couldn't get Location")
                            .setContentText("Please try again...!");
                    locationDialog.setCancelable(false);
                    locationDialog.show();
                    locationDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                }
                else
                {
                    locationDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("Got location successfully !");
                    locationDialog.setCancelable(false);
                    locationDialog.show();
                    locationDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            kitchen_table.setLatitudeValue(String.valueOf(lattitude));
                            kitchen_table.setLongitudeValue(String.valueOf(longitude));
                        }
                    });

                    Toast.makeText(getContext(), "Lattitude : " + String.valueOf(lattitude) + "\nLongitude : " + String.valueOf(longitude), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setKitchenData()
    {
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
        if (resultCode == Activity.RESULT_CANCELED) {
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
        kitchenUniqueIdText.setText(KITCHEN_PREFIX+datetime);
    }

    public void toggleGPSUpdates()
    {
        if (checkLocation())
        {
            buildGoogleApiClient();
        }
    }

    private boolean checkLocation()
    {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Unable to get Location")
                .setMessage("Your Location is disabled. Please Enable Location Services.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });

        dialog.show();
    }

    private boolean isLocationEnabled()
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected( Bundle bundle)
    {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,locationListener);
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,locationListener);
    }

    private void setLocationListner()
    {
        locationListener =new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
               // edtAddress.setText(getAddress(lattitude,longitude));
                kitchen_table.setGeoAddressValue(getAddress(lattitude,longitude));
            }
        };
    }

    /*
    * onStart : Called when the activity is becoming visible to the user.
    * */
    @Override
    public void onStart()
    {
        //EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    /* * onStop : Called when the activity is no longer visible to the user
        * */
    @Override
    public void onStop()
    {
        // EventBus.getDefault().unregister(this);
        super.onStop();
        //Disconnect the google client api connection.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            stopLocationUpdates();
        }
    }

    /*
    * onPause : Called when the system is about to start resuming a previous activity.
    * */
    @Override
    public void onPause()
    {
        try {

            super.onPause();

            /*
            * Stop retrieving locations when we go out of the application.
            * */
            if (mGoogleApiClient != null) {
                stopLocationUpdates();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
    }

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    public String getAddress(double lat, double lng)
    {
        try {
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            String add = "";
            if(addresses!=null) {

                if (addresses.size() > 0) {
                    Address obj = addresses.get(0);
                    add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();
                    Log.d("Area", add);
                }
            }
            return add;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            //   Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }


}
