package com.eileng.app.library.share;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eileng.app.library.R;
import com.eileng.app.library.util.CLog;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by enlong on 2017/3/23.
 */

public class QQShareHelper implements IShareMethod, IUiListener {
    private Tencent mTencent;
    private int mExtarFlag = 0x00;
    private Activity mActivity;

    public QQShareHelper(Activity mActivity, String APP_ID) {
        this.mActivity = mActivity;
        mTencent = Tencent.createInstance(APP_ID, mActivity.getApplicationContext());
    }

    @Override
    public void share(ShareBean shareBean) {
        if(!isQQInstalled()){
            Toast.makeText(mActivity, R.string.qq_install_notices, Toast.LENGTH_SHORT).show();
            return;
        }


        Bundle bundle = new Bundle();
//这条分享消息被好友点击后的跳转URL。
        if(!TextUtils.isEmpty(shareBean.link)){
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.link);
        }

//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareBean.title);
//分享的图片URL
        if(!TextUtils.isEmpty(shareBean.imgUrl)){
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                    shareBean.imgUrl);
        }

//分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBean.text);
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);

//标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "立学");

        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        mTencent.shareToQQ(mActivity, bundle, this);
    }

    public void shareQzone(ShareBean shareBean) {
        if(null == mTencent){
            Toast.makeText(mActivity, R.string.qq_install_notices, Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
//这条分享消息被好友点击后的跳转URL。
        if(!TextUtils.isEmpty(shareBean.link)){
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.link);
        }

//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareBean.title);
//分享的图片URL
        if(!TextUtils.isEmpty(shareBean.imgUrl)){
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                    shareBean.imgUrl);
        }

//分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBean.text);
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);

//标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "立学");

        mTencent.shareToQzone(mActivity, bundle, this);
    }

    @Override
    public void onComplete(Object o) {
        CLog.i("share", "onComplete:" );

        ShareResultEvent event = new ShareResultEvent();
        event.msg = "分享到QQ成功";
        event.status = 1;
        EventBus.getDefault().post(event);
    }

    @Override
    public void onError(UiError uiError) {
        CLog.i("share", "onError:" + uiError.errorDetail);

        ShareResultEvent event = new ShareResultEvent();
        event.msg = uiError.errorMessage;
        event.status = 0;
        EventBus.getDefault().post(event);
    }

    @Override
    public void onCancel() {
        CLog.i("share", "onCancel");
    }


    private boolean isQQInstalled() {
        // TODO Auto-generated method stub
        PackageInfo packageInfo;
        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(
                    "com.tencent.mobileqq", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
