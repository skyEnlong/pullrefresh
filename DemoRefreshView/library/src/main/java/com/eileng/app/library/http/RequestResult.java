package com.eileng.app.library.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import okhttp3.Response;

/**
 * Created by enlong on 2017/1/22.
 */

public class RequestResult {
    private Response response;
    private int statusCode;
    private String responseAsString = null;
    private InputStream is;
    public long len;

    public RequestResult(Response response) throws IOException {

        if (null != response) {
            this.statusCode = response.code();

            len = response.body().contentLength();

            this.response = response;
        } else {
            this.statusCode = 404;
        }

    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     * <p>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body stream
     */
    public InputStream asStream() throws IOException {
        is = response.body().byteStream();

        String ty = response.header("Content-Encoding");
        if ("gzip".equalsIgnoreCase(ty)) {
            is = new GZIPInputStream(is);
        }
        return is;
    }

    public byte[] asByte() throws IOException {

        byte[] bytes = response.body().bytes();
        close();
        return bytes;
    }

    public String asString() throws IOException {

        String s = "socket exception";
        try {
            if (null != response) {
                s = response.body().string();
                close();
            }

        }catch (Exception e){
            this.statusCode = 404;
        }


        return s;
    }

    public void close() {

        if (null != response) response.body().close();
    }

}
