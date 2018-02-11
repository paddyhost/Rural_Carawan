package com.hatchers.ruralcaravane.user_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.MainMenus;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.customer_registration.CustomerRegistrationActivity;
import com.hatchers.ruralcaravane.locality.apihelper.Locality_Web_ApiHelper;
import com.hatchers.ruralcaravane.locality.listeners.CityListener;
import com.hatchers.ruralcaravane.locality.listeners.StateListener;
import com.hatchers.ruralcaravane.locality.listeners.VillageListner;
import com.hatchers.ruralcaravane.locality.model.Locality;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.user_login.apihelper.Login_ApiHelper;
import com.hatchers.ruralcaravane.user_login.listener.LoginListner;
import com.hatchers.ruralcaravane.user_login.model.LoginUser;
import com.hatchers.ruralcaravane.utils.Utility;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText edtName, edtPassword;
    private PrefManager prefManager;
    private SweetAlertDialog sweetAlertDialog;
    private SweetAlertDialog localityDialog;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        setLanguageToUI();
        clickListners();

    }

    private void initialization()
    {
        prefManager = new PrefManager(this);
        edtName = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginBtn);
        txtLogin=(TextView)findViewById(R.id.txt_login);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }
    }

    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            txtLogin.setText(getResources().getString(R.string.login));
            txtLogin.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,12));

            edtName.setHint(getResources().getString(R.string.username));
            edtName.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,8));
            edtName.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            edtPassword.setHint(getResources().getString(R.string.password));
            edtPassword.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,8));
            edtPassword.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            login.setText(getResources().getString(R.string.login));
            login.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,12));

        }
        else
        {
            txtLogin.setText(getResources().getString(R.string.login1));
            txtLogin.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,12));

            edtName.setHint(getResources().getString(R.string.username1));
            edtName.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,8));
            edtName.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            edtPassword.setHint(getResources().getString(R.string.password1));
            edtPassword.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,8));
            edtPassword.setHintTextColor(getResources().getColor(R.color.DarkGrey));

            login.setText(getResources().getString(R.string.login1));
            login.setTextSize(Utility.getConvertFloatToDP(LoginActivity.this,10));

        }
    }


    private void clickListners()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    setUserInfo();
                    sweetAlertDialog =new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                        sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
                    }
                    else
                    {
                        sweetAlertDialog.setTitleText(getResources().getString(R.string.please_wait_english));
                    }
                    sweetAlertDialog.show();
                    LoginUser loginUser = new LoginUser();
                    Login_ApiHelper.userLoginApi(LoginActivity.this,loginUser);
                    getloginEvent(loginUser);
                }
            }
        });
    }

    private void setUserInfo()
    {
        prefManager.setUserName(edtName.getText().toString());
        prefManager.setPassword(edtPassword.getText().toString());
    }

    private boolean checkValidation()
    {
        if (edtName.getText().toString().equalsIgnoreCase(""))
        {
            if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_username_marathi), Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_username_english), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        else if( edtPassword.getText().toString().equalsIgnoreCase(""))
        {
            if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_password_marathi), Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_password_english), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    private void getloginEvent(LoginUser loginUser)
    {
        loginUser.setOnLoginEvent(new LoginListner() {
            @Override
            public void onLogin_Success()
            {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_successfully_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_successfully_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        localityDialog =new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                            localityDialog.setTitleText(getResources().getString(R.string.please_wait_marathi));
                        }
                        else
                        {
                            localityDialog.setTitleText(getResources().getString(R.string.please_wait_english));
                        }
                        localityDialog.show();

                        //call city list api
                        Locality stateList=new Locality();
                        Locality_Web_ApiHelper.getStateList(LoginActivity.this,stateList);
                        setStateEvent(stateList);

                        sweetAlertDialog.dismissWithAnimation();

                    }
                });

            }

            @Override
            public void onLogin_Failed() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_failed_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                    else{
                        sweetAlertDialog.setTitleText(getResources().getString(R.string.login_failed_english));
                        sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                    }

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_Response_Failed() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);

                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {

                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_failed_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));

                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_failed_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));

                }
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

            }

            @Override
            public void onLogin_Json_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_error_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.login_error_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));

                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_No_Connection_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.check_internet_connection_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.check_internet_connection_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_Server_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.server_error_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.server_error_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_Network_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.network_error_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.network_error_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_Parse_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.parse_error_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.parse_error_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }

            @Override
            public void onLogin_Unknown_Error() {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);

                if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI)) {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.unknown_error_marathi));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_marathi));
                }
                else
                {
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.unknown_error_english));
                    sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok_english));
                }

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }
        });
    }

    private void setCityEvent(final Locality cityList)
    {
        cityList.setOnCityEvent(new CityListener() {
            @Override
            public void onCity_Add_Success()
            {
                //call village list api
                Locality villageList=new Locality();
                Locality_Web_ApiHelper.getVillageList(LoginActivity.this,villageList);
                setVillageEvent(villageList);
            }

            @Override
            public void onCity_Add_Failed() {
                //Toast.makeText(LoginActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Response_Failed() {
                //Toast.makeText(LoginActivity.this,"Response Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Json_Error() {
               // Toast.makeText(LoginActivity.this,"Json Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_No_Connection_Error() {
               // Toast.makeText(LoginActivity.this,"Check net connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Server_Error() {
              //  Toast.makeText(LoginActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Network_Error() {
               // Toast.makeText(LoginActivity.this,"Network error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Parse_Error() {
               // Toast.makeText(LoginActivity.this,"Parse error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCity_Add_Unknown_Error() {
              //  Toast.makeText(LoginActivity.this,"Unknown Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVillageEvent(final Locality villageList)
    {
        villageList.setOnVillageEvent(new VillageListner() {
            @Override
            public void onVillage_Add_Success() {

                localityDialog.dismissWithAnimation();
                Intent intent = new Intent(LoginActivity.this, MainMenus.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onVillage_Add_Failed() {
                //   Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Response_Failed() {
               // Toast.makeText(getActivity(),"Response Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Json_Error() {
               // Toast.makeText(getActivity(),"Json Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_No_Connection_Error() {
               // Toast.makeText(getActivity(),"Check net Connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Server_Error() {
               // Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Network_Error() {
              //  Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Parse_Error() {
             //   Toast.makeText(getActivity(),"Parse Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVillage_Add_Unknown_Error() {
               // Toast.makeText(getActivity(),"Unknown Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStateEvent(final Locality stateList)
    {
        stateList.setOnStateEvent(new StateListener() {
            @Override
            public void onState_Add_Success() {
                //call city list api
                Locality cityList=new Locality();
                Locality_Web_ApiHelper.getCityList(LoginActivity.this,cityList);
                setCityEvent(cityList);

            }

            @Override
            public void onState_Add_Failed() {
               // Toast.makeText(LoginActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Response_Failed() {
              //  Toast.makeText(LoginActivity.this,"Response Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Json_Error() {
               // Toast.makeText(LoginActivity.this,"JSON Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_No_Connection_Error() {
              //  Toast.makeText(LoginActivity.this,"No Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Server_Error() {
              //  Toast.makeText(LoginActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Network_Error() {
               // Toast.makeText(LoginActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Parse_Error() {
               // Toast.makeText(LoginActivity.this,"Parse Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onState_Add_Unknown_Error() {
               // Toast.makeText(LoginActivity.this,"Unknown Error",Toast.LENGTH_SHORT).show();
            }

        });
    }

}
