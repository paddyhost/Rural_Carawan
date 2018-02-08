package com.hatchers.ruralcaravane.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.construction_team.ConstructionFragment;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.AddKitchenSuitability;
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

public class CompleteConstructionActivity extends AppCompatActivity{


    private Toolbar completeConstructionToolbar;
    private PrefManager prefManager;
    private TextView txtMakeConstructionTeam,txtUploadChullahaPhotos,btn_doPayment,txtAddKitchen;
    private KitchenTable kitchenTable;
    private PaymentTable paymentTable;
    private CustomerTable customerTable;
    private ConstructionTable constructionTable;
    private FragmentTransaction fragmentTransaction;
    ArrayList<ConstructionTable> constructionTableArrayList;
    ArrayList<KitchenTable> kitchenTableArrayList;
    private LinearLayout houseSurveyLayout,addConstructionTeamLayout,uploadChullhaPhotosLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_construction);

        customerTable=getIntent().getParcelableExtra(CustomerTable.CUSTOMER_TABLE);
        kitchenTable=KitchenTableHelper.getKitchenDetailsData(this,customerTable.getUniqueIdValue());
        initializations();
        setLanguageToUI();

        addKitchenClickListener();
        doPaymentClickListener();
        chulhaPhotosClickListener();
        makeConstructionTeamClickListener();

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
        txtMakeConstructionTeam=(TextView)findViewById(R.id.txt_make_construction_team);
        txtUploadChullahaPhotos=(TextView)findViewById(R.id.txt_upload_chullaha_photos);
        btn_doPayment=(Button)findViewById(R.id.do_payment);
        txtAddKitchen=(TextView)findViewById(R.id.txt_add_kitchen);
        houseSurveyLayout=(LinearLayout)findViewById(R.id.house_survey_layout);
        addConstructionTeamLayout=(LinearLayout)findViewById(R.id.add_construction_team_layout);
        uploadChullhaPhotosLayout=(LinearLayout)findViewById(R.id.upload_chullha_photos_layout);


        kitchenTableArrayList=KitchenTableHelper.getKitchenDataList(CompleteConstructionActivity.this,customerTable);

        if (kitchenTableArrayList != null)
        {
            if(kitchenTableArrayList.size()<=0)
            {
                houseSurveyLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                houseSurveyLayout.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));
            }
        }
    }

    private void addKitchenClickListener()
    {
        houseSurveyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kitchenTableArrayList=KitchenTableHelper.getKitchenDataList(CompleteConstructionActivity.this,customerTable);

                if (kitchenTableArrayList != null)
                {
                    if(kitchenTableArrayList.size()<=0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //AddKitchenSuitabilityFragment addKitchenSuitabilityFragment = AddKitchenSuitabilityFragment.getInstance(customerTable);
                        AddKitchenSuitability addKitchenSuitability=AddKitchenSuitability.getInstance(customerTable);
                        fragmentTransaction.replace(R.id.complete_construction_frame, addKitchenSuitability).addToBackStack(null).commit();
                        //houseSurveyLayout.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));
                    }
                    else
                    {
                        houseSurveyLayout.setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));
                        Toast.makeText(CompleteConstructionActivity.this,"Already added kitchen",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            completeConstructionToolbar.setTitle(getResources().getString(R.string.complete_remaining_construction_marathi));

            txtMakeConstructionTeam.setText(getResources().getString(R.string.make_construction_team_marathi));
            txtMakeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            txtUploadChullahaPhotos.setText(getResources().getString(R.string.upload_kitchen_photo_marathi));
            txtUploadChullahaPhotos.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            btn_doPayment.setText(getResources().getString(R.string.do_payment_marathi));
            btn_doPayment.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

            txtAddKitchen.setText(getResources().getString(R.string.house_survey_marathi));
            txtAddKitchen.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,15));

        }
        else
        {
            completeConstructionToolbar.setTitle(getResources().getString(R.string.complete_remaining_construction_english));

            txtMakeConstructionTeam.setText(getResources().getString(R.string.make_construction_team_english));
            txtMakeConstructionTeam.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            txtUploadChullahaPhotos.setText(getResources().getString(R.string.upload_kitchen_photo_english));
            txtUploadChullahaPhotos.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            btn_doPayment.setText(getResources().getString(R.string.do_payment_english));
            btn_doPayment.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));

            txtAddKitchen.setText(getResources().getString(R.string.house_survey_english));
            txtAddKitchen.setTextSize(Utility.getConvertFloatToDP(CompleteConstructionActivity.this,12));
        }
    }

    private void doPaymentClickListener()
    {
        btn_doPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                PaymentDetailsFragment paymentDetailsFragment = PaymentDetailsFragment.getInstance(customerTable,paymentTable);
                fragmentTransaction.replace(R.id.complete_construction_frame, paymentDetailsFragment).addToBackStack(null).commit();
            }
        });


        uploadChullhaPhotosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConstructionTableHelper.getConstructionTeamList(CompleteConstructionActivity.this,customerTable).size()>0) {

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    KitchenConstructionFragment kitchenConstructionFragment = KitchenConstructionFragment.getInstance(kitchenTable,customerTable);
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
                            //AddKitchenSuitability addKitchenSuitabilityFragment = AddKitchenSuitability.getInstance(customertable);
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


        addConstructionTeamLayout.setOnClickListener(new View.OnClickListener() {
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
                                addConstructionTeamLayout.setBackgroundColor(getResources().getColor(R.color.colorDarkgray));
                            }
                        }
                    }
                }

            }
        });
    }

    private void chulhaPhotosClickListener()
    {
        uploadChullhaPhotosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConstructionTableHelper.getConstructionTeamList(CompleteConstructionActivity.this,customerTable).size()>0) {

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    KitchenConstructionFragment kitchenConstructionFragment = KitchenConstructionFragment.getInstance(kitchenTable,customerTable);
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
                            //AddKitchenSuitability addKitchenSuitabilityFragment = AddKitchenSuitability.getInstance(customertable);
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

    }

    private void makeConstructionTeamClickListener()
    {
        txtMakeConstructionTeam.setOnClickListener(new View.OnClickListener() {
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
                                addConstructionTeamLayout.setBackgroundColor(getResources().getColor(R.color.colorDarkgray));
                            }
                        }
                    }
                }

            }
        });
    }

}
