package com.sebrs3018.SmartSharing;

//import android.app.AlertDialog;
//import android.graphics.Bitmap;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.face.FirebaseVisionFace;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
//import com.sebrs3018.SmartSharing.Helper.GraphicOverlay;
//import com.sebrs3018.SmartSharing.Helper.RectOverlay;
//import com.wonderkiln.camerakit.CameraKit;
//import com.wonderkiln.camerakit.CameraKitError;
//import com.wonderkiln.camerakit.CameraKitEvent;
//import com.wonderkiln.camerakit.CameraKitEventListener;
//import com.wonderkiln.camerakit.CameraKitImage;
//import com.wonderkiln.camerakit.CameraKitVideo;
//import com.wonderkiln.camerakit.CameraView;
//
//import java.util.List;
//
//import dmax.dialog.SpotsDialog;
//
//public class MainActivity extends AppCompatActivity {
//
//
//    private Button bttFaceDetection;
//    private GraphicOverlay graphicOverlay;
//    private CameraView cameraView;
//    AlertDialog alertDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.face_recognizer);
//
//
//        bttFaceDetection    = findViewById(R.id.detect_face_btn);
//        graphicOverlay      = findViewById(R.id.graphic_overlay);
//        cameraView          = findViewById(R.id.camera_view);
//        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
//
//        alertDialog = new SpotsDialog.Builder()
//                                     .setContext(this)
//                                     .setMessage("Please Wait, Loading...")
//                                     .setCancelable(false)
//                                     .build();
//
//
//        bttFaceDetection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cameraView.start();
//                cameraView.captureImage();
//                graphicOverlay.clear();
//            }
//        });
//
//
//        cameraView.addCameraKitListener(new CameraKitEventListener() {
//            @Override
//            public void onEvent(CameraKitEvent cameraKitEvent) {
//
//            }
//
//            @Override
//            public void onError(CameraKitError cameraKitError) {
//
//            }
//
//            @Override
//            public void onImage(CameraKitImage cameraKitImage) {
//                alertDialog.show(); //Mostra il dialogo quando si to
//                Bitmap bitmap = cameraKitImage.getBitmap();
//                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
//                cameraView.stop();
//
//                processFaceDetection(bitmap);
//
//            }
//
//            @Override
//            public void onVideo(CameraKitVideo cameraKitVideo) {
//
//            }
//        });
//
//    }
//
//    private void processFaceDetection(Bitmap bitmap) {
//        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions.Builder().build();
//        FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance()
//                .getVisionFaceDetector(firebaseVisionFaceDetectorOptions);
//
//        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
//            @Override
//            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
//                getFaceResults(firebaseVisionFaces);
//
//
//                alertDialog = new SpotsDialog.Builder()
//                        .setContext(MainActivity.this)
//                        .setMessage("Utente Riconosciuto!")
//                        .setCancelable(false)
//                        .build();
//
//                alertDialog.show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Error: " +  e.getMessage(), Toast.LENGTH_SHORT);
//            }
//        });
//    }
//
//    private void getFaceResults(List<FirebaseVisionFace> firebaseVisionFaces) {
//        int counter = 0;
//        //Get rectangles on the pictures
//        for (FirebaseVisionFace face : firebaseVisionFaces){
//            Rect rect = face.getBoundingBox();
//
//            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, rect);
//            graphicOverlay.add(rectOverlay);
//        }
//        alertDialog.dismiss();
//
//
//
//        counter = counter++;
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cameraView.stop();
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        cameraView.start();
//    }
//}