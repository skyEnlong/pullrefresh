package com.eileng.app.library.img.upload;

/**
 * Created by enlong on 2017/3/8.
 */

public interface IUploadCallBack {
    public void onProgress(String file, int progress);
    public void onSuccess(String file, String result);
    public void onErr(String file);
}
