package com.victorsystems.zodiacal.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class SignosContract {
    public static final String CONTENT_AUTHORITY = "com.victorsystems.zodiacal";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SIGNO = "signo";
    public static final String PATH_CUALIDAD = "cualidad";

    public static final class SignosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SIGNO).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_SIGNO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_SIGNO;

        public static final String TABLE_NAME = "signos";

        public static final String COLUMN_SIGNO_ID = "signoID";
        public static final String COLUMN_SIGNO_DESCRIPCION = "signoDescripcion";
        public static final String COLUMN_AMOR = "amor";
        public static final String COLUMN_SALUD = "salud";
        public static final String COLUMN_DINERO = "dinero";

        public static Uri buildSignosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildHoroscopeUri() {
            return CONTENT_URI;
        }

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class CualidadesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CUALIDAD).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_CUALIDAD;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_CUALIDAD;

        public static final String TABLE_NAME = "cualidades";

        public static final String COLUMN_SIGNO_ID = "signoID";
        public static final String COLUMN_CUALIDAD = "cualidad";

        public static Uri buildCualidadesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int getSignoIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }
}
