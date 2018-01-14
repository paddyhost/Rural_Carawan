package com.hatchers.ruralcaravane.customer_registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.activity.CustomerMenus;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class CustomerListAdapter  extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CustomerTable> customerTableArrayList;

    public CustomerListAdapter(Context context, ArrayList<CustomerTable> customerTableArrayList) {
        this.context = context;
        this.customerTableArrayList = customerTableArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
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

        File image = FileHelper.createfile(Folders.CUSTOMERFOLDER, customerTable.getImagePathValue(), FileType.PNG);
        Glide.with(context)
                .load(image.getAbsoluteFile())
                .error(R.drawable.user_profile)
                .into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         Intent intent=new Intent(context, CustomerMenus.class);
                intent.putExtra(CustomerTable.CUSTOMER_TABLE,customerTable);
                context.startActivity(intent);

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name,address,mobile,age;
        CircleImageView user_profile;

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            address = (TextView) itemView.findViewById(R.id.customer_address);
            mobile = (TextView) itemView.findViewById(R.id.customer_mobileno);
            age = (TextView) itemView.findViewById(R.id.customer_age);
            user_profile=(CircleImageView)itemView.findViewById(R.id.customer_image);

            this.itemView = itemView;
        }
    }

}

