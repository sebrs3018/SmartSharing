package com.sebrs3018.SmartSharing;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.sebrs3018.SmartSharing.DB.DbContext;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;


import java.util.List;

public class BarcodeScanner extends AppCompatActivity {

    private final String TAG = "BarcodeScanner";
    private CameraView cameraView = null;
    private Button bttScan = null;

    private InputImage inputImage = null;
    private com.google.mlkit.vision.barcode.BarcodeScanner scanner = null;
    private String barcodeInfo = null;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);

        cameraView = findViewById(R.id.cameraView);
        bttScan = findViewById(R.id.bttScan);

        scanner = BarcodeScanning.getClient();

        bttScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick() fired");
                cameraView.start();
                cameraView.captureImage();
            } /* OnClick() */
        }); /* OnClick() Listener */

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                /* non ci serve */
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
                /* non ci serve */
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Log.e(TAG, "onImage() fired");
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);

                inputImage = InputImage.fromBitmap(bitmap, 0);
                scanBarcode(inputImage);
//                cameraView.stop();
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
                /* non ci serve */
            }
        });
    }/* onCreate() */

    public void scanBarcode(InputImage _image){
        if (inputImage != null){
            Task<List<Barcode>> result = scanner.process(_image).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                @Override
                public void onSuccess(List<Barcode> barcodes) {
                    Log.e(TAG,"barcodes size: "+ barcodes.size());

                    for (Barcode b : barcodes){
                        if (b.getValueType() == Barcode.TYPE_ISBN){
                            barcodeInfo = b.getRawValue();
                            Toast.makeText(BarcodeScanner.this, "Barcode Found: "+barcodeInfo, Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e(TAG,"Smartsharing can only scan barcodes, try again.");
                        }
                    } /* for Barcode ... */

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"Error: onFailure() fired.");
                    Log.e(TAG,"Error: "+e);
                }
            });
        } else {
            Toast.makeText(BarcodeScanner.this,"Impossible to scan from null imageInput", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

}