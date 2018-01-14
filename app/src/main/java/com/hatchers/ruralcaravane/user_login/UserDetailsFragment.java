package com.hatchers.ruralcaravane.user_login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;


public class UserDetailsFragment extends Fragment
{
    PrefManager prefManager;
    private ImageView userDetails_btnBack;
    private TextView name/*,city_name*/,mobile_number,village_name,gender;
    private Toolbar userDetailsToolbar;
    public UserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_user__details, container, false);

        initialization(view);
        setUserDetails();
        clickListeners();

        return view;
    }

    private void initialization(View view)
    {
        prefManager=new PrefManager(getActivity());
        ((AppCompatActivity)getActivity()).setSupportActionBar(userDetailsToolbar);
        userDetailsToolbar=(Toolbar) view.findViewById(R.id.userDetailsToolbar);
        userDetails_btnBack = (ImageView)view.findViewById(R.id.userDetails_btnBack);
        name=(TextView)view.findViewById(R.id.name);
        //city_name=(TextView)view.findViewById(R.id.city_name);
        mobile_number=(TextView)view.findViewById(R.id.mobile_number);
        village_name=(TextView)view.findViewById(R.id.village_name);
        gender=(TextView)view.findViewById(R.id.gender);

    }

    private void clickListeners()
    {
        userDetails_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setUserDetails()
    {
        name.setText(String.valueOf("  " + prefManager.getUserName()));
        //city_name.setText(String.valueOf("  " + prefManager.getCityname()));
        mobile_number.setText(String.valueOf("  " + prefManager.getMobile()));
        village_name.setText(String.valueOf("  " + prefManager.getVillageName()));
        gender.setText(String.valueOf("  " + prefManager.getGender()));
    }

    /*private void passwordClick()
    {
        visiblePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordTxt.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    passwordTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                    visiblePassword.setImageResource(R.drawable.eye_hidden);
                }
                else
                {
                    passwordTxt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visiblePassword.setImageResource(R.drawable.eye_show);

                }
            }
        });
    }*/
}
