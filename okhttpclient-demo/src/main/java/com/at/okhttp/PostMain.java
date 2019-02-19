package com.at.okhttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class PostMain {
	private static final Logger logger = LoggerFactory.getLogger(GetMain.class);
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private OkHttpClient client = null;

	public PostMain() {
		client = new OkHttpClient();
		client.setConnectTimeout(60, TimeUnit.SECONDS);
		client.setReadTimeout(60, TimeUnit.SECONDS);
		client.setWriteTimeout(60, TimeUnit.SECONDS);
		client.setRetryOnConnectionFailure(true);
	}

	public void post(String url, String json) throws IOException {
		// request
		RequestBody body = RequestBody.create(JSON, json);
		Map<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("Accept", "application/json");
		headersMap.put("X-Auth-Token", "7MqVIRJzWZ4qf=Hk");
		Headers headers = Headers.of(headersMap);
		Request request = new Request.Builder().url(url).post(body).headers(headers).build();

		// call and response
		Response response = client.newCall(request).execute();
		// headers
		Headers responseHeaders = response.headers();
		Map<String, List<String>> responseHeadersMap = responseHeaders.toMultimap();
		logger.debug("Response Headers: ");
		for (String k : responseHeadersMap.keySet()) {
			logger.debug("\t" + k + ": " + String.join(",", responseHeadersMap.get(k)));
		}
		logger.debug(";");

		// body
		logger.debug("Response Body: '" + response.body().string() + "'");
	}

	public String bowlingJson(String player1, String player2) {
		String v_js = "{'winCondition':'HIGH_SCORE'," + "'name':'Bowling'," + "'round':4,"
				+ "'lastSaved':1367702411696," + "'dateStarted':1367702378785," + "'players':[" + "{'name':'" + player1
				+ "','history':[10,8,6,7,8],'color':-13388315,'total':39}," + "{'name':'" + player2
				+ "','history':[6,10,5,10,10],'color':-48060,'total':41}" + "]}";
		return v_js.replaceAll("'", "\"");
	}

	public static void main(String[] args) throws IOException {
		PostMain example = new PostMain();
		String json = example.bowlingJson("Jesse", "Jake");
		example.post("http://localhost:8080/spring_mvc/api/raw/a/b/c?X=b&aO=c&x=y", json);
	}
}
