package com.sebrs3018.SmartSharing.ui.BCScan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.sebrs3018.SmartSharing.Login.SessionManager;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TOARRANGE.DataManager;
import com.sebrs3018.SmartSharing.barcode.BCScanner;
import com.sebrs3018.SmartSharing.barcode.BarcodeLookup;
import com.sebrs3018.SmartSharing.databinding.FragmentBCScanBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.sebrs3018.SmartSharing.Constants.BOOKS;
import static com.sebrs3018.SmartSharing.Constants.SHARED_PREFS;
import static com.sebrs3018.SmartSharing.Constants.USERNAME_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BCScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BCScanFragment extends Fragment {



    private View myFragment;
    private FragmentBCScanBinding binding;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "BCScanFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BCScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BCScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BCScanFragment newInstance(String param1, String param2) {
        BCScanFragment fragment = new BCScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    //TODO: creare una sessione in cui mantenere almeno l'id dell'utente in modo da poterci salvare i libri che ha messo in prestito!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentBCScanBinding.inflate(inflater, container, false);
        myFragment = binding.getRoot();

        binding.bttScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: bttScanning touched!");
                
                binding.cameraKV.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage,0,capturedImage.length);
                        bitmap = Bitmap.createScaledBitmap(bitmap,cameraKitView.getWidth(),cameraKitView.getHeight(),false);

                        InputImage inputImage = InputImage.fromBitmap(bitmap, (int) cameraKitView.getRotation());
                        BarcodeScanner scanner = BarcodeScanning.getClient();

                        Task<List<Barcode>> result = scanner.process(inputImage)
                                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                    @Override
                                    public void onSuccess(List<Barcode> barcodes) {
                                        for (Barcode bc : barcodes) {
                                            if (bc.getValueType() == Barcode.TYPE_ISBN){
                                                Log.i(TAG, "onSuccess: Barcode found ==> " + bc.getRawValue());
                                                new BarcodeLookup(Navigation.findNavController(getView()), getContext()).execute(bc.getRawValue());   //lookingUp for the book just scanned
                                            }
                                            else
                                                Log.i(TAG, "Wrong barcode found " );
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Barcode detection failed.",Toast.LENGTH_SHORT).show();
                                        Log.e(TAG,e.getMessage());
                                    }
                                });
                    }
                });
            }
        });

        return myFragment;
    }




    @Override
    public void onStart() {
        super.onStart();
        binding.cameraKV.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.cameraKV.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.cameraKV.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.cameraKV.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        binding.cameraKV.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





}