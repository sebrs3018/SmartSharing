package com.sebrs3018.SmartSharing.ui.BCScan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.sebrs3018.SmartSharing.BookInfoStructure.BookInfoArgs;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.barcode.BCScanner;
import com.sebrs3018.SmartSharing.barcode.BarcodeLookup;
import com.sebrs3018.SmartSharing.databinding.FragmentBCScanBinding;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BCScanFragment extends Fragment {

    private static final String TAG = "BCScanFragment";
    private View myFragment;
    private FragmentBCScanBinding binding;
    private CameraKitView cameraKitView;

    public BCScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentBCScanBinding.inflate(inflater, container, false);
        myFragment = binding.getRoot();
        cameraKitView = binding.cameraKV;

        binding.bttScanning.bringToFront();
        binding.bttScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: bttScanning touched!");
                /* Nota: captureImage di cameraKit non ha il 100% di probabilità di successo... */
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        Log.i(TAG, "onImage: Image captured");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage,0,capturedImage.length);
                        bitmap = Bitmap.createScaledBitmap(bitmap,cameraKitView.getWidth(),cameraKitView.getHeight(),false);

                        InputImage inputImage = InputImage.fromBitmap(bitmap, (int) cameraKitView.getRotation());
                        performBCScanning(inputImage);

                    }
                });
            }
        });

        return myFragment;
    }

    private void performBCScanning(InputImage inputImage){
        Log.i(TAG, "performBCScanning: performing scanning...");
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        boolean isJustScanning = onlyScanning();
                        Log.i(TAG, "onSuccess: is onlyScanning? " + isJustScanning);
                        for (Barcode bc : barcodes) {
                            if (bc.getValueType() == Barcode.TYPE_ISBN ){
                                Log.i(TAG, "onSuccess: Barcode found ==> " + bc.getRawValue());
                                String ISBN = bc.getRawValue();
                                if(!isJustScanning)
                                    new BarcodeLookup(Navigation.findNavController(getView()), getContext()).execute(ISBN);   //lookingUp for the book just scanned
                                else
                                    sendISBNresult(ISBN);
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


    private boolean onlyScanning(){
        if(getArguments() == null)
            return false;

        BCScanFragmentArgs args = BCScanFragmentArgs.fromBundle(getArguments());
        return args.getSearchBrScanning();
    }

    private void sendISBNresult(String ISBN){

        final NavController navController = Navigation.findNavController(getView());

        BCScanFragmentDirections.ActionNavigationScanToNavigationSearch action =
                BCScanFragmentDirections.actionNavigationScanToNavigationSearch();
        action.setISBNresult(ISBN);
        navController.navigate(action);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}