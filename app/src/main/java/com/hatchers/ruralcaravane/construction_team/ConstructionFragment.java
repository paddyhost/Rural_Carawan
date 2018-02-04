package com.hatchers.ruralcaravane.construction_team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.constants.AppConstants;
import com.hatchers.ruralcaravane.construction_team.adapter.ConstructionListAdapter;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.kitchen_suitability.database.KitchenTable;
import com.hatchers.ruralcaravane.pref_manager.PrefManager;
import com.hatchers.ruralcaravane.utils.Utility;

import java.util.ArrayList;

public class ConstructionFragment extends Fragment {

    private Toolbar constructionToolbar;
    private Button addConstructionMember;
    private RecyclerView recyclerConstruction;
    private TextView constructionLabel;

    private ConstructionListAdapter constructionListAdapter;
    private KitchenTable kitchenTable;
    private CustomerTable customerTable;
    ArrayList<ConstructionTable> constructionTableArrayList;
    private PrefManager prefManager;

    public static ConstructionFragment getInstance(KitchenTable kitchenTable, CustomerTable customerTable) {
        ConstructionFragment fragment = new ConstructionFragment();
        Bundle args = new Bundle();
        args.putParcelable(KitchenTable.KITCHEN_TABLE, kitchenTable);
        args.putParcelable(CustomerTable.CUSTOMER_TABLE, customerTable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kitchenTable = getArguments().getParcelable(KitchenTable.KITCHEN_TABLE);
            customerTable = getArguments().getParcelable(CustomerTable.CUSTOMER_TABLE);
        }

    }


    public ConstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_construction, container, false);
        initializations(view);
        setLanguageToUI();
        onClickListeners();
        return view;
    }


    private void setLanguageToUI()
    {
        if(prefManager.getLanguage().equalsIgnoreCase(AppConstants.MARATHI))
        {
            constructionToolbar.setTitle(R.string.construction_team_marathi);

            addConstructionMember.setText(getResources().getString(R.string.add_new_construction_team_member_marathi));
            addConstructionMember.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));

            constructionLabel.setText(getResources().getString(R.string.construction_team_member_marathi));
            constructionLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),12));


        }
        else
        {

            constructionToolbar.setTitle(R.string.construction_team_english);

            addConstructionMember.setText(getResources().getString(R.string.add_new_construction_team_member_english));
            addConstructionMember.setTextSize(Utility.getConvertFloatToDP(getActivity(),10));

            constructionLabel.setText(getResources().getString(R.string.construction_team_member_english));
            constructionLabel.setTextSize(Utility.getConvertFloatToDP(getActivity(),8));

        }
    }


    private void initializations(View view)
    {
        constructionToolbar=(Toolbar)view.findViewById(R.id.construction_toolbar);
        addConstructionMember=(Button)view.findViewById(R.id.add_construction_member);
        constructionLabel=(TextView)view.findViewById(R.id.desc_txt);

        prefManager=new PrefManager(getActivity());

        recyclerConstruction=(RecyclerView)view.findViewById(R.id.construction_recycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerConstruction.setLayoutManager(layoutManager);

        constructionTableArrayList= ConstructionTableHelper.getConstructionTeamList(getContext(),customerTable);
        constructionListAdapter=new ConstructionListAdapter(getContext(),constructionTableArrayList);

        recyclerConstruction.setItemAnimator(new DefaultItemAnimator());

        recyclerConstruction.setAdapter(constructionListAdapter);
        constructionListAdapter.notifyDataSetChanged();

    }

    private void onClickListeners()
    {
        constructionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addConstructionMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ConstructionTeamRegistrationFragment constructionTeamRegistrationFragment = ConstructionTeamRegistrationFragment.getInstance(kitchenTable);
                fragmentTransaction.replace(R.id.complete_construction_frame,constructionTeamRegistrationFragment).addToBackStack(null).commit();
            }
        });
    }
}
