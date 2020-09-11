package com.calvin.android.binderlistener.fwlib;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.IntDef;

import com.calvin.android.binderlistener.IConfService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class ConfCtlManager {

    private static final String TAG = "ConfCtlManager";
    public static final String SERVICE_NAME = "conf_service";

    public static final String CONF_SERVER_ACTION = "com.calvin.android.action.CONF_SERVER";

    @IntDef(value = {CONF_STATE_IDLE, CONF_STATE_RING, CONF_STATE_CALLING, CONF_STATE_CONNECTED, CONF_STATE_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfState {}

    public static final int CONF_STATE_IDLE = 0;

    public static final int CONF_STATE_RING = 1;

    public static final int CONF_STATE_CALLING =2;

    public static final int CONF_STATE_CONNECTED = 3;

    public static final int CONF_STATE_END = 4;

    private IConfService confService;

    private static ConfCtlManager instance;

    private ConfCtlManager(){
        IConfService binderService = getConferenceService();
        if (binderService == null){
            Log.e(TAG, "======== error,ConfService is null.");
        }
        confService = binderService;
    }

    public static ConfCtlManager getInstance(){
        if (instance == null){
            synchronized (ConfCtlManager.class){
                if (instance == null){
                    instance = new ConfCtlManager();
                }
            }
        }
        return instance;
    }
    private IConfService getConferenceService() {
        if (confService == null) {
            IBinder binder = ServiceManagerReflect.getService(SERVICE_NAME);
            confService = IConfService.Stub.asInterface(binder);
            if (confService == null) {
                Log.e(TAG, "getConferenceService CmccConferenceService is  null!!!");
                return null;
            }
            try {
                confService.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return confService;
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: CONFERENCE SERVICE is Died!");
            if (confService != null) {
                confService.asBinder().unlinkToDeath(this, 0);
                confService = null;
            }
        }
    };

    public void createConf(){
        if (getConferenceService() == null) {
            Log.e(TAG, "ConfService is  null!!!");
            return;
        }

        try {
            getConferenceService().createConf();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void endConf(){
        if (getConferenceService() == null) {
            Log.e(TAG, "ConfService is  null!!!");
            return;
        }
        try {
            getConferenceService().endConf();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * when you want to listener some event, eg: listen(listener, ConfStateListener.LISTEN_CONF_STATE |
     * ConfStateListener.LISTEN_SUBSCRIBER_STATE)
     * when you want to cancel listener, eg: listen(listener, ConfStateListener.LISTEN_NONE)
     * @param listener conf state listener
     * @param event subscribe event
     */
    public void listen(ConfStateListener listener, int event){
        if (getConferenceService() == null) {
            Log.e(TAG, "ConfService is  null!!!");
            return;
        }
        try {
            String pkg = getConferenceService().getCurrentPackageName();
            getConferenceService().listenForSubscriber(pkg, listener.callback, event, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
