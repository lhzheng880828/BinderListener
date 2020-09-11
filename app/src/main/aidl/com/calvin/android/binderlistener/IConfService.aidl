// IConfService.aidl
package com.calvin.android.binderlistener;

import com.calvin.android.binderlistener.IConfChangedListener;

// Declare any non-default types here with import statements

interface IConfService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void createConf();

    void endConf();

    void listenForSubscriber(String callingPackage, in IConfChangedListener listener, int events, boolean notifyNow);

    String getCurrentPackageName();
}
