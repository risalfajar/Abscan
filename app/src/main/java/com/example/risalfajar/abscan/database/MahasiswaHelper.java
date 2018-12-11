package com.example.risalfajar.abscan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
                DatabaseContract.MahasiswaColumns._ID + " DESC", null);
        cursor.moveToFirst();
        Mahasiswa mahasiswa;

        if(cursor.getCount() > 0){
            do{
                mahasiswa = new Mahasiswa();
                mahasiswa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns._ID)));
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

    public long insert(Mahasiswa mahasiswa){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.MahasiswaColumns.NAME, mahasiswa.getNama());
        initialValues.put(DatabaseContract.MahasiswaColumns.NIM, mahasiswa.getNim());
        initialValues.put(DatabaseContract.MahasiswaColumns.EMAIL, mahasiswa.getEmail());

        return database.insert(DatabaseContract.TABLE_MHS, null, initialValues);
    }

    public int update(Mahasiswa mahasiswa){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.MahasiswaColumns.NAME, mahasiswa.getNama());
        args.put(DatabaseContract.MahasiswaColumns.NIM, mahasiswa.getNim());
        args.put(DatabaseContract.MahasiswaColumns.EMAIL, mahasiswa.getEmail());

        return database.update(DatabaseContract.TABLE_MHS, args, DatabaseContract.MahasiswaColumns._ID + "= '" + mahasiswa.getId() + "'", null);
    }

    public int delete(int id){
        return database.delete(DatabaseContract.TABLE_MHS, DatabaseContract.MahasiswaColumns._ID + " = '" + id + "'", null);
    }
}
