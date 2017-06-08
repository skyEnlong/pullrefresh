package com.eileng.app.library.img.upload;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

import java.io.File;
import java.util.List;

/**
 * Created by enlong on 2017/3/8.
 */

public class ImageUpLoader {
    public static final String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    private static final String accessKeyId = "LTAIsKI7tds4oWyM";
    private static final String accessKeySecret = "TQH27QPtoJhKuFVxCVF4z3OyzkdPsc";
    private static final String bucket = "lixueweb";

    private OSSClient oss;
    private static ImageUpLoader instance;

    public  synchronized  static ImageUpLoader getInstance(Context context){
        if(null == instance){
            instance = new ImageUpLoader(context);
        }
        return instance;
    }


    private ImageUpLoader(Context mContext){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(10); // 最大并发请求书，默认10个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mContext.getApplicationContext(), endpoint, credentialProvider, conf);

    }

    /**
     *
     * @param fileName   上传文件路径
     * @param uid    用户ID
     * @param callBack
     */
    public void uploadFile(String fileName, String uid, IUploadCallBack callBack){
        String keyObject = uid + File.separator + System.currentTimeMillis() + ".jpg";
        new ImageUpLoadCore(oss, bucket, keyObject, fileName, callBack).asyncPutObjectFromLocalFile();
    }


    /**
     *
     * @param fileNames   上传文件路径
     * @param uid    用户ID
     * @param callBack
     */
    public void uploadFile(List<String> fileNames, String uid, IUploadCallBack callBack){
        int index = 0;
        for(String s : fileNames){
            String keyObject = uid + File.separator +
                    System.currentTimeMillis()+ String.valueOf(index++) + ".jpg";
            new ImageUpLoadCore(oss, bucket, keyObject, s, callBack).asyncPutObjectFromLocalFile();

        }
       }
}
