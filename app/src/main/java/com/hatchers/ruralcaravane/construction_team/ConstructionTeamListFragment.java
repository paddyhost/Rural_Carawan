package com.hatchers.ruralcaravane.construction_team;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hatchers.ruralcaravane.construction_team.adapter.ConstructionListAdapter;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTable;
import com.hatchers.ruralcaravane.construction_team.database.ConstructionTableHelper;
import com.hatchers.ruralcaravane.customer_registration.database.CustomerTable;
import com.hatchers.ruralcaravane.R;

import java.util.ArrayList;


public class ConstructionTeamListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Toolbar constructionTeamListToolbar;
    private FloatingActionButton add_construction;
    private RecyclerView constructionRecyclerView;
    private FragmentTransaction fragmentTransaction;
    private ConstructionListAdapter constructionListAdapter;

    ArrayList<ConstructionTable> constructionTables;
    ImageView backImg;


    private CustomerTable customertable;
    public static ConstructionTeamListFragment getInstance(CustomerTable customertable)
    {
        ConstructionTeamListFragment fragment = new ConstructionTeamListFragment();
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

    public ConstructionTeamListFragment() {
        // Required empty public constructor
    }

      // TODO: Rename and change types and number of parameters
    public static ConstructionTeamListFragment newInstance(String param1, String param2) {
        ConstructionTeamListFragment fragment = new ConstructionTeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_construction_team_list, container, false);

        initializations(view);
        onClickListeners();

        constructionRecyclerView = (RecyclerView) view.findViewById(R.id.constructionRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        constructionRecyclerView.setLayoutManager(layoutManager);

        constructionTables= ConstructionTableHelper.getConstructionTeamList(getContext());
        constructionListAdapter=new ConstructionListAdapter(getContext(),constructionTables);

        constructionRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        constructionRecyclerView.setItemAnimator(new DefaultItemAnimator());

        constructionRecyclerView.setAdapter(constructionListAdapter);
        constructionListAdapter.notifyDataSetChanged();

        return  view;
    }

    private void initializations(View view)
    {
        backImg = (ImageView)view.findViewById(R.id.const_team_back);
        constructionTeamListToolbar=(Toolbar)view.findViewById(R.id.constructionTeamListToolbar);
        add_construction=(FloatingActionButton)view.findViewById(R.id.add_construction);
        constructionRecyclerView=(RecyclerView)view.findViewById(R.id.constructionRecyclerView);
    }

    private void onClickListeners()
    {
        add_construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ConstructionTeamRegistrationFragment constructionTeamRegistrationFragment=new ConstructionTeamRegistrationFragment();
                fragmentTransaction.replace(R.id.frame_layout,constructionTeamRegistrationFragment).addToBackStack(null).commit();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

}
