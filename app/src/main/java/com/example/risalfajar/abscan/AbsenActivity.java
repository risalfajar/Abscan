package com.example.risalfajar.abscan;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.risalfajar.abscan.database.AbsenHelper;
import com.example.risalfajar.abscan.entity.AbsenData;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AbsenActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, AbsenHelper.AbsenResponse {

    private ZXingScannerView scannerView;
    private TextView hasil;
    private AbsenHelper absenHelper;
    private AbsenData dataToInput;

    //region default Override Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        scannerView = findViewById(R.id.zxing_barcode_scanner);
        hasil = findViewById(R.id.tv_barcode);
        absenHelper = new AbsenHelper(this);
        absenHelper.setCheckResponseHandler(this);
        absenHelper.open();
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
    protected void onDestroy() {
        absenHelper.close();
        super.onDestroy();
    }

    //endregion

    @Override
    public void handleResult(Result result) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(AbsenHelper.DATETIME_FORMAT);
        hasil.setText(datetimeFormat.format(date));
        inputAbsen(result.getText());
    }

    private void inputAbsen(String nim) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(AbsenHelper.DATETIME_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(AbsenHelper.DATE_FORMAT);
        String datetime = datetimeFormat.format(date);
        String dateString = dateFormat.format(date);

        dataToInput = new AbsenData();
        dataToInput.setNim(nim);
        dataToInput.setDatetime(datetime);

        absenHelper.absenCheck(nim, dateString);
    }

    @Override
    public void onMahasiswaNotRegistered(String nim) {
        new AlertDialog.Builder(this)
                .setTitle("Tidak Terdaftar")
                .setMessage("Mahasiswa tidak terdaftar")
                .create()
                .show();
    }

    @Override
    public void onMahasiswaHaveAbsen(String nim) {
        new AlertDialog.Builder(this)
                .setTitle("Sudah Absen")
                .setMessage(nim + " sudah melakukan absen hari ini")
                .create()
                .show();
    }

    @Override
    public void onAbsenValid(String nim) {
        absenHelper.insert(dataToInput);

        new AlertDialog.Builder(this)
                .setTitle("Sukses")
                .setMessage("Selamat datang " + nim)
                .create()
                .show();
    }
}
