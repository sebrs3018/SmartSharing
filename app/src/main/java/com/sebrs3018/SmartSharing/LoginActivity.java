package com.sebrs3018.SmartSharing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sebrs3018.SmartSharing.DB.DbManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private final String FullPathLog = "/data/user/0/com.sebrs3018.login/files/SmartSharing/Logs.txt";
    private final String TAG = "LoginActivity";
    private TextView tvRegister_page = null; // tvLogin è il bottone per loggare.

    private TextInputLayout ilUser = null, ilPassword = null;
    private TextInputEditText etUser = null, etPassword = null;


    private DbManager db = null;
    private CardView cvBioAccess = null, cvLogin = null;
    private TextView tvBioAuth = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUser      = findViewById(R.id.etUser);
        etPassword  = findViewById(R.id.etPassword);

        ilPassword  = findViewById(R.id.ilPassword);

        cvBioAccess = findViewById(R.id.cvBioAccess);
        cvLogin     = findViewById(R.id.cvLogin);

        tvRegister_page = findViewById(R.id.tvRegister_page);

        /* Redirect alla page di registrazione */
        tvRegister_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        /* Listener per pulsante acccesso con impronta digitale */
        cvBioAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisteredUsers.class));
            }
        });

        /* Login regular */
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();

                if(!isPasswordValid(etPassword.getText()))
                    ilPassword.setError("La password deve contere almeno 8 caratteri");
                else
                    ilPassword.setError(null);

        /* Parte intuile, usata solo per testare lo scanner*/
//        Button bttToScanner = findViewById(R.id.bttToScanner);
//        bttToScanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, BarcodeScanner.class));
//            }
//        });
                db = new DbManager(LoginActivity.this);

                if (db.login(_user, _password)) {
                    startActivity(new Intent(LoginActivity.this, Navigation_Activity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failure!!!!!!!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: Login Failure!!!!!!");
                }

            }
        });

    }



    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }


}