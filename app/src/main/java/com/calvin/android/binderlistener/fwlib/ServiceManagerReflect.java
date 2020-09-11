package com.calvin.android.binderlistener.fwlib;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:19-11-28
 */
public class ServiceManagerReflect {

    private static final String TAG = "ServiceManagerReflect";

    private static Method addService;
    private static Method getService;

    private static Class<?> sServiceManager;

    static {

        try {

            sServiceManager = Class.forName("android.os.ServiceManager");
            addService = sServiceManager.getMethod("addService",
                    new Class[]{String.class, IBinder.class});
            getService = sServiceManager.getMethod("getService",
                    new Class[]{String.class});
        } catch (Exception e) {
            Log.e(TAG,"error find serviceManager");
        }
    }

    public static void addService(String name, IBinder binder){
        try {
            addService.invoke(null, name, binder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static IBinder getService(String name){
        IBinder binder = null;
        if(sServiceManager == null || getService == null){
                throw new NoClassDefFoundError("ServiceManager not found.");
        }
        try {
            binder = (IBinder)getService.invoke(null, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return binder;
    }
}
