package com.eileng.app.library.img.upload;

/**
 * Created by enlong on 2017/3/8.
 */

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.AppendObjectRequest;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.eileng.app.library.util.CLog;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageUpLoadCore {
    // 需要配置以下字段为有效的值
    private final static String PRE = "https://lixueweb.oss-cn-shanghai.aliyuncs.com";
    private String uploadFilePath = "<upload_file_path>";
    private String testBucket = "<bucket_name>";
    private OSS oss;
    private String testObject;
    private IUploadCallBack callBack;

    public ImageUpLoadCore(OSS client,
                           String testBucket,
                           String testObject,
                           String uploadFilePath,
                           IUploadCallBack callBack
    ) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.callBack = callBack;
        this.uploadFilePath = uploadFilePath;
    }


    // 从本地文件上传，采用阻塞的同步接口
    public void putObjectFromLocalFile() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        try {
            PutObjectResult putResult = oss.putObject(put);

            CLog.d("PutObject", "UploadSuccess");

            CLog.d("ETag", putResult.getETag());
            CLog.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            CLog.e("RequestId", e.getRequestId());
            CLog.e("ErrorCode", e.getErrorCode());
            CLog.e("HostId", e.getHostId());
            CLog.e("RawMessage", e.getRawMessage());
        }
    }

    // 从本地文件上传，使用非阻塞的异步接口
    public void asyncPutObjectFromLocalFile() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                CLog.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if(null != callBack){
                    callBack.onProgress(uploadFilePath, (int)(currentSize * 100 / totalSize));
                }
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                CLog.d("PutObject", "UploadSuccess");

                CLog.d("ETag", result.getETag());
                CLog.d("RequestId", result.getRequestId());
                String url = PRE + File.separator + testObject;

                if(null != callBack) callBack.onSuccess(uploadFilePath, url);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    CLog.e("ErrorCode", serviceException.getErrorCode());
                    CLog.e("RequestId", serviceException.getRequestId());
                    CLog.e("HostId", serviceException.getHostId());
                    CLog.e("RawMessage", serviceException.getRawMessage());
                }

                if(null != callBack) callBack.onErr(uploadFilePath);
            }
        });

    }

    // 直接上传二进制数据，使用阻塞的同步接口
    public void putObjectFromByteArray() {
        // 构造测试的上传数据
        byte[] uploadData = new byte[100 * 1024];
        new Random().nextBytes(uploadData);

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadData);

        try {
            PutObjectResult putResult = oss.putObject(put);

            CLog.d("PutObject", "UploadSuccess");

            CLog.d("ETag", putResult.getETag());
            CLog.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            CLog.e("RequestId", e.getRequestId());
            CLog.e("ErrorCode", e.getErrorCode());
            CLog.e("HostId", e.getHostId());
            CLog.e("RawMessage", e.getRawMessage());
        }
    }

    // 上传时设置ContentType等，也可以添加自定义meta信息
    public void putObjectWithMetadataSetting() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        metadata.addUserMetadata("x-oss-meta-name1", "value1");

        put.setMetadata(metadata);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                CLog.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if(null != callBack){
                    callBack.onProgress(uploadFilePath, (int)(currentSize * 100 / totalSize));
                }
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                CLog.d("PutObject", "UploadSuccess");

                CLog.d("ETag", result.getETag());
                CLog.d("RequestId", result.getRequestId());
                String url = PRE + File.separator + testObject;

                if(null != callBack) callBack.onSuccess(uploadFilePath, url);


            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    CLog.e("ErrorCode", serviceException.getErrorCode());
                    CLog.e("RequestId", serviceException.getRequestId());
                    CLog.e("HostId", serviceException.getHostId());
                    CLog.e("RawMessage", serviceException.getRawMessage());
                }

                if(null != callBack) callBack.onErr(uploadFilePath);

            }
        });
    }

    // 上传文件可以设置server回调
    public void asyncPutObjectWithServerCallback() {
        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        put.setMetadata(metadata);


        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                CLog.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                CLog.d("PutObject", "UploadSuccess");

                // 只有设置了servercallback，这个值才有数据
                String serverCallbackReturnJson = result.getServerCallbackReturnBody();

                CLog.d("servercallback", serverCallbackReturnJson);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    CLog.e("ErrorCode", serviceException.getErrorCode());
                    CLog.e("RequestId", serviceException.getRequestId());
                    CLog.e("HostId", serviceException.getHostId());
                    CLog.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    public void asyncPutObjectWithMD5Verify() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        try {
            // 设置Md5以便校验
            metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        put.setMetadata(metadata);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                CLog.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                CLog.d("PutObject", "UploadSuccess");

                CLog.d("ETag", result.getETag());
                CLog.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    CLog.e("ErrorCode", serviceException.getErrorCode());
                    CLog.e("RequestId", serviceException.getRequestId());
                    CLog.e("HostId", serviceException.getHostId());
                    CLog.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    // 追加文件
    public void appendObject() {
        // 如果bucket中objectKey存在，将其删除
        try {
            DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, testObject);
            DeleteObjectResult result = oss.deleteObject(delete);
        } catch (ClientException clientException) {
            clientException.printStackTrace();
        } catch (ServiceException serviceException) {
            CLog.e("ErrorCode", serviceException.getErrorCode());
            CLog.e("RequestId", serviceException.getRequestId());
            CLog.e("HostId", serviceException.getHostId());
            CLog.e("RawMessage", serviceException.getRawMessage());
        }
        AppendObjectRequest append = new AppendObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        append.setMetadata(metadata);

        // 设置追加位置，只能从文件末尾开始追加，如果是新文件，从0开始
        append.setPosition(0);

        append.setProgressCallback(new OSSProgressCallback<AppendObjectRequest>() {
            @Override
            public void onProgress(AppendObjectRequest request, long currentSize, long totalSize) {
                CLog.d("AppendObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncAppendObject(append, new OSSCompletedCallback<AppendObjectRequest, AppendObjectResult>() {
            @Override
            public void onSuccess(AppendObjectRequest request, AppendObjectResult result) {
                CLog.d("AppendObject", "AppendSuccess");
                CLog.d("NextPosition", "" + result.getNextPosition());
            }

            @Override
            public void onFailure(AppendObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    CLog.e("ErrorCode", serviceException.getErrorCode());
                    CLog.e("RequestId", serviceException.getRequestId());
                    CLog.e("HostId", serviceException.getHostId());
                    CLog.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

}
