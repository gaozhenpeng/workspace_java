package com.at.vmware.vcloud;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.Version;

public class VCloudLogin {
	private static final Logger logger = LoggerFactory.getLogger(VCloudLogin.class);

	public static void main(String[] args) throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		VcloudClient vcloudClient = new VcloudClient("<vcloud_url>", Version.V5_5);

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream truststore_is = VCloudLogin.class.getClassLoader().getResourceAsStream("truststore.jks");
		keystore.load(truststore_is, "<truststore_password>".toCharArray());
		truststore_is.close();

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keystore);
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustManagers, null);
		SSLContext.setDefault(sslContext);

		SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		vcloudClient.registerScheme("https", 443, sslSocketFactory);
		vcloudClient.login("<username>", "<password>");
		logger.info("Logined.");
		logger.info("\tToken - " + vcloudClient.getVcloudToken());
		logger.info("\t" + vcloudClient.getVcloudAdmin().getResource().getDescription());

		vcloudClient.logout();
		logger.info("Logouted.");
	}
}
