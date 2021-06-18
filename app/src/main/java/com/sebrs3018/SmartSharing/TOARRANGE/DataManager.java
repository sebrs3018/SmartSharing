package com.sebrs3018.SmartSharing.TOARRANGE;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataManager {

    private static final String TAG = "DataManager";

    private FirebaseDatabase db;
    private DatabaseReference userRoot;
    private DatabaseReference bookRoot;

    public DataManager(){
        db = FirebaseDatabase.getInstance();
        userRoot = db.getReference().child("Users");
        bookRoot = db.getReference().child("Books");
    }


    /**
     *   This class contain only the methods to add data
     *   to the DB, to get data the query has to be made before
     *   the visualization in UI.
     * */


    public DatabaseReference getUserRoot() {
        return userRoot;
    }
    public DatabaseReference getBookRoot() {
        return bookRoot;
    }

    /**
     * Add a user on the DB
     * */
    public boolean addUser(String _username, String _password){

        User user = new User(_username, _password);
        userRoot.push().setValue(user);
        return true;
    }

    /**
     * Add a book on the DB
     * */
    public boolean addBook( String _name, String _category, String _author ){

        Book book = new Book( _name, _category, _author);
        bookRoot.push().setValue(book);
        return true;
    }


}
