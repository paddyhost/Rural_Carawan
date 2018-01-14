package com.hatchers.ruralcaravane.kitchen_suitability;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.googlemap.MapViewFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddKitchenAddress extends Fragment implements View.OnClickListener{
    public static final int NEWADDRESS=1,UPDATEADDRESS=1,ADDMOREADDRESS=3;

   FrameLayout frameLayout;
    private int activityOpenFor;
    private  final int  MAP_LOCATION=1;
    private Toolbar toolbar_add_address;
    MapViewFragment map;
    TextView placeNameTxt,addresstxt;
    String state,city,area,postalCode;
    private Button btnpickAddress,btn_saveaddress;
    static double getlatitude =0, getlongitude = 0;

    private CustomerTable customertable;
    private KitchenTable kitchenTable;
    private ImageView backImg;

    public static AddKitchenAddress getInstance(CustomerTable customertable, KitchenTable kitchenTable)
    {
        AddKitchenAddress fragment = new AddKitchenAddress();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
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
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
        }
    }

    public AddKitchenAddress()
    {

    }

    @SuppressLint("ValidFragment")
    public AddKitchenAddress(FrameLayout frameLayout)
    {

        // Required empty public constructor
        this.activityOpenFor=NEWADDRESS;
        this.frameLayout=frameLayout;

    }

    @SuppressLint("ValidFragment")
    public AddKitchenAddress(int activityOpenFor)
    {
        // Required empty public constructor
        this.activityOpenFor=activityOpenFor;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_addnew_address, container, false);
        initialise( rootView );
        clickListners();

        return rootView;
    }


    void initialise(View rootView )
    {
        btnpickAddress=(Button)rootView.findViewById(R.id.btnpickAdress);
        btn_saveaddress=(Button)rootView.findViewById(R.id.btn_saveaddress);
        placeNameTxt=(TextView) rootView.findViewById(R.id.place_name);
        addresstxt=(TextView) rootView.findViewById(R.id.address_txt);
        toolbar_add_address = (Toolbar) rootView.findViewById(R.id.toolbar_add_address);
        backImg = (ImageView)rootView.findViewById(R.id.back_img_address);
        map=new MapViewFragment("maharastra india");
        map.setArguments(getActivity().getIntent().getExtras());

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        btnpickAddress.setOnClickListener(this);
        if(activityOpenFor==ADDMOREADDRESS)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map_layout, map).commit();
        }
        else if(activityOpenFor==NEWADDRESS) {

            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.map_layout, map).commit();
        }


    }

    private void clickListners()
    {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getlatitude!=0 && getlongitude!=0 && addresstxt.getText().toString().trim()!="")
                {
                    kitchenTable.setLatitudeValue(getlatitude+"");
                    kitchenTable.setLongitudeValue(getlongitude+"");
                    kitchenTable.setGeoAddressValue(addresstxt.getText().toString());

                    SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait");
                    sweetAlertDialog.show();

                    if(KitchenTableHelper.updateKitchenData(getContext(),kitchenTable))
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Kitchen Data Update Successfully");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                getActivity().onBackPressed();
                            }
                        });
                    }
                    else
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Kitchen Data Update Failed");
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
                    Toast.makeText(getContext(),"PLease Select Geo Address",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLatitudeLongitude(getlatitude,getlongitude);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {

            case R.id.btnpickAdress:
                Intent intent=new Intent(getActivity(),FindGeoLocationActivity.class);
                startActivity(intent);
                break;
            default:

                break;

        }

    }


    public static void setLatitudeLongitude(double latitude, double longitude)
    {
        getlatitude=latitude;
        getlongitude =longitude;

    }

    private void getLatitudeLongitude(double latitude, double longitude)
    {
        LatLng latLng = new LatLng(latitude,longitude);

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        try {

            List<Address> addresses = null;

            addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0) {
                //Country=addresses.get(0).getCountryName();
                state= addresses.get(0).getAdminArea();
                city=addresses.get(0).getLocality();
                area=addresses.get(0).getFeatureName();
                postalCode=addresses.get(0).getPostalCode();
                addresstxt.setText(area+","+city+",    "+state+","+postalCode);
                // etzipCode.setText(addresses.get(0).getPostalCode());
                //addresstxt.get(0).getAddressLine();
                map=new MapViewFragment(latLng,addresses.get(0).getLocality());
                map.setArguments(getActivity().getIntent().getExtras());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map_layout, map).commit();

                placeNameTxt.setText(addresses.get(0).getFeatureName());
                // addresstxt.setText(addresses.get(0).getAddressLine(0));
                // etLandmark.setText(addresses.get(0).getFeatureName()+" "+ addresses.get(0).getSubLocality());

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
