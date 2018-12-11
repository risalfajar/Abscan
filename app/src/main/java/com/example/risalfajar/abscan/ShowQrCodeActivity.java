package com.example.risalfajar.abscan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.risalfajar.abscan.entity.Mahasiswa;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowQrCodeActivity extends AppCompatActivity implements View.OnClickListener {

    static String EXTRA_MHS_DATA = "extra_mhs_data";
    private TextView tvNimMhs;
    private ImageView ivQrCode;
    private Button btnSendEmail;
    private Mahasiswa mahasiswa;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        tvNimMhs = findViewById(R.id.tv_mhs_qr);
        ivQrCode = findViewById(R.id.iv_mhs_qr);
        btnSendEmail = findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(this);

        mahasiswa = getIntent().getParcelableExtra(EXTRA_MHS_DATA);
        tvNimMhs.setText(mahasiswa.getNim());

        try {
            generateQRCodeImage(mahasiswa);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        String[] TO = {mahasiswa.getEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setDataAndType(Uri.parse("mailto:"), "image/png")
                .putExtra(Intent.EXTRA_EMAIL, TO)
                .putExtra(Intent.EXTRA_SUBJECT, "QR Code untuk NIM" + mahasiswa.getNim())
                .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imgFile));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ShowQrCodeActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    void generateQRCodeImage(Mahasiswa mahasiswa) throws WriterException, IOException{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(mahasiswa.getNim(), BarcodeFormat.QR_CODE, 720, 720);

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bmp = barcodeEncoder.createBitmap(bitMatrix);

        File root = Environment.getExternalStorageDirectory();
        if(root.canWrite()){
            imgFile = new File(root, mahasiswa.getNim() + ".png");
            FileOutputStream out = new FileOutputStream(imgFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }

        ivQrCode.setImageBitmap(bmp);
    }

}
