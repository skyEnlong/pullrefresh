package com.eileng.app.library.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.eileng.app.library.http.HttpRequest;
import com.eileng.app.library.http.RequestResult;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenqiang on 16/2/19.
 */

public class FileDownLoadTask {
    private Handler mHandler = null;
    private String mUrl = null;
    private String desPath = null;
    private String desFile = null;
    public final static int patchDownLoadCode = 1000;
    private DownProcessCallBack mDownProcessCallBack;
    private Context mContext;

    public interface DownProcessCallBack {
        public int onDownProgress(long progress);

        public void onDownFailed();

        public void onDownSuccess(String savePath);
    }





    public FileDownLoadTask(Context mContext,
                            String url,
                            String desPath,
                            String desFile) {
        this.mContext = mContext;
        this.mUrl = url;
        this.desFile = desFile;
        this.desPath = desPath;

    }


    public void excute() {
        if (TextUtils.isEmpty(this.mUrl)) return;

        final HttpRequest request = new HttpRequest(mContext, mUrl);
        request.setMethod(HttpRequest.METHOD_GET);

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(final FlowableEmitter<String> subscriber) throws Exception {

                final RequestResult result = request.doTask();
                if (result.getStatusCode() == 200) {
                    String fileName = downLoadFile(result);
                    if(!TextUtils.isEmpty(fileName)){
                        subscriber.onNext(fileName);
                    }else {
                        subscriber.onError(new Throwable("down load failed " + mUrl));

                    }
                } else {
                    subscriber.onError(new Throwable("can not connect " + mUrl));
                }


            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(String fileName) {
                        if (null != mDownProcessCallBack)
                            mDownProcessCallBack.onDownSuccess(fileName);
                        //发送处理 下载成功
                        if (mHandler != null) {
                            Log.d("chenqiang", "mHandler.sendEmptyMessage.....");
                            mHandler.sendEmptyMessage(patchDownLoadCode);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        //发送处理 下载失败
                        sendFailedBack();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private String downLoadFile(final RequestResult result) throws IOException {
        String file = FileUtils.saveAsFile(desPath, desFile, result.asStream(), new FileDownLoadTask.DownProcessCallBack() {
            @Override
            public int onDownProgress(long progress) {

                if (null != mDownProcessCallBack)
                    return mDownProcessCallBack.onDownProgress(progress * 100 / result.len);

                return 0;
            }

            @Override
            public void onDownFailed(){
//                    CLog.i("enlong", "dow load file failed: ");

            }

            @Override
            public void onDownSuccess(String  fileName) {
//                    CLog.i("enlong", "dow load file success: " + mUrl);
                result.close();

            }
        });

        return file;
    }

    /**
     * call back in io-thread
     *
     * @param callBack
     */
    public void downLoadAsProcessCallBack(DownProcessCallBack callBack) {
        this.mDownProcessCallBack = callBack;
        excute();
    }


    /**
     * can not doing this ui-thread
     *
     * @return
     * @throws IOException
     */
    private void sendFailedBack() {
        if (null != mDownProcessCallBack) mDownProcessCallBack.onDownFailed();

        //发送处理 下载失败
        if (mHandler != null) {
            Log.d("chenqiang", "mHandler.sendEmptyMessage.....");
            Message message = new Message();
            message.what = patchDownLoadCode;
            message.arg1 = 1;//表示上传失败
            mHandler.sendMessage(message);
        }
    }
}
