package com.hatchers.ruralcaravane.customer_registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.CompleteConstructionActivity;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.CustomerListFragment;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.GetPayment;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsFragment;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsListFragment;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.sync.Web_SyncApi_Helper;
import com.hatchers.ruralcaravane.utils.Utility;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class CustomerListAdapter  extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CustomerTable> customerTableArrayList;
    private FragmentTransaction fragmentTransaction;
    private String openFrom;
    private PrefManager prefManager;

    public CustomerListAdapter(Context context, ArrayList<CustomerTable> customerTableArrayList,String openFrom) {
        this.context = context;
        this.openFrom=openFrom;
        this.customerTableArrayList = customerTableArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_list_row, viewGroup, false);
        CustomerListAdapter.ViewHolder viewHolder = new CustomerListAdapter.ViewHolder(v);
        context = viewGroup.getContext();
        prefManager=new PrefManager(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CustomerTable customerTable = customerTableArrayList.get(position);

        holder.customer_name.setText(String.valueOf(customerTable.getCustomerNameValue() + ""));
        holder.address.setText(String.valueOf(customerTable.getCustomerAddressValue() + ""));
        holder.mobile.setText(String.valueOf("Mobile :"+customerTable.getCustomerMobilenoValue() + ""));
        holder.age.setText(String.valueOf("Age "+customerTable.getCustomerAgeValue()+ ""));
        holder.uploadStatus.setText("Status :\n" +customerTable.getUpload_statusValue());

            Glide.with(context).load(customerTable.getImagePathValue())
                    .error(R.drawable.user_profile)
                    .into(holder.user_profile);

            if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_CONSTRUCTION))
            {
                holder.uploadPayment.setVisibility(View.GONE);
            }
            else if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_PAYMENT))
            {
                holder.uploadKichen.setVisibility(View.GONE);
            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_CONSTRUCTION)) {
                    Intent intent = new Intent(context, CompleteConstructionActivity.class);
                    intent.putExtra(CustomerTable.CUSTOMER_TABLE, customerTable);
                    context.startActivity(intent);

                }
                else if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_PAYMENT))
                {
                    ArrayList<KitchenTable> kitchenTableArrayList= KitchenTableHelper.getKitchenDataList(context,customerTable);

                    if (kitchenTableArrayList != null)
                    {
                        if(kitchenTableArrayList.size()<=0)
                        {
                            if (prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                                Toast.makeText(context, context.getResources().getString(R.string.add_kitchen_first_marathi), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, context.getResources().getString(R.string.add_kitchen_first_english), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            fragmentTransaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            PaymentDetailsListFragment detailsListFragment = PaymentDetailsListFragment.getInstance(customerTable);
                            fragmentTransaction.replace(R.id.frame_layout, detailsListFragment).addToBackStack(null).commit();
                        }
                    }
                }
            }
        });

        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
            holder.uploadKichen.setText(context.getResources().getString(R.string.upload_kitchen_marathi));
            holder.uploadKichen.setTextSize(Utility.getConvertFloatToDP((AppCompatActivity)context,10));

            holder.uploadPayment.setText(context.getResources().getString(R.string.upload_payment_marathi));
            holder.uploadPayment.setTextSize(Utility.getConvertFloatToDP((AppCompatActivity)context,10));
        }
        else
        {
            holder.uploadKichen.setText(context.getResources().getString(R.string.upload_kitchen_english));
            holder.uploadKichen.setTextSize(Utility.getConvertFloatToDP((AppCompatActivity)context,8));

            holder.uploadPayment.setText(context.getResources().getString(R.string.upload_payment_english));
            holder.uploadPayment.setTextSize(Utility.getConvertFloatToDP((AppCompatActivity)context,8));
        }

            holder.uploadPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Please wait...")
                        .setContentText("wait uploading data");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                Web_SyncApi_Helper.UploadPaymentsOfCustomerToServer(((AppCompatActivity)context),sweetAlertDialog,customerTable);
            }
        });

        holder.uploadKichen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(context.getResources().getString(R.string.please_wait_marathi)).setContentText(context.getResources().getString(R.string.wait_uploading_data_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(context.getResources().getString(R.string.please_wait_english)).setContentText(context.getResources().getString(R.string.wait_uploading_data_english));
                }
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();

                Web_SyncApi_Helper.UploadCustomerAndCompletedKitchenDataToServer(((AppCompatActivity)context),sweetAlertDialog,customerTable);
            }
        });



    }

    @Override
    public int getItemCount() {
        try {
            return customerTableArrayList.size();
        } catch (Exception e) {
            return 0;
        }

    }




    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView customer_name,address,mobile,age,uploadStatus;
        CircleImageView user_profile;
        Button uploadPayment, uploadKichen;


        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            address = (TextView) itemView.findViewById(R.id.customer_address);
            mobile = (TextView) itemView.findViewById(R.id.customer_mobileno);
            age = (TextView) itemView.findViewById(R.id.customer_age);
            user_profile=(CircleImageView)itemView.findViewById(R.id.customer_image);
            uploadStatus=(TextView)itemView.findViewById(R.id.upload_status);
            uploadPayment = (Button)itemView.findViewById(R.id.upload_payment);
            uploadKichen = (Button)itemView.findViewById(R.id.upload_kitchen);

            this.itemView = itemView;
        }

    }

}

