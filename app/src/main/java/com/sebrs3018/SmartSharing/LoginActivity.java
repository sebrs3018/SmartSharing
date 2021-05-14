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
import com.sebrs3018.SmartSharing.Exceptions.InvalidPasswordException;
import com.sebrs3018.SmartSharing.Exceptions.UserNotFoundException;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initComponents();

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

                String _user, _password;

                if(!isInputDataValid(etUser.getText(), false)){
                    ilUser.setError("Username vuoto");
                    etUser.setText(null);
                    return;
                }
                ilUser.setError(null);
                _user = etUser.getText().toString();

                if(!isInputDataValid(etPassword.getText(),true)) {
                    ilPassword.setError("La password deve contere almeno 8 caratteri");
                    return;
                }
                ilPassword.setError(null);
                _password = etPassword.getText().toString();

                db = new DbManager(LoginActivity.this);

                try {
                    db.login(_user, _password);
                    startActivity(new Intent(LoginActivity.this, Navigation_Activity.class));
                }
                catch (UserNotFoundException e){
                    ilUser.setError("Utente non riconosciuto");
                }
                catch (InvalidPasswordException e){
                    etPassword.setText(null);
                    ilPassword.setError("Password sbagliata");
                }

/*                if (db.login(_user, _password)) {
                    ilUser.setError(null);
                }
                else{
                    ilUser.setError("Utente non riconosciuto");
                }*/

            }
        });

    }

    private void initComponents(){
        ilUser      = findViewById(R.id.ilUser);
        etUser      = findViewById(R.id.etUser);

        ilPassword  = findViewById(R.id.ilPassword);
        etPassword  = findViewById(R.id.etPassword);

        cvBioAccess = findViewById(R.id.cvBioAccess);
        cvLogin     = findViewById(R.id.cvLogin);

        tvRegister_page = findViewById(R.id.tvRegister_page);
    }

    private boolean isInputDataValid(@Nullable Editable text, boolean isPassword) {
        if(text == null)
            return false;
        if(isPassword && text.length() <= 8)
            return false;
        return text.toString().trim().length() != 0;
    }


}