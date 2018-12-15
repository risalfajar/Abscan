package com.example.risalfajar.abscan.database;

import android.provider.BaseColumns;

/**
 * Menyimpan data-data seputar database Mahasiswa
 * Seperti nama table, dan nama-nama kolom
 */
public class DatabaseContract {

    static String TABLE_MHS = "mahasiswa";
    static String TABLE_ABSEN = "absen";

    static final class MahasiswaColumns {
        static String NAME = "name";
        static String NIM = "nim";
        static String EMAIL = "email";
    }

    static final class AbsenColumns implements BaseColumns {
        static String DATETIME = "datetime";
    }
}
