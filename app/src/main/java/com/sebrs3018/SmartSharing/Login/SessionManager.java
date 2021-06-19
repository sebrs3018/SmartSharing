package com.sebrs3018.SmartSharing.Login;

import android.content.Context;
import android.content.SharedPreferences;

import static com.sebrs3018.SmartSharing.Constants.*;

public class SessionManager {

    //Context
    private final Context c;

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String username, address;


    public SessionManager(Context _c){
        c = _c;
        //getting a reference to the file sharedPreferences in the device...
        sharedpreferences = c.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void setUserSession(String usernameToSave, String addressToSave){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // below two lines will put values for
        // email and password in shared preferences.
        editor.putString(USERNAME_KEY, usernameToSave);
        editor.putString(ADDRESS_KEY, addressToSave);

        // to save our data with key and value.
        editor.apply();
    }

    //userValues[0] ==> username;   userValues[1] ==> address
    public String[] getUserSession(){
        String[] userValues = new String[2];
        userValues[0] = sharedpreferences.getString(USERNAME_KEY, null);
        userValues[1] = sharedpreferences.getString(ADDRESS_KEY, null);
        return userValues;
    }

}
