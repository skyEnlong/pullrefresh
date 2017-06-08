package com.eileng.app.library.util;

import android.text.TextUtils;
import android.util.Log;


/**
 * Created by enlong on 2017/2/4.
 */

public class CLog {


    public static boolean isDebug = true;


    public static synchronized void v(String tag, String msg) {

        if (!isDebug) {
            return;
        }
        if (null != tag ) return;
        String str = msg;
        if (null == msg) str = "null";
        Log.i(tag, str);
    }
    public static synchronized void e( String msg) {

        if (!isDebug) {
            return;
        }

        String str = msg;
        if (null == msg) str = "null";
        Log.e("lixue", str);

    }


    public static synchronized void e(String tag, String msg) {

        if (!isDebug) {
            return;
        }

        String str = msg;
        if (null == msg) str = "null";
        Log.e(tag, str);

     }

    public static synchronized void i(String tag, String msg) {

        if (!isDebug) {
            return;
        }
        if (null == tag ) return;

        String str = msg;
        if (TextUtils.isEmpty(msg)) str = "null";
        Log.i(tag, str);
//        if(tag.equals("kevin"))
//        UserCollection.getInstance().recordAction(tag +":"+ msg);

    }


    public static void d(String tag, String msg) {
        if (!isDebug) {
            return;
        }
        if (null == tag ) return;

        String str = msg;
        if (null == msg) str = "null";
        Log.d(tag, str);
    }
}
