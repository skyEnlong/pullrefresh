package com.eileng.app.library.util;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * check the string contains  illegle  char or not
     *
     * @param args
     * @return
     */
    public static boolean containIllegle(String args) {
        String str = "/";
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(args);
        return m.find();
    }

    public static boolean checkNick(String args) {
        String regEx = "[-a-zA-Z0-9_\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(args);
        return m.matches();
    }

    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isAllSpace(String args) {
        if (TextUtils.isEmpty(args)) {
            return true;
        }
        String temp = args.replaceAll("^[\\s　]*|[\\s　]*$", "");
        if (temp.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把userid转化成topic
     *
     * @param userId
     * @return
     */
    public static String userId2Topic(String userId) {
        StringBuffer s = new StringBuffer(userId.replace("-", ""));
        for (int index = 0; index < s.length(); index++) {
            if (index % 3 == 0) {
                s.insert(index, "/");
            }
        }
        return s.toString();
    }



    /**
     * 手机号验证 8, 10 , 11
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (null == str) {
            return false;
        }
//    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(str);

        Pattern p1 = Pattern.compile("^\\d{8}$");
        Matcher m1 = p1.matcher(str);

        Pattern p2 = Pattern.compile("^\\d{10}$");
        Matcher m2 = p2.matcher(str);
        return m.matches() || m1.matches() || m2.matches();
    }

    public static boolean isMainMobile(String str) {
        if (null == str) {
            return false;
        }
//    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    public static boolean isMobile(String code, String phone) {
        if (StringUtil.isEmpty(code) || StringUtil.isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d{11}$");
        if (code.equals("852") || code.equals("853")) {
            p = Pattern.compile("^\\d{8}$");
        } else if (code.equals("886")) {
            p = Pattern.compile("^\\d{10}$");
        }
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static boolean isHKMACAOMobile(String str) {
        if (null == str) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d{8}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isPhone(String phoneNum) {
        if (StringUtil.isEmpty(phoneNum)) {
            return false;
        }
        if (StringUtil.isNumeric(phoneNum) && phoneNum.length() >= 7 && phoneNum.length() <= 11) {
            return true;
        }

        String tregEx = "[0]{1}[0-9]{2,3}-[0-9]{7,8}";
        boolean tp = Pattern.compile(tregEx).matcher(phoneNum).find();
        return tp;
    }

    public static boolean isEmail(String email) {
        boolean tag = true;
        final String pattern1 = "^[-_A-Za-z0-9\\.]+@([_A-Za-z0-9]+\\.)+[A-Za-z0-9]{2,32}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    public static boolean isIDNumber(String id) {
        boolean tag = true;
        final String pattern1 = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(id);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }


    /**
     * check the string is "" or null
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isListEmpty(List list) {
        if (list != null && list.size() != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String formatPhone(String phone) {
        if (isEmpty(phone)) {
            return "";
        } else {
            if (phone.length() == 11) {
                String s1 = phone.substring(0, 3);
                String s2 = phone.substring(3, 7);
                String s3 = phone.substring(7, 11);
                return s1 + " " + s2 + " " + s3;
            } else {
                return phone;
            }
        }
    }

    /**
     * 数组分割
     *
     * @param join
     * @param strAry
     * @return
     */
    public static String join(String[] strAry, String join) {
        if (strAry == null || strAry.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }

    /**
     * List分割
     *
     * @param join
     * @param strAry
     * @return
     */
    public static String join(List<String> strAry, String join) {
        if (strAry == null || strAry.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.size(); i++) {
            if (i == (strAry.size() - 1)) {
                sb.append(strAry.get(i));
            } else {
                sb.append(strAry.get(i)).append(join);
            }
        }
        return new String(sb);
    }

    public static String get2fFormat(Number params) {
        DecimalFormat distanceDecimalFormat = new DecimalFormat("0.00");
        distanceDecimalFormat.setRoundingMode(RoundingMode.DOWN);
        return distanceDecimalFormat.format(params);
    }

    public static String get1fFormat(Number params) {
        DecimalFormat distanceDecimalFormat = new DecimalFormat("0.0");
        distanceDecimalFormat.setRoundingMode(RoundingMode.DOWN);
        String str = distanceDecimalFormat.format(Double.valueOf(String.valueOf(params)));
        return str;
    }


    public static String convertByteToHexString(byte[] data) {
        if (null == data || data.length == 0) {
            return null;
        }

        String str = "";
        for (int i = 0; i < data.length; i++) {
            String hexString = Integer.toHexString(data[i] & 0xff);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            str += hexString;
        }

        return str;
    }

    public static byte[] convertHexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    public static String getHexString(byte data) {
        String str = Integer.toHexString(data & 0xff);
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    };

}
