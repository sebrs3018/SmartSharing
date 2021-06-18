package com.sebrs3018.SmartSharing.TOARRANGE;



public class User {

    private String username;
    private String password;

    public User(){
        /** Default constructor required by Firebase */
    }

    public User( String _username, String _password ){
        username = _username;
        password = _password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
