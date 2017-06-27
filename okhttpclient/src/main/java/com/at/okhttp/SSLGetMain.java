package com.at.okhttp;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.at.okhttp.utils.SSLContextFactory;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SSLGetMain {
    private static final Logger logger = LoggerFactory.getLogger(SSLGetMain.class);
    private OkHttpClient client = null;
    
    public SSLGetMain(String keystore_in_base64, String trustKeystore_in_base64) throws IOException, GeneralSecurityException{
        client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout(60, TimeUnit.SECONDS);
        client.setRetryOnConnectionFailure(true);
        client.setHostnameVerifier(new HostnameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
//              if("localhost".equals(hostname) || hostname.startsWith("127.")){ // always approve local host
//                  logger.debug("HostnameVerifier. Approving certificate for '" + hostname + "'");
//                  return true;
//              }else{
//                  logger.debug("OkHostnameVerifier. Verifying certificate for '" + hostname + "'");
//                  return OkHostnameVerifier.INSTANCE.verify(hostname, session);
//              }
            }
            
        });
        SSLContextFactory sslContextFactory = new SSLContextFactory();
        SSLSocketFactory sslSocketFactory = sslContextFactory.createSSLSocketFactory(keystore_in_base64, "<keystorepassword>", trustKeystore_in_base64, "<truststorepassword>");
        client.setSslSocketFactory(sslSocketFactory);
    }

    public Response run(String url, Headers headers) throws IOException {
        // request
        Request request = new Request.Builder().headers(headers).url(url).build();
        // call and response
        Response response = client.newCall(request).execute();

        return response;
    }

    public static void main(String[] args) throws Exception {
        String keystore_in_base64 = null;
        String trustKeystore_in_base64 = null;
        // right, key/store password: "<keystorepassword>"
        keystore_in_base64 = "<keystore in base64>";
        SSLGetMain example = new SSLGetMain(keystore_in_base64, trustKeystore_in_base64);
        Headers headers = new Headers.Builder()
                                .add("X-VC3-CloudType", "aliyun")
                                .add("X-VC3-endpoint-protocol", "http")
                                .add("X-VC3-endpoint-host", "127.0.0.1")
                                .add("X-VC3-endpoint-port", "8888")
                                .add("X-VC3-User-ID", "9o0bVvO7nPzc1RHt")
                                .add("Content-Type", "application/json")
                                .build();
        Response response = example.run("https://101.200.210.6:8443/gateway/a/b/c?X=b&aO=c&x=y", headers); // TODO: url is not correct.
        logger.info(response.toString());
        logger.info(response.headers().toString());
    }
}