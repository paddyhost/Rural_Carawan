package com.hatchers.ruralcaravane.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.AddCustomerFragment;
import com.hatchers.ruralcaravane.customer_registration.CustomerListFragment;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.util.ArrayList;

public class MainMenus extends AppCompatActivity
{
    private TextView txtCollectCash, txtConstChulha, txtAddNewCustomer;
    private LinearLayout addCustomer_layout,constructChulha_layout,payment_layout;
    private PrefManager prefManager;
    private Toolbar mainMenusToolbar;
    private ArrayList<CustomerTable> customerTableArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menus);

        initializations();

        setLanguageToUI();

        clickListner();

    }

    private void initializations()
    {
        mainMenusToolbar=(Toolbar)findViewById(R.id.mainMenus_toolbar);
        prefManager = new PrefManager(MainMenus.this);
        txtAddNewCustomer = (TextView) findViewById(R.id.txt_add_new_customer);
        txtConstChulha = (TextView)findViewById(R.id.txt_const_chulha);
        txtCollectCash = (TextView)findViewById(R.id.txt_collect_cash);
        addCustomer_layout=(LinearLayout)findViewById(R.id.addCustomerLayout);
        constructChulha_layout=(LinearLayout)findViewById(R.id.constructChulhaLayout);
        payment_layout=(LinearLayout)findViewById(R.id.paymentLayout);
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            mainMenusToolbar.setTitle(getResources().getString(R.string.customer_agreement));

            txtAddNewCustomer.setText(getResources().getString(R.string.new_customer));
            txtAddNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,15));
            txtCollectCash.setText(getResources().getString(R.string.do_payment_marathi));
            txtCollectCash.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,15));
            txtConstChulha.setText(getResources().getString(R.string.chullaha_construction_marathi));
            txtConstChulha.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,15));
        }
        else
        {
            mainMenusToolbar.setTitle(getResources().getString(R.string.menu_english));

            txtAddNewCustomer.setText("New Customer");
            txtAddNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,12));
            txtCollectCash.setText("Collect Cash");
            txtCollectCash.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,12));
            txtConstChulha.setText("Construct Chulha");
            txtConstChulha.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,12));
        }
    }

    private void clickListner()
    {
        addCustomer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                AddCustomerFragment addKitchenSuitabilityFragment = new AddCustomerFragment();
                fragmentTransaction.replace(R.id.frame_layout, addKitchenSuitabilityFragment).addToBackStack(null).commit();
            }
        });

        constructChulha_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CustomerListFragment customerListFragment = CustomerListFragment.getInstance(CustomerListFragment.FROM_CONSTRUCTION);
                fragmentTransaction.replace(R.id.frame_layout, customerListFragment).addToBackStack(null).commit();

            }
        });

        payment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CustomerListFragment customerListFragment = CustomerListFragment.getInstance(CustomerListFragment.FROM_PAYMENT);
                fragmentTransaction.replace(R.id.frame_layout, customerListFragment).addToBackStack(null).commit();
            }
        });
    }
}
