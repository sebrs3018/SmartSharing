package com.sebrs3018.SmartSharing.FBRealtimeDB.Database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sebrs3018.SmartSharing.Utils;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.User;
import com.sebrs3018.SmartSharing.Login.LoginActivity;
import com.sebrs3018.SmartSharing.Login.SessionManager;
import com.sebrs3018.SmartSharing.Navigation_Activity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.sebrs3018.SmartSharing.Constants.*;


public class DataManager {

    private static final String TAG = "DataManager";

    private FirebaseDatabase db;
    private DatabaseReference userRoot;
    private DatabaseReference bookRoot;
    private SessionManager sessionManager;
    private Context c;

    private long progessiveID = 0;

    private boolean isUserInDb = true;

    public DataManager() {
        db = FirebaseDatabase.getInstance();
        userRoot = db.getReference().child("Users");
        bookRoot = db.getReference().child("Books");
    }

    public DataManager(String rootRef) {
        db = FirebaseDatabase.getInstance();
        setRoot(rootRef);
    }

    /**
     * @param context used for handling errors in UI
     * @param rootRef used for root entity in DB
     * */
    public DataManager(String rootRef, Context context){
        this(rootRef);
        c = context;
    }

    public DataManager(String rootRef, Context context, SessionManager sm){
        this(rootRef,context);
        sessionManager = sm;
    }



    private void setRoot(String rootRef) {
        if (rootRef.equals(USERS)){
            userRoot = db.getReference().child(USERS);
        }
        else if (rootRef.equals(BOOKS)){
            bookRoot = db.getReference().child(BOOKS);


            /*incremento variabile*/
            bookRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        progessiveID = (snapshot.getChildrenCount() + 1);
                        Log.i(TAG, "onDataChange: nroChildren ===> " + progessiveID);

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    /**
     * This class contain only the methods to add data
     * to the DB, to get data the query has to be made before
     * the visualization in UI.
     */


    public DatabaseReference getUserRoot() {
        return userRoot;
    }

    public DatabaseReference getBookRoot() {
        return bookRoot;
    }

    /**
     * Add a user on the DB
     * @param _username is the key for the user
     */
    public boolean addUser(String _username, String _password, String _address, String _email) {
        User user = new User(_username, _password, _address, _email);
        userRoot.child(_username).setValue(user);
        return true;
    }


    public boolean login(String _username, String _password) {

            userRoot.child(_username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        User u = task.getResult().getValue(User.class);

                            if(u == null) { isUserInDb = false;
                                Log.i(TAG, "onComplete: Utente non trovato!!");
                                ((LoginActivity) c).setUserError();
                            }
                            else{

                                if(!u.getPassword().equals(Utils.md5(_password))){
                                    ((LoginActivity) c).setPasswdError();
                                    Log.i(TAG, "onComplete: Password non corretta!" + _password + "\t!=\t" + u.getPassword());
                                    return; }

                                Log.i(TAG, "onDataChange: " + u.getPassword());
                                Log.i(TAG, "onDataChange: " + u.getUsername());
                                Log.i(TAG, "onDataChange: " + u.getAddress());

                                //Salvo il nome e l'indirizzo in sessione
                                sessionManager.setUserSession(u.getUsername(), u.getAddress());

                                //Una volta eseguito il login, posso proseguire!
                                c.startActivity(new Intent(c, Navigation_Activity.class));
                            }
                    }
                }
            });

        Log.i(TAG, "login: login successs!\t" + isUserInDb);
        return true;
    }


    /*
    *
    * */
    public void addBookLender(String _ISBN, String _titolo, String _autore, String _editore, String _dataPubblicazione, String _nroPagine, String _descrizione, String _urlImage, String _lender ){
        bookRoot.child(_ISBN).setValue(new Book(_ISBN, _titolo, _autore, _editore, _dataPubblicazione, _nroPagine, _descrizione, _urlImage, _lender));
    }

    /**
     * The key for this entity is both identifier and logical clock for the most recent inserted book
     * @param bookToAdd in DB
     * */
    public void addBookLender(Book bookToAdd){
        bookToAdd.setIDlogicalClock(progessiveID);
        bookRoot.child(String.valueOf(progessiveID)).setValue(bookToAdd);
    }

    public List<Book> getBooksAvailable() {

        List<Book> bookList = new ArrayList<>();
        bookRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);

                    if (book != null) {
                        bookList.add(new Book(book.getISBN(), book.getTitolo(), book.getAutore(), book.getEditore(), book.getDataPubblicazione(), book.getNroPagine(), book.getDescrizione(), book.getUrlImage(), book.getLender()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO show error message
            }
        });
        return bookList;

    }



    /**
     * Add a book on the DB
     */
/*
    public boolean addBookLender(String _ISBN, String _lender, String _author) {
        Book book = new Book(_name, _category, _author);
        bookRoot.push().setValue(book);
        return true;
    }
*/




}
