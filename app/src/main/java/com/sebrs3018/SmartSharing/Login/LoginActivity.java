package com.sebrs3018.SmartSharing.Login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.RegisteredUsers;
import com.sebrs3018.SmartSharing.RegistrationActivity;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Database.DataManager;

import static com.sebrs3018.SmartSharing.Constants.ERROR_DIALOG_REQUEST;
import static com.sebrs3018.SmartSharing.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.sebrs3018.SmartSharing.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.sebrs3018.SmartSharing.Constants.USERS;

public class LoginActivity extends AppCompatActivity {


    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.RECORD_AUDIO",
    };

    private final String TAG = "LoginActivity";
    private TextView tvRegister_page = null;

    private TextInputLayout ilUser = null, ilPassword = null;
    private TextInputEditText etUser = null, etPassword = null;
    private boolean mLocationPermissionGranted = false;

    private CardView cvBioAccess = null, cvLogin = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        if(!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        askToTurnOnGps();
        getLocationUpdate();
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

                if (!isInputDataValid(etUser.getText(), false)) {
                    ilUser.setError("Username vuoto");
                    etUser.setText(null);
                    return;
                }
                ilUser.setError(null);
                _user = etUser.getText().toString();

                if (!isInputDataValid(etPassword.getText(), true)) {
                    ilPassword.setError("La password deve contere almeno 8 caratteri");
                    return;
                }
                ilPassword.setError(null);
                _password = etPassword.getText().toString();

                SessionManager sm = new SessionManager(LoginActivity.this);
                DataManager dm = new DataManager(USERS, LoginActivity.this, sm);
                dm.login(_user, _password);

            }
        });

    }

    public void setPasswdError(){
        etPassword.setText(null);
        ilPassword.setError("Password sbagliata");
    }

    public void setUserError(){
        etUser.setText(null);
        ilUser.setError("Utente non trovato");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initComponents() {
        ilUser = findViewById(R.id.ilUser);
        etUser = findViewById(R.id.etUser);

        ilPassword = findViewById(R.id.ilPassword);
        etPassword = findViewById(R.id.etPassword);

        cvBioAccess = findViewById(R.id.cvBioAccess);
        cvLogin = findViewById(R.id.cvLogin);

        tvRegister_page = findViewById(R.id.tvRegister_page);
    }

    private boolean isInputDataValid(@Nullable Editable text, boolean isPassword) {
        if (text == null)
            return false;
        if (isPassword && text.length() <= 8)
            return false;
        return text.toString().trim().length() != 0;
    }

    // Method to force current location for GPS
    public void getLocationUpdate() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                /* Idea: dopo aver trovato la prima posizione attuale, aumento l'intervallo... */
                mLocationRequest.setInterval(60000);
                mLocationRequest.setFastestInterval(5000);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }


    /* richiesta permessi in maniera dinamica */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(!mLocationPermissionGranted){
                    getLocationPermission();
                }
            }
            case Activity.RESULT_OK:
                // All required changes were successfully made
                Log.i(TAG, "onActivityResult: GPS Enabled by user");
                break;
            case Activity.RESULT_CANCELED:
                // The user was asked to change settings, but chose not to
                Log.i(TAG, "onActivityResult: User rejected GPS request");
                break;
            default:
                break;
        }

    }

    private void askToTurnOnGps(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(LoginActivity.this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        LoginActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return false;
    }


}