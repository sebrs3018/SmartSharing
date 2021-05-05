package com.sebrs3018.SmartSharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sebrs3018.SmartSharing.DB.DbManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private final String FullPathLog = "/data/user/0/com.sebrs3018.login/files/SmartSharing/Logs.txt";
    private String TAG = "LoginActivity";
    private TextView tvLogin = null, tvRegister_page = null; // tvLogin è il bottone per loggare.
    private EditText etUser = null, etPassword = null;
    private DbManager db = null;
    private CardView cvBioAccess = null;

//    private FirebaseAuth firebaseAuthenticator = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUser          = findViewById(R.id.etUser);
        etPassword      = findViewById(R.id.etPassword);
        tvLogin         = findViewById(R.id.tvLogin);
        tvRegister_page = findViewById(R.id.tvRegister_page);
        cvBioAccess     = findViewById(R.id.cvBioAccess);


        // Redirect alla page di registrazione
        tvRegister_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });


        cvBioAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();

                /* Recupero tutti i log che sono salvati nel dispositivo dell'utente*/
                ReadLogs(FullPathLog);

                db = new DbManager(LoginActivity.this);

                if(db.login(_user, _password)){
                    Toast.makeText(LoginActivity.this, "Login Success!!!!!!!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onClick: Login success!!!!!!");
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Failure!!!!!!!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onClick: Login Failuree!!!!!!");
                }

            }
        });


    }



    public void ReadLogs(String _fileName) {


        //reading text from file
        try {
            FileInputStream fileIn  = new FileInputStream (new File(_fileName));
//                    = openFileInput(_fileName);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            StringBuilder resultStringBuilder = new StringBuilder();

            try (BufferedReader br = new BufferedReader(InputRead)) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
                Log.i(TAG, "ReadLogs: " + resultStringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

/*
            char[] inputBuffer= new char[128];
            String s="";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer))>0) {
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s += readstring;
            }
            InputRead.close();
            Log.i(TAG, "ReadBtn: Stringa recuperata" + s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

