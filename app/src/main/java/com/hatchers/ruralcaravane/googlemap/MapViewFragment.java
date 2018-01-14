package com.hatchers.ruralcaravane.googlemap;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hatchers.ruralcaravane.R;
import com.hatchers.ruralcaravane.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapViewFragment extends Fragment {
    MapView mapView;
    GoogleMap map;
    double lang, lat;
    String adress;
    LatLng latLng;

    public MapViewFragment() {

    }

    @SuppressLint("ValidFragment")
    public MapViewFragment(double lang, double lat) {
        this.lang = lang;
        this.lat = lat;
    }

    @SuppressLint("ValidFragment")
    public MapViewFragment(String address) {
        this.adress = address;
        //
    }

    @SuppressLint("ValidFragment")
    public MapViewFragment(LatLng latLng, String address) {
        lang = latLng.latitude;
        lat = latLng.latitude;
        this.latLng = latLng;
        this.adress = address;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(false);
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION );

                        return;
                    }
                    else
                    {
                        map.setMyLocationEnabled(true);
                    }

                } catch (Exception e) {

                }
                MapsInitializer.initialize(getActivity());
                if (latLng != null) {
                    setMapViewByLangLat(adress, latLng);
                    ;
                } else {
                    setMapViewByAddress(adress);
                }
            }
        });

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls


        // Updates the location and zoom of the MapView
    /*    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lang,lat), 10);
        map.animateCamera(cameraUpdate);*/

        return v;
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                try {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            map.setMyLocationEnabled(true);
                        }

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                catch (Exception e)
                {
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setMapViewByAddress(final String addresss) {
        String addresss1 = addresss.replace(" ", "%20d");
        String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + addresss1 + "&sensor=true";
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONArray jarrayresult = result.getJSONArray("results");
                    JSONObject obj = jarrayresult.getJSONObject(0);
                    JSONObject geometry = obj.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    Double lat = Double.parseDouble(location.getString("lat"));
                    Double lang = Double.parseDouble(location.getString("lng"));

                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lang)).title(addresss));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 15);
                    map.animateCamera(cameraUpdate);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {


        };

        MyApplication.getInstance().addToRequestQueue(strReq);


    }


    private void setMapViewByLangLat(final String addresss, LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng).title(addresss));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        map.animateCamera(cameraUpdate);


    }


}