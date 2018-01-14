package com.hatchers.ruralcaravane.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hatchers.ruralcaravane.customer_registration.CustomerRegistrationActivity;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.user_login.LoginActivity;

public class SplashActivity extends AppCompatActivity
{
    PrefManager prefManager;
    int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializations();
    }

    private void initializations()
    {
        prefManager = new PrefManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                check();
            }
        }, SPLASH_TIME_OUT);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window =getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void check()
    {
        if (prefManager.isLoggedIn()) {

            Intent intent=new Intent(SplashActivity.this, CustomerRegistrationActivity.class);
            startActivity(intent);
            finish();

        }
        else
        {
            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
