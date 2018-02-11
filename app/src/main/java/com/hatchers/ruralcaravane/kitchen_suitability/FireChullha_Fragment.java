package com.hatchers.ruralcaravane.kitchen_suitability;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hatchers.ruralcaravane.constants.AppConstants.FIRE_PREFIX;
import static com.hatchers.ruralcaravane.constants.AppConstants.MARATHI;
import static com.hatchers.ruralcaravane.constants.AppConstants.STEP1_PREFIX;
import static com.hatchers.ruralcaravane.constants.AppConstants.STEP2_PREFIX;
import static com.hatchers.ruralcaravane.current_date_time_function.CurrentDateTime.getCurrentDateTime;

public class FireChullha_Fragment extends Fragment
{
    private KitchenTable kitchenTable;
    private CustomerTable customerTable;
    private PrefManager prefManager;
    private Toolbar kitchen_const_Toolbar;
    private ImageView fireChulhaImg;
    private TextView fireChulhaLabel;
    private Button btnSubmit;
    private int FIRED_CHULHA_IMAGE = 111;
    Bitmap conBitmap1;

    public FireChullha_Fragment()
    {
        // Required empty public constructor
    }

    public static FireChullha_Fragment newInstance(KitchenTable kitchenTable, CustomerTable customerTable)
    {
        FireChullha_Fragment fragment = new FireChullha_Fragment();
        Bundle args = new Bundle();
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customerTable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
            customerTable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fire_chullha, container, false);

        initializations(view);
        setLanguageToUi();
        firePhotoClickListener();
        submitClickListener();
        setUploadedPhoto();
        toolbarClickListener();

        return view;
    }

    private void initializations(View view)
    {
        prefManager = new PrefManager(getActivity());
        kitchen_const_Toolbar = (Toolbar) view.findViewById(R.id.kitchen_const_Toolbar);
        fireChulhaImg = (ImageView) view.findViewById(R.id.fired_chulha_image);
        fireChulhaLabel = (TextView) view.findViewById(R.id.fire_chulha_image_label);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
    }

    private void setLanguageToUi()
    {
        if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            kitchen_const_Toolbar.setTitle(getResources().getString(R.string.chullaha_construction_marathi));

            fireChulhaLabel.setText(getResources().getString(R.string.fired_chullha_image_marathi));
            fireChulhaLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(), 8));

            btnSubmit.setText(getResources().getString(R.string.save));
            btnSubmit.setTextSize(Utility.getConvertFloatToDP(getActivity(), 12));

        } else {
            kitchen_const_Toolbar.setTitle(getResources().getString(R.string.chullaha_construction_english));

            fireChulhaLabel.setText(getResources().getString(R.string.fired_chullha_image_english));
            fireChulhaLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(), 8));

            btnSubmit.setText(getResources().getString(R.string.save1));
            btnSubmit.setTextSize(Utility.getConvertFloatToDP(getActivity(), 10));

        }
    }

    private void firePhotoClickListener()
    {
        fireChulhaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File image1 = FileHelper.createfile(Folders.CHULHAFOLDER, STEP1_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                File image2 = FileHelper.createfile(Folders.CHULHAFOLDER, STEP2_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
                File image3 = FileHelper.createfile(Folders.CHULHAFOLDER, FIRE_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);

                if (image1 != null) {
                    if (!image1.exists()) {
                        showUploadStep1ImgToast();
                    } else {
                        if (image2 != null) {
                            if (!image2.exists()) {
                                showUploadStep2ImgToast();
                            } else {
                                if (image3 != null) {
                                    if (!image3.exists()) {
                                        showPictureDialog(fireChulhaImg);
                                    } else {
                                        showUploadFiredImgToast();
                                    }
                                }
                            }
                        } else {
                            showUploadStep2ImgToast();
                        }
                    }
                } else {
                    showUploadStep1ImgToast();
                }

            }
        });
    }

    private void showUploadStep1ImgToast()
    {
        if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_marathi), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_upload_step1_image_first_english), Toast.LENGTH_SHORT).show();
        }
    }

    private void showUploadStep2ImgToast()
    {
        if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.upload_step2_image_marathi), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.upload_step2_image_english), Toast.LENGTH_SHORT).show();
        }
    }

    private void showUploadFiredImgToast()
    {
        if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.fired_chullha_image_marathi), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.fired_chullha_image_english), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPictureDialog(final ImageView imageView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_marathi), getResources().getString(R.string.cancel_marathi)};
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
        } else {
            final CharSequence[] options = {getResources().getString(R.string.take_photo_english), getResources().getString(R.string.cancel_english)};
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
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void takePhotoFromCamera(ImageView imageView)
    {
        if (imageView == fireChulhaImg) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, FIRED_CHULHA_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == FIRED_CHULHA_IMAGE) {
            conBitmap1 = (Bitmap) data.getExtras().get("data");

            fireChulhaImg.setImageBitmap(conBitmap1);

            FileHelper.savePNGImage(Folders.CHULHAFOLDER, conBitmap1, FIRE_PREFIX + kitchenTable.getKitchenUniqueIdValue());

            File image = FileHelper.createfile(Folders.CHULHAFOLDER, FIRE_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
            if (image != null) {
                if (image.exists()) {
                    kitchenTable.setFiredChulhaPhotoValue(image.getAbsolutePath());
                    kitchenTable.setUpload_statusValue(KitchenTable.FIRED_CHULHA_PHOTO_ADD_LOCAL);
                    kitchenTable.setConstructionEndDateTimeValue(getCurrentDateTime());

                }
            }
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
            } else {
                sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
            }

            sweetAlertDialog.show();

            if (KitchenTableHelper.updateKitchenData(getActivity(), kitchenTable)) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                } else {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_uploaded_successfully_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        fireChulhaImg.setImageBitmap(conBitmap1);

                        customerTable.setUpload_statusValue("12");
                        customerTable.setFiredPhotoAdded(CustomerTable.LOCAL);
                        customerTable.setconstructionComplete(CustomerTable.LOCAL);
                        CustomerTableHelper.updateCustomerData(getActivity(), customerTable);
                    }
                });

            } else {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if (prefManager.getLanguage().equalsIgnoreCase(MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.image_upload_failed_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                } else {
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
    }

    private void submitClickListener()
    {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setUploadedPhoto()
    {
        Glide.with(getActivity())
                .load(kitchenTable.getFiredChulhaPhotoValue())
                .error(R.drawable.capture_area)
                .into(fireChulhaImg);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        File image = FileHelper.createfile(Folders.CHULHAFOLDER, FIRE_PREFIX + kitchenTable.getKitchenUniqueIdValue(), FileType.PNG);
        if (image != null) {
            Glide.with(getActivity())
                    .load(image.getAbsolutePath())
                    .error(R.drawable.capture_area)
                    .into(fireChulhaImg);
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

}