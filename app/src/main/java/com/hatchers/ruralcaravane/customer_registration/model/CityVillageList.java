package com.hatchers.ruralcaravane.customer_registration.model;

import com.hatchers.ruralcaravane.customer_registration.listener.CityListener;
import com.hatchers.ruralcaravane.customer_registration.listener.StateListener;
import com.hatchers.ruralcaravane.customer_registration.listener.VillageListner;

import java.util.ArrayList;
import java.util.List;



public class CityVillageList
{
    private ArrayList<CityTable> cityListArrayList;
    private ArrayList<VillageTable> villageArrayList;
    private ArrayList<StateTable> stateArrayList;

    public ArrayList<CityTable> getCityListArrayList() {
        return cityListArrayList;
    }

    public void setCityListArrayList(ArrayList<CityTable> cityListArrayList) {
        this.cityListArrayList = cityListArrayList;
    }
    public ArrayList<VillageTable> getVillageArrayList() {
        return villageArrayList;
    }

    public void setVillageArrayList(ArrayList<VillageTable> villageArrayList) {
        this.villageArrayList = villageArrayList;
    }

    public ArrayList<StateTable> getStateArrayList() {
        return stateArrayList;
    }

    public void setStateArrayList(ArrayList<StateTable> stateArrayList) {
        this.stateArrayList = stateArrayList;
    }

    ///events
    static final public int CITY_ADD_SUCCESS=0,CITY_ADD_FAILED=1,CITY_ADD_RESPONSE_FAILED=2,
            CITY_ADD_JSON_ERROR=3,CITY_ADD_NO_CONNECTION_ERROR=4,CITY_ADD_SERVER_ERROR=5,
            CITY_ADD_NEWORK_ERROR=6,CITY_ADD_PARSE_ERROR=7, CITY_ADD_UNKNOWN_ERROR=8;


    private List<CityListener> cityEvents = new ArrayList<CityListener>();

    public void setOnCityEvent(CityListener toAdd) {

        cityEvents.add(toAdd);
    }

    public void fireOnCityEvent(int event) {

        for (CityListener hl : cityEvents) {

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

    ///events
    static final public int STATE_ADD_SUCCESS=0,STATE_ADD_FAILED=1,STATE_ADD_RESPONSE_FAILED=2,
            STATE_ADD_JSON_ERROR=3,STATE_ADD_NO_CONNECTION_ERROR=4,STATE_ADD_SERVER_ERROR=5,
            STATE_ADD_NEWORK_ERROR=6,STATE_ADD_PARSE_ERROR=7, STATE_ADD_UNKNOWN_ERROR=8;


    private List<StateListener> stateEvents = new ArrayList<StateListener>();

    public void setOnStateEvent(StateListener toAdd) {

        stateEvents.add(toAdd);
    }

    public void fireOnStateEvent(int event) {

        for (StateListener h2 : stateEvents) {

            if(event==STATE_ADD_SUCCESS)
            {
                h2.onState_Add_Success();
            }
            else if(event==STATE_ADD_FAILED)
            {
                h2.onState_Add_Failed();
            }
            else if(event==STATE_ADD_RESPONSE_FAILED)
            {
                h2.onState_Add_Response_Failed();
            }
            else if(event==STATE_ADD_JSON_ERROR)
            {
                h2.onState_Add_Json_Error();
            }
            else if(event==STATE_ADD_NO_CONNECTION_ERROR)
            {
                h2.onState_Add_No_Connection_Error();
            }
            else if(event==STATE_ADD_SERVER_ERROR)
            {
                h2.onState_Add_Server_Error();
            }
            else if(event==STATE_ADD_NEWORK_ERROR)
            {
                h2.onState_Add_Network_Error();
            }
            else if(event==STATE_ADD_PARSE_ERROR)
            {
                h2.onState_Add_Parse_Error();
            }
            else if(event==STATE_ADD_UNKNOWN_ERROR)
            {
                h2.onState_Add_Unknown_Error();
            }


            
        }
    }

}
