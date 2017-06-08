package com.eileng.app.library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * Created by enlong on 2017/1/22.
 */

public class NetUtil {

    /**
     * 判断当前网络连接是否是CMWap
     */
    public static boolean isCmwap(Context context) {

        if (context == null) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {

            return false;
        }

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null) {

            return false;

        }

        String extraInfo = info.getExtraInfo();


        // 工具类，判断是否为空及null

        if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3)) {

            return false;

        }

        if (extraInfo.toLowerCase().indexOf("wap") > 0) {

            return true;

        }

        return false;

    }


    // 获取外网IP
    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        try {

            infoUrl = new URL("http://iframe.ip138.com/ic.asp");
            // infoUrl = new URL("http://www.baidu.com");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();

                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("[");
                int end = strber.indexOf("]", start + 1);
                line = strber.substring(start + 1, end);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getLocalIpAddress();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e(TAG, ex.toString());
        }
        return "";
    }

    public static String getNetAccessName(Context mContext) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return info.getTypeName();
                    } else {
                        return null;
                    }
                }
            }

        } catch (Exception e) {
            Log.v("------------net------", e.toString());
        }
        return null;

    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前DNS
     *
     * @param mContext
     * @return
     */
    public static String getDNS(Context mContext) {
        try {
            if (isWifi(mContext)) {
                WifiManager connectivityManager = (WifiManager) mContext
                        .getSystemService(Context.WIFI_SERVICE);
                DhcpInfo dhcpInfo = connectivityManager.getDhcpInfo();
                if (dhcpInfo != null) {
                    return intToIp(dhcpInfo.dns1);
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." +
                (0xFF & paramInt >> 8) + "." +
                (0xFF & paramInt >> 16) + "." +
                (0xFF & paramInt >> 24);
    }

    /**
     * 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
     */
    public static boolean isNetEnable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            Log.v("------------net------", e.toString());
        }
        return false;
    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }
}
