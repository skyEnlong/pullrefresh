package com.eileng.app.library.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by enlong on 2017/4/27.
 */

public class VoiceUtil {

    protected void playSound(){

    }

    protected int getDuration(Context mContext, int raw_id) {

        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + raw_id);

        return getDuration(mContext, uri);
    }

    protected int getDuration(Context mContext, Uri uri) {
        MediaPlayer mediaPlayer = null;
        int duration = 0;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mContext, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer = null;
        return duration;
    }

    /**
     * 追加流到文件尾部
     *
     * @return
     * @throws IOException
     */
    public static String mergeMp3List(List<InputStream> ins,
                                      String desPath,
                                      String destFileName) throws IOException {

        File destFile = new File(destFileName);
        FileOutputStream out = new FileOutputStream(destFile, true); //追加到文件尾

        for (InputStream in : ins) {
            String fenLiData = separateMp3(in, desPath); //分离出数据帧
            File file = new File(fenLiData);
            FileInputStream in_read = new FileInputStream(file);
            byte bs[] = new byte[1024 * 4];
            int len;
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            in_read.close();
            if (file.exists()) file.delete();

        }

        out.close();


        return destFile.getAbsolutePath();
    }


    /**
     * 返回分离出MP3文件中的数据帧的文件路径
     */
    public static String separateMp3(InputStream inStream, String savePath) throws IOException {
        //把raw文件写在本地然后调用RandomAccessFile
        String path = savePath;
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len;

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] bos = outStream.toByteArray();
        fileOutputStream.write(bos);
        outStream.close();
        inStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();


        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        File file1 = new File(path + "01");// 分离ID3V2后的文件,这是个中间文件，最后要被删除
        FileOutputStream fos = new FileOutputStream(file1);
        byte ID3[] = new byte[3];
        rf.read(ID3);
        String ID3str = new String(ID3);
        // 分离ID3v2
        if (ID3str.equals("ID3")) {
            rf.seek(6);
            byte[] ID3size = new byte[4];
            rf.read(ID3size);
            int size1 = (ID3size[0] & 0x7f) << 21;
            int size2 = (ID3size[1] & 0x7f) << 14;
            int size3 = (ID3size[2] & 0x7f) << 7;
            int size4 = (ID3size[3] & 0x7f);
            int size = size1 + size2 + size3 + size4 + 10;
            rf.seek(size);
            int lens;
            byte[] bs = new byte[1024 * 4];
            while ((lens = rf.read(bs)) != -1) {
                fos.write(bs, 0, lens);
            }
            fos.close();
            rf.close();
        } else {// 否则完全复制文件
            int lens;
            rf.seek(0);
            byte[] bs = new byte[1024 * 4];
            while ((lens = rf.read(bs)) != -1) {
                fos.write(bs, 0, lens);
            }
            fos.close();
            rf.close();
        }


        RandomAccessFile raf = new RandomAccessFile(file1, "rw");
        File file2 = new File(path + "001");// 分离id3v1后的文件
        byte TAG[] = new byte[3];
        raf.seek(raf.length() - 128);
        raf.read(TAG);
        String tagstr = new String(TAG);
        if (tagstr.equals("TAG")) {
            FileOutputStream fs = new FileOutputStream(file2);
            raf.seek(0);
            byte[] bs = new byte[(int) (raf.length() - 128)];
            raf.read(bs);
            fs.write(bs);
            raf.close();
            fs.close();
        } else {// 否则完全复制内容至file2
            FileOutputStream fs = new FileOutputStream(file2);
            raf.seek(0);
            byte[] bs = new byte[1024 * 4];
            len = 0;
            while ((len = raf.read(bs)) != -1) {
                fs.write(bs, 0, len);
            }
            raf.close();
            fs.close();
        }


        if (file.exists()) {// 删除中间文件
            file.delete();
        }

        if (file1.exists()) {// 删除中间文件
            file1.delete();
        }
        return file2.getAbsolutePath(); //file2在处理完后删除
    }

}
