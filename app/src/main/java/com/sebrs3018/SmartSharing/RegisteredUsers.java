package com.sebrs3018.SmartSharing;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardUsers.User;
import com.sebrs3018.SmartSharing.GridCardUsers.UserCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardUsers.UserGridItemDecoration;
import com.sebrs3018.SmartSharing.TouchCardListener.OnTouchedItemListener;

import java.util.List;

public class RegisteredUsers extends AppCompatActivity implements OnTouchedItemListener {

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
    public void onItemTouched(int position) {
        /*Intent intent = new Intent(this, NewActivity.class);
        * startActivity(intent);
        * */

//        registeredUsers.get(position);

        Log.i(TAG, "onUserClick: clicked " + registeredUsers.get(position).username);
        FingerprintDetector fingerprintDetector = new FingerprintDetector(registeredUsers.get(position).username, RegisteredUsers.this);
        fingerprintDetector.startFingerPrintDetection(false);
        
    }
}
