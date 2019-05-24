package com.at.crypt;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.openssl.EncryptionException;
import org.bouncycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECMain {
    private static final String INNER_STRING_CHARSET = "ISO-8859-1";
    private static final String ENCRYPT_ALGORITHM = "ECIESwithDESede/NONE/PKCS7Padding";
    
    private static String sign_algorithm = null;
    
    /**
     * 签名
     * @param key
     * @param raw
     * @return
     * @throws Exception
     */
    public static byte[] sign(PrivateKey key, byte[] raw) throws Exception{
        java.security.Signature signature = java.security.Signature.getInstance(sign_algorithm, new BouncyCastleProvider());
        signature.initSign(key);
        signature.update(raw);
        return signature.sign();
    }
    /**
     * 验签
     * @param key
     * @param raw
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(PublicKey key, byte[] raw, byte[] sign) throws Exception{
        java.security.Signature signature = java.security.Signature.getInstance(sign_algorithm, new BouncyCastleProvider());
        signature.initVerify(key);
        signature.update(raw);
        return signature.verify(sign);
    }
    
    /**
     * 加密
     * 
     * @param key
     *            加密的密钥
     * @param data
     *            待加密的明文数据
     * @return 加密后的数据
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws ShortBufferException
     * @throws EncryptionException
     */
    public static byte[] encrypt(PublicKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
//      int blockSize = cipher.getBlockSize();  // 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
//                                              // 加密块大小为127 byte,加密后为128个byte; 因此共有2个加密块,
//                                              // 第一个127 byte, 第二个为1个byte
//      
//      ByteArrayOutputStream bout = new ByteArrayOutputStream();
//      for(int i = 0; data.length - i * blockSize > 0 ; i++) {
//          if(data.length - i * blockSize > blockSize){
//              bout.write(cipher.doFinal(data, i*blockSize, blockSize));
//          }else{
//              bout.write(cipher.doFinal(data, i*blockSize, data.length - i * blockSize));
//          }
//      }
//      return bout.toByteArray();
    }

    /**
     * 解密
     * 
     * @param key
     *            解密的密钥
     * @param raw
     *            已经加密的数据
     * @return 解密后的明文
     * @throws EncryptionException
     */
    public static byte[] decrypt(PrivateKey key, byte[] raw) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(raw);
//      int blockSize = cipher.getBlockSize();
//      ByteArrayOutputStream bout = new ByteArrayOutputStream();
//      int j = 0;
//
//      while (raw.length - j * blockSize > 0) {
//          bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
//          j++;
//      }
//      return bout.toByteArray();
    }
    
    public static KeyPairGenerator prepareForSM2() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException{
        ECMain.sign_algorithm = "SHA256withECDSA";
        
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        
        // 使用 SM2 的参考参数, 256 位, 相当于 RSA-3072
        ECCurve curve = new ECCurve.Fp(
                new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16), // q
                new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16), // a
                new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16)); // b
        
        ECParameterSpec ecSpec = new ECParameterSpec(
                curve,
                curve.createPoint(
                        new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16) // Gx
                        , new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16)), // Gy
                new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16)); // n
        
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator;
    }
    
    public static KeyPairGenerator prepareForLongKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        // 使用命名的规格, 521 位，相当于 RSA-15360
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", new BouncyCastleProvider());
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp521r1");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        
        ECMain.sign_algorithm = "SHA512withECDSA";
        
        return keyPairGenerator;
    }

    public static void main(String[] args) throws Exception {
        log.trace("logback is ready.");
        
        long startTime_ms = new Date().getTime();
        long i=0;
        for(i = 0; i < 1; i++){
            log.trace("i = " + i);
            
//          Security.addProvider(new BouncyCastleProvider());
            // 生成公钥和私钥
            KeyPairGenerator keyPairGenerator = ECMain.prepareForLongKey();
            
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
            
            // 打印密钥信息
            ECCurve ecCurve = ecPublicKey.getParameters().getCurve();
            log.trace("椭圆曲线参数a = " + ecCurve.getA().toBigInteger().toString(16));
            log.trace("椭圆曲线参数b = " + ecCurve.getB().toBigInteger().toString(16));
            ECPoint basePoint = ecPublicKey.getParameters().getG();
            log.trace("基点橫坐标              " + basePoint.getAffineXCoord().toBigInteger().toString(16));
            log.trace("基点纵坐标              " + basePoint.getAffineYCoord().toBigInteger().toString(16));
            
            // 公钥：标记位(2B, 0x0004) + 公钥横坐标(32B) + 公钥纵坐标(32B)
            // 注：每个 byte 需要用 2 个 HEX 数表示
            log.trace("公钥横坐标              " + ecPublicKey.getQ().getAffineXCoord().toBigInteger().toString(16));
            log.trace("公钥纵坐标              " + ecPublicKey.getQ().getAffineYCoord().toBigInteger().toString(16));
            log.trace("私钥                         " + ecPrivateKey.getD().toString(16));
            
            
            // ECIES用于加密解密，ECDSA用于签名验签
            
            byte[] orgText_bytes = "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!".getBytes(INNER_STRING_CHARSET);
    
            // 加密
            log.trace("明文: " + new String(orgText_bytes, INNER_STRING_CHARSET));
            byte[] encrypted_bytes = encrypt(ecPublicKey, orgText_bytes);
            log.trace("密文, 长度: '"+encrypted_bytes.length+"'; 内容: '" + Hex.toHexString(encrypted_bytes) + "'");
            // 解密
            byte[] decrypted_text = decrypt(ecPrivateKey, encrypted_bytes);
            // 打印解密后的明文
            log.trace("解密后的明文: " + new String(decrypted_text, INNER_STRING_CHARSET));
            
            
            // 签名
            byte[] signed_bytes = sign(ecPrivateKey, orgText_bytes);
            log.trace("签名, 长度: '"+signed_bytes.length+"'; 内容: '" + Hex.toHexString(signed_bytes)+"'");
            // 验签
            boolean isGoodSign = verify(ecPublicKey, orgText_bytes, signed_bytes);
            log.trace("isGoodSign: " + isGoodSign);
        }
    
        long endTime_ms = new Date().getTime();
        log.debug("Ran "+i+" times: "+(endTime_ms - startTime_ms)+"ms");
    }
}