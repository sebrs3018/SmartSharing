package com.sebrs3018.SmartSharing.FBRealtimeDB;

import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button bttInsert = null;
    private Button bttShowUser = null;
    private Button bttShowUsers = null;

    private EditText etMail = null;
    private EditText etPassword = null;
    private EditText etUser = null;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttInsert = findViewById(R.id.bttInsert);
        bttShowUser = findViewById(R.id.bttShowUser);
        bttShowUsers = findViewById(R.id.bttShow);

        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etUser = findViewById(R.id.etUser);

        DataManager dm = new DataManager();

        // add one user
        bttInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dm.addUser(etMail.getText().toString(), etPassword.getText().toString())){
                    Toast.makeText(MainActivity.this, "User added", Toast.LENGTH_SHORT).show();

                    etMail.setText("");
                    etPassword.setText("");
                }
                else {
                    Toast.makeText(MainActivity.this, "Username " + etMail.getText().toString() + " already in use", Toast.LENGTH_SHORT).show();
                    etMail.setText("");
                }

            }
        });

        // show all users
        bttShowUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.getUserRoot().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren() ) {
                            Toast.makeText(MainActivity.this, "Username: " +ds.getValue(User.class).getUsername() +
                                    " Password: " +ds.getValue(User.class).getPassword() + " ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // TODO show error message
                    }
                });
            }
        });


        // show one user
        bttShowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.getUserRoot().orderByChild("username")
                .equalTo(etUser.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        etUser.setText("");
                        for (DataSnapshot ds: snapshot.getChildren() ) {
                            etMail.setText(ds.getValue(User.class).getUsername());
                            etPassword.setText(ds.getValue(User.class).getPassword());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });








    }

    */
}