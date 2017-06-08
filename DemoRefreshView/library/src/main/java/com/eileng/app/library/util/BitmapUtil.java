package com.eileng.app.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具
 */
public class BitmapUtil {
    private static int REQ_WIDTH = 720;
    private static int REQ_HEIGHT = 1080;

    public static boolean setOrientation(String srcFileStr, int ori) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcFileStr);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
            return false;
        }

        switch (ori) {
            case 90:
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
                break;
            case 180:
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
                break;
            case 270:
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_270));
                break;
        }
        try {
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void saveImg2File(Bitmap bitmap, String dest) {

        File file = new File(dest);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {

        }
    }

    public static int[] getPicSize(String filePath){
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        return new int[]{options.outWidth, options.outHeight};

    }
    /**
     * 将图片保存到本地， 压缩到100k
     *
     * @param bitmap
     * @param imgSize 原图的大小
     * @param dest
     */
    public static void saveImg2FileBysize(Bitmap bitmap, long imgSize, String dest) {
        FileOutputStream out = null;
        try {
            File f = new File(dest);
            if (f.exists()) {
                f.delete();
            }
            try {
                f.createNewFile();
            } catch (Exception e) {
                // TODO: handle exception
            }
            out = new FileOutputStream(f);
            if (bitmap == null) {
                Log.e("pic_chat", "---bm is null");
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            if (imgSize > 100 * 1024) {
                while (baos.toByteArray().length > imgSize) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    baos.reset();//重置baos即清空baos
                    options -= 10;//每次都减少10
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                }
                if (options < 0 || options > 100) {
                    options = 80;
                }
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
            Log.e("pic_chat", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                out.flush();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取有效压缩的图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize

        options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH, REQ_HEIGHT);

        // Decode bitmap with inSampleSize set

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;

    }


    /**
     * 矫正图片
     *
     * @param srcFileStr
     * @param revert
     * @return
     */
    public static boolean fixPhoto(String srcFileStr, boolean revert) {
        boolean ret = false;
        File srcFile = new File(srcFileStr);
        if (!srcFile.exists()) {
            return ret;
        }

        Bitmap bitmap = null;
        try {

        } catch (OutOfMemoryError e) {
            Log.e("##### OutOfMemoryError", "compress bitmap", e);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            bitmap = fixPhoto(srcFileStr, bitmap);
        } catch (OutOfMemoryError e) {
            Log.e("##### OutOfMemoryError", "rotatePhoto", e);
            return false;
        }
        if (bitmap == null) {
            return ret;
        }


        srcFile.delete();

        File destFile = new File(srcFileStr);
        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            if (revert) {
                bitmap = reverse(bitmap);
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.close();
        } catch (OutOfMemoryError e) {
            Log.e("##### OutOfMemoryError", "compress bitmap", e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
        }
        return ret;
    }


    /**
     * 反转图片
     *
     * @param src
     * @return
     */
    public static Bitmap reverse(Bitmap src) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
    }


    /**
     * 更具源文件矫正得到的bitmap
     *
     * @param srcFileStr 源文件
     * @param bitmap     经过压缩后的 srcBitmap
     * @return
     */
    private static Bitmap fixPhoto(String srcFileStr, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcFileStr);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }

        int digree = 0;
        if (exif != null) {
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }

        if (digree != 0) {
            Matrix m = new Matrix();
            m.postRotate(digree);
            Bitmap rotatedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            bitmap.recycle();
            return rotatedBmp;
        }
        return bitmap;
    }


    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImage(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && (!bitmap.isRecycled())) {
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将drawable转换为bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

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
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static byte[] bmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static Bitmap decodeBitmapFrom64Str(String base64Str){
        Bitmap bitmap = null;
        if (!StringUtil.isEmpty(base64Str)) {
            try {
                byte[] bitmapArray = Base64.decode(base64Str.split(",")[1]);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
