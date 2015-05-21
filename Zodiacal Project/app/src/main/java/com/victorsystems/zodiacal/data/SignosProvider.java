package com.victorsystems.zodiacal.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SignosProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SignosDbHelper mOpenHelper;

    static final int SIGNOS = 100;
    static final int SIGNO_WITH_CUALIDADES = 101;
    static final int CUALIDADES = 200;

    private static final SQLiteQueryBuilder sSignoWithCualidadesQueryBuilder;
    static{
        sSignoWithCualidadesQueryBuilder = new SQLiteQueryBuilder();
        sSignoWithCualidadesQueryBuilder.setTables(
                SignosContract.CualidadesEntry.TABLE_NAME + " INNER JOIN " +
                        SignosContract.SignosEntry.TABLE_NAME +
                        " ON " + SignosContract.CualidadesEntry.TABLE_NAME +
                        "." + SignosContract.CualidadesEntry.COLUMN_SIGNO_ID +
                        " = " + SignosContract.SignosEntry.TABLE_NAME +
                        "." + SignosContract.SignosEntry.COLUMN_SIGNO_ID);
    }

    private static String selectString(Context context) {
        String a, b, result;

        a = "SELECT " + SignosContract.SignosEntry._ID + ", " +
                SignosContract.SignosEntry.COLUMN_SIGNO_ID + ", " +
                SignosContract.SignosEntry.COLUMN_SIGNO_DESCRIPCION + ", " +
                SignosContract.SignosEntry.COLUMN_AMOR + ", " +
                SignosContract.SignosEntry.COLUMN_DINERO + ", " +
                SignosContract.SignosEntry.COLUMN_DINERO + ", " +
                "1 as jerarquia " + " " +
                "FROM " + SignosContract.SignosEntry.TABLE_NAME + " " +
                "WHERE " + SignosContract.SignosEntry.COLUMN_SIGNO_ID + " = " +
                String.valueOf(AuxiliarFunctions.personalSignToSignoId(context));
        b = "SELECT " + SignosContract.SignosEntry._ID + ", " +
                SignosContract.SignosEntry.COLUMN_SIGNO_ID + ", " +
                SignosContract.SignosEntry.COLUMN_SIGNO_DESCRIPCION + ", " +
                SignosContract.SignosEntry.COLUMN_AMOR + ", " +
                SignosContract.SignosEntry.COLUMN_DINERO + ", " +
                SignosContract.SignosEntry.COLUMN_DINERO + ", " +
                "2 as jerarquia " + " " +
                "FROM " + SignosContract.SignosEntry.TABLE_NAME + " " +
                "WHERE " + SignosContract.SignosEntry.COLUMN_SIGNO_ID + " <> " +
                String.valueOf(AuxiliarFunctions.personalSignToSignoId(context));

        result = a + " UNION " + b + " ORDER BY jerarquia ";
        return result;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SignosContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SignosContract.PATH_SIGNO, SIGNOS);
        matcher.addURI(authority, SignosContract.PATH_SIGNO + "/#", SIGNO_WITH_CUALIDADES);

        matcher.addURI(authority, SignosContract.PATH_CUALIDAD, CUALIDADES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SignosDbHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SIGNOS:
                return SignosContract.SignosEntry.CONTENT_TYPE;
            case SIGNO_WITH_CUALIDADES:
                return SignosContract.SignosEntry.CONTENT_ITEM_TYPE;
            case CUALIDADES:
                return SignosContract.CualidadesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case SIGNOS:
            {
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(selectString(getContext()), null);
                break;
            }
            case SIGNO_WITH_CUALIDADES: {
                String[] condition = new String[]{String.valueOf(SignosContract.SignosEntry.getIdFromUri(uri))};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SignosContract.SignosEntry.TABLE_NAME,
                        projection,
                        SignosContract.SignosEntry._ID + " = ?",
                        condition,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CUALIDADES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SignosContract.CualidadesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SIGNOS: {
                long _id = db.insert(SignosContract.SignosEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SignosContract.SignosEntry.buildSignosUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CUALIDADES: {
                long _id = db.insert(SignosContract.CualidadesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SignosContract.CualidadesEntry.buildCualidadesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case SIGNOS:
                rowDeleted = db.delete(SignosContract.SignosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CUALIDADES:
                rowDeleted = db.delete(SignosContract.CualidadesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match) {
            case SIGNOS:
                rowsUpdated = db.update(SignosContract.SignosEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CUALIDADES:
                rowsUpdated = db.update(SignosContract.CualidadesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;

        switch (match) {
            case SIGNOS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(SignosContract.SignosEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case CUALIDADES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(SignosContract.CualidadesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
