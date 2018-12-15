package com.example.risalfajar.abscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.risalfajar.abscan.database.MahasiswaHelper;
import com.example.risalfajar.abscan.entity.Mahasiswa;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerBtn;
    private EditText nameEt, nimEt, emailEt;
    private boolean isEmptyFields = false;
    private MahasiswaHelper mahasiswaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.btn_register);
        nameEt = findViewById(R.id.edt_name);
        nimEt = findViewById(R.id.edt_nim);
        emailEt = findViewById(R.id.edt_email);

        registerBtn.setOnClickListener(this);
        mahasiswaHelper = new MahasiswaHelper(this);
        mahasiswaHelper.open();
    }

    @Override
    public void onClick(View view) {
        String name = nameEt.getText().toString().trim();
        String nim = nimEt.getText().toString().trim().toUpperCase();
        String email = emailEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            isEmptyFields = true;
            nameEt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(nim)) {
            isEmptyFields = true;
            nimEt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(email)) {
            isEmptyFields = true;
            emailEt.setError("Field ini tidak boleh kosong");
        }
        if (nim.contains(" ")) {
            isEmptyFields = true;
            nimEt.setError("NIM tidak valid");
        }
        if (!isEmptyFields) {
            Register(name, nim, email);
        }
    }

    void Register(String name, String nim, String email) {
        //Jika mahasiswa sudah terdaftar
        if (mahasiswaHelper.query(nim) != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Gagal")
                    .setMessage("NIM sudah terdaftar sebelumnya")
                    .create()
                    .show();

            return;
        }

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNama(name);
        mahasiswa.setNim(nim);
        mahasiswa.setEmail(email);

        mahasiswaHelper.insert(mahasiswa);

        Intent showQr = new Intent(RegisterActivity.this, ShowQrCodeActivity.class);
        showQr.putExtra(ShowQrCodeActivity.EXTRA_MHS_DATA, mahasiswa);

        startActivity(showQr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mahasiswaHelper != null)
            mahasiswaHelper.close();
    }
}

