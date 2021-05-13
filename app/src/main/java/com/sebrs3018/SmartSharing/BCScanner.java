package com.sebrs3018.SmartSharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class BCScanner extends AppCompatActivity {


    private CameraKitView cameraKV = null;
    private Button bttScan = null;

    private final String TAG = "BCScanner";


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        cameraKV = findViewById(R.id.cameraKV);

        bttScan = findViewById(R.id.bttScan);

        bttScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cameraKV.captureImage(new CameraKitView.ImageCallback() {
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
                                            if (bc.getValueType() == Barcode.TYPE_ISBN)
                                                Toast.makeText(BCScanner.this,"Barcode found: " + bc.getRawValue(),Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(BCScanner.this,"Wrong barcode type found",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(BCScanner.this,"Barcode detection failed.",Toast.LENGTH_SHORT).show();
                                        Log.e(TAG,e.getMessage());
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKV.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKV.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraKV.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraKV.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKV.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}