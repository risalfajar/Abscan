package com.example.risalfajar.abscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.risalfajar.abscan.entity.Mahasiswa;

import java.util.ArrayList;

public class MahasiswaHelper {

    private static String DATABASE_TABLE = DatabaseContract.TABLE_MHS;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public MahasiswaHelper(Context context) {
        this.context = context;
    }

    public MahasiswaHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<Mahasiswa> query(){
        ArrayList<Mahasiswa> arrayList = new ArrayList<Mahasiswa>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
                null, null);
        cursor.moveToFirst();
        Mahasiswa mahasiswa;

        if(cursor.getCount() > 0){
            do{
                mahasiswa = new Mahasiswa();
                mahasiswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NAME)));
                mahasiswa.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM)));
                mahasiswa.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.EMAIL)));

                arrayList.add(mahasiswa);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Mahasiswa query(String nim) {
        Log.d(getClass().getSimpleName(), "Input query: " + nim);

        Cursor cursor = database.query(DATABASE_TABLE, null, DatabaseContract.MahasiswaColumns.NIM + "= '" + nim + "'", null, null, null,
                null, null);
        cursor.moveToFirst();
        Mahasiswa mahasiswa = null;

        if (cursor.getCount() > 0) {
            do {
                mahasiswa = new Mahasiswa();
                mahasiswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NAME)));
                mahasiswa.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM)));
                mahasiswa.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.EMAIL)));

                Log.d(getClass().getSimpleName(), "Hasil query: " + mahasiswa.getNim());

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return mahasiswa;
    }

    public long insert(Mahasiswa mahasiswa){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.MahasiswaColumns.NAME, mahasiswa.getNama());
        initialValues.put(DatabaseContract.MahasiswaColumns.NIM, mahasiswa.getNim());
        initialValues.put(DatabaseContract.MahasiswaColumns.EMAIL, mahasiswa.getEmail());

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(Mahasiswa mahasiswa){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.MahasiswaColumns.NAME, mahasiswa.getNama());
        args.put(DatabaseContract.MahasiswaColumns.NIM, mahasiswa.getNim());
        args.put(DatabaseContract.MahasiswaColumns.EMAIL, mahasiswa.getEmail());

        return database.update(DATABASE_TABLE, args, DatabaseContract.MahasiswaColumns.NIM + "= '" + mahasiswa.getNim() + "'", null);
    }

    public int delete(String nim) {
        return database.delete(DATABASE_TABLE, DatabaseContract.MahasiswaColumns.NIM + " = '" + nim + "'", null);
    }
}
