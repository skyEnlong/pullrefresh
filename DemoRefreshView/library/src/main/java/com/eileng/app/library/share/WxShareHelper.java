package com.eileng.app.library.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.eileng.app.library.R;
import com.eileng.app.library.util.BitmapUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by enlong on 2017/3/23.
 */

public class WxShareHelper implements IShareMethod {

    private Context mContext;
    private IWXAPI api;
    private int shareScene = SendMessageToWX.Req.WXSceneSession;

    public WxShareHelper(Context context, String appId) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);

    }

    public void setShareScene(int scene) {
        this.shareScene = scene;
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void share(ShareBean shareBean) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, R.string.wechatpay_weixin_install_notices1, Toast.LENGTH_LONG).show();
            return;
        }


        if (!api.isWXAppSupportAPI()) {
            Toast.makeText(mContext, R.string.wechatpay_weixin_install_notices2, Toast.LENGTH_SHORT).show();
            return;
        }


        WXMediaMessage msg = new WXMediaMessage();
        SendMessageToWX.Req req = new SendMessageToWX.Req();

        switch (shareBean.shareType) {
            case SHARE_TYPE_TEXT: {
                WXTextObject textObj = new WXTextObject();
                textObj.text = shareBean.text;
                msg.mediaObject = textObj;
                msg.description = shareBean.text;
                req.message = msg;
                req.transaction = buildTransaction("text");
            }

            break;
            case SHARE_TYPE_PIC: {
                if(null != shareBean.imgPath){
                    Bitmap bm = BitmapUtil.getSmallBitmap(shareBean.imgPath);
                    WXImageObject imgObj = new WXImageObject(bm);
                    msg.mediaObject = imgObj;
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, 120, 120, true);
                    bm.recycle();
                    msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp);
                }

                req.transaction = buildTransaction("img");

            }

            break;

            case SHARE_TYPE_URL: {

                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = shareBean.link;
                msg.mediaObject = webpage;
                if(!TextUtils.isEmpty(shareBean.imgPath)){
                    Bitmap bm = BitmapUtil.getSmallBitmap(shareBean.imgPath);

                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, 120, 120, true);
                    bm.recycle();
                    msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp);
                }

                req.transaction = buildTransaction("webpage");
            }

            break;
        }

//        msg.title = shareBean.title;

        req.message = msg;
        req.scene = shareScene;

        api.sendReq(req);
    }
}
