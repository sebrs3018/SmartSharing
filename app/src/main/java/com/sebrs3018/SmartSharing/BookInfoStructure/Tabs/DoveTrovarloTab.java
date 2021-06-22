package com.sebrs3018.SmartSharing.BookInfoStructure.Tabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.GeoApiContext;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.User;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.databinding.FragmentDoveTrovarloTabBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.sebrs3018.SmartSharing.Constants.MAPVIEW_BUNDLE_KEY;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DoveTrovarloTab extends Fragment implements OnMapReadyCallback {

    private FragmentDoveTrovarloTabBinding binding;
    private FusedLocationProviderClient mfusedLocaltionClient;

    private GoogleMap mGoogleMap;
    private boolean isCurrentPositionAvailable = false;
    private boolean isLenderAddressAvailable = true;
    private static final String TAG = "DoveTrovarloTab";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDoveTrovarloTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mfusedLocaltionClient = LocationServices.getFusedLocationProviderClient(getContext());
        initGoogleMap(savedInstanceState);
        return root;

    }

    /* This function founds your current location - if GPS is recently activated, the result may be null */
    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location == null) return;

                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(myLocation).title("It's me!"));
                isCurrentPositionAvailable = true;
                if(!isLenderAddressAvailable)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));
                    }
                });
        }


    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        binding.map.onCreate(mapViewBundle);
        binding.map.getMapAsync(this);
    }


    /* add marker to the current Map */
    private void addMapMarkers() {
        if (mGoogleMap != null) {

            Geocoder coder = new Geocoder(getActivity().getApplicationContext());
            String bookerAddress = binding.tvIndirizzoUtente.getText().toString();

                /* Poichè la locazione attuale è quella che può ritardare, lo si inserisce dopo... */

                List<Address> address = null;
                try {
                    address = coder.getFromLocationName(bookerAddress, 5);

                    if (address == null || address.size() == 0) {
                        Log.i(TAG, "addMapMarkers: adress results ==> " + address);
                        Log.e(TAG, "addMapMarkers: Indirizzo utente non trovato");
                        isLenderAddressAvailable = false;
                        getCurrentPosition();
                        return;
                    }

                    Log.i(TAG, "******** OK: addMapMarkers: address results ==> " + address);
                    isLenderAddressAvailable = true;
                    double longitude = address.get(0).getLongitude();
                    double latitude = address.get(0).getLatitude();
                    LatLng bookerLatlng = new LatLng(latitude, longitude);


                    /*nel caso in cui ci fossero altri utenti, sarebbe necessario aggiungere altri marker da qua! */
                    MarkerOptions mo = new MarkerOptions().position(bookerLatlng).title("The Booker");
                    Log.i(TAG, "addMapMarkers: Marker options ==> " + mo);
                    Marker m =  mGoogleMap.addMarker(mo);
                    Log.i(TAG, "*** addMapMarkers: created marker ==> " + m);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bookerLatlng, 10));

                    getCurrentPosition();

                } catch (IOException e) {
                    Log.e(TAG, "addMapMarkers: ", e.fillInStackTrace());

                }

            }
        else Log.e(TAG, "addMapMarkers: mappa non inizializzata");
        }

    private void getCurrentPosition(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            CompletableFuture.runAsync(() -> {
                try {
                    if (!isCurrentPositionAvailable) {
                        Log.i(TAG, "addMapMarkers: la posizione non è ancora disponibile... waiting!");
                        TimeUnit.SECONDS.sleep(3);  //dopo all'incirca due secondi si ha il risultato del GPS
                    }

                    Log.i(TAG, "addMapMarkers: la posizione dovrebbe essere disponibile!!");
                    getLastKnownLocation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            Log.e(TAG, "getCurrentPosition: Versione non supportata" );
        }
}

    /**
     * This function allows a deferred update of user info
     * @param lender contains the info of the lender.
     */
    public void setUserInfo(User lender){
        binding.tvUtente.setText(lender.getUsername());
        binding.tvIndirizzoUtente.setText(lender.getAddress());
        Log.i(TAG, "setUserInfo: email utente ==>" + lender.getEmail());
        binding.tvEmail.setText(lender.getEmail());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /* Aggiungo MapView al lifecycle... */

    @Override
    public void onStart() {
        super.onStart();
        binding.map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.map.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        binding.map.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.map.onLowMemory();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mGoogleMap = googleMap;
        /* da notare che si prende la locazione solo dopo aver inizializzato la mappa! */
        addMapMarkers();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastKnownLocation();
            }
        }
    }
}