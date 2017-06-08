package com.eileng.app.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private static String SDPATH = Environment.getExternalStorageDirectory() + File.separator;

    public static String getSDPATH() {
        return SDPATH;
    }

    public static boolean isFileExist(String filename) {
        File file = new File(SDPATH + filename);
        return file.exists();

    }

    /*
     * 在SD上创建文件;
     */
    public static File createSDFile(String filename) throws IOException {

        File file = new File(SDPATH + filename);
        file.createNewFile();
        return file;

    }

    /*
     * 在SD上创建目录
     */
    public static File createSDDir(String dirname) {
        File dir = new File(SDPATH + dirname);
        if (!dir.exists())
            dir.mkdir();
        return dir;
    }

    /*
     * 将一个InputStream数据写入到SD卡中
     */
    public static File write2SDFromInput(String path, String filename, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = new File(path + filename);
            file.createNewFile();
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1204];
            int length = 0;
            while (true) {
                length = input.read(buffer);
                if (length < 0) {
                    break;
                }
                output.write(buffer, 0, length);
            }
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    public static String getCachePath(Context context) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = context.getExternalCacheDir();
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 拷贝文件夹
     *
     * @param sourcePath
     * @param desPath
     */
    public static boolean copyFiles(String sourcePath, String desPath) {
        File sourceFiles = new File(sourcePath);
        if (!sourceFiles.exists()) {
            return false;
        }
        File[] files = sourceFiles.listFiles();

        if (null == files) {
            return false;
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            desPath = Environment.getExternalStorageDirectory() + File.separator + desPath;
            File desDir = new File(desPath);
            if (desDir == null || !desDir.exists()) {
                desDir.mkdir();
            }

            for (File f : files) {
                copyFile(f, desDir.getPath() + f.getName());
            }
        } else {
            return false;
        }


        return true;
    }

    public static void copyFile(InputStream in, String desPath, String desFile) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            desPath = Environment.getExternalStorageDirectory() + File.separator + desPath;
            File desDir = new File(desPath);
            if (desDir == null || !desDir.exists()) {
                desDir.mkdir();
            }

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(desPath + File.separator + desFile);

                byte[] buffer = new byte[1024];
                int length = -1;
                while (-1 != (length = in.read(buffer))) {
                    out.write(buffer, 0, length);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public static void copyFile(InputStream in, String desFile) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(desFile);

                byte[] buffer = new byte[1024];
                int length = -1;
                while (-1 != (length = in.read(buffer))) {
                    out.write(buffer, 0, length);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 拷贝文件
     */
    public static void copyFile(File sourceFile, String desFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(desFile);

            byte[] buffer = new byte[1024];
            int length = -1;
            while (-1 != (length = in.read(buffer))) {
                out.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }




    /*
     * RW File
     */

    /**
     * Use getFilesDir()
     *
     * @param context
     * @param fileName
     * @param byteArr
     * @throws IOException
     */
    public static void writeFileInInternalStorage(Context context,
                                                  String fileName, byte[] byteArr) throws IOException {
        writeFile(context, context.getFilesDir(), fileName, byteArr);
    }

    /**
     * Use getFilesDir()
     *
     * @param context
     * @param fileName
     * @param byteArr
     * @throws IOException
     */
    public static void writeFileInCacheStorage(Context context,
                                               String fileName, byte[] byteArr) throws IOException {
        writeFile(context, context.getCacheDir(), fileName, byteArr);
    }

    private static void writeFile(Context context, File fileDir,
                                  String fileName, byte[] byteArr) throws IOException {
        File file = new File(fileDir, fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(byteArr);
        outputStream.close();
    }

    public static byte[] readFileFromInternalStorage(Context context,
                                                     String fileName) throws IOException {
        return readFile(context, context.getFilesDir(), fileName);
    }

    public static byte[] readFileFromCacheStorage(Context context,
                                                  String fileName) throws IOException {
        return readFile(context, context.getCacheDir(), fileName);
    }

    public static byte[] readFile(Context context, File dirName,
                                  String fileName) throws IOException {
        File file = new File(dirName, fileName);
        int size = (int) file.length();
        byte byteArr[] = new byte[size];

        FileInputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                inputStream);
        bufferedInputStream.read(byteArr, 0, byteArr.length);
        bufferedInputStream.close();

        return byteArr;
    }

    public static byte[] readFile(
            String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) return null;

        int size = (int) file.length();
        byte byteArr[] = new byte[size];

        FileInputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                inputStream);
        bufferedInputStream.read(byteArr, 0, byteArr.length);
        bufferedInputStream.close();

        return byteArr;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            File externalDirectory = Environment.getExternalStorageDirectory();
            file = new File(externalDirectory + "/" + filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    public static void writeFileInExternalStorage(String fileName, byte byteArr[])
            throws  IOException {
        if (isExternalStorageWritable()) {
            File externalDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalDirectory + "/" + fileName);
            if (!file.exists()) {
                boolean b = file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(byteArr);
            fileOutputStream.close();
        } else {
            throw new IOException(
                    "External storage is not writable!");
        }
    }


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)
                || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        } else {
            return false;
        }
    }

    public static void downLoadFile(Context mContext, String url, String desPath, String desFilename) {
        downLoadFile(mContext, url, desPath, desFilename, null);
    }

    public static void downLoadFile(Context mContext, String url, String desPath, String desFilename, FileDownLoadTask.DownProcessCallBack callBack) {
        FileDownLoadTask req = new FileDownLoadTask(mContext, url, desPath, desFilename);
        req.downLoadAsProcessCallBack(callBack);
    }

    public static void saveBmpToFile(String strFileName, Bitmap bitmap) {
        if (null == bitmap) return;
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(strFileName);
            if (bitmap != null)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {//由于权限等原因，children可能为null
                try {
                    // 目录此时为空，可以删除
                    return dir.delete();
                } catch (Exception ex) {
                    return false;
                }
            }
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String saveAsFile(String desPath, String fileName, InputStream in, FileDownLoadTask.DownProcessCallBack mDownProcessCallBack) {
        File desDir = new File(desPath);
        if ( !desDir.exists()) {
            desDir.mkdirs();
        }

        File file = new File(desPath + File.separator + fileName);
        FileOutputStream out = null;
        long tmp_total = 0;
        try {
            out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length = -1;
            while (-1 != (length = in.read(buffer))) {
                out.write(buffer, 0, length);
                tmp_total += length;
                if(null != mDownProcessCallBack){
                    mDownProcessCallBack.onDownProgress(tmp_total);
                }
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            if(null != mDownProcessCallBack){
                mDownProcessCallBack.onDownFailed();
            }
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(null != mDownProcessCallBack){
                mDownProcessCallBack.onDownFailed();
            }
            return "";
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if(null != mDownProcessCallBack) mDownProcessCallBack.onDownSuccess(file.getAbsolutePath());
        return file.getAbsolutePath();
    }


    public static void saveAsFile( String fileName, byte[] data) {

        File file = new File(fileName);
        try {

            FileOutputStream e = new FileOutputStream( file);
            e.write(data);
            e.flush();
            e.close();
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }
    }
}
