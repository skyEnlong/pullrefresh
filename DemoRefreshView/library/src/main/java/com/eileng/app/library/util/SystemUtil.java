package com.eileng.app.library.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by enlong on 2017/3/9.
 */

public class SystemUtil {
    public static boolean isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {

        }
        return false;


    }

    /**
     * 跳转到发短信页面
     *
     * @param context
     * @param tel
     */
    public static void launchSmsView(Context context, String tel) {
        Uri smsToUri = Uri.parse("smsto:" + tel);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", "");

        context.startActivity(intent);
    }


    /**
     * 跳转到打电话页面
     *
     * @param context
     * @param tel
     */
    public static void lanchTelView(Context context, String tel) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }
}
