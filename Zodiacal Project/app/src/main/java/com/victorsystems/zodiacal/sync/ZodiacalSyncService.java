package com.victorsystems.zodiacal.sync;

import android.content.Intent;
import android.os.IBinder;
import android.app.Service;

public class ZodiacalSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ZodiacalSyncAdapter sZodiacalSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sZodiacalSyncAdapter == null) {
                sZodiacalSyncAdapter = new ZodiacalSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sZodiacalSyncAdapter.getSyncAdapterBinder();
    }
}
