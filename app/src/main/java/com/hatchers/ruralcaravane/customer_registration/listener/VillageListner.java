package com.hatchers.ruralcaravane.customer_registration.listener;

/**
 * Created by Ashwin on 07-Jan-18.
 */

public interface VillageListner
{
    void onVillage_Add_Success();

    void onVillage_Add_Failed();

    void onVillage_Add_Response_Failed();

    void onVillage_Add_Json_Error();

    void onVillage_Add_No_Connection_Error();

    void onVillage_Add_Server_Error();

    void onVillage_Add_Network_Error();

    void onVillage_Add_Parse_Error();

    void onVillage_Add_Unknown_Error();
}
