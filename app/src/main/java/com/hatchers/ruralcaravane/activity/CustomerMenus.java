package com.hatchers.ruralcaravane.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.R;


public class CustomerMenus extends AppCompatActivity {
    private  CustomerTable customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
       customer=  getIntent().getParcelableExtra(CustomerTable.CUSTOMER_TABLE);
        callMenuFragment();
    }


    private void callMenuFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MenuFragment menuFragment = MenuFragment.getInstance(customer);
        fragmentTransaction.replace(R.id.frame_layout,menuFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

