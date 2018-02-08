package com.hatchers.ruralcaravane.construction_team;

import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.scaner.AdharScanner;
import com.hatchers.ruralcaravane.utils.Utility;
import com.hatchers.ruralcaravane.utils.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.CONSTRUCT_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;


public class ConstructionTeamRegistrationFragment extends Fragment {

    private KitchenTable kitchenTable;
    private  FragmentTransaction fragmentTransaction;
    private Toolbar constructionTeamToolbar;
    private Button saveBtn,register_Byscanid;
    private TextInputEditText construction_member_name, construction_member_address,
            construction_member_mobileno, construction_member_age;
    private RadioGroup radioGroupGender;
    private RadioButton male, female;
    private String selectedGender = "";
    PrefManager prefManager;
    private TextView constructionUniqueIdText,constructionRegistrationTxt;
    ConstructionTable constructionTable;
    private int SCAN_ID=4;
    CustomerTable customerTable;


    public static ConstructionTeamRegistrationFragment getInstance(KitchenTable kitchenTable,CustomerTable customerTable)
    {
        ConstructionTeamRegistrationFragment fragment = new ConstructionTeamRegistrationFragment();
        Bundle args = new Bundle();
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
        args.putParcelable(CustomerTable.CUSTOMER_TABLE,customerTable);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
            customerTable=getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }
    }

    public ConstructionTeamRegistrationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_construction_team_details, container, false);

        initializations(view);
        setLanguageToUI();
        onclicklisteners();
        setGender();


        return view;
    }


    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            constructionTeamToolbar.setTitle(R.string.construction_team_information_marathi);

            register_Byscanid.setText(getResources().getString(R.string.scan_by_id_card));
            register_Byscanid.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

            construction_member_name.setHint(getResources().getString(R.string.construction_team_member_name));
            construction_member_name.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            construction_member_name.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            construction_member_address.setHint(getResources().getString(R.string.construction_team_member_address));
            construction_member_address.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            construction_member_address.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            construction_member_age.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            construction_member_age.setHint(getResources().getString(R.string.construction_team_member_age));
            construction_member_age.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            construction_member_mobileno.setHint(getResources().getString(R.string.construction_team_member_mobile));
            construction_member_mobileno.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            construction_member_mobileno.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            male.setText(getResources().getString(R.string.male));
            male.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            female.setText(getResources().getString(R.string.female));
            female.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            saveBtn.setText(getResources().getString(R.string.save));
            saveBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

            constructionRegistrationTxt.setText(getResources().getString(R.string.construction_team_member_marathi));
            constructionRegistrationTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

        }
        else
        {
            constructionTeamToolbar.setTitle(R.string.construction_team_information_english);

            register_Byscanid.setText(getResources().getString(R.string.scan_by_id_card1));
            register_Byscanid.setTextSize(Utility.getConvertFloatToDP(getActivity(),10));

            construction_member_name.setHint(getResources().getString(R.string.construction_team_member_name1));
            construction_member_name.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            construction_member_name.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            construction_member_address.setHint(getResources().getString(R.string.construction_team_member_address1));
            construction_member_address.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            construction_member_address.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            construction_member_age.setHintTextColor(getResources().getColor(R.color.DarkGrey));
            construction_member_age.setHint(getResources().getString(R.string.construction_team_member_age1));
            construction_member_age.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            construction_member_mobileno.setHint(getResources().getString(R.string.construction_team_member_mobile1));
            construction_member_mobileno.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            construction_member_mobileno.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            male.setText(getResources().getString(R.string.male1));
            male.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            female.setText(getResources().getString(R.string.female1));
            female.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            saveBtn.setText(getResources().getString(R.string.save1));
            saveBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),10));

        }
    }

    private void initializations(View view)
    {
        ((AppCompatActivity) getActivity()).setSupportActionBar(constructionTeamToolbar);

        prefManager=new PrefManager(getActivity());
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        constructionTeamToolbar = (Toolbar) view.findViewById(R.id.construction_team_toolbar);


        register_Byscanid=(Button)view.findViewById(R.id.register_Byscanid);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        construction_member_name = (TextInputEditText) view.findViewById(R.id.construction_member_name);
        construction_member_address = (TextInputEditText) view.findViewById(R.id.construction_member_address);
        construction_member_mobileno = (TextInputEditText) view.findViewById(R.id.construction_member_mobileno);
        construction_member_age = (TextInputEditText) view.findViewById(R.id.construction_member_age);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.radio_gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        constructionRegistrationTxt=(TextView)view.findViewById(R.id.constructionRegistration_txt);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }

    }

    private void onclicklisteners()
    {

        constructionTeamToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConstructionTeamData();
                if (checkValidation()) {
                    SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait");

                    sweetAlertDialog.show();
                    if(ConstructionTableHelper.insertConstructionTeamData(getContext(), constructionTable))
                    {
                        kitchenTable.setUpload_statusValue(KitchenTable.TEAM_ADDED_LOCAL);
                        KitchenTableHelper.updateKitchenData(getActivity(),kitchenTable);
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Construction Team Data Added Successfully");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                                construction_member_name.setText("");
                                construction_member_address.setText("");
                                construction_member_mobileno.setText("");
                                construction_member_age.setText("");
                                male.setChecked(false);
                                female.setChecked(false);

                                customerTable.setUpload_statusValue("4");
                                customerTable.setTeam_added(CustomerTable.LOCAL);
                                CustomerTableHelper.updateCustomerData(getContext(),customerTable);

                                getActivity().onBackPressed();

                            }
                        });
                    }

                    else
                    {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Construction Team Data Failed");
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

        register_Byscanid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AdharScanner.class);
                startActivityForResult(intent, SCAN_ID);// Activity is started with requestCode 2
            }
        });

    }

    private void setConstructionTeamData()
    {
        constructionTable = new ConstructionTable();

        constructionTable.setTechnicianNameValue(construction_member_name.getText().toString());
        constructionTable.setTechnicianAddressValue(construction_member_address.getText().toString());
        constructionTable.setTechnicianAgeValue(construction_member_age.getText().toString());
        constructionTable.setTechnicianMobileNoValue(construction_member_mobileno.getText().toString());
        constructionTable.setTechnicianGenderValue(selectedGender);
        constructionTable.setAddedDateValue(getCurrentDateTime());
        constructionTable.setUpdateDateValue(getCurrentDateTime());
        constructionTable.setKitchentUniqueId(kitchenTable.getKitchenUniqueIdValue());
        constructionTable.setKitchenIdValue(kitchenTable.getKitchen_idValue());
        constructionTable.setCustomerIdValue(kitchenTable.getCustomer_idValue());
        constructionTable.setUploadStatusValue(ConstructionTable.TEAM_ADDED_LOCAL);
        constructionTable.setTechnicianUniqueIdValue(generateUniqueId());
        constructionTable.setAddedByIdValue(new PrefManager(getActivity()).getUserId());
    }

    private String generateUniqueId()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        String uniqueId = CONSTRUCT_PREFIX+datetime;

        return uniqueId;
    }

    public void setGender()
    {
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    selectedGender = "M";
                } else if (checkedId == R.id.female) {
                    selectedGender = "F";
                }
            }
        });

    }

    private boolean checkValidation()
    {
        boolean response = true;

        if (construction_member_name.getText().toString().trim().length() == 0) {
            construction_member_name.setError("Please Enter Construction Member Name");
            response = false;
        } else {
            construction_member_name.setError(null);
        }

        if (construction_member_age.getText().toString().trim().length() == 0) {
            construction_member_age.setError("Please Enter Construction Member Age ");
            response = false;
        } else {
            construction_member_age.setError(null);
        }

        if (construction_member_address.getText().toString().trim().length() == 0) {
            construction_member_address.setError("Please Enter Construction Member Address ");
            response = false;
        } else {
            construction_member_address.setError(null);
        }

        if(!Validations.isValidPhoneNumber(construction_member_mobileno.getText().toString()))
        {
            construction_member_mobileno.setError("Please Enter valid Mobile Number");
            response = false;
        }

        else
        {
            construction_member_mobileno.setError(null);
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
            response = false;
            // no radio buttons are checked
        } else {
            // one of the radio buttons is checked

        }
        return response;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == SCAN_ID) {
            String scanData = data.getStringExtra("MESSAGE");

            /*   */
            try {
                JSONObject object = new JSONObject(scanData);
                construction_member_name.setText(object.getString("name"));
                construction_member_name.setFocusable(false);
                construction_member_address.setText(object.getString("address"));
                construction_member_address.setFocusable(false);
                construction_member_age.setText(object.getString("age"));
                construction_member_age.setFocusable(false);
                construction_member_mobileno.setText(object.getString("mobile"));
                construction_member_mobileno.setFocusable(false);
                selectedGender = object.getString("gender");
                if(selectedGender.equalsIgnoreCase("F"))
                {
                    female.setChecked(true);
                }
                else
                {
                    male.setChecked(true);
                }
                female.setClickable(false);
                male.setClickable(false);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}


