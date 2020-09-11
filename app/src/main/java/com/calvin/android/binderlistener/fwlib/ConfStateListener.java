package com.calvin.android.binderlistener.fwlib;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.calvin.android.binderlistener.ConfInfo;
import com.calvin.android.binderlistener.IConfChangedListener;
import com.calvin.android.binderlistener.SubscriberInfo;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class ConfStateListener {
    private static final String TAG = "ConfStateListener";

    public static final int LISTEN_NONE     = 0x00000000;

    public static final int LISTEN_CONF_STATE = 0x00000001;

    public static final int LISTEN_SUBSCRIBER_STATE = 0x00000002;

    public final IConfChangedListener callback;

    public ConfStateListener() {
        this(Looper.myLooper());
    }

    public ConfStateListener( Looper looper){
        this(new HandlerExecutor(new Handler(looper)));
    }

    private ConfStateListener( Executor e) {
        if (e == null) {
            throw new IllegalArgumentException("ConfStateListener Executor must be non-null");
        }
        callback = new ConfStateListener.IConfStateListenerStub(this, e);
    }

    private static class IConfStateListenerStub extends IConfChangedListener.Stub {
        private WeakReference<ConfStateListener> mConfStateListenerWeakRef;
        private Executor mExecutor;

        public IConfStateListenerStub(ConfStateListener listener, Executor executor){
            this.mConfStateListenerWeakRef = new WeakReference<ConfStateListener>(listener);
            this.mExecutor = executor;
        }

        @Override
        public void onConfChanged(@ConfCtlManager.ConfState final int state, final ConfInfo confInfo) throws RemoteException {

            final ConfStateListener csl = mConfStateListenerWeakRef.get();
            if (csl == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    csl.onConfChanged(state, confInfo);
                }
            });
        }

        @Override
        public void onSubscriberChanged(final SubscriberInfo subInfo) throws RemoteException {
            final ConfStateListener csl = mConfStateListenerWeakRef.get();
            if (csl == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    csl.onSubscriberChanged(subInfo);
                }
            });
        }
    }

    protected void onConfChanged(@ConfCtlManager.ConfState int state, ConfInfo confInfo){

    }

    protected void onSubscriberChanged(SubscriberInfo subInfo){

    }
}
