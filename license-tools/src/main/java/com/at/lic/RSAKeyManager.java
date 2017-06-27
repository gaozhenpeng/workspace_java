package com.at.lic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的 Ronal
 * Rivest，Adi Shamir和Len Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，
 * 是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的!
 *
 * 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。
 * 它的算法如下：
 * 1.找两个非常大的质数p和q（通常p和q都有155十进制位或都有512十进制位）并计算n=pq，k=(p-1)(q-1)。
 * 2.将明文编码成整数M，保证M不小于0但是小于n。
 * 3.任取一个整数e，保证e和k互质，而且e不小于0但是小于k。加密钥匙（称作公钥）是(e, n)。
 * 4.找到一个整数d，使得ed除以k的余数是1（只要e和n满足上面条件，d肯定存在）。解密钥匙（称作密钥）是(d, n)。
 *
 * 加密过程：
 *     加密后的编码C等于M的e次方除以n所得的余数。
 *
 * 解密过程：
 *     解密后的编码N等于C的d次方除以n所得的余数。 只要e、d和n满足上面给定的条件。M等于N。
 * 
 */
public class RSAKeyManager {
    /** 指定加密算法为RSA */
    private static String ALGORITHM = "RSA";
    /** 指定key的大小 */
    private static int KEYSIZE = 4096;
    /** 指定公钥存放文件 */
    private static String PUBLIC_KEY_FILE = "cfg/grgbanking.pub";
    /** 指定私钥存放文件 */
    private static String PRIVATE_KEY_FILE = "cfg/grgbanking.ppk";
    
    private static String serializeObject(Key k) throws IOException{
        // to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(k);
        byte[] o = baos.toByteArray();
        
        // encode by BASE64
        String encText = Base64.encodeBase64String(o);
        
        oos.close();
        baos.close();
        
        return encText;
    }
    
    private static Key deserializeObject(String encText) throws IOException, ClassNotFoundException{
        // decode by BASE64
        byte[] o = Base64.decodeBase64(encText);
        
        // to Object
        ByteArrayInputStream bais = new ByteArrayInputStream(o);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Key k = (Key)ois.readObject();
        
        ois.close();
        bais.close();
        
        return k;
    }
    
    private static void saveKey(Key k, String keyPath) throws IOException{
        String encText = serializeObject(k);
        FileWriter fw = new FileWriter(keyPath);
        fw.write(encText);
        fw.close();
    }
    
    private static void savePubKey(PublicKey k) throws IOException{
        saveKey(k, PUBLIC_KEY_FILE);
    }
    private static void savePriKey(PrivateKey k) throws IOException{
        saveKey(k, PRIVATE_KEY_FILE);
    }
    
    private static Key readKey(String keyFile) throws IOException, ClassNotFoundException{
        /** 将文件中的公钥对象读出 */
        BufferedReader reader = new BufferedReader(new FileReader(keyFile));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while( (line = reader.readLine()) != null ){
            sb.append(line.trim());
        }
        reader.close();
        Key k = deserializeObject(sb.toString());
        return k;
    }
    
    private static PublicKey readPubKey()throws IOException, ClassNotFoundException{
        return (PublicKey)readKey(PUBLIC_KEY_FILE);
    }
    
    private static PrivateKey readPriKey()throws IOException, ClassNotFoundException{
        return (PrivateKey)readKey(PRIVATE_KEY_FILE);
    }
    
    
    /**
     * 生成密钥对
     */
    public static void generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEYSIZE, sr);
        
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        PublicKey publicKey = kp.getPublic();
        /** 得到私钥 */
        PrivateKey privateKey = kp.getPrivate();
        
        /** 用对象流将生成的密钥写入文件 */
        savePubKey(publicKey);
        savePriKey(privateKey);
        
        
    }

    /**
     * 加密方法 source： 源数据
     */
    public static String encrypt(String source) throws Exception {
        PublicKey key = readPubKey();
        
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeBase64String(b1);
    }

    /**
     * 解密算法 cryptograph:密文
     */
    public static String decrypt(String cryptograph) throws Exception {
        /** 将文件中的私钥对象读出 */
        PrivateKey key = readPriKey();
        
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }
    
    public static byte[] sign(String text) throws Exception{
        /** 将文件中的私钥对象读出 */
        PrivateKey key = readPriKey();
        
        Signature sig = Signature.getInstance("SHA1WithRSA"); 
        sig.initSign(key); 
        sig.update(text.getBytes("UTF-8"));
        return sig.sign();
    }
    
    public static boolean validSign(String text, byte[] signature) throws Exception{
        PublicKey key = readPubKey();
        
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(key);
        sig.update(text.getBytes("UTF-8"));
        return sig.verify(signature);
    }
    
    
    public static void main(String[] args) throws Exception {
        if(args.length > 0){
            generateKeyPair(); // generate keys
        }else{
        
            String source = "Hello World!";// 要加密的字符串
            System.out.println("Source: \n" + source + "\n");
            
            String cryptograph = encrypt(source);// 生成的密文
            System.out.println("Encrypted: \n" + cryptograph + "\n");
    
            String target = decrypt(cryptograph);// 解密密文
            System.out.println("Decrypted: \n" + target + "\n");
            
            
            byte[] s = sign(source); // 签名
            System.out.println("Sign: \n" + new String(s, "UTF-8"));
            
            boolean isValid = validSign(source, s); // 验证签名
            System.out.println("Sign isValid: \n" + isValid);
            
        }
    }

}