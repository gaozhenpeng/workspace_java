package com.at.okhttp.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SSLContextFactory {
    private static final Logger logger = LoggerFactory.getLogger(SSLContextFactory.class);
    /**
     * keystore type.
     * "jks", by default
     */
    private String defaultKeystoreType = KeyStore.getDefaultType(); // KeyStore.getDefaultType(): "jks";
    
    /**
     * Creates a SSLSocketFactory object with specified keystore stream and password.
     * 
     * @param keyStoreText_in_base64
     *            the keystore content encoded in base64
     * @return An <code>SSLSocketFactory</code> instance.
     * @throws GeneralSecurityException 
     * @throws IOException 
     */
    public SSLSocketFactory createSSLSocketFactory(
            String keyStoreText_in_base64
            , String keystorePassword
            , String trustStoreText_in_base64
            , String trustStorePassword) throws IOException, GeneralSecurityException{
        
        byte[] keyStoreText_bytes = null;
        InputStream keyStoreInputStream = null;
        KeyManager[] keyManagers = null;
        
        byte[] trustKeyStoreText_bytes = null;
        InputStream trustKeyStoreInputStream = null;
        TrustManager[] trustManagers = null;
        
        SSLContext sslContext = null;
        
        try{
            if(keyStoreText_in_base64 != null){
                keyStoreText_bytes = Base64.getDecoder().decode(keyStoreText_in_base64);
                keyStoreInputStream = new ByteArrayInputStream(keyStoreText_bytes);
                keyManagers = initKeyManagers(keyStoreInputStream, keystorePassword, defaultKeystoreType);
            }
            
            if(trustStoreText_in_base64 != null){
                trustKeyStoreText_bytes = Base64.getDecoder().decode(trustStoreText_in_base64);
                trustKeyStoreInputStream = new ByteArrayInputStream(trustKeyStoreText_bytes);
                trustManagers = initTrustManagers(trustKeyStoreInputStream, trustStorePassword, defaultKeystoreType);
            }else{ // if no trust keystore, trust all.
                trustManagers = new TrustManager[] { new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                         logger.debug("getAcceptedIssuers");
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                         logger.debug("checkClientTrusted ");
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                         logger.debug("checkServerTrusted");
                    }
                } };
            }
            
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            
            return sslContext.getSocketFactory();
        }finally{
            sslContext = null;
            
            trustManagers = null;
            if(trustKeyStoreInputStream != null){
                trustKeyStoreInputStream.close();
                trustKeyStoreInputStream = null;
            }
            trustKeyStoreText_bytes = null;
            
            keyManagers = null;
            if(keyStoreInputStream != null){
                keyStoreInputStream.close();
                keyStoreInputStream = null;
            }
            keyStoreText_bytes = null;
        }
    }

    /**
     * Gets the key managers.
     * 
     * @param keyStoreStream
     *            the key store stream
     * @param keyStorePassword
     *            the key stream password
     * @param type
     *            the type
     * @return the key managers
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws GeneralSecurityException
     *             the general security exception
     */
    private KeyManager[] initKeyManagers(InputStream keyStoreInputStream, String keyStorePassword,
            String keyStoreType) throws IOException, GeneralSecurityException {
        if(keyStoreInputStream == null){
            return null;
        }

        KeyStore keyStore = null;
        KeyManagerFactory keyManagerFactory = null;
        try{
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
    
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
    
            return keyManagerFactory.getKeyManagers();
        }finally{
            keyManagerFactory = null;
            keyStore = null;
        }
    }
    
    private TrustManager[] initTrustManagers(InputStream trustStoreInputStream, String trustStorePassword,
            String keyStoreType) throws IOException, GeneralSecurityException {
        if(trustStoreInputStream == null){
            return null;
        }
    
        KeyStore keystore = null;
        TrustManagerFactory trustManagerFactory = null;
        try{
            keystore = KeyStore.getInstance(keyStoreType);
        
            keystore.load(trustStoreInputStream, trustStorePassword.toCharArray());
            
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            return trustManagerFactory.getTrustManagers();
        }finally{
            trustManagerFactory = null;
            keystore = null;
        }
    }

}