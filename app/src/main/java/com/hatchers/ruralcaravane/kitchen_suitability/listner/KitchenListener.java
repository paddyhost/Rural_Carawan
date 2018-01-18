package com.hatchers.ruralcaravane.kitchen_suitability.listner;

/**
 * Created by Ashwin on 18-Jan-18.
 */

public interface KitchenListener
{
    void onKitchen_Add_Success();

    void onKitchen_Add_Failed();

    void onKitchen_Add_Response_Failed();

    void onKitchen_Add_Json_Error();

    void onKitchen_Add_No_Connection_Error();

    void onKitchen_Add_Server_Error();

    void onKitchen_Add_Network_Error();

    void onKitchen_Add_Parse_Error();

    void onKitchen_Add_Unknown_Error();

}
