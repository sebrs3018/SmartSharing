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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.model.DirectionsResult;
import com.google.mlkit.vision.barcode.Barcode;
import com.sebrs3018.SmartSharing.BookInfoStructure.GeoLocalization.UserLocation;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.databinding.FragmentDoveTrovarloTabBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static com.sebrs3018.SmartSharing.Constants.MAPVIEW_BUNDLE_KEY;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DoveTrovarloTab extends Fragment implements OnMapReadyCallback {

    private FragmentDoveTrovarloTabBinding binding;
    private FusedLocationProviderClient mfusedLocaltionClient;

    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private UserLocation mUserPosition;



    private GeoApiContext mGeoApiContext = null;
    private ClusterManager<ClusterItem> mClusterManager; //questo cluster manager mi permette di marcare più utenti...


    private static final String TAG = "DoveTrovarloTab";

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDoveTrovarloTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mfusedLocaltionClient = LocationServices.getFusedLocationProviderClient(getContext());
        initGoogleMap(savedInstanceState);
        /* da notare che si prende la locazione solo dopo aver inizializzato la mappa! */
        getLastKnownLocation();

        return root;
    }

    /* This function founds your current location ! */
    //TODO: aggiungere permessi!
    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mfusedLocaltionClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    Log.d(TAG, "onComplete: latitude " + location.getLatitude() + "\tlongitude: " + location.getLongitude());

                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mUserPosition = new UserLocation(null, myLocation, null);
                    mGoogleMap.addMarker(new MarkerOptions().position(mUserPosition.getGeo_point()).title("It's me!"));
//                    setCameraView();
                    addMapMarkers();

/*                  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));*/
                }
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

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        }

    }


    /* add marker to the current Map */
    private void addMapMarkers() {
        if (mGoogleMap != null) {


            Geocoder coder = new Geocoder(getActivity().getApplicationContext());

            //TODO: questo sarà l'indirizzo dell'utente TO (da pescare dal DB)
            String bookerAddress = "Via della torre 7, 11013 Courmayeur";
            try {
                List<Address> address = coder.getFromLocationName(bookerAddress, 5);

                double longitude = address.get(0).getLongitude();
                double latitude = address.get(0).getLatitude();
                LatLng bookerLatlng = new LatLng(latitude, longitude);

                /*nel caso in cui ci fossero altri utenti, sarebbe necessario aggiungere altri marker da qua! */
                mGoogleMap.addMarker(new MarkerOptions().position(bookerLatlng).title("The Booker"));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bookerLatlng, 10));


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }*/

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
    }


    /**
     * Determines the view boundary then sets the camera
     * Sets the view
     */
    private void setCameraView() {

        // Set a boundary to start (a rectangle view! 0.2 * 0.2)

        double bottomBoundary = mUserPosition.getGeo_point().latitude - .1;
        double leftBoundary = mUserPosition.getGeo_point().longitude - .1;
        double topBoundary = mUserPosition.getGeo_point().latitude + .1;
        double rightBoundary = mUserPosition.getGeo_point().longitude + .1;

                mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));

    }







}