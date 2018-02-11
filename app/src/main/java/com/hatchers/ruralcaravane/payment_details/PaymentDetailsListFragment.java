package com.hatchers.ruralcaravane.payment_details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.payment_details.adapter.PaymentListAdapter;
import com.hatchers.ruralcaravane.payment_details.database.PaymentDetailsHelper;
import com.hatchers.ruralcaravane.payment_details.database.PaymentTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.util.ArrayList;


public class PaymentDetailsListFragment extends Fragment {

    RecyclerView paymentRecyclerView;
    //ImageView backImg;
    PaymentListAdapter paymentListAdapter;
    Button paymentBtn;
    private CustomerTable customertable;
    ArrayList<PaymentTable> paymentTableArrayList;
    private Toolbar paymentListToolbar;
    private FloatingActionButton paymentListBtn;
    private PrefManager prefManager;

    public PaymentDetailsListFragment() {
        // Required empty public constructor
    }


    public static PaymentDetailsListFragment getInstance(CustomerTable customertable)
    {
        PaymentDetailsListFragment fragment = new PaymentDetailsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customertable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment_details_list, container, false);

        initializations(view);
        setLanguageToUi();
        onClickListeners();
        return view;
    }

    private void setLanguageToUi()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            paymentListToolbar.setTitle(R.string.do_payment_marathi);
            paymentBtn.setText(getResources().getString(R.string.take_payment));
            paymentBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),15));
        }
        else
        {
            paymentListToolbar.setTitle(R.string.do_payment_english);
            paymentBtn.setText(getResources().getString(R.string.make_payment_english));
            paymentBtn.setTextSize(Utility.getConvertFloatToDP(getActivity(),15));
        }
    }


    private void initializations(View view)
    {
        prefManager = new PrefManager(getActivity());
        ((AppCompatActivity)getActivity()).setSupportActionBar(paymentListToolbar);
        paymentListToolbar=(Toolbar)view.findViewById(R.id.paymentToolbar);
        paymentListBtn=(FloatingActionButton)view.findViewById(R.id.paymentListBtn);
        //backImg = (ImageView)view.findViewById(R.id.back_paymt_list);
        paymentBtn = (Button)view.findViewById(R.id.payment_btn);

        paymentRecyclerView = (RecyclerView) view.findViewById(R.id.paymentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        paymentRecyclerView.setLayoutManager(layoutManager);
        paymentTableArrayList= PaymentDetailsHelper.getPaymentDetailsList(getContext(),customertable.getUniqueIdValue());
        paymentListAdapter=new PaymentListAdapter(getContext(),paymentTableArrayList);

        paymentRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        paymentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        paymentRecyclerView.setAdapter(paymentListAdapter);
        paymentListAdapter.notifyDataSetChanged();


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void onClickListeners()
    {
        paymentListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        paymentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               /* if(paymentTableArrayList.size()>0)
                {
                    PaymentTable paymentTable = PaymentDetailsHelper.getPaymentAmountByCustomerId(getActivity(), customertable.getUniqueIdValue());
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    PaymentDetailsFragment paymentDetailsFragment = PaymentDetailsFragment.getInstance(customertable, paymentTable);
                    fragmentTransaction.replace(R.id.frame_layout, paymentDetailsFragment).addToBackStack(null).commit();
                }
                else
                {*/
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    GetPayment paymentDetailsFragment = GetPayment.newInstance(customertable);
                    fragmentTransaction.replace(R.id.frame_layout, paymentDetailsFragment).addToBackStack(null).commit();
                /*}*/

            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                GetPayment paymentDetailsFragment = GetPayment.newInstance(customertable);
                fragmentTransaction.replace(R.id.frame_layout, paymentDetailsFragment).addToBackStack(null).commit();
            }
        });
    }
}
