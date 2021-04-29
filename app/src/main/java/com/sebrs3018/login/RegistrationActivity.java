package com.sebrs3018.login;

import android.content.Intent;
import android.os.Bundle;
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

public class RegistrationActivity extends AppCompatActivity {

    private TextView tvRegister = null, tvLogin_page = null; // tvRegister è il bottone per registrarsi.
    private EditText etUser = null, etPassword = null;

    private FirebaseAuth firebaseAuthenticator = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        tvRegister = findViewById(R.id.tvRegister);
        tvLogin_page = findViewById(R.id.tvLogin_page);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        firebaseAuthenticator = FirebaseAuth.getInstance();

        // Redirect alla page di login
        tvLogin_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Controllare che i due et non siano vuoti.
                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();

                firebaseAuthenticator.createUserWithEmailAndPassword(_user,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,"Registrazione effettuata", Toast.LENGTH_LONG).show();
                            // Una volta creato l'utente si va al login per effettuare l'accesso.
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        }else {
                            Toast.makeText(RegistrationActivity.this,"Registrazione fallita", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });



    }
}
