package com.hatchers.ruralcaravane.customer_registration.listener;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public interface CityListner
{
    void onCity_Add_Success();

    void onCity_Add_Failed();

    void onCity_Add_Response_Failed();

    void onCity_Add_Json_Error();

    void onCity_Add_No_Connection_Error();

    void onCity_Add_Server_Error();

    void onCity_Add_Network_Error();

    void onCity_Add_Parse_Error();

    void onCity_Add_Unknown_Error();
}
