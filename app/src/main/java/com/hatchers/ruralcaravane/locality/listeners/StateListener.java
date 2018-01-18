package com.hatchers.ruralcaravane.locality.listeners;

/**
 * Created by Nikam on 15/01/2018.
 */

public interface StateListener {

    void onState_Add_Success();

    void onState_Add_Failed();

    void onState_Add_Response_Failed();

    void onState_Add_Json_Error();

    void onState_Add_No_Connection_Error();

    void onState_Add_Server_Error();

    void onState_Add_Network_Error();

    void onState_Add_Parse_Error();

    void onState_Add_Unknown_Error();
}
