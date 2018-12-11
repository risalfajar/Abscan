package com.example.risalfajar.abscan;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AbsenActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        scannerView = findViewById(R.id.zxing_barcode_scanner);
        hasil = findViewById(R.id.tv_barcode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        scannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void handleResult(Result result) {
        hasil.setText(result.getText());

        new AlertDialog.Builder(this)
                .setTitle("Hasil")
                .setMessage(hasil.getText())
                .create()
                .show();

        scannerView.resumeCameraPreview(this);
    }
}
