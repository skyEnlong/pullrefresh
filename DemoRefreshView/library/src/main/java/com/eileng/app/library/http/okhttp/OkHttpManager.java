package com.eileng.app.library.http.okhttp;


import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by enlong on 2017/1/20.
 */

public class OkHttpManager {

    public static OkHttpManager manager;
    protected final static int TimeOut_Count = 1;
    private OkHttpClient httpClient = null;
    private MediaType type = MediaType.parse("application/x-www-form-urlencode; charset=utf-8");

    public synchronized static OkHttpManager getInstance() {


        if (null == manager) manager = new OkHttpManager();

        return manager;
    }

    public MediaType getMediaType() {
        return type;
    }


    private OkHttpManager() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TimeOut_Count, TimeUnit.MINUTES);
        builder.writeTimeout(TimeOut_Count, TimeUnit.MINUTES);
        builder.readTimeout(TimeOut_Count, TimeUnit.MINUTES);


        builder.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));
        try {
            setSSLSocketFactory(builder);

        } catch (Exception e) {

        }

        httpClient = builder.build();

    }

    public synchronized OkHttpClient getClient() {
        return httpClient;
    }


    public Response excute(Request request) throws IOException {
        Call call = httpClient.newCall(request);
        Response rs = null;
        try {
            rs = call.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public static void setSSLSocketFactory(OkHttpClient.Builder builder) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {


                if (chain == null) {

                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");

                }

                if (!(chain.length > 0)) {

                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");

                }

                try {
                    chain[0].checkValidity();
                } catch (Exception e) {
                    throw new CertificateException("Certificate not valid or trusted.");
                }


                if (!(null != authType && authType.toUpperCase().contains("RSA"))) {

                    throw new CertificateException("checkServerTrusted: AuthType is not RSA:" + authType);

                }


            }
        }};

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, trustAllCerts, null);

        builder.sslSocketFactory(sslcontext.getSocketFactory());

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

    }
}
