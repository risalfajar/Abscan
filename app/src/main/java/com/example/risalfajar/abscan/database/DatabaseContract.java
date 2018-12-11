package com.example.risalfajar.abscan.database;

import android.provider.BaseColumns;

/**
 * Menyimpan data-data seputar database Mahasiswa
 * Seperti nama table, dan nama-nama kolom
 */
public class DatabaseContract {

    static String TABLE_MHS = "mahasiswa";

    static final class MahasiswaColumns implements BaseColumns{
        static String NAME = "name";
        static String NIM = "nim";
        static String EMAIL = "email";
    }
}
