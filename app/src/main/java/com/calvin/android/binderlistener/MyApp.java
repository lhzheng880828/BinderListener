package com.calvin.android.binderlistener;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.calvin.android.binderlistener.fwlib.ConfCtlManager;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class MyApp extends Application {

    private static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();
        startServiceComponent(ConfCtlManager.CONF_SERVER_ACTION);
    }


    private void startServiceComponent(String action){
        Log.d(TAG, "startServiceComponent invoke");
        try {
            ComponentName com = CommonUtils.resolveComponent(this, action);
            Intent intent = new Intent(action);
            if (com != null)
                intent.setComponent(com);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
