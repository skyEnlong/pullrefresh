package com.eileng.app.library.util;

import android.content.Context;

import com.tendcloud.tenddata.TCAgent;

/**
 * Created by enlong on 2017/3/7.
 */

public class StatisticUtil {

    public static void statistic(Context ctx, String action) {
        TCAgent.onEvent( ctx, action);

    }

    public static void statistic(Context ctx, String key, String action) {
        TCAgent.onEvent( ctx, key, action);
    }

    public static void logOut(Context context, String uid) {
        statistic(context, "logout", uid);
    }

    /**
     * 用户登录埋点
     **/
    public static void login(Context context, String uid) {
        statistic(context, "login", uid );
    }
}
