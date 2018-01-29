package com.hatchers.ruralcaravane.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.AddCustomerFragment;
import com.hatchers.ruralcaravane.customer_registration.CustomerListFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.AddKitchenSuitabilityFragment;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

public class MainMenus extends AppCompatActivity
{
    private Button btnNewCustomer, btnConsturctChulha, btnCollectCash;
    private PrefManager prefManager;
    private Toolbar mainMenusToolbar;

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
        btnNewCustomer = (Button)findViewById(R.id.btn_add_new_cust);
        btnConsturctChulha = (Button)findViewById(R.id.btn_const_chulha);
        btnCollectCash = (Button)findViewById(R.id.btn_collect_cash);
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            mainMenusToolbar.setTitle(getResources().getString(R.string.customer_agreement));

            btnNewCustomer.setText(getResources().getString(R.string.new_customer));
            btnNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
            btnCollectCash.setText(getResources().getString(R.string.do_payment));
            btnCollectCash.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
            btnConsturctChulha.setText(getResources().getString(R.string.chullaha_construction));
            btnConsturctChulha.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
        }
        else
        {
            mainMenusToolbar.setTitle(getResources().getString(R.string.menu_english));

            btnNewCustomer.setText("New Customer");
            btnNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
            btnCollectCash.setText("Collect Cash");
            btnNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
            btnConsturctChulha.setText("Construct Chulha");
            btnNewCustomer.setTextSize(Utility.getConvertFloatToDP(MainMenus.this,20));
        }
    }

    private void clickListner()
    {
        btnNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                AddCustomerFragment addKitchenSuitabilityFragment = new AddCustomerFragment();
                fragmentTransaction.replace(R.id.frame, addKitchenSuitabilityFragment).addToBackStack(null).commit();
            }
        });

        btnConsturctChulha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CustomerListFragment customerListFragment = CustomerListFragment.getInstance(CustomerListFragment.FROM_CONSTRUCTION);
                fragmentTransaction.replace(R.id.frame, customerListFragment).addToBackStack(null).commit();
            }
        });

        btnCollectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CustomerListFragment customerListFragment = CustomerListFragment.getInstance(CustomerListFragment.FROM_PAYMENT);
                fragmentTransaction.replace(R.id.frame, customerListFragment).addToBackStack(null).commit();
            }
        });
    }
}
