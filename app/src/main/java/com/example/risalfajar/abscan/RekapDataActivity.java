package com.example.risalfajar.abscan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.risalfajar.abscan.database.AbsenHelper;
import com.example.risalfajar.abscan.entity.AbsenData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RekapDataActivity extends AppCompatActivity {

    private AbsenHelper absenHelper;
    private TableLayout tableLayout;
    private int rowIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_data);

        tableLayout = findViewById(R.id.table_rekap);

        absenHelper = new AbsenHelper(this);
        absenHelper.open();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(AbsenHelper.DATE_FORMAT);
        String dateString = dateFormat.format(date);
        Log.d(this.getClass().getSimpleName(), "Date String now : " + dateString);
        loadData(dateString);
    }

    private void loadData(String dateString) {
        ArrayList<AbsenData> data = absenHelper.query(dateString);
        for (int i = 0; i < data.size(); i++) {
            addRow(data.get(i).getDatetime(), data.get(i).getNim());
        }
    }

    private void addRow(String datetime, String nim) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setWeightSum(10);

        //Nomor
        TextView tvNo = new TextView(this);
        tvNo.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvNo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvNo.setTextSize(12f);

        //DateTime
        TextView tvDatetime = new TextView(this);
        tvDatetime.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4f));
        tvDatetime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvDatetime.setTextSize(12f);

        //NIM
        TextView tvNim = new TextView(this);
        tvNim.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 5f));
        tvNim.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvNim.setTextSize(12f);

        tvNo.setText(String.valueOf(rowIndex));
        tvNim.setText(nim);
        tvDatetime.setText(datetime);
        rowIndex++;

        tr.addView(tvNo);
        tr.addView(tvDatetime);
        tr.addView(tvNim);
        tableLayout.addView(tr);
    }

    @Override
    protected void onDestroy() {
        absenHelper.close();
        super.onDestroy();
    }
}
