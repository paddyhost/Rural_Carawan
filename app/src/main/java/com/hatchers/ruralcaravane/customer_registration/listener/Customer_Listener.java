package com.hatchers.ruralcaravane.customer_registration.listener;

/**
 * Created by Ashwin on 14-Jan-18.
 */

public interface Customer_Listener
{
    void onCustomer_Add_Success();

    void onCustomer_Add_Failed();

    void onCustomer_Add_Response_Failed();

    void onCustomer_Add_Json_Error();

    void onCustomer_Add_No_Connection_Error();

    void onCustomer_Add_Server_Error();

    void onCustomer_Add_Network_Error();

    void onCustomer_Add_Parse_Error();

    void onCustomer_Add_Unknown_Error();

}
