package com.hatchers.ruralcaravane.payment_details.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;

import java.util.ArrayList;

public class PaymentListAdapter  extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder>{

    private Context context;
    private ArrayList<PaymentTable> paymentTableArrayList;

    public PaymentListAdapter(Context context, ArrayList<PaymentTable> paymentTableArrayList) {

        this.context=context;
        this.paymentTableArrayList=paymentTableArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_list_row, viewGroup, false);
        PaymentListAdapter.ViewHolder viewHolder = new PaymentListAdapter.ViewHolder(v);
        context = viewGroup.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PaymentTable paymentTable = paymentTableArrayList.get(position);

        holder.totalCost.setText(String.valueOf(context.getResources().getString(R.string.Rs)+" "+paymentTable.getPayment_amountValue()+"/-"));
        holder.paitAmount.setText(String.valueOf("Paid : "+context.getResources().getString(R.string.Rs)+" "+paymentTable.getTotalPaidValue()+"/-"));
        holder.remainAmunt.setText(String.valueOf("Balance : "+context.getResources().getString(R.string.Rs)+" "+paymentTable.getRemaining_amountValue()+"/-"));

    }

    @Override
    public int getItemCount() {
        try {
            return paymentTableArrayList.size();
        } catch (Exception e) {
            return 0;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView totalCost, remainAmunt, paitAmount;

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            totalCost = (TextView)itemView.findViewById(R.id.cost_of_chullha_);
            paitAmount= (TextView)itemView.findViewById(R.id.total_paid);
            remainAmunt=(TextView)itemView.findViewById(R.id.remaining_amount);
            this.itemView = itemView;
        }
    }
}
