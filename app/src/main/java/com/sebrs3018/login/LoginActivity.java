package com.sebrs3018.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView tvLogin = null, tvRegister_page = null; // tvLogin è il bottone per loggare.
    private EditText etUser = null, etPassword = null;
    private FirebaseAuth firebaseAuthenticator = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUser      = findViewById(R.id.etUser);
        etPassword  = findViewById(R.id.etPassword);
        tvLogin    = findViewById(R.id.tvLogin);
        tvRegister_page = findViewById(R.id.tvRegister_page);

        firebaseAuthenticator = FirebaseAuth.getInstance();

        // Redirect alla page di registrazione
        tvRegister_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Controllare che i due et non siano vuoti.
                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();

                firebaseAuthenticator.signInWithEmailAndPassword(_user,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login effettuato",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(LoginActivity.this,"Utente non trovato, verificare le credenziali",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}

