package com.victorsystems.zodiacal.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ZodiacalAuthenticatorService extends Service {

    private ZodiacalAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new ZodiacalAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }


}
