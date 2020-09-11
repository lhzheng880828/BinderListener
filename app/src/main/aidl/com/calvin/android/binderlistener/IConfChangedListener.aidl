// IConfChangedListener.aidl
package com.calvin.android.binderlistener;

import com.calvin.android.binderlistener.ConfInfo;
import com.calvin.android.binderlistener.SubscriberInfo;

// Declare any non-default types here with import statements

oneway interface IConfChangedListener {

    void onConfChanged(int state, in ConfInfo confInfo);

    void onSubscriberChanged(in SubscriberInfo subInfo);
}
