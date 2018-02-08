package com.hatchers.ruralcaravane.customer_registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.adapter.CustomerListAdapter;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTableHelper;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.util.ArrayList;

public class CustomerListFragment extends Fragment
{
    private CustomerListAdapter customerListAdapter;
    private RecyclerView customerRecyclerView;
    private TextView no_cust_txt;
    public static final String OPEN_FROM = "open_from";
    public static final String FROM_CONSTRUCTION = "from_construction";
    public static final String FROM_PAYMENT = "from_payment";
    private String openFrom;
    private PrefManager prefManager;
    private Toolbar customerListToolbar;
    private ArrayList<CustomerTable> customerTableArrayList;
    private TextView remaingTxt;

    public CustomerListFragment()
    {
        // Required empty public constructor
    }

    public static CustomerListFragment getInstance(String openFrom)
    {
        CustomerListFragment fragment = new CustomerListFragment();
        Bundle args = new Bundle();
        args.putString(OPEN_FROM, openFrom);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            openFrom = getArguments().getString(OPEN_FROM);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer__list, container, false);

        initializations(view);
        setLanguageToUI();
        setData();
        backClickListener();

        return view;
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            customerListToolbar.setTitle(getResources().getString(R.string.customer_list_marathi));

            no_cust_txt.setText(getResources().getString(R.string.no_customer_marathi));
            no_cust_txt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_CONSTRUCTION))
            {
                remaingTxt.setText(getResources().getString(R.string.remaing_chulha_customer_marathi));
                remaingTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            }
            else if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_PAYMENT))
            {
                remaingTxt.setText(getResources().getString(R.string.remaing_payment_customer_marathi));
                remaingTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            }
        }
        else
        {
            customerListToolbar.setTitle(getResources().getString(R.string.customer_list_english));

            no_cust_txt.setText(getResources().getString(R.string.no_customer_english));
            no_cust_txt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

            if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_CONSTRUCTION))
            {
                remaingTxt.setText(getResources().getString(R.string.remaing_chulha_customer_english));
                remaingTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            }
            else if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_PAYMENT))
            {
                remaingTxt.setText(getResources().getString(R.string.remaing_payment_customer_english));
                remaingTxt.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));
            }
        }
    }

    private void initializations(View view)
    {
        prefManager=new PrefManager(getActivity());
        customerListToolbar=(Toolbar)view.findViewById(R.id.customer_list_toolbar);
        no_cust_txt=(TextView)view.findViewById(R.id.no_cust_txt);
        remaingTxt = (TextView)view.findViewById(R.id.remaing_list_txt);
        customerRecyclerView = (RecyclerView) view.findViewById(R.id.customerRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        customerRecyclerView.setLayoutManager(layoutManager);

        customerRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        customerRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void backClickListener()
    {
        customerListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public  void setData()
    {
        try
        {
            if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_CONSTRUCTION))
            {
                customerTableArrayList = CustomerTableHelper.getCustomerAddedList(getContext());

                if (customerTableArrayList != null) {
                    for (CustomerTable customerTable:customerTableArrayList)
                    {
                        if(customerTable.getChulha_photo_added().equalsIgnoreCase(CustomerTable.SERVER))
                        {
                            customerTableArrayList.remove(customerTable);
                        }
                    }
                }
            }
            else if(openFrom.equalsIgnoreCase(CustomerListFragment.FROM_PAYMENT))
            {
                customerTableArrayList = CustomerTableHelper.getCustomerWithCompleteConstructionList(getContext());

                if (customerTableArrayList != null) {
                    for (CustomerTable customerTable:customerTableArrayList)
                    {
                        if(customerTable.getPayment_completed().equalsIgnoreCase(CustomerTable.SERVER))
                        {
                            customerTableArrayList.remove(customerTable);
                        }
                    }
                }
            }

            customerListAdapter = new CustomerListAdapter(getContext(), customerTableArrayList,openFrom);
            customerRecyclerView.setAdapter(customerListAdapter);
            if(!(customerTableArrayList.size() >0))
            {
                customerRecyclerView.setVisibility(View.GONE);
                no_cust_txt.setVisibility(View.VISIBLE);
            }
            else
            {
                customerRecyclerView.setVisibility(View.VISIBLE);
                no_cust_txt.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {
                e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        setData();
        super.onResume();
    }
}
