package com.sebrs3018.SmartSharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sebrs3018.SmartSharing.DB.DBUtils;
import com.sebrs3018.SmartSharing.DB.DbManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class RegistrationActivity extends AppCompatActivity {

    private final String TAG = "RegistrationActivity";
    private TextView tvRegister = null, tvLogin_page = null; // tvRegister è il bottone per registrarsi.
    private EditText etUser = null, etPassword = null;
    private DbManager db = null;
    private CursorAdapter adapter;
    private static Context savedContext;
    final int ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        savedContext = this;
        tvRegister = findViewById(R.id.tvRegister);
        tvLogin_page = findViewById(R.id.tvLogin_page);


        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);


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

                String _user = etUser.getText().toString().trim();
                String _password = etPassword.getText().toString().trim();

                db = new DbManager(RegistrationActivity.this);

                if(db.insertUser(_user, DBUtils.md5(_password))) {    //salvo in DB password cifrata
                    writeFileOnInternalStorage(RegistrationActivity.this, getString(R.string.LogFileName), _user);



                    /* finestrella pop-up */
                    new AlertDialog.Builder(RegistrationActivity.this)
                                    .setTitle("SetUp FingerPrint")
                                    .setMessage("Vuoi inserire un'impronta?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /* Associo all'email dell'utente la sua impronta digitale*/
                                            /*Intent intent4results = new Intent(getString(R.string.FingerPrintActivity));
                                            intent4results.putExtra(getString(R.string.username), _user);
                                            startActivityForResult(intent4results, ACTIVITY_REQUEST_CODE);*/
                                            FingerprintDetector fingerprintDetector = new FingerprintDetector(_user, RegistrationActivity.this);
                                            fingerprintDetector.startFingerPrintDetection();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();

                    /*TODO: non è necessario creare un'altra Activity, è sufficiente attivare il pop-up
                    * IDEA: trasformare FingerprintAuthentication in una classe in cui si passa il contesto
                    *       cercare di di fare una finestra in cui si chiede se si vuole registrare l'impronta o meno!
                    * */


                   // startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }

            }
        });

    }



    //Chiamato in automatico quando un activity chiama metodo setResult --> arrivaa starActiviyForResult
    //requestCode corrisponde al code per chiamarla ==> dato da setResult (riga 69)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //controllo che il request_code sia quello che voglio io
        if( requestCode == ACTIVITY_REQUEST_CODE ){
            //controllo che l'activity di supporto mi abbia inviato il risultato corretto (definito da me)
            if( resultCode == Activity.RESULT_OK ){
                Toast.makeText(RegistrationActivity.this, "Registrazione impronta avvenuta con successo! ", Toast.LENGTH_LONG).show();
                /*TODO: una volta avvenuta la registrazione dell'impronta, ritorno alla pagina di login*/
            }
        }

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



}
