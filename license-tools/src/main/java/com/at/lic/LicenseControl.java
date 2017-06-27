package com.at.lic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class LicenseControl {
	private static final Logger log = Logger.getLogger(LicenseControl.class);
	private String licenseKeyFilepath = "cfg/grgbanking.lic";

	private boolean validateLicense() {
		boolean isValid = false;
		File f = new File(licenseKeyFilepath);
		if (f.exists() && f.isFile() && f.canRead()) {
			Serializer serializer = new Persister();
			License lic = null;
			try {
				lic = serializer.read(License.class, f);
			} catch (Exception e) {
				log.error("License file reading failed.", e);
				isValid = false;
				return isValid;
			}

			String lic_checkCode = lic.getCheckCode();
			String lic_code = lic.getLicenseCode();
			String sign_code = lic.getSignCode();
			String sign_date = lic.getSignDate();

			String lcc = getLicenseCheckCode(); // get license check code
			if (!lcc.equals(lic_checkCode)) {
				log.error("License file is not for this machine.");
				isValid = false;
				return isValid;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(lcc);
			sb.append(lic_code);
			sb.append(sign_date);
			String text = sb.toString();

			byte[] signature = Base64.decodeBase64(sign_code);

			try {
				isValid = RSAKeyManager.validSign(text, signature);
			} catch (Exception e) {
				log.error("Validate sign failed.", e);
				isValid = false;
				return isValid;
			}

		}
		return isValid;
	}

	private void generateLicense() throws Exception {

		String lcc = getLicenseCheckCode();
		generateLicense(lcc);
	}

	private void generateLicense(String licenseCheckCode) throws Exception {

		String lcc = licenseCheckCode;
		String lic_code = DigestUtils.md5Hex(lcc);
		String date = License.getSimpleDateFormat().format(new Date());

		StringBuilder sb = new StringBuilder();
		sb.append(lcc);
		sb.append(lic_code);
		sb.append(date);

		byte[] signature = RSAKeyManager.sign(sb.toString());
		String sign_code = Base64.encodeBase64String(signature);

		License lic = new License();
		lic.setCheckCode(lcc);
		lic.setLicenseCode(lic_code);
		lic.setSignCode(sign_code);
		lic.setSignDate(date);

		File f = new File("cfg/grgbanking.lic");
		if (!f.exists()) {
			File pf = f.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
		} else {
			f.renameTo(new File(f.getAbsolutePath() + ".bak"));
		}
		Serializer serializer = new Persister();
		serializer.write(lic, f);

		log.info("Generating License Successful.");
	}

	private String getSysProp(String propName) {
		return System.getProperty(propName);
	}

	private String getMAC() throws Exception {
		String mac = "1:2:3:4:5:6:7:8"; // default mac address

		InetAddress addr = InetAddress.getLocalHost();
		NetworkInterface ni = NetworkInterface.getByInetAddress(addr);

		if (ni.isLoopback() || ni.isVirtual()) {
			ni = null;
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements()) {
				NetworkInterface aNI = (NetworkInterface) nis.nextElement();
				if (!aNI.isLoopback() && !aNI.isVirtual()) {
					ni = aNI;
					break;
				}
			}
		}

		if (ni != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			byte[] HAs = ni.getHardwareAddress();
			for (int i = 0; i < HAs.length; i++) {
				ps.format("%02X:", HAs[i]);
			}
			mac = baos.toString();
			if (mac.length() > 0) {
				mac = mac.replaceFirst(":$", "");
			}

			ps.close();
			baos.close();

		}

		return mac;
	}

	private String getLicenseCheckCode() {
		String os_arch = getSysProp("os.arch"); // x86
		String os_name = getSysProp("os.name"); // Windows XP
		String os_version = getSysProp("os.version"); // 5.1
		String sun_arch_data_model = getSysProp("sun.arch.data.model"); // 32
		String user_language = getSysProp("user.language"); // zh
		String sun_cpu_isalist = getSysProp("sun.cpu.isalist"); // pentium_pro +
																// mmx
																// pentium_pro
																// pentium+mmx
																// pentium i486
																// i386 i86

		String mac = null;
		InetAddress addr = null;

		try {
			mac = getMAC();
			addr = InetAddress.getLocalHost();
		} catch (Exception e) {
			log.error("Getting information from ethernet card failed.", e);
			die();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(os_arch.hashCode());
		sb.append(os_name.hashCode());
		sb.append(os_version.hashCode());
		sb.append(sun_arch_data_model.hashCode());
		sb.append(user_language.hashCode());
		sb.append(sun_cpu_isalist.hashCode());
		sb.append(mac.hashCode());
		sb.append(addr.hashCode());

		int licCheckCode = sb.toString().hashCode();

		return String.valueOf(licCheckCode);
	}

	private void die() {
		System.exit(128);
	}

	public void validLicenseOrDie() {
		if (!validateLicense()) {
			log.error("License Validate FAILED!");
			String cc = getLicenseCheckCode();
			log.error("Please contact with GRGBanking with following License Check Code: '" + cc + "'");
			die();
		}
	}

	public static void main(String[] args) throws Exception {
		LicenseControl lc = new LicenseControl();
		if (args.length > 0) {
			lc.generateLicense(args[0]);
		} else {
			lc.validLicenseOrDie();
			log.info("License Validate Successful.");
		}
	}
}
