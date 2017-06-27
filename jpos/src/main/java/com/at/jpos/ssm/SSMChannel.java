package com.at.jpos.ssm;

import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.core.SimpleConfiguration;
import org.jpos.iso.FSDISOMsg;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.FSDChannel;
import org.jpos.util.FSDMsg;

public class SSMChannel extends FSDChannel {
	private String basePath = null;
	private String schema = null;

	public SSMChannel() {
		super();
		Configuration cfg = new SimpleConfiguration();
		try {
			super.setConfiguration(cfg);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public void setConfiguration(Configuration cfg) throws ConfigurationException {
		super.setConfiguration(cfg);
		schema = cfg.get("schema", "base");
		basePath = SSMConstants.CLASS_RESOURCE_URL.getProtocol() + "://" + SSMConstants.CLASS_RESOURCE_URL.getPath()
				+ "../cfg/schema/" + cfg.get("schema_prefix");
	}

	@Override
	public ISOMsg createMsg() {
		ISOMsg isoMsg = null;
		if (basePath != null) {
			FSDMsg msg = null;
			if (schema != null) {
				msg = new FSDMsg(basePath, schema);
			} else {
				msg = new FSDMsg(basePath);
			}
			msg.setSeparator("SS", ';');
			isoMsg = new FSDISOMsg(msg);
		} else {
			isoMsg = new FSDISOMsg();
		}
		return isoMsg;
	}
}
