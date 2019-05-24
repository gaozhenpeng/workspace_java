package com.at.crypt;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.EncryptionException;
import org.bouncycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSAMain {
    private static final String INNER_STRING_CHARSET = "ISO-8859-1";

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
    public static byte[] encrypt(Key key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int blockSize = cipher.getBlockSize();  // 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
                                                // 加密块大小为127
                                                // byte,加密后为128个byte;因此共有2个加密块，第一个127
                                                // byte第二个为1个byte
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for(int i = 0; data.length - i * blockSize > 0 ; i++) {
            if(data.length - i * blockSize > blockSize){
                bout.write(cipher.doFinal(data, i*blockSize, blockSize));
            }else{
                bout.write(cipher.doFinal(data, i*blockSize, data.length - i * blockSize));
            }
        }
        byte[] result = bout.toByteArray();
        bout = null;
        return result;
//      int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
//      int leftSize = data.length % blockSize;
//      int blocksSize = leftSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
//      byte[] raw = new byte[outputSize * blocksSize];
//      int i = 0;
//      while (data.length - i * blockSize > 0) {
//          if (data.length - i * blockSize > blockSize){
//              cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
//          }else{
//              cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
//          }
//          // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
//          i++;
//      }
//      return raw;
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
    public static byte[] decrypt(Key key, byte[] raw) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, key);
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int j = 0;

        while (raw.length - j * blockSize > 0) {
            bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
            j++;
        }
        return bout.toByteArray();
    }
    /**
     * 签名
     * @param key
     * @param raw
     * @return
     * @throws Exception
     */
    public static byte[] sign(Key key, byte[] raw) throws Exception{
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA", new BouncyCastleProvider());
        signature.initSign((PrivateKey)key);
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
    public static boolean verify(Key key, byte[] raw, byte[] sign) throws Exception{
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA", new BouncyCastleProvider());
        signature.initVerify((PublicKey)key);
        signature.update(raw);
        return signature.verify(sign);
    }

    /**
     * 生成公钥
     * 
     * @param modulus
     * @param publicExponent
     * @return RSAPublicKey
     * @throws EncryptionException
     */
    public static RSAPublicKey genPK(BigInteger modulus, BigInteger publicExponent) throws Exception {
        KeyFactory keyFac = null;
        keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
    }

    /**
     * 生成私钥
     * 
     * @param modulus
     * @param privateExponent
     * @return RSAPrivateKey
     * @throws EncryptionException
     */
    public static RSAPrivateKey genSK(BigInteger modulus, BigInteger privateExponent) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
    }

    public static void newKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        RSAPublicKey pk = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey sk = (RSAPrivateKey) kp.getPrivate();
        BigInteger pkMod = pk.getModulus();
        BigInteger pkExp = pk.getPublicExponent();
        log.trace("pk.mod: '" + pkMod.toString(16).toUpperCase(Locale.US) + "'");
        log.trace("pk.exp: '" + pkExp.toString(16).toUpperCase(Locale.US) + "'");
        BigInteger skMod = sk.getModulus();
        BigInteger skExp = sk.getPrivateExponent();
        log.trace("sk.mod: '" + skMod.toString(16).toUpperCase(Locale.US) + "'");
        log.trace("sk.exp: '" + skExp.toString(16).toUpperCase(Locale.US) + "'");

    }

    public static void main(String[] args) throws Exception {
        log.trace("logback is ready.");
        String pkMod_hex = "9A998FCE07F479669E2D367751E52E6FABF18D584BA8E2EDEB55F7D89CA3095A1A2D9954962B3950786780BE7D6C86D6FFAA851E7175A0754F16226E9BEC197B1A94507E54E1EC0A726D5642C7A23B561E551DC357EC30C9461F27720EA5E97A9DF2A450F20EB1F3CA861AF50EBB5736677232F6FA6399DF8D505F6B8BD1B555BD1B25AA798DC8C51283B99A3C29AAAFA957B793196BB72ACB6B4229E8C19643646D2FD866292E1E454AA0D6937A014BA7A837DB2FA96EC91326DF48ED5E0BA1CF4579159D766EEA70FA61354B2710479ED5B324582A24C90C92F30932923765C7FB05DAF4334014C4051DC0974E9CD372C15E9B8957E4D8B8ADFDE22A27CF13";
        BigInteger pkMod = new BigInteger(pkMod_hex, 16);

        String pkExp_hex = "10001";
        BigInteger pkExp = new BigInteger(pkExp_hex, 16);

        RSAPublicKey pk = genPK(pkMod, pkExp);
        log.trace("pk.mod equals: " + pkMod.equals(pk.getModulus()));
        log.trace("pk.exp equals: " + pkExp.equals(pk.getPublicExponent()));

        String skMod_hex = "9A998FCE07F479669E2D367751E52E6FABF18D584BA8E2EDEB55F7D89CA3095A1A2D9954962B3950786780BE7D6C86D6FFAA851E7175A0754F16226E9BEC197B1A94507E54E1EC0A726D5642C7A23B561E551DC357EC30C9461F27720EA5E97A9DF2A450F20EB1F3CA861AF50EBB5736677232F6FA6399DF8D505F6B8BD1B555BD1B25AA798DC8C51283B99A3C29AAAFA957B793196BB72ACB6B4229E8C19643646D2FD866292E1E454AA0D6937A014BA7A837DB2FA96EC91326DF48ED5E0BA1CF4579159D766EEA70FA61354B2710479ED5B324582A24C90C92F30932923765C7FB05DAF4334014C4051DC0974E9CD372C15E9B8957E4D8B8ADFDE22A27CF13";
        BigInteger skMod = new BigInteger(skMod_hex, 16);

        String skExp_hex = "15780FC54FCB7E7466B0E6A869420751D0B1E9AC024DC0BDC3799BC283F60C1D6C173AFCC921D582BC45BB5638BAB27AF6E42A3A510D41AC28C06D25A9177F6E5E018CB00E7F9534DA311ED285409D36D75D809599071EBB44B69CD6B4FC0B5150CFDA4BB7619D0ABAEC26A0335C86BEF25CA48131C25522FB4FD7006B5942F21E823F1F469B06E283EB2C97CFDF9D708011989E18EF723142143B85EF101EF4C167D268DEF6D57C36FF5F5B103D78B06F9317DAEC20224E6AC91ACFD3126C061CC8D7722FDAB52C30697559FC1C086068DD10482F75FBC9327B2A4ECA4C5C265AA1D12344B825564FD42A40924CA41F3355A49224B555C478FBDF7CB3017161";
        BigInteger skExp = new BigInteger(skExp_hex, 16);

        RSAPrivateKey sk = genSK(skMod, skExp);
        log.trace("sk.mod equals: " + skMod.equals(sk.getModulus()));
        log.trace("sk.exp equals: " + skExp.equals(sk.getPrivateExponent()));

        log.trace("pk, enc: '" + Hex.toHexString(pk.getEncoded()) + "'");
        log.trace("pk, alg: '" + pk.getAlgorithm() + "'");
        log.trace("pk, for: '" + pk.getFormat() + "'");
        log.trace("pk, mod: len: '" + pk.getModulus().toString(16).length() + "'; val: '"
                + pk.getModulus().toString(16) + "'");
        log.trace("pk, exp: '" + pk.getPublicExponent().toString(16).length() + "'; val: '"
                + pk.getPublicExponent().toString(16) + "'");

        log.trace("sk, enc: '" + Hex.toHexString(sk.getEncoded()) + "'");
        log.trace("sk, alg: '" + sk.getAlgorithm() + "'");
        log.trace("sk, for: '" + sk.getFormat() + "'");
        log.trace("sk, mod: len: '" + sk.getModulus().toString(16).length() + "'; val: '"
                + sk.getModulus().toString(16) + "'");
        log.trace("sk, exp: len: '" + sk.getPrivateExponent().toString(16).length() + "'; val: '"
                + sk.getPrivateExponent().toString(16) + "'");

        ASN1Integer pkMod_di = new ASN1Integer(pkMod);
        ASN1Integer pkExp_di = new ASN1Integer(pkExp);
        ASN1EncodableVector dev = new ASN1EncodableVector();
        dev.add(pkMod_di);
        dev.add(pkExp_di);
        DERSequence derSeq = new DERSequence(dev);
        byte[] pk_der_bytes = derSeq.getEncoded("DER");
        log.trace("pk_der: " + new String(pk_der_bytes, INNER_STRING_CHARSET));
        log.trace("pk_der_hex: " + Hex.toHexString(pk_der_bytes));

        String pkEPP_rkl = "3082010A0282010100" + pkMod_hex + "0203010001";
        log.trace("pkEPP_rkl: '" + pkEPP_rkl + "'");

        long startTime_ms = new Date().getTime();
        long i=0;

        for(i = 0; i < 1; i++){
            log.debug("i = " + i);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            RSAPublicKey bc_pk = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey bc_sk = (RSAPrivateKey) kp.getPrivate();
            BigInteger bc_pkMod = bc_pk.getModulus();
            BigInteger bc_pkExp = bc_pk.getPublicExponent();
            log.trace("pk.mod: '" + bc_pkMod.toString(16).toUpperCase(Locale.US) + "'");
            log.trace("pk.exp: '" + bc_pkExp.toString(16).toUpperCase(Locale.US) + "'");
            BigInteger bc_skMod = bc_sk.getModulus();
            BigInteger bc_skExp = bc_sk.getPrivateExponent();
            log.trace("sk.mod: '" + bc_skMod.toString(16).toUpperCase(Locale.US) + "'");
            log.trace("sk.exp: '" + bc_skExp.toString(16).toUpperCase(Locale.US) + "'");
            
            log.trace("------------------------------------------------------------");
    
            byte[] orgText_bytes = "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!".getBytes(INNER_STRING_CHARSET);
            String orgText = Hex.toHexString(orgText_bytes);
            byte[] encText_bytes = encrypt(bc_pk, orgText_bytes);
            String encText = Hex.toHexString(encText_bytes);
            byte[] clrText_bytes = decrypt(bc_sk, encText_bytes);
            String clrText = Hex.toHexString(clrText_bytes);
            
            byte[] signed_bytes = sign(bc_sk, orgText_bytes);
            String signed_str = Hex.toHexString(signed_bytes);
            boolean isGoodSign = verify(bc_pk, orgText_bytes, signed_bytes);
    
            log.trace("orgText: len: '" + orgText_bytes.length + "'; text: '" + orgText + "'");
            log.trace("encText: len: '" + encText_bytes.length + "'; text: '" + encText + "'");
            log.trace("clrText: len: '" + clrText_bytes.length + "'; text: '" + clrText + "'");
            
            log.trace("signed_str: len: '" + signed_bytes.length + "'; text: '" + signed_str + "'; isGoodSign: '" + isGoodSign + "';");
    
            log.trace("------------------------------------------------------------");
        }
    
        long endTime_ms = new Date().getTime();
        log.debug("Ran "+i+" times: "+(endTime_ms - startTime_ms)+"ms");
    }
}