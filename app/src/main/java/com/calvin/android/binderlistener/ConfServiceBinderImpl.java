package com.calvin.android.binderlistener;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.calvin.android.binderlib.BinderListenerManager;
import com.calvin.android.binderlistener.fwlib.ConfCtlManager;
import com.calvin.android.binderlistener.fwlib.ConfStateListener;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class ConfServiceBinderImpl extends IConfService.Stub{

    private Context mContext;

    private BinderListenerManager<IConfChangedListener> binderListenerManager;

    public ConfServiceBinderImpl(Context context) {
        mContext = context;
        binderListenerManager = new BinderListenerManager<>("ConfStateListener");
    }

    @Override
    public void createConf() throws RemoteException {
        //do Something

        //通知会议状态变化
        binderListenerManager.notifyForAllSubsriber(ConfStateListener.LISTEN_CONF_STATE,
                new BinderListenerManager.IBinderCallback<IConfChangedListener>() {
            @Override
            public void onCallback(IConfChangedListener callback) throws RemoteException {
                callback.onConfChanged(ConfCtlManager.CONF_STATE_CONNECTED, new ConfInfo("123456", "My Conf"));
            }
        });
    }

    @Override
    public void endConf() throws RemoteException {

        //do somethings

        //通知会议参与者发生变化
        binderListenerManager.notifyForAllSubsriber(ConfStateListener.LISTEN_CONF_STATE,
                new BinderListenerManager.IBinderCallback<IConfChangedListener>() {
                    @Override
                    public void onCallback(IConfChangedListener callback) throws RemoteException {
                        callback.onConfChanged(ConfCtlManager.CONF_STATE_END, new ConfInfo("123456", "My Conf"));
                    }
                });
    }

    @Override
    public void listenForSubscriber(String callingPackage, IConfChangedListener callback, int events, boolean notifyNow) throws RemoteException {
        String str = "listen: E pkg=" + callingPackage + " uid=" + Binder.getCallingUid()
                + " events=0x" + Integer.toHexString(events) + " notifyNow=" + notifyNow ;
        Log.d(TAG, "listen info = "+str);
        if (events != ConfStateListener.LISTEN_NONE){
            IBinder b = callback.asBinder();
            BinderListenerManager.Record r = binderListenerManager.add(b, Binder.getCallingUid(), Binder.getCallingPid());
            if (r  == null) return;
            r.callingPackage = callingPackage;
            r.callback = callback;
            r.events = events;
            if (notifyNow){
                if ((events & ConfStateListener.LISTEN_CONF_STATE) != 0){
                    binderListenerManager.notifyForAllSubsriber(ConfStateListener.LISTEN_CONF_STATE,
                            new BinderListenerManager.IBinderCallback<IConfChangedListener>() {
                                @Override
                                public void onCallback(IConfChangedListener callback) throws RemoteException {
                                    callback.onConfChanged(ConfCtlManager.CONF_STATE_IDLE,  new ConfInfo("123456", "New Conf"));
                                }
                            });
                }

                if ((events & ConfStateListener.LISTEN_SUBSCRIBER_STATE) !=0){
                    binderListenerManager.notifyForAllSubsriber(ConfStateListener.LISTEN_SUBSCRIBER_STATE,
                            new BinderListenerManager.IBinderCallback<IConfChangedListener>() {
                                @Override
                                public void onCallback(IConfChangedListener callback) throws RemoteException {
                                    callback.onSubscriberChanged(new SubscriberInfo("123", "zhangsan"));
                                }
                            });
                }

            }

        }else{
            Log.i(TAG, "listen: Unregister");
            binderListenerManager.remove(callback.asBinder());
        }

    }

    private static final String TAG = "ConfServiceBinderImpl";

    @Override
    public String getCurrentPackageName() throws RemoteException {
        return mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid())[0];
    }
}
