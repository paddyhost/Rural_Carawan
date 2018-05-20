package com.hatchers.ruralcaravane.kitchen_suitability;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.hatchers.ruralcaravane.construction_team.ConstructionTeamRegistrationFragment;
import com.hatchers.ruralcaravane.construction_team.adapter.ConstructionListAdapter;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsFragment;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.runtime_permissions.RuntimePermissions;
import com.hatchers.ruralcaravane.utils.Utility;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.KITCHEN_PREFIX;
import static com.hatchers.ruralcaravane.constants.AppConstants.MARATHI;
import static com.hatchers.ruralcaravane.constants.AppConstants.STEP1_PREFIX;
import static com.hatchers.ruralcaravane.constants.AppConstants.STEP2_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;

public class KitchenConstructionFragment extends Fragment {

    private KitchenTable kitchenTable;
    private PaymentTable paymentTable;
    ArrayList<ConstructionTable> constructionTables;
    private RecyclerView constructionRecyclerView;
    private ConstructionListAdapter constructionListAdapter;
    private Button add_construction,add_LocationBtn,saveBtn;
    private TextView houseTypeTxt, roofTypeTxt, heightTxt;
    private ImageView place_image;
    private Toolbar kitchen_const_Toolbar;
    private ImageView half_constructed_image,complete_constructed_image;
    private int HALF_IMAGE = 1, FULL_IMAGE = 2,PLACE_IMAGE=3;
    Bitmap conBitmap,conBitmap1,placeBitmap;
    private TextView statusTxt,completeConstructedImageLabel,halfConstructedImageLabel;
    private PrefManager prefManager;
    private CustomerTable customerTable;

    public KitchenConstructionFragment()
    {
        // Required empty public constructor
    }

    public static KitchenConstructionFragment getInstance(KitchenTable kitchenTable,CustomerTable customerTable)
    {
        KitchenConstructionFragment fragment = new KitchenConstructionFragment();
        Bundle args = new Bundle();
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customerTable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
            customerTable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kitchen_constuction, container, false);

        initializations(view);
        setLanguageToUI();
        toolbarClickListener();
        //addTeamClickListener();
        addStep1ImageClickListener();
        addStep2ImageClickListener();
        //addPlaceImageClickListener();

        setKitchenData();
        //addLocation();

