package com.at.jpos.ssm;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.FSDISOMsg;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.util.FSDMsg;

public class SSMRequestListener implements ISORequestListener, Configurable {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SSMRequestListener.class);
	private String response_schema = null;
	private String base_path_for_response_schema = null;

	@Override
	public void setConfiguration(Configuration cfg) throws ConfigurationException {
		response_schema = cfg.get("response_schema", "base");
		base_path_for_response_schema = SSMConstants.CLASS_RESOURCE_URL.getProtocol() + "://"
				+ SSMConstants.CLASS_RESOURCE_URL.getPath() + "../cfg/schema/" + cfg.get("response_schema_prefix");
	}

	@Override
	public boolean process(ISOSource src, ISOMsg isoMsg) {
		try {
			FSDMsg m = ((FSDISOMsg) isoMsg).getFSDMsg();
			String cmd = m.get("command");
			FSDMsg respMsg = new FSDMsg(base_path_for_response_schema, response_schema);
			respMsg.setSeparator("SS", ';');
			switch (cmd) {
			case "A0":
				respMsg.set("response-code", "A1");
				respMsg.set("error-code", "00");
				respMsg.set("key-scheme-and-key-enc-by-lmk", "UC0B67C3782163B04DB49B89B50DE96A3");
				respMsg.set("check-value", "087C61");
				break;
			case "BU":
				respMsg.set("response-code", "BV");
				respMsg.set("error-code", "00");
				respMsg.set("key-check-value", "087C618FF9C207DF");
				break;
			case "EO":
				respMsg.set("response-code", "EP");
				respMsg.set("error-code", "00");
				respMsg.set("public-key-mac", "951A5A91");
				respMsg.set("public-key-seq-of-modulus-exponent",
						"3082010A0282010100846B00D8C397E1C0AFBCE2A7DB0E931E8697CADBF51EDDE4B59A777F1AA30CB904F7F798687824963975F468CB992425918941173695EB44EAE61105C0106761BE5BD1FEF809B7CD4C94A60813A775364CDF27E7131A4AF927711B5E66EA3A790618F38D092B83EA94FCA93CDA94DC062D7888867BB7D7B7AAC2D1A4635FEEF4FB57AC09616CB16A5015FDE63017E0E486577B6B93A884B481E929E0861DF86F4493E56A03E42FC331F15028483EEF94999F127168F622021CDAACC42F812D9E4C4FCF9F6EF8B8C1860AC5FB3085D3F955ED26D582680F7EC0E31B3F1892DA0E0392E97F7B5E1EA3EF5A3DB2A724E73973B2F19266A000F04C5FD138EC8DC9D10203010001");
				break;
			case "EY":
				respMsg.set("response-code", "EZ");
				respMsg.set("error-code", "00");
				break;
			case "GK":
				respMsg.set("response-code", "GL");
				respMsg.set("error-code", "00");
				respMsg.set("encrypted-key-length", "0256");
				respMsg.set("encrypted-key",
						"7B0EC3CEE9DDDE7C93793D6C5E28E722AA8945238D7ED7E954CFC6D10DD4086B7065EBE9C0B775F1DE17BD6D82EE52267FFCE49E4AE94084E935F2B7D7FAF6D88B94225531A08152330C3E14ECAA5AF55785E50BFF512D48894F80F622B7C4A590DE7C4ACC1307EB8124E273EF814AFB9772E38C00435EA1D626676A40B281641F3772099CF373AB9A8B363BB188AF8BC75896F67E4F415C4CA64D35A12C3C024A7FA01A9E5798C5A844A762FC3459078E260B52EC6A07DE2676FC70E53D7E1E191C1B453CD99314665B41AC72476C3F12BA24FFA6B257AF5537CC846305F6E3CCACED19FD8FCF88759CA30A51FE8BEB246B64FF347C3B8E1C03401EA2FD048D");
				respMsg.set("signature-length", "0256");
				break;
			default:
				respMsg.set("response-code", "UN");
				respMsg.set("error-code", "99");
				break;
			}
			logger.debug("respMsg: '" + respMsg.toString() + "'");
			src.send(new FSDISOMsg(respMsg));
		} catch (Exception e) {
			logger.warn("Unable to send message", e);
		}
		return false;
	}
}
