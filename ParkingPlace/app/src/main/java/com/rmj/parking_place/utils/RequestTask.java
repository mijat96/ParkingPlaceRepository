package com.rmj.parking_place.utils;

import android.os.AsyncTask;

import com.rmj.parking_place.exceptions.InvalidNumberOfParamsException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public abstract class RequestTask {
    private static long CONNECTION_TIMEOUT = 120; // sec
    private static long READ_TIMEOUT = 120; // sec
    private static long WRITE_TIMEOUT = 120; // sec

    private static OkHttpClient client;
    static {
        // client = new OkHttpClient();
        client = getUnsafeOkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
    }

    protected HttpRequestAndResponseType httpRequestAndResponseType;

    protected String url;
    protected String jwtToken;


    public RequestTask() {

    }

    protected abstract Request prepareRequest();

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient client = new OkHttpClient();
            client.setSslSocketFactory(sslSocketFactory);
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object execute(String... params) {
        if (params.length < 2) {
            throw new InvalidNumberOfParamsException("params.length < 2");
        }

        url = params[0];
        httpRequestAndResponseType = HttpRequestAndResponseType.valueOf(params[1]);
        if (params.length == 3) {
            jwtToken = params[2];
        }
        else {
            jwtToken = null;
        }

        String result = null;

        Request request = prepareRequest();

        com.squareup.okhttp.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response == null) {
            return null;
        }


        if (response.isSuccessful()) {
            try {
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return null;
        }

        return prepareResponse(result);
    }

    private Object prepareResponse(String result) {
        if (httpRequestAndResponseType == HttpRequestAndResponseType.LOGIN) {
            return JsonLoader.convertJsonToTokenDTO(result);
        }

        return null;
    }


}
