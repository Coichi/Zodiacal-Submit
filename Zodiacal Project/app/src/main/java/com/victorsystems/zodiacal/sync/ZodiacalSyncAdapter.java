package com.victorsystems.zodiacal.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.victorsystems.zodiacal.MainActivity;
import com.victorsystems.zodiacal.data.AuxiliarFunctions;
import com.victorsystems.zodiacal.data.SignosContract;
import com.victorsystems.zodiacal.data.Structures.Signos;
import com.victorsystems.zodiacal.data.Structures.Cualidades;
import com.victorsystems.zodiacal.R;
import com.victorsystems.zodiacal.data.WebServiceHandler;

import java.util.ArrayList;
import java.util.Vector;

public class ZodiacalSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 60 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24 * 2;
    private static final int APP_NOTIFICATION_ID = 1608;

    public ZodiacalSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager)context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (null == accountManager.getPassword(newAccount) ) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        ZodiacalSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {*/
        ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        //}
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (AuxiliarFunctions.checkInternetConnection(getContext())) {
            cleanDataBase();
            downloadSignos();
            downloadCualidades();
            sendNotification();
        }
    }

    private void cleanDataBase(){
        try {
            getContext().getContentResolver().delete(SignosContract.CualidadesEntry.CONTENT_URI, null, null);
            getContext().getContentResolver().delete(SignosContract.SignosEntry.CONTENT_URI, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadSignos(){
        try {
            WebServiceHandler wsHandler = new WebServiceHandler();
            ArrayList<Signos> signos = wsHandler.signos();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(signos.size());

            for(int i = 0; i < signos.size(); i++) {
                ContentValues signosValues = new ContentValues();

                signosValues.put(SignosContract.SignosEntry.COLUMN_SIGNO_ID, signos.get(i).getSignoID());
                signosValues.put(SignosContract.SignosEntry.COLUMN_SIGNO_DESCRIPCION, signos.get(i).getSignoDescripcion());
                signosValues.put(SignosContract.SignosEntry.COLUMN_AMOR, signos.get(i).getAmor());
                signosValues.put(SignosContract.SignosEntry.COLUMN_SALUD, signos.get(i).getSalud());
                signosValues.put(SignosContract.SignosEntry.COLUMN_DINERO, signos.get(i).getDinero());

                cVVector.add(signosValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(SignosContract.SignosEntry.CONTENT_URI, cvArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadCualidades() {
        try {
            WebServiceHandler wsHandler = new WebServiceHandler();
            ArrayList<Cualidades> cualidades = wsHandler.cualidades();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(cualidades.size());

            for(int i = 0; i < cualidades.size(); i++) {
                ContentValues cualidadesValues = new ContentValues();

                cualidadesValues.put(SignosContract.CualidadesEntry.COLUMN_SIGNO_ID, cualidades.get(i).getSignoID());
                cualidadesValues.put(SignosContract.CualidadesEntry.COLUMN_CUALIDAD, cualidades.get(i).getCualidad());

                cVVector.add(cualidadesValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(SignosContract.CualidadesEntry.CONTENT_URI, cvArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification() {
        Context context = getContext();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean displayNotifications = pref.getBoolean(context.getString(R.string.pref_notifications_key),
                Boolean.parseBoolean(context.getString(R.string.pref_notifications_default)));


        if (displayNotifications) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = pref.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= (DAY_IN_MILLIS)) {

                int iconId = R.mipmap.ic_launcher;
                Resources resources = context.getResources();
                Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
                String title = context.getString(R.string.app_name);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setColor(resources.getColor(R.color.black))
                                .setSmallIcon(iconId)
                                .setLargeIcon(largeIcon)
                                .setContentTitle(title)
                                .setContentText(context.getString(R.string.notification_message));

                Notification notification = mBuilder.build();
                notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

                Intent resultIntent = new Intent(context, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(APP_NOTIFICATION_ID, mBuilder.build());

                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }
        }
    }
}
