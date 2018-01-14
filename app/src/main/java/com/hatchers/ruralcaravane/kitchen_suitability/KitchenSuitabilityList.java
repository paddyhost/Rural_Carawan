package com.hatchers.ruralcaravane.kitchen_suitability;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTableHelper;
import com.hatchers.ruralcaravane.kitchen_suitability.adapter.KitchenListAdapter;
import com.hatchers.ruralcaravane.R;

import java.util.ArrayList;


public class KitchenSuitabilityList extends Fragment {

    private FloatingActionButton btnfloatadd;
    private Toolbar kitchenListToolbar;
    private RecyclerView kitchenRecyclerView;
    KitchenListAdapter kitchenListAdapter;
    FragmentTransaction fragmentTransaction;
    ArrayList<KitchenTable> kitchenTables;
    ImageView backImg;

    public KitchenSuitabilityList() {
       // Required empty public constructor
    }
    private CustomerTable customertable;
    public static KitchenSuitabilityList getInstance(CustomerTable customertable)
    {
        KitchenSuitabilityList fragment = new KitchenSuitabilityList();
        Bundle args = new Bundle();
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customertable);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            customertable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_kitchen_suitability_list, container, false);
        initializations(view);
        onClickListeners();
         return view;
    }
    private void initializations(View view)
    {
        ((AppCompatActivity)getActivity()).setSupportActionBar(kitchenListToolbar);

        btnfloatadd=(FloatingActionButton)view.findViewById(R.id.kitchenList);
        kitchenListToolbar=(Toolbar)view.findViewById(R.id.kitchenListToolbar);
        backImg = (ImageView)view.findViewById(R.id.back_kitchn_list);

        kitchenRecyclerView = (RecyclerView) view.findViewById(R.id.kitchenRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        kitchenRecyclerView.setLayoutManager(layoutManager);
        kitchenTables= KitchenTableHelper.getKitchenDataList(getContext(),customertable);
        kitchenListAdapter=new KitchenListAdapter(getContext(),kitchenTables);

        kitchenRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        kitchenRecyclerView.setItemAnimator(new DefaultItemAnimator());

        kitchenRecyclerView.setAdapter(kitchenListAdapter);
        kitchenListAdapter.notifyDataSetChanged();

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void onClickListeners()
    {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnfloatadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                KitchenSuitabilityFragment kitchenSuitabilityFragment=KitchenSuitabilityFragment.getInstance(customertable);
                fragmentTransaction.replace(R.id.frame_layout,kitchenSuitabilityFragment).addToBackStack(null).commit();

            }
        });
    }

}
