package com.eileng.app.library.http;

/**
 * Created by enlong on 2017/1/22.
 */

public interface IHttpResponse {
    void onResponse(String reqUrl, String result);
    void onErr( String msg);
}
