package com.eileng.app.library.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class MediaUtil {

    public Context mContext;

    public MediaUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    public void openSystemMediaPalyer(String file) {
        try {
            Intent activityIntent = new Intent();
            activityIntent.setAction("android.intent.action.MUSIC_PLAYER");
            activityIntent.addCategory("android.intent.category.LAUNCHER");
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP

                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(activityIntent);
            return;
        } catch (Exception e) {
            // TODO: handle exception
            PackageInfo pi = null;
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.android.music", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.android.music");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            // htc
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.htc.music", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.htc.music");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            // sony
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.sonyericsson.music", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.sonyericsson.music");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            // 三星
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.sec.android.app.muisc", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.sec.android.app.muisc");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            try {
                String pkgname = "com.sec.android.app.music";
                PackageManager pkgmanager = mContext.getPackageManager();
                Intent intent = pkgmanager.getLaunchIntentForPackage(pkgname);
                mContext.startActivity(intent);
                return;
            } catch (Exception ex) {
                // music player not found
            }
            // 小米
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.miui.player", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.miui.player");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

            // 努比亚
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "cn.nubia.music.preset", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("cn.nubia.music.preset");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            //魅族
            try {
                pi = mContext.getPackageManager().getPackageInfo(
                        "com.meizu.media.music", 0);
                if (pi != null) {
                    PackageManager packageManager = mContext
                            .getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager
                            .getLaunchIntentForPackage("com.meizu.media.music");
                    mContext.startActivity(intent);
                    return;
                }
            } catch (NameNotFoundException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

        }


        try {
            Intent it = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(file);
            it.setDataAndType(uri, "audio/*");

            mContext.startActivity(it);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        // return (int) Math.ceil(fm.descent - fm.top) + 2;
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static float getFontRate(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels / 480.0f;
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = revitionImageSize(context, uri, 1024);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static Bitmap revitionImageSize(Context context, Uri uri,
                                           Integer sizeLimit) throws IOException {
        int limit = 1024;
        if (sizeLimit != null) {
            limit = sizeLimit.intValue();
        }
        BufferedInputStream in = new BufferedInputStream(context
                .getContentResolver().openInputStream(uri));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= limit)
                    && (options.outHeight >> i <= limit)) {
                in = new BufferedInputStream(context.getContentResolver()
                        .openInputStream(uri));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap decodeInputStreamAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, options);
            // if(options.outWidth>2160){
            // options.inSampleSize=options.outWidth/1080;
            // }
            // if(options.outHeight>2160){
            // options.inSampleSize=options.outHeight/1080;
            // }
            if (options.outHeight * options.outWidth * 2 >= 1080 * 1080 * 2) {
                if (options.outWidth >= options.outHeight) {
                    options.inSampleSize = (int) Math.pow(2d,
                            options.outWidth / 1080);
                } else {
                    options.inSampleSize = (int) Math.pow(2d,
                            options.outHeight / 1080);
                }
            }
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static Bitmap getDiskBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPreferredConfig = Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(path, options);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return bitmap;
    }

    public static String savePic(String strFileName, Bitmap b) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strFileName;
    }


    public static Bitmap combineBitmap(Bitmap first,
                                       Bitmap second) {
        if (first == null || second == null) {
            return null;
        }
        int width = first.getWidth() > second.getWidth() ? first.getWidth() : second.getWidth();// 686
        int height = first.getHeight() > second.getHeight() ? first.getHeight() : second.getHeight();// 1080
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));// 抗锯齿
        float rate = 1;
        rate = (float) width / (float) second.getWidth();
        try {
            first = Bitmap.createScaledBitmap(first,
                    ((int) (first.getWidth() * rate)),
                    ((int) (first.getHeight() * rate)), false);
        } catch (OutOfMemoryError e) {
            try {
                first = Bitmap.createScaledBitmap(first,
                        ((int) (first.getWidth() * rate * 0.5)),
                        ((int) (first.getHeight() * rate * 0.5)), false);
            } catch (OutOfMemoryError e2) {
                // TODO: handle exception
            }
        }
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, 0, null);
        // Paint paint=new Paint();
        // paint.setColor(Color.BLACK);
        // paint.setAntiAlias(true);
        // paint.setTextSize(16*context.getResources().getDisplayMetrics().scaledDensity);
        // canvas.drawText("2014年7月22日", 20, 50, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (first != null && !first.isRecycled()) {
            first.recycle();
            first = null;
        }

        if (second != null && !second.isRecycled()) {
            second.recycle();
            second = null;
        }
        System.gc();
        return result;
    }

    public static void recycleBitmap(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        }
    }



    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dip2px_fixed(Context context, float dipValue) {
        Log.d("zeng", "scaledDensity:"
                + context.getResources().getDisplayMetrics().scaledDensity);
        if ((context.getResources().getDisplayMetrics().density
                - (int) (context.getResources().getDisplayMetrics().xdpi / 160) > 0.2)
                && context.getResources().getDisplayMetrics().density < 2) {
            final float scale = (context.getResources().getDisplayMetrics().xdpi / 160);
            return (int) ((dipValue + 20) * scale);
        } else {
            final float scale = (context.getResources().getDisplayMetrics().density);
            return (int) (dipValue * scale + 0.5f);
        }
    }

}
