package com.sebrs3018.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button bttLogin = null;
    private EditText etUser = null, etPassword = null;
    //    private TextView tvRegister = null;
    private FirebaseAuth firebaseAuthenticator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser      = findViewById(R.id.etUser);
        etPassword  = findViewById(R.id.etPassword);
        bttLogin    = findViewById(R.id.bttLogin);
        firebaseAuthenticator = FirebaseAuth.getInstance();
        bttLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add Data to DB
                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();
                firebaseAuthenticator.createUserWithEmailAndPassword(_user,_password).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Registrazione effettuata", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this,"Registrazione fallita", Toast.LENGTH_SHORT).show();
                                }
                    }
                });
            }
        });
    }
}

