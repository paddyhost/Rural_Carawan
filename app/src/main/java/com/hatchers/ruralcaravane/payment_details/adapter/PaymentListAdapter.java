package com.hatchers.ruralcaravane.payment_details.adapter;


import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsFragment;
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
        KitchenTable kitchenTable = KitchenTableHelper.getKitchenDetalBuUniqId(context,paymentTable.getKitchenIdValue());
        holder.totalCost.setText(String.valueOf(context.getResources().getString(R.string.Rs)+" "+paymentTable.getAmountValue()+"/-"));
        holder.paitAmount.setText(String.valueOf("Paid : "+context.getResources().getString(R.string.Rs)+" "+paymentTable.getTotalPaidValue()+"/-"));
        holder.remainAmunt.setText(String.valueOf("Balance : "+context.getResources().getString(R.string.Rs)+" "+paymentTable.getBalanceValue()+"/-"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                PaymentDetailsFragment paymentDetailsFragment=PaymentDetailsFragment.getPaymentInstance(paymentTable);
                fragmentTransaction.replace(R.id.frame_layout,paymentDetailsFragment).addToBackStack(null).commit();
*/
            }
        });
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
