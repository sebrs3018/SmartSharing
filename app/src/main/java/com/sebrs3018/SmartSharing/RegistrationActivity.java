package com.sebrs3018.SmartSharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sebrs3018.SmartSharing.Login.FingerprintDetector;
import com.sebrs3018.SmartSharing.Login.LoginActivity;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Database.DataManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import static com.sebrs3018.SmartSharing.Constants.*;

public class RegistrationActivity extends AppCompatActivity {

    private final String TAG = "RegistrationActivity";
    private TextView tvLogin_page = null;                       // tvRegister è il bottone per registrarsi.
    private TextInputEditText etUser = null, etPassword = null, etCPassword = null, etAddress = null, etEmail = null; // campi di input per user e password
    private TextInputLayout ilPassword, ilCPassword, ilUser, ilAddress, ilEmail;
    private CardView cvRegister = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        initRegistrationFields();
        DataManager dm = new DataManager(USERS);


        /* Inizializzo pulsanti per registrazione */
        cvRegister = findViewById(R.id.cvRegister);
        tvLogin_page = findViewById(R.id.tvLogin_page);

        // Redirect alla page di login
        tvLogin_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _user, _email ,_password, _cPassword, _address;

                if(!isInputDataValid(etUser.getText(), false)){
                    ilUser.setError("Il nome utente non può essere vuoto!");
                    etUser.setText(null);
                    return;
                }
                _user = etUser.getText().toString();
                ilUser.setError(null);

                if(!isInputDataValid(etEmail.getText(), false)){
                    ilEmail.setError("Email non valida");
                    etEmail.setText(null);
                    return;
                }

                _email = etEmail.getText().toString();
                ilEmail.setError(null);

                if(!isInputDataValid(etAddress.getText(), false)){
                    ilAddress.setError("L'indirizzo non può essere vuoto!");
                    etAddress.setText(null);
                    return;
                }
                ilAddress.setError(null);
                _address = etAddress.getText().toString();

                /* Validazione input */
                if(!isInputDataValid(etPassword.getText(), true)){
                    ilPassword.setError("La password deve contere almeno 8 caratteri");
                    resetPassFields();
                    return;
                }
                ilPassword.setError(null);
                _password = etPassword.getText().toString();

                if(!isInputDataValid(etCPassword.getText(), true)){
                    ilCPassword.setError("Le password non coincidono");
                    resetPassFields();
                    return;
                }
                ilCPassword.setError(null);
                _cPassword = etCPassword.getText().toString();

                if(!_cPassword.equals(_password)){
                    ilPassword.setError("Le password non coincidono");
                    resetPassFields();
                    return;
                }
                ilPassword.setError(null);


                if(dm.addUser(_user, Utils.md5(_password), _address, _email)) {    //salvo in DB password cifrata
                    /* finestrella pop-up per inserimento impronta */
                    registerFingerPrint(_user);
                }
                else
                    Toast.makeText(RegistrationActivity.this, "Utente già registrato", Toast.LENGTH_LONG).show();
            }
        });

    }

    /*IDEA: salvare tutte le email usate dall'utente nel SUO dispositivo */
    // Precondizione: si salva su Storage sse l'utente si registra!
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){

        File dir = new File(mcoContext.getFilesDir(), "SmartSharing");
        String fullPath = mcoContext.getFilesDir() + "/SmartSharing/" + sFileName;
        Log.i(TAG, "writeFileOnInternalStorage: fullpath -> " + fullPath);


        if(!dir.exists()){
            if(!dir.mkdir())
                Log.i(TAG, "writeFileOnInternalStorage: ");
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sBody + "\n");
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean isInputDataValid(@Nullable Editable text, boolean isPassword) {
        if(text == null)
            return false;
        if(isPassword && text.length() <= 8)
            return false;
        return text.toString().trim().length() != 0;
    }
    private void initRegistrationFields(){
        ilUser = findViewById(R.id.ilRegUser);
        etUser = findViewById(R.id.etRegUser);

        ilEmail = findViewById(R.id.ilEmail);
        etEmail = findViewById(R.id.etEmail);

        ilAddress = findViewById(R.id.ilRegAddress);
        etAddress = findViewById(R.id.etRegAddress);

        ilPassword = findViewById(R.id.ilRegPassword);
        etPassword = findViewById(R.id.etRegPassword);

        ilCPassword = findViewById(R.id.ilRegCPassword);
        etCPassword = findViewById(R.id.etRegCPassword);

    }
    private void resetPassFields(){
        etPassword.setText(null);
        etCPassword.setText(null);
    }

    private void registerFingerPrint(String _user){
        new AlertDialog.Builder(RegistrationActivity.this)
                .setTitle("Salva FingerPrint")
                .setMessage("Vuoi inserire un'impronta?")
                .setPositiveButton("Va bene", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Associo all'email dell'utente la sua impronta digitale*/
                        FingerprintDetector fingerprintDetector = new FingerprintDetector(_user, RegistrationActivity.this);
                        fingerprintDetector.startFingerPrintDetection(true);
                        /* Tengo traccia degli utenti che hanno inserito anche le loro impronta */
                        writeFileOnInternalStorage(RegistrationActivity.this, getString(R.string.LogFileName), _user);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    }
                }).create().show();
    }


}
