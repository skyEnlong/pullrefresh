package com.eileng.app.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by enlong on 2017/2/4.
 */

public class PermissionUtil {

    public static boolean checkPermissions(Context context, String... permissions){
        if(SDK_INT < 23)
            return true;
        for(String per : permissions) {
            int result = PermissionChecker.checkSelfPermission(context, per);
            //Log.d("ZYS", per + " " + v);
            if (PermissionChecker.PERMISSION_GRANTED != result) {
                return false;
            }
        }
        return true;
    }

    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
