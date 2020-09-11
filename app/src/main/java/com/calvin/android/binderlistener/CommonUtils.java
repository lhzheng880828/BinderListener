package com.calvin.android.binderlistener;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;

import java.util.List;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-2-18
 */
public class CommonUtils {

    public static int stringToInt(String string){
        int formatInt = 0;
        try {
            formatInt = Integer.valueOf(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formatInt;
    }

    public static ComponentName resolveComponent(Context mContext, String action){
        List<ResolveInfo> available =
                mContext.getPackageManager().queryIntentServices(
                        new Intent(action), 0);
        int numAvailable = available.size();
        if(numAvailable>0){
            ServiceInfo cur = available.get(0).serviceInfo;
            ComponentName comp = new ComponentName(cur.packageName, cur.name);
            return comp;
        }
        return null;
    }

    public static ComponentName getTopActComponentName(Context context){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager == null) return null;
        return activityManager.getRunningTasks(2).get(0).topActivity;
    }
}
