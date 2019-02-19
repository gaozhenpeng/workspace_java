package com.at.lic;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root(name = "license")
public class License {
	@Element(name = "signature", required = true)
	private String signCode = null;
	@Attribute(name = "check-code", required = true)
	private String checkCode = null;
	@Attribute(name = "license-code", required = true)
	private String licenseCode = null;
	@Attribute(name = "sign-date", required = true)
	private String signDate = null;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) throws ParseException {
		sdf.parse(signDate);
		this.signDate = signDate;
	}

	public static SimpleDateFormat getSimpleDateFormat() {
		return sdf;
	}

	public static void main(String[] args) throws Exception {

		String date = sdf.format(new Date());

		License lic = new License();
		lic.setCheckCode("470522490");
		lic.setLicenseCode("license-code");
		lic.setSignCode("signature content");
		lic.setSignDate(date);

		File f = new File("cfg/grgbanking.lic");
		if (!f.exists()) {
			File pf = f.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
		} else {
			f.delete();
		}
		Serializer serializer = new Persister();
		serializer.write(lic, f);

		System.out.println("Generating License Successful.");
	}
}
