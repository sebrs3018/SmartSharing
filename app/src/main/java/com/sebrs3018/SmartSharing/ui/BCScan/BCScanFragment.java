package com.sebrs3018.SmartSharing.ui.BCScan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
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
import com.sebrs3018.SmartSharing.barcode.BarcodeLookup;
import com.sebrs3018.SmartSharing.databinding.FragmentBCScanBinding;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
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
//        cameraKitView = binding.cameraKV;

        binding.imgCapture.bringToFront();
        startCamera();
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



    private void startCamera(){
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(binding.viewFinder.getWidth(), binding.viewFinder.getHeight());
        Size screen = new Size(binding.viewFinder.getWidth(), binding.viewFinder.getHeight());

        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();

        Preview preview = new Preview(pConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) binding.viewFinder.getParent();
                parent.removeView(binding.viewFinder);
                parent.addView(binding.viewFinder, 0);
                /* refreshing rendering camera screen to view  */
                binding.viewFinder.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation()).build();

        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        binding.imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
                /* catturo immagine... */
                imgCap.takePicture(new ImageCapture.OnImageCapturedListener() {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
//                        Toast.makeText(getContext(), "Foto effettuata! rotation degrees ==> " + rotationDegrees,Toast.LENGTH_SHORT).show();
                        InputImage inputImage = InputImage.fromBitmap(imageProxyToBitmap(image), rotationDegrees);
                        performBCScanning(inputImage);
                        super.onCaptureSuccess(image, rotationDegrees);
                    }

                    @Override
                    public void onError(ImageCapture.UseCaseError useCaseError, String message, @Nullable @org.jetbrains.annotations.Nullable Throwable cause) {
                        Toast.makeText(getContext(), "L'immagine non è stata catturata, riprova ",Toast.LENGTH_SHORT).show();
                        super.onError(useCaseError, message, cause);
                    }
                });


            }
        });

        /* binding to lifeCycle */
        CameraX.bindToLifecycle((LifecycleOwner)this, preview, imgCap);

    }


    private Bitmap imageProxyToBitmap(ImageProxy image)
    {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public void updateTransform(){

        //import android graphics
        Matrix mx = new Matrix();

        float w = binding.viewFinder.getMeasuredWidth();
        float h = binding.viewFinder.getMeasuredHeight();

        float cx = w/2f;
        float cy = h/2f;

        int rotationDgr = 90;
        int rotation = (int)binding.viewFinder.getRotation();

        switch (rotation){
            case Surface
                    .ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface
                    .ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface
                    .ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface
                    .ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        /* rotation degree and point to rotate */
        mx.postRotate((float)rotationDgr,cx,cy);
        binding.viewFinder.setTransform(mx);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


}