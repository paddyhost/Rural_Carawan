package com.hatchers.ruralcaravane.user_login.model;

import com.hatchers.ruralcaravane.user_login.listener.LoginListner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashwin on 10-Jan-18.
 */

public class LoginUser
{
    ///events
    static final public int LOGIN_SUCCESS=0,LOGIN_FAILED=1,LOGIN_RESPONSE_FAILED=2,
            LOGIN_JSON_ERROR=3,LOGIN_NO_CONNECTION_ERROR=4,LOGIN_SERVER_ERROR=5,
            LOGIN_NEWORK_ERROR=6,LOGIN_PARSE_ERROR=7,LOGIN_UNKNOWN_ERROR=8;


    private List<LoginListner> loginEvents = new ArrayList<LoginListner>();

    public void setOnLoginEvent(LoginListner toAdd) {

        loginEvents.add(toAdd);
    }
    public void fireOnLoginEvent(int event) {

        for (LoginListner hl : loginEvents) {

            if(event==LOGIN_SUCCESS)
            {
                hl.onLogin_Success();
            }
            else if(event==LOGIN_FAILED)
            {
                hl.onLogin_Failed();
            }
            else if(event==LOGIN_RESPONSE_FAILED)
            {
                hl.onLogin_Response_Failed();
            }
            else if(event==LOGIN_JSON_ERROR)
            {
                hl.onLogin_Json_Error();
            }
            else if(event==LOGIN_NO_CONNECTION_ERROR)
            {
                hl.onLogin_No_Connection_Error();
            }
            else if(event==LOGIN_SERVER_ERROR)
            {
                hl.onLogin_Server_Error();
            }
            else if(event==LOGIN_NEWORK_ERROR)
            {
                hl.onLogin_Network_Error();
            }
            else if(event==LOGIN_PARSE_ERROR)
            {
                hl.onLogin_Parse_Error();
            }
            else if(event==LOGIN_UNKNOWN_ERROR)
            {
                hl.onLogin_Unknown_Error();
            }


        }
    }

}
