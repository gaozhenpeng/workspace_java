package com.at.okhttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GetMain {
	private static final Logger logger = LoggerFactory.getLogger(GetMain.class);
	private OkHttpClient client = null;

	public GetMain() {
		client = new OkHttpClient();
		client.setConnectTimeout(60, TimeUnit.SECONDS);
		client.setReadTimeout(60, TimeUnit.SECONDS);
		client.setWriteTimeout(60, TimeUnit.SECONDS);
		client.setRetryOnConnectionFailure(true);
	}

	public String run(String url) throws IOException {
		// proxy
		SocketAddress addr = new InetSocketAddress("127.0.0.1", 9876);
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);

		// request
		Request request = new Request.Builder().url(url).build();
		// call and response
		Response response = client.setProxy(proxy).newCall(request).execute();

		return response.body().string();
	}

	public static void main(String[] args) throws IOException {
		GetMain example = new GetMain();
		String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
		logger.info(response);
	}
}
