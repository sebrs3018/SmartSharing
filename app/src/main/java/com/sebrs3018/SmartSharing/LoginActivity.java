package com.sebrs3018.SmartSharing;

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
import com.sebrs3018.SmartSharing.DB.DbManager;
import com.sebrs3018.SmartSharing.Exceptions.InvalidPasswordException;
import com.sebrs3018.SmartSharing.Exceptions.UserNotFoundException;

import static com.sebrs3018.SmartSharing.Constants.ERROR_DIALOG_REQUEST;
import static com.sebrs3018.SmartSharing.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.sebrs3018.SmartSharing.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class LoginActivity extends AppCompatActivity {


    private final String FullPathLog = "/data/user/0/com.sebrs3018.login/files/SmartSharing/Logs.txt";
    private final String TAG = "LoginActivity";
    private TextView tvRegister_page = null; // tvLogin è il bottone per loggare.

    private TextInputLayout ilUser = null, ilPassword = null;
    private TextInputEditText etUser = null, etPassword = null;
    private boolean mLocationPermissionGranted = false;


    private DbManager db = null;
    private CardView cvBioAccess = null, cvLogin = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

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

                db = new DbManager(LoginActivity.this);

                try {
                    db.login(_user, _password);
                    startActivity(new Intent(LoginActivity.this, Navigation_Activity.class));
                } catch (UserNotFoundException e) {
                    ilUser.setError("Utente non riconosciuto");
                } catch (InvalidPasswordException e) {
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

    //Method to force current location for GPS
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

               // Log.i(TAG, "onLocationResult: " + locationResult.getLocations().get(0).getLatitude());

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
















    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
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

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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

}