        onClickListeners();
        return view;
    }


    private void onClickListeners()
    {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            kitchen_const_Toolbar.setTitle(getResources().getString(R.string.chullaha_construction_marathi));

            completeConstructedImageLabel.setText(getResources().getString(R.string.complete_costructed_chullha_image_marathi));
            completeConstructedImageLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            halfConstructedImageLabel.setText(getResources().getString(R.string.half_costructed_chullha_image_marathi));
            halfConstructedImageLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            saveBtn.setText(getResources().getString(R.string.save));
            saveBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

        }
        else
        {
            kitchen_const_Toolbar.setTitle(getResources().getString(R.string.chullaha_construction_english));

            completeConstructedImageLabel.setText(getResources().getString(R.string.complete_costructed_chullha_image_english));
            completeConstructedImageLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            halfConstructedImageLabel.setText(getResources().getString(R.string.half_costructed_chullha_image_english));
            halfConstructedImageLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));


            saveBtn.setText(getResources().getString(R.string.save1));
            saveBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),10));


        }
    }

    private void initializations(View view)
    {
        prefManager=new PrefManager(getActivity());
        statusTxt = (TextView)view.findViewById(R.id.upload_status);
        add_construction=(Button)view.findViewById(R.id.add_construction);
        constructionRecyclerView=(RecyclerView)view.findViewById(R.id.const_list);
        houseTypeTxt = (TextView)view.findViewById(R.id.housetype_txt);
        roofTypeTxt = (TextView)view.findViewById(R.id.roogtype_txt);
        heightTxt = (TextView)view.findViewById(R.id.height_txt);
        kitchen_const_Toolbar=(Toolbar)view.findViewById(R.id.kitchen_const_Toolbar);
        place_image=(ImageView)view.findViewById(R.id.place_image);
        add_LocationBtn=(Button)view.findViewById(R.id.addLocationBtn);
        completeConstructedImageLabel=(TextView)view.findViewById(R.id.complete_constructed_image_label);
        halfConstructedImageLabel=(TextView)view.findViewById(R.id.half_constructed_image_label);
        constructionTables= ConstructionTableHelper.getConstructionTeamListByKitchen(getContext(),kitchenTable.getKitchenUniqueIdValue());
        saveBtn=(Button)view.findViewById(R.id.btn_save);


      /*  constructionRecyclerView = (RecyclerView) view.findViewById(R.id.const_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        constructionRecyclerView.setLayoutManager(layoutManager);

        constructionTables= ConstructionTableHelper.getConstructionTeamListByKitchen(getContext(),kitchenTable.getKitchenUniqueIdValue());
        constructionListAdapter=new ConstructionListAdapter(getContext(),constructionTables);

        constructionRecyclerView.setItemAnimator(new DefaultItemAnimator());

        constructionRecyclerView.setAdapter(constructionListAdapter);
        constructionListAdapter.notifyDataSetChanged();
*/
        half_constructed_image=(ImageView)view.findViewById(R.id.half_constructed_image1);
        complete_constructed_image=(ImageView)view.findViewById(R.id.complete_constructed_image1);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void setKitchenData()
    {
        //statusTxt.setText(String.valueOf("Upload Status :"+kitchenTable.getUpload_statusValue()));
        //houseTypeTxt.setText(String.valueOf("House Type : "+kitchenTable.getHouse_typeValue()));
       // roofTypeTxt.setText(String.valueOf("Roof Type : "+kitchenTable.getRoof_typeValue()));
       // heightTxt.setText(String.valueOf("Height : "+kitchenTable.getKitchen_heightValue()));

           /* Glide.with(getActivity())
                    .load(kitchenTable.getPlaceImageValue())
                    .error(R.drawable.capture_area)
                    .into(place_image);
*/

            Glide.with(getActivity())
                    .load(kitchenTable.getStep1_imageValue())
                    .error(R.drawable.capture_area)
                    .into(half_constructed_image);


            Glide.with(getActivity()).load(kitchenTable.getStep2_imageValue())
                    .error(R.drawable.capture_area)
                    .into(complete_constructed_image);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP1_PREFIX+kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
        if (image != null) {
            Glide.with(getActivity())
                    .load(image.getAbsolutePath())
                    .error(R.drawable.capture_area)
                    .into(half_constructed_image);
        }

        File image1 = FileHelper.createfile(Folders.CHULHAFOLDER,STEP2_PREFIX+kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
        if (image1 != null) {
            Glide.with(getActivity())
                    .load(image1.getAbsolutePath())
                    .error(R.drawable.capture_area)
                    .into(complete_constructed_image);
        }

    }

    private void toolbarClickListener()
    {
            kitchen_const_Toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
    }

    /*private void addTeamClickListener() {
        add_construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                if (image != null) {
                    if (!image.exists()) {
                        if(kitchenTable.getUpload_statusValue().equalsIgnoreCase("1"))
                        {
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            ConstructionTeamRegistrationFragment constructionTeamRegistrationFragment = ConstructionTeamRegistrationFragment.getInstance(kitchenTable,customerTable);
                            fragmentTransaction.replace(R.id.frame_layout, constructionTeamRegistrationFragment).addToBackStack(null).commit();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Kitchen Data not uploaded to server.Please Upload data First", Toast.LENGTH_SHORT).show();
                        }

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        ConstructionTeamRegistrationFragment constructionTeamRegistrationFragment = ConstructionTeamRegistrationFragment.getInstance(kitchenTable,customerTable);
                        fragmentTransaction.replace(R.id.frame_layout, constructionTeamRegistrationFragment).addToBackStack(null).commit();
                    } else
                    {
                        Toast.makeText(getActivity(), "Process Completed. Can't add team member ", Toast.LENGTH_SHORT).show();
                        add_construction.setBackgroundColor(getActivity().getResources().getColor(R.color.colorDarkgray));
                    }
                }
            }
        });

    }
*/
    private void addStep1ImageClickListener() {
        half_constructed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (constructionTables!=null && constructionTables.size() > 0)
                {
                    File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP1_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                    if (image != null) {
                        if (!image.exists()) {

                            showPictureDialog(half_constructed_image);

                        } else {
                            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.already_uploaded_step1_image_marathi), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), getResources().getString(R.string.already_uploaded_step1_image_english), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.already_uploaded_step1_image_marathi), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getResources().getString(R.string.already_uploaded_step1_image_english), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.please_add_atleast_one_team_member_marathi), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.please_add_atleast_one_team_member_english), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addStep2ImageClickListener() {
        complete_constructed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP1_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                if (image != null) {
                    if (!image.exists()) {

                        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_marathi), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_english), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        File image1 = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                        if (image1 != null) {
                            if (!image1.exists()) {
                                showPictureDialog(complete_constructed_image);
                            } else {
                                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.process_completed_canot_upload_step2_image_marathi), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.process_completed_canot_upload_step2_image_english), Toast.LENGTH_SHORT).show();
                                }
                                add_construction.setBackgroundColor(getActivity().getResources().getColor(R.color.colorDarkgray));
                            }
                        }
                    }
                } else {
                    if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_marathi), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_english), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addPlaceImageClickListener()
    {
     place_image.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             File image = FileHelper.createfile(Folders.CHULHAFOLDER, KITCHEN_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
             if (image != null) {
                 if (!image.exists()) {

                     showPictureDialog(place_image);
                 }

                 else
                 {
                     Toast.makeText(getActivity(),"Planned Area Image Already Uploaded",Toast.LENGTH_SHORT).show();
                 }
             }

             }
     });
    }

    private void showPictureDialog(final ImageView imageView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_marathi),getResources().getString(R.string.cancel_marathi)};
            builder.setTitle(getResources().getString(R.string.add_photo_marathi));
            builder.setItems(options,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    takePhotoFromCamera(imageView);
                                    break;

                                case 2:
                                    dialog.dismiss();
                            }
                        }
                    });
        }
        else
        {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_english),getResources().getString(R.string.cancel_english)};
            builder.setTitle(getResources().getString(R.string.add_photo_english));
            builder.setItems(options,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    takePhotoFromCamera(imageView);
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

    private void takePhotoFromCamera(ImageView imageView)
    {
        if(imageView==half_constructed_image)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, HALF_IMAGE);
        }
        else if(imageView==complete_constructed_image)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, FULL_IMAGE);
        }
       /* else if(imageView==place_image)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PLACE_IMAGE);
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == HALF_IMAGE)
        {
            conBitmap = (Bitmap) data.getExtras().get("data");

            half_constructed_image.setImageBitmap(conBitmap);

            FileHelper.savePNGImage(Folders.CHULHAFOLDER,conBitmap, STEP1_PREFIX+kitchenTable.getKitchenUniqueIdValue());

            File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP1_PREFIX+kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);

            if(image!=null)
            {
                if(image.exists())
                kitchenTable.setStep1_imageValue(image.getAbsolutePath());
                kitchenTable.setConstructionStartDateTimeValue(getCurrentDateTime());
            }

            SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
            }
            else
            {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
            }
            sweetAlertDialog.show();

            if(KitchenTableHelper.updateKitchenData(getActivity(),kitchenTable))
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        half_constructed_image.setImageBitmap(conBitmap);
                    }
                });


            }
            else
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);

                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_upload_failed_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_upload_failed_english));
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
        else if (requestCode == FULL_IMAGE) {
             conBitmap1 = (Bitmap) data.getExtras().get("data");

             complete_constructed_image.setImageBitmap(conBitmap1);

            FileHelper.savePNGImage(Folders.CHULHAFOLDER,conBitmap1,STEP2_PREFIX+kitchenTable.getKitchenUniqueIdValue());

            File image = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX+kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
            if(image!=null) {
                if(image.exists()) {
                    kitchenTable.setStep2_imageValue(image.getAbsolutePath());
                    kitchenTable.setUpload_statusValue(KitchenTable.PHOTOS_ADDED_LOCAL);
                    kitchenTable.setConstructionEndDateTimeValue(getCurrentDateTime());

                }
            }
            SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
            }
            else
            {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
            }

            sweetAlertDialog.show();

            if(KitchenTableHelper.updateKitchenData(getActivity(),kitchenTable))
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        complete_constructed_image.setImageBitmap(conBitmap1);
                        add_construction.setBackgroundColor(getActivity().getResources().getColor(R.color.colorDarkgray));

                        customerTable.setUpload_statusValue("6");
                        customerTable.setChulha_photo_added(CustomerTable.LOCAL);
                        customerTable.setconstructionComplete(CustomerTable.LOCAL);
                        CustomerTableHelper.updateCustomerData(getActivity(),customerTable);
                    }
                });

            }

            else
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_upload_failed_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_upload_failed_english));
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


        /*else if (requestCode == PLACE_IMAGE)
        {
            placeBitmap = (Bitmap) data.getExtras().get("data");
            place_image.setImageBitmap(placeBitmap);

            FileHelper.savePNGImage(Folders.CHULHAFOLDER,placeBitmap,KITCHEN_PREFIX+kitchenTable.getKitchenUniqueIdValue());

            File image = FileHelper.createfile(Folders.CHULHAFOLDER, KITCHEN_PREFIX+kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
            if(image!=null) {
                if(image.exists()) {
                    kitchenTable.setPlaceImageValue(image.getAbsolutePath());
                }
            }

            SweetAlertDialog sweetAlertDialog =new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Please wait");

            sweetAlertDialog.show();

            if(KitchenTableHelper.updateKitchenData(getActivity(),kitchenTable))
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Image Uploaded Successfully");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        place_image.setImageBitmap(placeBitmap);
                    }
                });


            }

            else
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Image Upload Failed");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

        }
*/
    }

    private void addLocation() {
        add_LocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(RuntimePermissions.isNetworkConnectionAvailable(getActivity())) {

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    AddKitchenAddress addKitchenAddress = AddKitchenAddress.getInstance(kitchenTable);
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
                        }
                    });
                    android.support.v7.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
*/
        }

        });
    }
}






