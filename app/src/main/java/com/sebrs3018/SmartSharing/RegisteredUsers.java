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
    private TextView tvRegisteredUser1 = null;
    private TextView tvRegisteredUser2 = null;
    private TextView tvRegisteredUser3 = null;

    private List<User> registeredUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_users);

/*        tvRegisteredUser1 = findViewById(R.id.tvUser1);
        tvRegisteredUser2 = findViewById(R.id.tvUser2);
        tvRegisteredUser3 = findViewById(R.id.tvUser3);*/

        /* Recupero tutti i log che sono salvati nel dispositivo dell'utente */
//        ArrayList<String> registeredUsers  = ReadLogs(getString(R.string.LogsPath));

        /* Mostro al massimo tre utenti */
/*        if(registeredUsers.isEmpty())
            tvRegisteredUser1.append("Non ci sono utenti registrati");*/

       /* int counter = 0;
        for (String username : registeredUsers){
            if(counter == 0){
                tvRegisteredUser1.append(username);
                tvRegisteredUser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FingerprintDetector fingerprintDetector = new FingerprintDetector(username, RegisteredUsers.this);
                        fingerprintDetector.startFingerPrintDetection(false);
                    }
                });
            }
            else if(counter == 1){
                tvRegisteredUser2.append(username);
                tvRegisteredUser2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FingerprintDetector fingerprintDetector = new FingerprintDetector(username, RegisteredUsers.this);
                        fingerprintDetector.startFingerPrintDetection(false);
                    }
                });
            }
            else if(counter == 2){
                tvRegisteredUser3.append(username);
                tvRegisteredUser3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FingerprintDetector fingerprintDetector = new FingerprintDetector(username, RegisteredUsers.this);
                        fingerprintDetector.startFingerPrintDetection(false);
                    }
                });
            }
            else
                break;

        counter++;*/



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
