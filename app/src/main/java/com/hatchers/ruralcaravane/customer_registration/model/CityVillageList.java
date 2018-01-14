package com.hatchers.ruralcaravane.customer_registration.model;

import com.hatchers.ruralcaravane.customer_registration.listener.CityListner;
import com.hatchers.ruralcaravane.customer_registration.listener.VillageListner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public class CityVillageList
{
    private ArrayList<City> cityListArrayList;
    private ArrayList<Village> villageArrayList;

    public ArrayList<City> getCityListArrayList() {
        return cityListArrayList;
    }

    public void setCityListArrayList(ArrayList<City> cityListArrayList) {
        this.cityListArrayList = cityListArrayList;
    }
    public ArrayList<Village> getVillageArrayList() {
        return villageArrayList;
    }

    public void setVillageArrayList(ArrayList<Village> villageArrayList) {
        this.villageArrayList = villageArrayList;
    }


    ///events
    static final public int CITY_ADD_SUCCESS=0,CITY_ADD_FAILED=1,CITY_ADD_RESPONSE_FAILED=2,
            CITY_ADD_JSON_ERROR=3,CITY_ADD_NO_CONNECTION_ERROR=4,CITY_ADD_SERVER_ERROR=5,
            CITY_ADD_NEWORK_ERROR=6,CITY_ADD_PARSE_ERROR=7, CITY_ADD_UNKNOWN_ERROR=8;


    private List<CityListner> cityEvents = new ArrayList<CityListner>();

    public void setOnCityEvent(CityListner toAdd) {

        cityEvents.add(toAdd);
    }
    public void fireOnCityEvent(int event) {

        for (CityListner hl : cityEvents) {

            if(event==CITY_ADD_SUCCESS)
            {
                hl.onCity_Add_Success();
            }
            else if(event==CITY_ADD_FAILED)
            {
                hl.onCity_Add_Failed();
            }
            else if(event==CITY_ADD_RESPONSE_FAILED)
            {
                hl.onCity_Add_Response_Failed();
            }
            else if(event==CITY_ADD_JSON_ERROR)
            {
                hl.onCity_Add_Json_Error();
            }
            else if(event==CITY_ADD_NO_CONNECTION_ERROR)
            {
                hl.onCity_Add_No_Connection_Error();
            }
            else if(event==CITY_ADD_SERVER_ERROR)
            {
                hl.onCity_Add_Server_Error();
            }
            else if(event==CITY_ADD_NEWORK_ERROR)
            {
                hl.onCity_Add_Network_Error();
            }
            else if(event==CITY_ADD_PARSE_ERROR)
            {
                hl.onCity_Add_Parse_Error();
            }
            else if(event==CITY_ADD_UNKNOWN_ERROR)
            {
                hl.onCity_Add_Unknown_Error();
            }


        }
    }

    ///events
    static final public int VILLAGE_ADD_SUCCESS=0,VILLAGE_ADD_FAILED=1,VILLAGE_ADD_RESPONSE_FAILED=2,
            VILLAGE_ADD_JSON_ERROR=3,VILLAGE_ADD_NO_CONNECTION_ERROR=4,VILLAGE_ADD_SERVER_ERROR=5,
            VILLAGE_ADD_NEWORK_ERROR=6,VILLAGE_ADD_PARSE_ERROR=7, VILLAGE_ADD_UNKNOWN_ERROR=8;


    private List<VillageListner> villageEvents = new ArrayList<VillageListner>();

    public void setOnVillageEvent(VillageListner toAdd) {

        villageEvents.add(toAdd);
    }
    public void fireOnVillageEvent(int event) {

        for (VillageListner hl : villageEvents) {

            if(event==VILLAGE_ADD_SUCCESS)
            {
                hl.onVillage_Add_Success();
            }
            else if(event==VILLAGE_ADD_FAILED)
            {
                hl.onVillage_Add_Failed();
            }
            else if(event==VILLAGE_ADD_RESPONSE_FAILED)
            {
                hl.onVillage_Add_Response_Failed();
            }
            else if(event==VILLAGE_ADD_JSON_ERROR)
            {
                hl.onVillage_Add_Json_Error();
            }
            else if(event==VILLAGE_ADD_NO_CONNECTION_ERROR)
            {
                hl.onVillage_Add_No_Connection_Error();
            }
            else if(event==VILLAGE_ADD_SERVER_ERROR)
            {
                hl.onVillage_Add_Server_Error();
            }
            else if(event==VILLAGE_ADD_NEWORK_ERROR)
            {
                hl.onVillage_Add_Network_Error();
            }
            else if(event==VILLAGE_ADD_PARSE_ERROR)
            {
                hl.onVillage_Add_Parse_Error();
            }
            else if(event==VILLAGE_ADD_UNKNOWN_ERROR)
            {
                hl.onVillage_Add_Unknown_Error();
            }


        }
    }

}
