package com.sebrs3018.SmartSharing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RegisteredUsers extends AppCompatActivity {

    private final String TAG = "RegisteredUsers";
    private TextView tvRegisteredUser1 = null;
    private TextView tvRegisteredUser2 = null;
    private TextView tvRegisteredUser3 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_users);

        tvRegisteredUser1 = findViewById(R.id.tvUser1);
        tvRegisteredUser2 = findViewById(R.id.tvUser2);
        tvRegisteredUser3 = findViewById(R.id.tvUser3);

        /* Recupero tutti i log che sono salvati nel dispositivo dell'utente */
        ArrayList<String> registeredUsers  = ReadLogs(getString(R.string.LogsPath));

        /* Mostro al massimo tre utenti */
        if(registeredUsers.isEmpty())
            tvRegisteredUser1.append("Non ci sono utenti registrati");

        int counter = 0;
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

        counter++;
        }



/*
        tvRegisteredUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerprintDetector fingerprintDetector = new FingerprintDetector(registeredUsers.get(1), RegisteredUsers.this);
                fingerprintDetector.startFingerPrintDetection(false);
            }
        });
        tvRegisteredUser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerprintDetector fingerprintDetector = new FingerprintDetector(registeredUsers.get(2), RegisteredUsers.this);
                fingerprintDetector.startFingerPrintDetection(false);
            }
        });
*/



    }



    private ArrayList<String> ReadLogs(String _filePath) {

        ArrayList<String> usernames = new ArrayList<>();
        //reading text from file

        try {
            FileInputStream fileIn = new FileInputStream(new File(_filePath));
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            StringBuilder resultStringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(InputRead);
            String line;

            while ((line = br.readLine()) != null) {
                usernames.add(line);    //aggiungo utente registrato
                resultStringBuilder.append(line).append("\n");
            }
            Log.i(TAG, "ReadLogs: " + resultStringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }


}
