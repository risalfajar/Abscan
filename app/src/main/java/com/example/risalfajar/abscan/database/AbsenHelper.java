package com.example.risalfajar.abscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.risalfajar.abscan.entity.AbsenData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AbsenHelper {

    public static String DATETIME_FORMAT = "HH:mm:ss E dd-MM-yyyy";
    public static String DATE_FORMAT = "dd-MM-yyyy";

    private static String DATABASE_TABLE = DatabaseContract.TABLE_ABSEN;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private MahasiswaHelper mahasiswaHelper;
    private AbsenResponse absenResponse;

    public AbsenHelper(Context context) {
        this.context = context;
    }

    public AbsenHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        mahasiswaHelper = new MahasiswaHelper(context);
        mahasiswaHelper.open();
        return this;
    }

    public void close() {
        databaseHelper.close();
        mahasiswaHelper.close();
    }

    public void setCheckResponseHandler(AbsenResponse absenResponse) {
        this.absenResponse = absenResponse;
    }

    public ArrayList<AbsenData> query() {
        ArrayList<AbsenData> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        AbsenData data;

        if (cursor.getCount() > 0) {
            do {
                data = new AbsenData();
                data.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.AbsenColumns._ID)));
                data.setDatetime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AbsenColumns.DATETIME)));
                data.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM)));

                arrayList.add(data);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public void absenCheck(String nim, String dateString) {
        //region Cek mahasiswa sudah terdaftar
        if (mahasiswaHelper.query(nim) == null) {
            //Mahasiswa tidak terdaftar
            absenResponse.onMahasiswaNotRegistered(nim);
            return;
        }
        //endregion
        Cursor cursor = database.query(DATABASE_TABLE, null, DatabaseContract.MahasiswaColumns.NIM + "= '" + nim + "'", null, null, null, null);
        cursor.moveToFirst();

        Log.d(getClass().getSimpleName(), "Cursor Count: " + cursor.getCount());

        //Jika mahasiswa pernah absen sebelumnya
        if (cursor.getCount() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
            Date date;

            do {
                //region CEK TANGGAL
                //Apakah pada tanggal yang sama sudah mealkukan absen?
                String datetimeRecorded = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AbsenColumns.DATETIME));
                String dateRecorded = null;
                try {
                    //Mengambil tanggal absen yang tersimpan pada database
                    date = datetimeFormat.parse(datetimeRecorded);
                    dateRecorded = dateFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    dateRecorded = null;
                }
                //endregion
                if (dateRecorded != null) {
                    //Jika tanggal yang diinputkan sama dengan tanggal yang tersimpan dalam database
                    if (dateString.equals(dateRecorded)) {
                        absenResponse.onMahasiswaHaveAbsen(nim);
                        cursor.close();
                        return;
                    }
                }
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        absenResponse.onAbsenValid(nim);
        cursor.close();
    }

    //region CRUD
    public long insert(AbsenData data) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.AbsenColumns.DATETIME, data.getDatetime());
        initialValues.put(DatabaseContract.MahasiswaColumns.NIM, data.getNim());

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(AbsenData data) {
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.AbsenColumns.DATETIME, data.getDatetime());
        args.put(DatabaseContract.MahasiswaColumns.NIM, data.getNim());

        return database.update(DATABASE_TABLE, args, DatabaseContract.AbsenColumns._ID, new String[]{String.valueOf(data.getId())});
    }

    public int delete(int id) {
        return database.delete(DATABASE_TABLE, DatabaseContract.AbsenColumns._ID, new String[]{String.valueOf(id)});
    }
    //endregion

    public interface AbsenResponse {
        void onMahasiswaNotRegistered(String nim);

        void onMahasiswaHaveAbsen(String nim);

        void onAbsenValid(String nim);
    }
}
