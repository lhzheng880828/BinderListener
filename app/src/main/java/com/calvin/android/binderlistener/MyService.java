package com.calvin.android.binderlistener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.calvin.android.binderlistener.fwlib.ConfCtlManager;
import com.calvin.android.binderlistener.fwlib.ServiceManagerReflect;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private ConfServiceBinderImpl binder;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new ConfServiceBinderImpl(getApplicationContext());
        ServiceManagerReflect.addService(ConfCtlManager.SERVICE_NAME, binder);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

}
