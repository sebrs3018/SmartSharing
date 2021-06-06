package com.sebrs3018.SmartSharing.GridCardUsers;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private final String TAG = "User";
    public final String username;
    public final String address;


    public User(String _username){
        username = _username;
        address = "Via";
    }

    public User(String _username, String _address){
        username = _username;
        address  = _address;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }


    //restituisce lista di utenti registrati
    public static List<User> initRegisteredUsers(String _filePath) {

        ArrayList<User> usernames = new ArrayList<>();
        //reading text from file
        try {
            FileInputStream fileIn = new FileInputStream(new File(_filePath));
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            StringBuilder resultStringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(InputRead);
            String line;

            while ((line = br.readLine()) != null) {
                usernames.add(new User(line));    //aggiungo utente registrato
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }

//TODO: usare parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
