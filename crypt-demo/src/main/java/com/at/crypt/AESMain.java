package com.at.crypt;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AESMain {
    private static final String INNER_STRING_CHARSET = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS7Padding";
    
    private static final SecretKey KEY;
    private static final AlgorithmParameters ALGORITHM_PARAMETERS;
    
    static {
        byte[] keyBytes = null;
        byte[] ivBytes = null;
        try {
            keyBytes = "12345678901234567890123456789012".getBytes(INNER_STRING_CHARSET); // 32 bytes
            ivBytes = "1234567890123456".getBytes(INNER_STRING_CHARSET); // 16 bytes
        } catch (UnsupportedEncodingException e) {
            log.error("character encoding not supported: '{}'", INNER_STRING_CHARSET, e);
            Runtime.getRuntime().exit(-1);
        }
        
        KEY = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        
        AlgorithmParameters params = null;
        try {
            params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
            params.init(new IvParameterSpec(ivBytes));
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException e) {
            log.error("initializing algorithm parameters failed", e);
            Runtime.getRuntime().exit(-1);
        }   
        ALGORITHM_PARAMETERS = params;
    }

    private String decrypt(String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] cipherBytes = Base64.decode(cipherText);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, KEY, ALGORITHM_PARAMETERS);
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        return new String(decryptedBytes, INNER_STRING_CHARSET);
    }
    
    /**
     * <code><pre>
     * &lt;script type="text/javascript" src="./crypto-js-3.1.9-1/crypto-js.js"&gt;&lt;/script&gt;
     * &lt;script&gt;
     * let cipherText = CryptoJS.AES.encrypt(
     *      '123456'
     *      ,CryptoJS.enc.Utf8.parse("12345678901234567890123456789012")
     *      ,{
     *          "iv" : CryptoJS.enc.Utf8.parse("1234567890123456"),
     *          "mode" : CryptoJS.mode.CBC,
     *          "padding" : CryptoJS.pad.Pkcs7
     *      });
     *  document.write("&lt;p&gt; encrypted: '" + cipherText+ "' &lt;/p&gt;");
     *  let decipherText = CryptoJS.AES.decrypt(
     *      cipherText
     *      ,CryptoJS.enc.Utf8.parse("12345678901234567890123456789012") // 32 bytes
     *      ,{
     *          "iv" : CryptoJS.enc.Utf8.parse("1234567890123456"), // 16 bytes
     *          "mode" : CryptoJS.mode.CBC,
     *          "padding" : CryptoJS.pad.Pkcs7
     *      });
     *  document.write("&lt;p&gt; decrypted: '" + decipherText.toString(CryptoJS.enc.Utf8)+ "' &lt;/p&gt;");
     *  &lt;/script&gt;
     * </pre></code>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        log.info("Entering main...");
        AESMain aesMain = new AESMain();
        
        String decryptedString = aesMain.decrypt("QWybmnK5wr8/u0akEKFvVQ==");
        log.info("decryptedString: '{}'", decryptedString);
        
        log.info("Leaving main...");
    }
}
