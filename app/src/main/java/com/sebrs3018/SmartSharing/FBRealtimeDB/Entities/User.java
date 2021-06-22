package com.sebrs3018.SmartSharing.FBRealtimeDB.Entities;

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

    private static String TAG = "User";
    public String username;
    public String address;
    public String password;
    private String email;

    public User(){ }

    public User(String _username, String _address){
       this(_username, "", _address, "");
    }


    public User(String _username,String _password, String _address, String _email){
        username = _username;
        password = _password;
        address = _address;
        email = _email;
    }




    protected User(Parcel in) {
        TAG = in.readString();
        username = in.readString();
        address = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getAddress() {
        return address;
    }
    public String getPassword(){
        return password;
    }
    public String getUsername() {
        return username;
    }


    //restituisce lista di utenti registrati che vengono salvati in memoria locale del telefono!
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
                Log.i(TAG, "initRegisteredUsers: line ==> " + line);
                String[] userInfo = line.split("-");
                Log.i(TAG, "initRegisteredUsers: user info ==> username: " + userInfo[0] + "\taddress: " + userInfo[1]);
                usernames.add(new User(userInfo[0], userInfo[1]));    //aggiungo utente registrato
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TAG);
        dest.writeString(username);
        dest.writeString(address);
        dest.writeString(password);
    }

    public String getEmail() {
        return email;
    }

}
