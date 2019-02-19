package com.at.crypt;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ListSecurityInformation {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());

		System.out.println("-------列出加密服务提供者-----");
		Provider[] pro = Security.getProviders();
		for (Provider p : pro) {
			System.out.println("Provider:" + p.getName() + " - version:" + p.getVersion());
			System.out.println(p.getInfo());
		}
		System.out.println("");
		System.out.println("-------列出系统支持的消息摘要算法：");
		for (String s : Security.getAlgorithms("MessageDigest")) {
			System.out.println(s);
		}
		System.out.println("-------列出系统支持的生成公钥和私钥对的算法：");
		for (String s : Security.getAlgorithms("KeyPairGenerator")) {
			System.out.println(s);
		}
	}

}
