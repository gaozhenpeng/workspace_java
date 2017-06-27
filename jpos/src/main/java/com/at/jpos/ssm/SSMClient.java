package com.at.jpos.ssm;

import java.io.IOException;

import org.jdom.JDOMException;
import org.jpos.core.SimpleConfiguration;
import org.jpos.iso.FSDISOMsg;
import org.jpos.iso.ISOException;
import org.jpos.iso.packager.FSDPackager;
import org.jpos.util.FSDMsg;
import org.jpos.util.Logger;
import org.jpos.util.RotateLogListener;
import org.slf4j.LoggerFactory;

import com.at.jpos.helper.LogbackListener;

public class SSMClient {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SSMClient.class);

	private static final String REQ_BASE_PATH = SSMConstants.CLASS_RESOURCE_URL.getProtocol() + "://"
			+ SSMConstants.CLASS_RESOURCE_URL.getPath() + "../../cfg/schema/ssm-req-";
	private static final String REQ_BASE_SCHEMA = "base";
	private static final String RESP_BASE_PATH = SSMConstants.CLASS_RESOURCE_URL.getProtocol() + "://"
			+ SSMConstants.CLASS_RESOURCE_URL.getPath() + "../../cfg/schema/ssm-res-";
	private static final String RESP_BASE_SCHEMA = "base";

	public static void main(String[] args) throws IOException, ISOException, JDOMException {

		String host = args[0];
		String port = args[1];

		SimpleConfiguration rotconf = new SimpleConfiguration();
		rotconf.put("file", "log/ssm-client.log");
		rotconf.put("window", "86400");
		rotconf.put("copies", "20");
		rotconf.put("maxsize", "104857600");
		RotateLogListener rotLogLtn = new RotateLogListener();
		rotLogLtn.setConfiguration(rotconf);

		Logger jposLogger = new Logger();
		jposLogger.addListener(new LogbackListener());
		jposLogger.addListener(rotLogLtn);

		SSMChannel channel = new SSMChannel();
		channel.setTimeout(10000);
		channel.setPackager(new FSDPackager());
		channel.setBasePath(RESP_BASE_PATH);
		channel.setSchema(RESP_BASE_SCHEMA);
		channel.setLogger(jposLogger, "channel-hsm");
		channel.setHost(host, Integer.valueOf(port, 10));
		channel.connect();

		FSDMsg reqMsg = new FSDMsg(REQ_BASE_PATH, REQ_BASE_SCHEMA);
		reqMsg.setSeparator("SS", ';');
		reqMsg.set("command", "EO");
		reqMsg.set("public-key-encoding", "02"); // 01: unsigned; 02: 2's
													// complement
		reqMsg.set("public-key-seq-of-modulus-exponent",
				"3082010A0282010100846B00D8C397E1C0AFBCE2A7DB0E931E8697CADBF51EDDE4B59A777F1AA30CB904F7F798687824963975F468CB992425918941173695EB44EAE61105C0106761BE5BD1FEF809B7CD4C94A60813A775364CDF27E7131A4AF927711B5E66EA3A790618F38D092B83EA94FCA93CDA94DC062D7888867BB7D7B7AAC2D1A4635FEEF4FB57AC09616CB16A5015FDE63017E0E486577B6B93A884B481E929E0861DF86F4493E56A03E42FC331F15028483EEF94999F127168F622021CDAACC42F812D9E4C4FCF9F6EF8B8C1860AC5FB3085D3F955ED26D582680F7EC0E31B3F1892DA0E0392E97F7B5E1EA3EF5A3DB2A724E73973B2F19266A000F04C5FD138EC8DC9D10203010001");
		reqMsg.pack();

		channel.send(new FSDISOMsg(reqMsg));

		FSDISOMsg respISOMsg = null;
		try {
			respISOMsg = (FSDISOMsg) channel.receive();
		} finally {
			if (channel != null) {
				logger.info("channel.disconnect()");
				channel.disconnect();
			}
		}

		if (respISOMsg == null) {
			throw new RuntimeException("respISOMsg is expected.");
		}
		FSDMsg respMsg = respISOMsg.getFSDMsg();
		if (respMsg == null) {
			throw new RuntimeException("respMsg is expected.");
		}

		String respCode = respMsg.get("response-code");
		String errorCode = respMsg.get("error-code");
		String public_key_mac = respMsg.get("public-key-mac");
		String public_key_seq_of_modulus_exponent = respMsg.get("public-key-seq-of-modulus-exponent");

		if (!"EP".equals(respCode) || !"00".equals(errorCode) || "".equals(public_key_mac)) {
			throw new RuntimeException("Gening mac on pk failed.");
		}

		if (public_key_mac == null || "".equals(public_key_mac.trim()) || public_key_seq_of_modulus_exponent == null
				|| !public_key_seq_of_modulus_exponent.equals(reqMsg.get("public-key-seq-of-modulus-exponent"))) {
			throw new RuntimeException("The form of response is not as expected.");
		}

		logger.info("SSMClient Completed.");
	}
}
