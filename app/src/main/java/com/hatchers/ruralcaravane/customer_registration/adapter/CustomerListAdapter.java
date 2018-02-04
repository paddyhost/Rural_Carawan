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
import com.hatchers.ruralcaravane.customer_registration.CustomerListFragment;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.GetPayment;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsFragment;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomerListAdapter  extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CustomerTable> customerTableArrayList;
    private FragmentTransaction fragmentTransaction;
    private String openFrom;

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
                            Toast.makeText(context,"Add Kitchen First",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            fragmentTransaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            GetPayment getPayment = GetPayment.newInstance(customerTable);
                            fragmentTransaction.replace(R.id.frame_layout, getPayment).addToBackStack(null).commit();
                        }
                    }
                }
            }
        });

        holder.uploadChulha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.uploadKichen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Button uploadChulha, uploadKichen;

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            address = (TextView) itemView.findViewById(R.id.customer_address);
            mobile = (TextView) itemView.findViewById(R.id.customer_mobileno);
            age = (TextView) itemView.findViewById(R.id.customer_age);
            user_profile=(CircleImageView)itemView.findViewById(R.id.customer_image);
            uploadStatus=(TextView)itemView.findViewById(R.id.upload_status);
            uploadChulha = (Button)itemView.findViewById(R.id.upload_Chulha);
            uploadKichen = (Button)itemView.findViewById(R.id.upload_kitchen);

            this.itemView = itemView;
        }
    }

}

