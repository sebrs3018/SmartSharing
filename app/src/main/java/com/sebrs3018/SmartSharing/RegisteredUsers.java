package com.sebrs3018.SmartSharing;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardBooks.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardBooks.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.GridCardUsers.User;
import com.sebrs3018.SmartSharing.GridCardUsers.UserCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardUsers.UserGridItemDecoration;
import com.sebrs3018.SmartSharing.TouchCardListener.OnUserListener;
import com.sebrs3018.SmartSharing.network.BookEntry;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegisteredUsers extends AppCompatActivity implements OnUserListener {

    private final String TAG = "RegisteredUsers";
    private List<User> registeredUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_users);

        // Setting up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvRegisteredUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredUsers.this, LinearLayoutManager.VERTICAL, false));

        /* Inizializzo adapter dei dati */
        registeredUsers = User.initRegisteredUsers(getString(R.string.LogsPath));
        UserCardRecyclerViewAdapter adapter = new UserCardRecyclerViewAdapter(registeredUsers, this);
        recyclerView.setAdapter(adapter);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing_small);
        recyclerView.addItemDecoration(new UserGridItemDecoration(smallPadding));

        }

    @Override //Se volessi navigare su una nuova activity associata all'utente appena premuto passerei da qui
    //E' questo metodo che attiva il processo di individuazione e gestione del click!
    public void onUserClick(int position) {
        /*Intent intent = new Intent(this, NewActivity.class);
        * startActivity(intent);
        * */

//        registeredUsers.get(position);

        Log.i(TAG, "onUserClick: clicked " + registeredUsers.get(position).username);
        FingerprintDetector fingerprintDetector = new FingerprintDetector(registeredUsers.get(position).username, RegisteredUsers.this);
        fingerprintDetector.startFingerPrintDetection(false);
        
    }
}
