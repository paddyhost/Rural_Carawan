package com.hatchers.ruralcaravane.construction_team.adapter;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConstructionListAdapter extends RecyclerView.Adapter<ConstructionListAdapter.ViewHolder>
{
    KitchenTable kitchenTable;
    private Context context;
    private ArrayList<ConstructionTable> constructionTableArrayList;

    public ConstructionListAdapter(Context context,ArrayList<ConstructionTable> constructionTableArrayList) {
        this.context = context;
        this.constructionTableArrayList = constructionTableArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.construction_list_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        context = viewGroup.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ConstructionTable constructionTable = constructionTableArrayList.get(position);

        holder.member_name.setText(String.valueOf(constructionTable.getTechnicianNameValue() + ""));
        holder.construction_mobile_number.setText(String.valueOf(constructionTable.getTechnicianMobileNoValue() + ""));
        if(constructionTable.getTechnicianGenderValue().equalsIgnoreCase("F"))
        {
            holder.member_gender.setText(String.valueOf("Female"));

        }
        else
        {
            holder.member_gender.setText(String.valueOf("Male"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return constructionTableArrayList.size();
        } catch (Exception e) {
            return 0;
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView member_name,construction_mobile_number,age,member_address,member_gender;
        CircleImageView customer_image;
        View itemView;

        ViewHolder(View itemView)
        {
            super(itemView);

            member_name = (TextView) itemView.findViewById(R.id.member_name);
            construction_mobile_number = (TextView) itemView.findViewById(R.id.construction_mobile_number);
            member_gender=(TextView)itemView.findViewById(R.id.member_gender);
            customer_image=(CircleImageView)itemView.findViewById(R.id.customer_image);

            this.itemView = itemView;
        }
    }

}