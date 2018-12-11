package com.example.risalfajar.abscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister, btnAbsen, btnCekData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btn_register);
        btnAbsen = findViewById(R.id.btn_absen);
        btnCekData = findViewById(R.id.btn_rekap_data);

        btnRegister.setOnClickListener(this);
        btnAbsen.setOnClickListener(this);
        btnCekData.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.btn_absen:
                Intent absenIntent = new Intent(MainActivity.this, AbsenActivity.class);
                startActivity(absenIntent);
                break;
        }
    }
}
