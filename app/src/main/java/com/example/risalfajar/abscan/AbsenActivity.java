package com.example.risalfajar.abscan;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.util.List;

public class AbsenActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private TextView barcodeText;
    private Handler handler = new Handler();
    Runnable captureImageRunnable;
    private int delay = 2000; //miliseconds
    private FirebaseVisionImage image;
    private FirebaseVisionImageMetadata imageMetadata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        barcodeText = findViewById(R.id.tv_barcode);
        cameraKitView = findViewById(R.id.camera);
        cameraKitView.setImageMegaPixels(480);

        imageMetadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setWidth(360)
                .setHeight(360)
                .setRotation(0)
                .build();

        captureImageRunnable = new Runnable() {
            @Override
            public void run() {
                cameraKitView.captureFrame(new CameraKitView.FrameCallback() {
                    @Override
                    public void onFrame(CameraKitView cameraKitView, byte[] bytes) {

                    }
                });
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        image = FirebaseVisionImage.fromByteArray(bytes, imageMetadata);
                        scanBarcodes(image);
                    }
                });
                handler.postDelayed(this, delay);
            }
        };
    }

    private void scanBarcodes(FirebaseVisionImage image){
        //Mengatur barcode detector agar hanya mendeteksi format QR Code saja, ini mempercepat proses deteksi
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        for(FirebaseVisionBarcode barcode:firebaseVisionBarcodes){
                            String nim = barcode.getRawValue();
                            barcodeText.setText(nim);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AbsenActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
        handler.postDelayed(captureImageRunnable, delay);
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        handler.removeCallbacks(captureImageRunnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
