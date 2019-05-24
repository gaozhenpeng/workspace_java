package com.at.crypt;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListSecurityInformation {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());

		log.info("-------列出加密服务提供者-----");
		Provider[] pro = Security.getProviders();
		for (Provider p : pro) {
			log.info("Provider:" + p.getName() + " - version:" + p.getVersion());
			log.info(p.getInfo());
		}
		log.info("");
		log.info("-------列出系统支持的消息摘要算法：");
		for (String s : Security.getAlgorithms("MessageDigest")) {
			log.info(s);
		}
		log.info("-------列出系统支持的生成公钥和私钥对的算法：");
		for (String s : Security.getAlgorithms("KeyPairGenerator")) {
			log.info(s);
		}
	}

}
