package com.hatchers.ruralcaravane.kitchen_suitability;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hatchers.ruralcaravane.R;

public class KitchenDetailsFragment extends Fragment {
    public KitchenDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_kitchen_details, container, false);
        return view;
    }

  }
