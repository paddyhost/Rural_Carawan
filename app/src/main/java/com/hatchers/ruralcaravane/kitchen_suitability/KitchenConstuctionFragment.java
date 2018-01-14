package com.hatchers.ruralcaravane.kitchen_suitability;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.construction_team.ConstructionTeamRegistrationFragment;
import com.hatchers.ruralcaravane.construction_team.adapter.ConstructionListAdapter;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KitchenConstuctionFragment extends Fragment {

    private KitchenTable kitchenTable;
    ArrayList<ConstructionTable> constructionTables;
    private RecyclerView constructionRecyclerView;
    private ConstructionListAdapter constructionListAdapter;
    private FloatingActionButton add_construction;
    private TextView houseTypeTxt, roofTypeTxt, heightTxt;
    private ImageView kitchenConstructionImage;
    private Toolbar kitchen_const_Toolbar;
    private ImageView half_constructed_image,complete_constructed_image;
    private int HALF_IMAGE = 1, FULL_IMAGE = 2;
    Bitmap conBitmap,conBitmap1;
    ImageView backImg;

    public KitchenConstuctionFragment()
    {
        // Required empty public constructor
    }

    public static KitchenConstuctionFragment newInstance(KitchenTable kitchenTable) {
        KitchenConstuctionFragment fragment = new KitchenConstuctionFragment();
        Bundle args = new Bundle();
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kitchen_constuction, container, false);

        initializations(view);

        clickListners();

        setKitchenData();

        return view;
    }

    private void initializations(View view)
    {
        backImg = (ImageView)view.findViewById(R.id.kitchn_detail_back);
        add_construction=(FloatingActionButton)view.findViewById(R.id.add_const);
        constructionRecyclerView=(RecyclerView)view.findViewById(R.id.const_list);
        houseTypeTxt = (TextView)view.findViewById(R.id.housetype_txt);
        roofTypeTxt = (TextView)view.findViewById(R.id.roogtype_txt);
        heightTxt = (TextView)view.findViewById(R.id.height_txt);
        kitchen_const_Toolbar=(Toolbar)view.findViewById(R.id.kitchen_const_Toolbar);

        constructionRecyclerView = (RecyclerView) view.findViewById(R.id.const_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        constructionRecyclerView.setLayoutManager(layoutManager);

        constructionTables= ConstructionTableHelper.getConstructionTeamListByKitchen(getContext(),kitchenTable.getKitchenUniqueIdValue());
        constructionListAdapter=new ConstructionListAdapter(getContext(),constructionTables);

        constructionRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        constructionRecyclerView.setItemAnimator(new DefaultItemAnimator());

        constructionRecyclerView.setAdapter(constructionListAdapter);
        constructionListAdapter.notifyDataSetChanged();

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
        houseTypeTxt.setText(String.valueOf("House Type : "+kitchenTable.getHouse_typeValue()));
        roofTypeTxt.setText(String.valueOf("Roof Type : "+kitchenTable.getRoof_typeValue()));
        heightTxt.setText(String.valueOf("Height : "+kitchenTable.getKitchen_heightValue()));

        File image = FileHelper.createfile(Folders.CHULHAFOLDER, kitchenTable.getStep1_imageValue(), FileType.PNG);
        if (image != null) {
            Glide.with(getActivity())
                    .load(image.getAbsolutePath())
                    .error(R.drawable.ic_add_image)
                    .into(half_constructed_image);
        }

        File image1 = FileHelper.createfile(Folders.CHULHAFOLDER, kitchenTable.getStep2_imageValue(), FileType.PNG);
        if (image1 != null) {
            Glide.with(getActivity())
                    .load(image1.getAbsolutePath())
                    .error(R.drawable.ic_add_image)
                    .into(complete_constructed_image);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        File image = FileHelper.createfile(Folders.CHULHAFOLDER, kitchenTable.getStep1_imageValue(), FileType.PNG);
        if (image != null) {
            Glide.with(getActivity())
                    .load(image.getAbsolutePath())
                    .error(R.drawable.ic_add_image)
                    .into(half_constructed_image);
        }

        File image1 = FileHelper.createfile(Folders.CHULHAFOLDER, kitchenTable.getStep2_imageValue(), FileType.PNG);
        if (image1 != null) {
            Glide.with(getActivity())
                    .load(image1.getAbsolutePath())
                    .error(R.drawable.ic_add_image)
                    .into(complete_constructed_image);
        }

    }

    private void clickListners()
    {
        add_construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ConstructionTeamRegistrationFragment constructionTeamRegistrationFragment= ConstructionTeamRegistrationFragment.newInstance(kitchenTable);
                fragmentTransaction.replace(R.id.frame_layout,constructionTeamRegistrationFragment).addToBackStack(null).commit();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        half_constructed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog(half_constructed_image);
            }
        });

        complete_constructed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog(complete_constructed_image);
            }
        });
    }

    private void showPictureDialog(final ImageView imageView)
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
                                takePhotoFromCamera(imageView);
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

    private void takePhotoFromCamera(ImageView imageView)
    {
        if(imageView==half_constructed_image)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, HALF_IMAGE);
        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, FULL_IMAGE);
        }

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

            FileHelper.savePNGImage(Folders.CHULHAFOLDER,conBitmap,"KIT_Step1_image"+kitchenTable.getKitchenUniqueIdValue());
            kitchenTable.setStep1_imageValue("KIT_Step1_"+kitchenTable.getKitchenUniqueIdValue());
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
                        half_constructed_image.setImageBitmap(conBitmap);
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
        else if (requestCode == FULL_IMAGE) {
             conBitmap1 = (Bitmap) data.getExtras().get("data");


            FileHelper.savePNGImage(Folders.CHULHAFOLDER,conBitmap1,"KIT_Step2_image"+kitchenTable.getKitchenUniqueIdValue());
            kitchenTable.setStep2_imageValue("KIT_Step2_"+kitchenTable.getKitchenUniqueIdValue());
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
                        complete_constructed_image.setImageBitmap(conBitmap1);
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

    }


}
