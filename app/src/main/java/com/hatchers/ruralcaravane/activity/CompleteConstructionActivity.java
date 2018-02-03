package com.hatchers.ruralcaravane.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.construction_team.ConstructionFragment;
import com.hatchers.ruralcaravane.construction_team.ConstructionTeamRegistrationFragment;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.AddKitchenSuitabilityFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.KitchenConstructionFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsFragment;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.io.File;
import java.util.ArrayList;

import static com.hatchers.ruralcaravane.constants.AppConstants.STEP2_PREFIX;
import static com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper.getKitchenDetailsData;

public class CompleteConstructionActivity extends AppCompatActivity{


    private Toolbar completeConstructionToolbar;
    private PrefManager prefManager;
    private Button btn_makeConstructionTeam,btn_uploadChullahaPhotos,btn_doPayment,btnAddKitchen;
    private KitchenTable kitchenTable;
    private PaymentTable paymentTable;
    private CustomerTable customerTable;
    private ConstructionTable constructionTable;
    private FragmentTransaction fragmentTransaction;
    ArrayList<ConstructionTable> constructionTableArrayList;
    ArrayList<KitchenTable> kitchenTableArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_construction);

        customerTable=getIntent().getParcelableExtra(CustomerTable.CUSTOMER_TABLE);
        kitchenTable=KitchenTableHelper.getKitchenDetailsData(this,customerTable.getUniqueIdValue());
        initializations();
        setLanguageToUI();
        onClickListeners();
        addKitchenClickListener();


        completeConstructionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializations()
    {
        completeConstructionToolbar=(Toolbar)findViewById(R.id.complete_construction_toolbar);
        prefManager=new PrefManager(this);
        btn_makeConstructionTeam=(Button)findViewById(R.id.make_construction_team);
        btn_uploadChullahaPhotos=(Button)findViewById(R.id.upload_chullaha_photos);
        btn_doPayment=(Button)findViewById(R.id.do_payment);
        btnAddKitchen=(Button)findViewById(R.id.btn_add_kitchen);


        kitchenTableArrayList=KitchenTableHelper.getKitchenDataList(CompleteConstructionActivity.this,customerTable);

        if (kitchenTableArrayList != null)
        {
            if(kitchenTableArrayList.size()<=0)
            {
                btnAddKitchen.setVisibility(View.VISIBLE);
            }
            else
            {
                btnAddKitchen.setVisibility(View.GONE);
                
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        kitchenTableArrayList=KitchenTableHelper.getKitchenDataList(CompleteConstructionActivity.this,customerTable);

        if (kitchenTableArrayList != null)
        {
            if(kitchenTableArrayList.size()<=0)
            {
                btnAddKitchen.setVisibility(View.VISIBLE);
            }
            else
            {
                btnAddKitchen.setVisibility(View.GONE);
            }
        }

    }

    private void addKitchenClickListener()
    {
        btnAddKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                AddKitchenSuitabilityFragment addKitchenSuitabilityFragment = AddKitchenSuitabilityFragment.getInstance(customerTable);
                fragmentTransaction.replace(R.id.complete_construction_frame, addKitchenSuitabilityFragment).addToBackStack(null).commit();
            }
        });
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            completeConstructionToolbar.setTitle(getResources().getString(R.string.complete_remaining_construction_marathi));

            btn_makeConstructionTeam.setText(getResources().getString(R.string.make_construction_team_marathi));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            btn_uploadChullahaPhotos.setText(getResources().getString(R.string.upload_kitchen_photo_marathi));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            btn_doPayment.setText(getResources().getString(R.string.do_payment_marathi));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            btnAddKitchen.setText(getResources().getString(R.string.house_survey_marathi));
            btnAddKitchen.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

        }
        else
        {
            completeConstructionToolbar.setTitle(getResources().getString(R.string.complete_remaining_construction_english));

            btn_makeConstructionTeam.setText(getResources().getString(R.string.make_construction_team_english));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            btn_uploadChullahaPhotos.setText(getResources().getString(R.string.upload_kitchen_photo_english));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            btn_doPayment.setText(getResources().getString(R.string.do_payment_english));
            btn_makeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            btnAddKitchen.setText(getResources().getString(R.string.house_survey_english));
            btnAddKitchen.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));
        }
    }

    private void onClickListeners()
    {
        btn_doPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                PaymentDetailsFragment paymentDetailsFragment = PaymentDetailsFragment.getInstance(customerTable,paymentTable);
                fragmentTransaction.replace(R.id.complete_construction_frame, paymentDetailsFragment).addToBackStack(null).commit();
            }
        });


        btn_uploadChullahaPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConstructionTableHelper.getConstructionTeamList(CompleteConstructionActivity.this,customerTable).size()>0) {

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    KitchenConstructionFragment kitchenConstructionFragment = KitchenConstructionFragment.getInstance(kitchenTable);
                    fragmentTransaction.replace(R.id.complete_construction_frame, kitchenConstructionFragment).addToBackStack(null).commit();
                }
                else
                {
                    Toast.makeText(CompleteConstructionActivity.this, "Please add atleast 1 team member", Toast.LENGTH_SHORT).show();
                }

                /*if (constructionTableArrayList!=null && constructionTableArrayList.size() > 0) {
                    KitchenTable kitchenTableArrayList = KitchenTableHelper.getKitchenDetailsData(CompleteConstructionActivity.this, customerTable.getUniqueIdValue());

                    if (kitchenTableArrayList != null) {
                        if (kitchenTableArrayList == null) {
                            //AddKitchenSuitabilityFragment addKitchenSuitabilityFragment = AddKitchenSuitabilityFragment.getInstance(customertable);
                            //fragmentTransaction.replace(R.id.frame_layout, addKitchenSuitabilityFragment).addToBackStack(null).commit();
                       } else {

                        }
                    }
                }
                else
                {
                    Toast.makeText(CompleteConstructionActivity.this, "Please add atleast 1 team member", Toast.LENGTH_SHORT).show();
                }*/
            }
        });


        btn_makeConstructionTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (kitchenTableArrayList != null)
                {
                    if(kitchenTableArrayList.size()<=0)
                    {
                        Toast.makeText(CompleteConstructionActivity.this,"Add kitchen first",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                        if (image != null) {
                            if (!image.exists()) {
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                ConstructionFragment constructionFragment = ConstructionFragment.getInstance(kitchenTable,customerTable);
                                fragmentTransaction.replace(R.id.complete_construction_frame, constructionFragment).addToBackStack(null).commit();

                            } else
                            {
                                Toast.makeText(CompleteConstructionActivity.this, "Process Completed. Can't add team member ", Toast.LENGTH_SHORT).show();
                                btn_makeConstructionTeam.setBackgroundColor(getResources().getColor(R.color.colorDarkgray));
                            }
                        }
                    }
                }

            }
        });
    }


}
