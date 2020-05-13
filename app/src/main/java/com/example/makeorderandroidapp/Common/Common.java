package com.example.makeorderandroidapp.Common;

import com.example.makeorderandroidapp.Model.User;

public class Common {

    public static User currentUser;

    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Placed";
        else if(code.equals("1"))
            return "On your way";
        else
            return "Shipped";
    }

}
