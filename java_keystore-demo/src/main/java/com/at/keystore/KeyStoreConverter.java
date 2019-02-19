package com.at.keystore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreConverter {
	private static final Logger logger = LoggerFactory.getLogger(KeyStoreConverter.class);

	public void fromPKCS12toJKS() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			InvalidKeySpecException, UnrecoverableKeyException {
		String pkcs12KeyStore_b64 = "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";
		byte[] pkcs12KeyStoreBytes = Base64.getDecoder().decode(pkcs12KeyStore_b64);
		String pkcs12KeyStorePassword = "";
		String jksKeyStorePassword = "";

		// keystore, pkcs12
		logger.debug("pkcs12 keystore");
		KeyStore pkcs12KeyStore = KeyStore.getInstance("pkcs12");
		// load keystore
		ByteArrayInputStream bais = new ByteArrayInputStream(pkcs12KeyStoreBytes);
		pkcs12KeyStore.load(bais, pkcs12KeyStorePassword.toCharArray());
		bais.close();
		// alias
		String alias = pkcs12KeyStore.aliases().nextElement();
		logger.debug("alias: " + alias);

		// private key
		Key key = pkcs12KeyStore.getKey(alias, pkcs12KeyStorePassword.toCharArray());
		// key factory
		KeyFactory keyfact = KeyFactory.getInstance(key.getAlgorithm());
		// generate private key
		PrivateKey priKey = keyfact.generatePrivate(new PKCS8EncodedKeySpec(key.getEncoded()));

		// the output stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// keystore, jks
		logger.debug("jks keystore");
		KeyStore jksKeyStore = KeyStore.getInstance("jks");
		jksKeyStore.load(null, null); // initial
		jksKeyStore.setKeyEntry(alias, priKey, jksKeyStorePassword.toCharArray(),
				pkcs12KeyStore.getCertificateChain(alias));
		jksKeyStore.store(baos, jksKeyStorePassword.toCharArray());

		// keystore in binary
		logger.debug("Gen jks keystore in bytes.");
		byte[] jksKeyStoreBytes = baos.toByteArray();
		baos.close();

		// encode jks keystore in base64
		logger.debug("encode jks keystore in base64");
		byte[] jksKeyStore_b64_bytes = Base64.getEncoder().encode(jksKeyStoreBytes);
		// output to file
		logger.debug("Save content to file 'jksKeyStore.jks'. ");
		FileOutputStream fos = new FileOutputStream("jksKeyStore.jks");
		fos.write(jksKeyStore_b64_bytes);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		KeyStoreConverter transformer = new KeyStoreConverter();
		transformer.fromPKCS12toJKS();
	}
}
