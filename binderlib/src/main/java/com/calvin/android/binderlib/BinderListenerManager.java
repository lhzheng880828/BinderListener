package com.calvin.android.binderlib;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-10
 */
public class BinderListenerManager<T extends IInterface> {

    private static final boolean DBG = false;
    private final ArrayList<IBinder> mRemoveList = new ArrayList<IBinder>();
    private final ArrayList<Record> mRecords = new ArrayList<Record>();

    public BinderListenerManager(){
        this("BinderListenerManager");
    }

    private final String logTag;

    public BinderListenerManager(String tag){
        logTag = tag;
    }


    public class Record<T> {
        public String callingPackage;
        public T callback;
        public int events;


        IBinder binder;
        BinderListenerDeathRecipient deathRecipient;

        int callerUid;
        int callerPid;



        boolean matchListenerEvent(int events) {
            return (callback != null) && ((events & this.events) != 0);
        }


        @Override
        public String toString() {
            return "{callingPackage=" + callingPackage + " callerUid=" + callerUid + " binder="
                    + binder + " callback=" + callback + " events=" + Integer.toHexString(events) + "}";
        }
    }

    private class BinderListenerDeathRecipient implements IBinder.DeathRecipient {

        private final IBinder binder;

        BinderListenerDeathRecipient(IBinder binder) {
            this.binder = binder;
        }

        @Override
        public void binderDied() {
            if (DBG) Log.d(logTag, "binderDied " + binder);
            remove(binder);
        }
    }

    public interface IBinderCallback<T> {
        void onCallback(T callback) throws RemoteException;
    }

    public void notifyForAllSubsriber(int event, IBinderCallback binderCallback){
        synchronized (mRecords) {
            for (Record r : mRecords) {
                if (r.matchListenerEvent(event)) {
                    try {
                        binderCallback.onCallback(r.callback);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    private void handleRemoveListLocked() {
        int size = mRemoveList.size();
        Log.d(logTag, "handleRemoveListLocked: mRemoveList.size()=" + size);
        if (size > 0) {
            for (IBinder b: mRemoveList) {
                remove(b);
            }
            mRemoveList.clear();
        }
    }



    public Record add(IBinder binder, int callingUid, int callingPid) {
        Record r;
        synchronized (mRecords) {
            final int N = mRecords.size();
            // While iterating through the records, keep track of how many we have from this pid.
            int numRecordsForPid = 0;
            for (int i = 0; i < N; i++) {
                r = mRecords.get(i);
                if (binder == r.binder) {
                    // Already existed.
                    return r;
                }
                if (r.callerPid == callingPid) {
                    numRecordsForPid++;
                }
            }

            r = new Record();
            r.binder = binder;
            r.deathRecipient = new BinderListenerDeathRecipient(binder);

            try {
                binder.linkToDeath(r.deathRecipient, 0);
            } catch (RemoteException e) {
                Log.d(logTag, "LinkToDeath remote exception sending to r=" + r + " e=" + e);
                // Binder already died. Return null.
                return null;
            }

            mRecords.add(r);
            Log.i(logTag, "add new record");
        }

        return r;
    }

    public void remove(IBinder binder) {
        synchronized (mRecords) {
            final int recordCount = mRecords.size();
            for (int i = 0; i < recordCount; i++) {
                Record r = mRecords.get(i);
                if (r.binder == binder) {
                    if (DBG)
                    Log.d(logTag, "remove: binder=" + binder + " r.callingPackage " + r.callingPackage
                            + " r.callback " + r.callback);

                    if (r.deathRecipient != null) {
                        try {
                            binder.unlinkToDeath(r.deathRecipient, 0);
                        } catch (NoSuchElementException e) {
                            if (DBG)
                            Log.d(logTag,"UnlinkToDeath NoSuchElementException sending to r="
                                    + r + " e=" + e);
                        }
                    }

                    mRecords.remove(i);

                }
            }
        }
    }
}
