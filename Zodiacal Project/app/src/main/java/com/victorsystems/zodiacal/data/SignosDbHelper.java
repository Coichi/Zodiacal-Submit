package com.victorsystems.zodiacal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.victorsystems.zodiacal.data.SignosContract.SignosEntry;
import com.victorsystems.zodiacal.data.SignosContract.CualidadesEntry;

public class SignosDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "zodiacal.db";

    public SignosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SIGNOS_TABLE = "CREATE TABLE " + SignosEntry.TABLE_NAME + " (" +
                SignosEntry._ID + " INTEGER PRIMARY KEY, " +

                SignosEntry.COLUMN_SIGNO_ID + " INTEGER UNIQUE NOT NULL, " +
                SignosEntry.COLUMN_SIGNO_DESCRIPCION + " TEXT NOT NULL, " +
                SignosEntry.COLUMN_AMOR + " TEXT NOT NULL, " +
                SignosEntry.COLUMN_SALUD + " TEXT NOT NULL, " +
                SignosEntry.COLUMN_DINERO + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_SIGNOS_TABLE);

        final String SQL_CREATE_CUALIDADES_TABLE = "CREATE TABLE " + CualidadesEntry.TABLE_NAME + " (" +
                CualidadesEntry._ID + " INTEGER PRIMARY KEY, " +

                CualidadesEntry.COLUMN_SIGNO_ID + " INTEGER NOT NULL, " +
                CualidadesEntry.COLUMN_CUALIDAD + " TEXT NOT NULL, " +

                "FOREIGN KEY (" + CualidadesEntry.COLUMN_SIGNO_ID + ") REFERENCES " +
                SignosEntry.TABLE_NAME + " (" + SignosEntry.COLUMN_SIGNO_ID + "));";

        db.execSQL(SQL_CREATE_CUALIDADES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SignosEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CualidadesEntry.TABLE_NAME);
        onCreate(db);
    }
}
