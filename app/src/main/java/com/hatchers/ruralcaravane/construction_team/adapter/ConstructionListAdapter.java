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
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;

import java.io.File;
import java.util.ArrayList;



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
        holder.member_address.setText(String.valueOf(constructionTable.getTechnicianAddressValue() + ""));
        holder.age.setText("Age : "+String.valueOf(constructionTable.getTechnicianAgeValue() + ""));
        if(constructionTable.getTechnicianGenderValue().equalsIgnoreCase("F"))
        {
            holder.member_gender.setText(String.valueOf("Gender : Female"));

        }
        else
        {
            holder.member_gender.setText(String.valueOf("Gender : Male"));
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
        View itemView;

        ViewHolder(View itemView)
        {
            super(itemView);

            member_name = (TextView) itemView.findViewById(R.id.member_name);
            construction_mobile_number = (TextView) itemView.findViewById(R.id.construction_mobile_number);
            age=(TextView)itemView.findViewById(R.id.age);
            member_address=(TextView)itemView.findViewById(R.id.member_address);
            member_gender=(TextView)itemView.findViewById(R.id.member_gender);
            this.itemView = itemView;
        }
    }

}