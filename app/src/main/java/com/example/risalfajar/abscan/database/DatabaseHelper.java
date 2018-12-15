package com.example.risalfajar.abscan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Berfungsi untuk membuat database dan table yang dibutuhkan
 * serta menghandle perubahan skema table di OnUpgrade()
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Nama Database
    public static String DATABASE_NAME = "dbloginmhs";
    //Versi Database
    private static final int DATABASE_VERSION = 1;
    //SQL untuk membuat table mahasiswa
    private static final String SQL_CREATE_TABLE_MHS = String.format("CREATE TABLE %s" +
                    " (%s TEXT PRIMARY KEY," +
            " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL);",
            DatabaseContract.TABLE_MHS,
            DatabaseContract.MahasiswaColumns.NIM,
            DatabaseContract.MahasiswaColumns.NAME,
            DatabaseContract.MahasiswaColumns.EMAIL
    );

    //SQL untuk membuat table absen
    private static final String SQL_CREATE_TABLE_ABSEN = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " FOREIGN KEY (%s) REFERENCES %s(%s));",
            DatabaseContract.TABLE_ABSEN,
            DatabaseContract.AbsenColumns._ID,
            DatabaseContract.AbsenColumns.DATETIME,
            DatabaseContract.MahasiswaColumns.NIM,
            DatabaseContract.MahasiswaColumns.NIM,
            DatabaseContract.TABLE_MHS,
            DatabaseContract.MahasiswaColumns.NIM
    );

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Mengeksekusi SQL untuk membuat table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MHS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ABSEN);
    }

    //Apabila terjadi perubahan skema table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Menghapus table yang sudah ada
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MHS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_ABSEN);
        //Membuat table baru
        onCreate(sqLiteDatabase);
    }
}
