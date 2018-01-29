package com.hatchers.ruralcaravane.user_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.MainMenus;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText edtName, edtPassword;
    private PrefManager prefManager;
    private SweetAlertDialog sweetAlertDialog;
    private SweetAlertDialog localityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        clickListners();

    }

    private void initialization()
    {
        prefManager = new PrefManager(this);
        edtName = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginBtn);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }
    }

    private void clickListners()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    setUserInfo();
                    sweetAlertDialog =new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait");

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
            Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( edtPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
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
                sweetAlertDialog.setTitleText("Login Successfully");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        localityDialog =new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Please wait");
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
                sweetAlertDialog.setTitleText("Invalid Login");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Login Failed");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Login Error");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Check internet connection");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Server Error");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Network Error");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Parse Error");
                sweetAlertDialog.setConfirmText("Ok");
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
                sweetAlertDialog.setTitleText("Unknown Error");
                sweetAlertDialog.setConfirmText("Ok");
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
                Toast.makeText(LoginActivity.this,"Unknown Error",Toast.LENGTH_SHORT).show();
            }

        });
    }


}
