package com.hatchers.ruralcaravane.activity;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.activity.apihelper.Web_SyncApiHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.file.FileHelper;
import com.hatchers.ruralcaravane.file.FileType;
import com.hatchers.ruralcaravane.file.Folders;
import com.hatchers.ruralcaravane.kitchen_suitability.KitchenConstructionFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.AddKitchenSuitabilityFragment;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.payment_details.PaymentDetailsListFragment;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper.getKitchenDetailsData;


public class MenuFragment extends Fragment implements View.OnClickListener{

    private Toolbar menuToolbar;
    PrefManager prefManager;
    private  FragmentTransaction fragmentTransaction;
    private Button kitchen_linear,payment_linear,sync_dataBtn;
    private CustomerTable customertable;
    private KitchenTable kitchenTable;
    private TextView customername_txt, customeraddress_txt, mobile_txt;
    private CircleImageView profImageView;

    public MenuFragment()
    {
        // Required empty public constructor
    }

    public static MenuFragment getInstance(CustomerTable customertable)
    {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            customertable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu, container, false);

        initialization(view);
        onClickListeners();
        setCustomerData();


        return view;
    }


    private void initialization(View view)
    {
        ((AppCompatActivity)getActivity()).setSupportActionBar(menuToolbar);
        menuToolbar = (Toolbar)view.findViewById(R.id.menuToolbar);
        prefManager=new PrefManager(getActivity());
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        customername_txt = (TextView)view.findViewById(R.id.customername_txt);
        customeraddress_txt = (TextView)view.findViewById(R.id.customeraddress_txt);
        mobile_txt = (TextView)view.findViewById(R.id.mobile_txt);

        profImageView = (CircleImageView)view.findViewById(R.id.customerImage);

        kitchen_linear=(Button)view.findViewById(R.id.kitchen_linear);
        payment_linear=(Button)view.findViewById(R.id.payment_linear);
        sync_dataBtn=(Button)view.findViewById(R.id.sync_data);

        kitchen_linear.setOnClickListener(this);
        payment_linear.setOnClickListener(this);
        sync_dataBtn.setOnClickListener(this);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window =getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }

    }

    private  void onClickListeners()
    {
        menuToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.kitchen_linear:

                    ArrayList<KitchenTable> kitchenTableArrayList=KitchenTableHelper.getKitchenDataList(getContext(),customertable);

                if (kitchenTableArrayList != null)
                {
                    if(kitchenTableArrayList.size()<=0)
                    {
                        AddKitchenSuitabilityFragment addKitchenSuitabilityFragment = AddKitchenSuitabilityFragment.getInstance(customertable);
                        fragmentTransaction.replace(R.id.frame_layout, addKitchenSuitabilityFragment).addToBackStack(null).commit();
                    }
                    else
                    {
                        kitchenTable = KitchenTableHelper.getKitchenDetailsData(getActivity(), customertable.getUniqueIdValue());
                        KitchenConstructionFragment kitchenConstructionFragment = KitchenConstructionFragment.getInstance(kitchenTable);
                        fragmentTransaction.replace(R.id.frame_layout, kitchenConstructionFragment).addToBackStack(null).commit();
                    }
                }
                break;


            case R.id.payment_linear:
               KitchenTable kitchen= getKitchenDetailsData(getActivity(),customertable.getUniqueIdValue());
                if(kitchen==null)
                {
                    Toast.makeText(getActivity(), "Please add kitchen Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PaymentDetailsListFragment paymentDetailsListFragment = PaymentDetailsListFragment.getInstance(customertable);
                    fragmentTransaction.replace(R.id.frame_layout, paymentDetailsListFragment).addToBackStack(null).commit();
                }
                break;

            case R.id.sync_data:
               /* SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText("Please wait");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
                Web_SyncApiHelper.addNewCustomerToServer(getActivity(),sweetAlertDialog,customertable);
               */

               break;

            default:
                break;
        }
    }

    private void setCustomerData()
    {
        customername_txt.setText(String.valueOf(customertable.getCustomerNameValue()));
        customeraddress_txt.setText(String.valueOf(customertable.getCustomerAddressValue()));
        mobile_txt.setText(String.valueOf(customertable.getCustomerMobilenoValue()));

        Glide.with(getActivity())
                    .load(customertable.getImagePathValue())
                    .error(R.drawable.user_profile)
                    .into(profImageView);

    }

}
