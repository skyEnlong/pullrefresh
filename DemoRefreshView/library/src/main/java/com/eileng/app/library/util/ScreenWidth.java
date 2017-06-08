package com.eileng.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Raymond.Peng
 */
public final class ScreenWidth {
    private ScreenWidth() {
    }

    private static int sWidth = 0;
    private static int sHeight = 0;

    /**
     * @param context context of the caller
     * @return screen width
     */
    public static int getScreenWidth(Context context) {
        if (sWidth != 0) {
            return sWidth;
        }
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        try {
            Class<?> cls = Display.class;
            Class<?>[] parameterTypes = {Point.class};
            Point parameter = new Point();
            Method method = cls.getMethod("getSize", parameterTypes);
            method.invoke(display, parameter);
            sWidth = parameter.x;
        } catch (Exception e) {
            sWidth = display.getWidth();
        }
        return sWidth;
    }

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public static int getQuarterWidth(Context context) {
        return getScreenWidth(context) / 4;
    }

    /**
     * @param context context of the caller
     * @return screen width
     */
    public static int getScreenHeight(Activity context) {
        if (sHeight != 0) {
            return sHeight;
        }
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        try {
            Class<?> cls = Display.class;
            Class<?>[] parameterTypes = {Point.class};
            Point parameter = new Point();
            Method method = cls.getMethod("getSize", parameterTypes);
            method.invoke(display, parameter);
            sHeight = parameter.y;
        } catch (Exception e) {
            sHeight = display.getHeight();
        }
        return sHeight - getStatusHeight(context);
    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    private static String getWidth(String[] imgWidth, int totalNum, int curNum) {
        switch (totalNum) {
            case 1:
                return imgWidth[0];
            case 2:
                return imgWidth[1];
            case 3:
                switch (curNum) {
                    case 0:
                        return imgWidth[1];
                    default:
                        return imgWidth[2];
                }
            case 4:
                return imgWidth[1];
            case 5:
                switch (curNum) {
                    case 0:
                    case 1:
                        return imgWidth[1];
                    default:
                        return imgWidth[2];
                }
            default:
                return imgWidth[2];
        }
    }

}
