package com.eileng.app.library.http;

import android.content.Context;
import android.text.TextUtils;

import com.eileng.app.library.base.BaseApplication;
import com.eileng.app.library.util.CLog;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by enlong on 2017/1/22.
 */

public class HttpManager {
    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }


    /**
     * call back in background
     *
     * @param request
     * @param handler
     */
    public void doTaskInBackGround(final HttpRequest request, final IHttpResponse handler) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {
                ResJsonString result = null;
                try {
                    result = dealWithRequest(request, "", subscriber);

                } catch (Exception e) {
                    if (null != subscriber) subscriber.onError(e);
                    return;
                }

                if (null != result) {
                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResJsonString>() {
                    @Override
                    public void accept(ResJsonString s) throws Exception {
                        if (null != handler) handler.onResponse(s.requestUrl, s.resultString);

                    }
                });

    }


    /**
     * call back in background
     *
     * @param request
     * @param handler
     */
    public void doTaskInMainThread(final HttpRequest request, final IHttpResponse handler) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {
                ResJsonString result = null;
                try {
                    result = dealWithRequest(request, "", subscriber);

                } catch (Exception e) {
                    if (null != subscriber) subscriber.onError(e);
                    return;
                }

                if (null != result) {
                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResJsonString>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(ResJsonString s) {
                        if (null != handler) handler.onResponse(s.requestUrl, s.resultString);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (null != handler) handler.onErr(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public ResJsonString dealWithRequest(HttpRequest request, String defErrMsg,
                                         FlowableEmitter<ResJsonString> subscriber) throws Exception {
        ResJsonString data = null;
        RequestResult result = null;
        String s = "";


        try {
            result = request.doTask();
            s = result.asString();

        } catch (Exception e) {

            if (!subscriber.isCancelled()){
                throw new Exception(e);
            }

        }

        boolean isValid = checkTokenValid(result);
        if (isValid) {
            CLog.i("enlong", s);
            JSONObject jo = new JSONObject(s);

            String code = jo.getString("status");
            if (!"success".equals(code)) {
                int err_code = 0;
                String err = defErrMsg;
                if (jo.has("failCode")) {

                    try {
                        err_code = Integer.parseInt(jo.getString("failCode"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    err = getWaringByCode(request.getContext(), err_code);

                    if (jo.has("failMessage")) {
                        err = jo.getString("failMessage");
                    }
                }


                if (!subscriber.isCancelled()) {
                    throw new Exception(err_code + ":" + err, null);
                }
            }

            data = new ResJsonString();
            data.requestUrl = request.getUrl();
            if (jo.has("result")) {
                data.resultString = jo.getString("result");

                if (!TextUtils.isEmpty(data.resultString)
                        && data.resultString.startsWith("{\"list\"")) {
                    JSONObject js = new JSONObject(data.resultString);
                    if (js.has("list")) {
                        data.resultString = js.getString("list");
                    }

                }
            }

            return data;
        } else {
            //// TODO: 2017/1/22 to refhresh token

            int code = result.getStatusCode();
            if (code != 200) {

                String msg = getWaringByCode(request.getContext(), code);
                if (!subscriber.isCancelled()) {
                    throw new Exception(msg, null);
                }

            }

        }

        return data;
    }


    public String getWaringByCode(Context mContext, int code) {
        String msg = "";
        switch (code) {
            case 1000: {
//                msg = "token缺失";
                ((BaseApplication) mContext).logout();

            }

            break;

            case 1001: {
//                msg = "token失效";
                ((BaseApplication) mContext).logout();

            }

            break;

            case 0001:
                msg = "未定义的API";
                break;
            case 0002:
                msg = "数据传输错误";
                break;

            case 0003:
                msg = "参数缺失";
                break;

            case 0004:
                msg = "身份错误无法获取验证码";
                break;

            case 0005:
                msg = "该用户未注册";
                break;

            case 1003:
                msg = "密码或账户不正确";
                break;

            default:
                msg = "err_code : " + code;
        }

        return msg;
    }

    /**
     * do rquest, call back in ui thread
     *
     * @param request
     */
    public void doTask(final HttpRequest request, final Subscriber<ResJsonString> s) {
        doTask(request, null, s);

    }

    /**
     * do rquest, call back in ui thread
     *
     * @param request
     * @param response call back task in no-ui thread
     */
    public void doTask(final HttpRequest request, final IHttpResponse response, final Subscriber<ResJsonString> s) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(FlowableEmitter<ResJsonString> subscriber) throws Exception {
                ResJsonString result = null;
                try {
                    result = dealWithRequest(request, "", subscriber);

                } catch (Exception e) {
                    if (null != subscriber) subscriber.onError(e);
                    return;
                }

                if (null != result) {
                    if (null != response) response.onResponse(request.getUrl(),
                            result.resultString);

                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
//                .subscribeWith(s);


    }

    /**
     * check token
     *
     * @param result
     * @return
     */
    private synchronized boolean checkTokenValid(RequestResult result) {


        return result.getStatusCode() == 200;
    }


}
