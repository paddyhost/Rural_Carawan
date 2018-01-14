package com.hatchers.ruralcaravane.customer_registration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.customer_registration.adapter.CustomerTabAdapter;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.runtime_permissions.RuntimePermissions;
import com.hatchers.ruralcaravane.user_login.LoginActivity;
import com.hatchers.ruralcaravane.user_login.UserDetailsFragment;


public class CustomerRegistrationActivity extends AppCompatActivity
{
    private Toolbar customer_toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    PrefManager prefManager;
    FragmentTransaction fragmentTransaction;
    CustomerListFragment customerListFragment;
    private int[] tabIcons = {
            R.drawable.customer_list,
            R.drawable.add_customer
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        initializations();
        SetupViewPager();
        setupTabIcons();
        tabListener();
        viewPagerListener();
        clickListener();


        RuntimePermissions.checkCameraPermission(CustomerRegistrationActivity.this);
        RuntimePermissions.checkReadExternalStoragePermission(CustomerRegistrationActivity.this);
        RuntimePermissions.checkWriteExternalStoragePermission(CustomerRegistrationActivity.this);
    }

    private void setupTabIcons()
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void initializations()
    {
        customer_toolbar = (Toolbar)findViewById(R.id.customer_toolbar);
        setSupportActionBar(customer_toolbar);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs_layout);

        prefManager=new PrefManager(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window =getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

    }

    private void SetupViewPager()
    {
        customerListFragment=   new CustomerListFragment();
        CustomerTabAdapter adapter = new CustomerTabAdapter(getSupportFragmentManager());
        adapter.addFragment(customerListFragment, "Customer List");
        adapter.addFragment(new AddCustomerFragment(), "Add Customer");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

}

    private void tabListener()
    {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               if(tab.getPosition()==0)
               {
                   customerListFragment.setData();
               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void viewPagerListener()
    {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if(position==0)
                {
                    customerListFragment.setData();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void clickListener()
    {
        /*customer_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.profile:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                UserDetailsFragment userDetailsFragment=new UserDetailsFragment();
                fragmentTransaction.replace(R.id.frame_layout2,userDetailsFragment).addToBackStack(null).commit();
                break;

            case R.id.logout:
                prefManager.setLogOut();
                Intent i= new Intent(CustomerRegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {

            case RuntimePermissions.MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RuntimePermissions.checkReadExternalStoragePermission(CustomerRegistrationActivity.this);

                            }
                        }, 500);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }


            case RuntimePermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RuntimePermissions.checkWriteExternalStoragePermission(CustomerRegistrationActivity.this);

                            }
                        }, 500);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

            case RuntimePermissions.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 500);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }

    }

}
