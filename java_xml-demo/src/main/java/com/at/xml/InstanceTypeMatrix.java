package com.at.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InstanceTypeMatrix {
	private static final Logger logger = LoggerFactory.getLogger(InstanceTypeMatrix.class);
	private Map<String, Map<String, String>> data = null;
	private Long updated_datetime = null;
	private Long xml_last_modified = null;
	private static final long UPDATE_INTERVAL = 10 * 1000;
	private ReentrantLock lock = new ReentrantLock();

	public Map<String, String> getInstanceType(String instanceType) {
		Map<String, String> result = null;
		try {
			updateData();
			result = data.get(instanceType);
		} catch (Exception e) {
			logger.error("Not able to get data", e);
		}
		return result;
	}

	public Map<String, Map<String, String>> getAllInstanceType() {
		Map<String, Map<String, String>> result = null;
		try {
			updateData();
			result = data;
		} catch (Exception e) {
			logger.error("Not able to get data", e);
		}
		return result;
	}

	/**
	 * <p>
	 * call initData() to do update
	 * </p>
	 * <p>
	 * use 2 levels lock in order to speed up the whole process.
	 * </p>
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void updateData() throws ParserConfigurationException, SAXException, IOException {
		Date curdate_out = new Date();
		if (updated_datetime == null || (curdate_out.getTime() - updated_datetime) > UPDATE_INTERVAL) {
			boolean isLocked = false;
			try {
				isLocked = lock.tryLock();
				if (isLocked) {
					Date curdate_in = new Date();
					try {
						if (updated_datetime == null || (curdate_in.getTime() - updated_datetime) > UPDATE_INTERVAL) {
							String classPathFile = "/instancetype-matrix.xml";
							File cpf = new File(this.getClass().getResource(classPathFile).getFile());
							logger.debug("File path: '" + cpf.getAbsolutePath() + "'");
							if (cpf.canRead()
									&& (xml_last_modified == null || cpf.lastModified() != xml_last_modified)) {
								initData(classPathFile);
								xml_last_modified = cpf.lastModified();
							}
						}
					} finally {
						curdate_in = null;
					}
				}
			} finally {
				if (isLocked) {
					lock.unlock();
				}
				curdate_out = null;
			}
		}
	}

	/**
	 * initial data from the file, filePathInClassPath
	 * 
	 * @param filePathInClassPath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void initData(String filePathInClassPath) throws ParserConfigurationException, SAXException, IOException {
		InputStream xml_is = this.getClass().getResourceAsStream(filePathInClassPath);

		DocumentBuilderFactory docBuiFac = DocumentBuilderFactory.newInstance();
		docBuiFac.setNamespaceAware(false);
		docBuiFac.setValidating(false);
		docBuiFac.setIgnoringComments(true);
		docBuiFac.setExpandEntityReferences(false);
		docBuiFac.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docBuiFac.newDocumentBuilder();

		Document xmlDoc = docBuilder.parse(xml_is);
		// xmlDoc.normalize(); // normalize in the code.
		Element rootEle = xmlDoc.getDocumentElement();

		Map<String, Map<String, String>> newData = new HashMap<String, Map<String, String>>();

		NodeList instanceType_items = ((Element) rootEle).getElementsByTagName("instance_type");
		if (instanceType_items != null && instanceType_items.getLength() > 0) {
			for (int i = 0; i < instanceType_items.getLength(); i++) {
				Node instanceType_item = instanceType_items.item(i);
				if (Node.ELEMENT_NODE == instanceType_item.getNodeType()) {
					String instance_type_val = instanceType_item.getTextContent();
					Map<String, String> newDataItem = new HashMap<String, String>();
					newDataItem.put("instance_type", instance_type_val);

					NodeList data_items = instanceType_item.getParentNode().getChildNodes();
					for (int j = 0; j < data_items.getLength(); j++) {
						Node data_item = data_items.item(j);
						if (Node.ELEMENT_NODE == data_item.getNodeType()) {
							String data_item_name = data_item.getNodeName();
							String data_item_val = data_item.getTextContent();
							newDataItem.put(data_item_name, data_item_val);
						}
					}
					newData.put(instance_type_val, newDataItem);
				}
			}
		}
		data = newData;
		Date curdate = new Date();
		updated_datetime = curdate.getTime();
		curdate = null;
		return;
	}

	/**
	 * <p>
	 * transform the html file, "/instancetype-matrix.xml", of the "Instance
	 * Types Matrix" section from "https://aws.amazon.com/ec2/instance-types/"
	 * to the xml file "/instancetype-matrix.xml"
	 * </p>
	 * <p>
	 * <strong>instance type has to be the first column</strong>
	 * </p>
	 * <br />
	 * <p>
	 * Example of html
	 * </p>
	 * 
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" ?&gt;
	 * &lt;table width="539" height="1630" cellspacing="0" cellpadding="1"
	 *  border="0" jcr:primarytype="nt:unstructured"&gt;
	 *  &lt;tbody&gt;
	 *      &lt;tr&gt;
	 *          &lt;td width="65" style="text-align: center;"&gt;Instance Type&lt;/td&gt;
	 *          &lt;td width="29" style="text-align: center;"&gt;vCPU&lt;/td&gt;
	 *          &lt;td width="42" style="text-align: center;"&gt;Memory (GiB)&lt;/td&gt;
	 *          &lt;td width="60" style="text-align: center;"&gt;&amp;nbsp;Storage (GB)&lt;/td&gt;
	 *          &lt;td width="200" style="text-align: center;"&gt;Networking
	 *              Performance&lt;br /&gt;
	 *          &lt;/td&gt;
	 *          &lt;td width="69" style="text-align: center;"&gt;Physical Processor&lt;/td&gt;
	 *          &lt;td width="45" style="text-align: center;"&gt;Clock Speed (GHz)&lt;/td&gt;
	 *          &lt;td width="39" style="text-align: center;"&gt;&lt;a href="#intel"&gt;Intel
	 *                  AVX&lt;sup&gt;?&lt;/sup&gt;
	 *          &lt;/a&gt;&lt;/td&gt;
	 *          &lt;td width="32" style="text-align: center;"&gt;&lt;a href="#intel"
	 *              adhocenable="false"&gt;Intel AVX2&lt;/a&gt;&lt;a href="#intel"&gt;&lt;sup&gt;?&lt;/sup&gt;&lt;/a&gt;&lt;/td&gt;
	 *          &lt;td width="30" style="text-align: center;"&gt;&lt;a href="#intel"
	 *              adhocenable="false"&gt;Intel Turbo&lt;/a&gt;&lt;/td&gt;
	 *          &lt;td width="47" style="text-align: center;"&gt;&lt;a
	 *              adhocenable="false" href="#ebs"&gt;EBS OPT &lt;br /&gt;
	 *          &lt;/a&gt;&lt;/td&gt;
	 *          &lt;td width="57" height="56" style="text-align: center;"&gt;&lt;a
	 *              href="#enhanced_networking"&gt;Enhanced Networking&lt;/a&gt;&lt;a href="#intel"&gt;&lt;sup&gt;?&lt;/sup&gt;&lt;/a&gt;&lt;/td&gt;
	 *      &lt;/tr&gt;
	 *      &lt;tr&gt;
	 *          &lt;td&gt;t2.nano&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;1&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;0.5&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;EBS Only&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;Low&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;Intel Xeon family&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;up to 3.3&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;Yes&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;-&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;Yes&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;-&lt;/td&gt;
	 *          &lt;td style="text-align: center;"&gt;-&lt;/td&gt;
	 *      &lt;/tr&gt;
	 *  &lt;/tbody&gt;
	 * &lt;/table&gt;
	 * </pre>
	 * 
	 * <br />
	 * <p>
	 * Example of xml
	 * </p>
	 * 
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
	 * &lt;instance-type-matrix ref="https://aws.amazon.com/ec2/instance-types/"&gt;
	 *   &lt;one-instance-type&gt;
	 *     &lt;instance_type&gt;t2.nano&lt;/instance_type&gt;
	 *     &lt;vcpu&gt;1&lt;/vcpu&gt;
	 *     &lt;memory&gt;0.5&lt;/memory&gt;
	 *     &lt;storage&gt;EBS Only&lt;/storage&gt;
	 *     &lt;networking_performance&gt;Low&lt;/networking_performance&gt;
	 *     &lt;physical_processor&gt;Intel Xeon family&lt;/physical_processor&gt;
	 *     &lt;clock_speed&gt;up to 3.3&lt;/clock_speed&gt;
	 *     &lt;intel_avx&gt;Yes&lt;/intel_avx&gt;
	 *     &lt;intel_avx2&gt;-&lt;/intel_avx2&gt;
	 *     &lt;intel_turbo&gt;Yes&lt;/intel_turbo&gt;
	 *     &lt;ebs_opt&gt;-&lt;/ebs_opt&gt;
	 *     &lt;enhanced_networking&gt;-&lt;/enhanced_networking&gt;
	 *   &lt;/one-instance-type&gt;
	 * &lt;/instance-type-matrix&gt;
	 * </pre>
	 * 
	 * @param fileAbsPath
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void transHTMLtoXML(String fileAbsPath)
			throws IOException, ParserConfigurationException, SAXException, TransformerException {
		// read html
		File inHtmlFile = new File(fileAbsPath);
		InputStream html = new BufferedInputStream(new FileInputStream(inHtmlFile));
		byte[] buffer = new byte[1 * 1024 * 1024]; // 1M = 1 * 1024 * 1024
		int read_cnt = 0;
		try {
			read_cnt = html.read(buffer);
			if (html.available() > 0) {
				throw new RuntimeException("Content should be smaller than 1M");
			}
		} finally {
			html.close();
			html = null;
		}

		// normalize
		String html_content = new String(buffer, 0, read_cnt, "UTF-8");
		html_content = html_content.replaceAll("\\&[^;]+;", " ").replaceAll("<sup>[^<]+</sup>", "").replaceAll("\\n",
				"");
		logger.debug("html_content: " + html_content);

		// read html to object
		ByteArrayInputStream html_is = new ByteArrayInputStream(html_content.getBytes("UTF-8"));
		DocumentBuilderFactory docBuiFac = DocumentBuilderFactory.newInstance();
		docBuiFac.setNamespaceAware(false);
		docBuiFac.setValidating(false);
		docBuiFac.setIgnoringComments(true);
		docBuiFac.setExpandEntityReferences(false);
		docBuiFac.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docBuiFac.newDocumentBuilder();

		Map<Integer, String> headers = new HashMap<Integer, String>();

		Document htmlDoc = docBuilder.parse(html_is);
		// htmlDoc.normalize(); // normalize in the code.
		Element rootEle = htmlDoc.getDocumentElement();
		NodeList TRs = rootEle.getElementsByTagName("tr");
		if (TRs != null && TRs.getLength() > 0) {

			// destination xml doc
			DocumentBuilder xmlDocBuilder = docBuiFac.newDocumentBuilder();
			Document xmlDoc = xmlDocBuilder.newDocument();
			xmlDoc.setXmlStandalone(true);
			Element xmlRootEle = xmlDoc.createElement("instance-type-matrix");
			xmlRootEle.setAttribute("ref", "https://aws.amazon.com/ec2/instance-types/");
			xmlDoc.appendChild(xmlRootEle);

			for (int i = 0; i < TRs.getLength(); i++) {
				Node TR = TRs.item(i);

				if (Node.ELEMENT_NODE == TR.getNodeType()) {
					logger.debug("");
					if (i == 0) {
						logger.debug("headers: ");
					} else {
						logger.debug("data row: ");
					}

					Element xmlOneInstanceType = xmlDoc.createElement("one-instance-type");

					NodeList TDs = ((Element) TR).getElementsByTagName("td");
					for (int j = 0; j < TDs.getLength(); j++) {
						Node TD = TDs.item(j);
						if (Node.ELEMENT_NODE == TD.getNodeType()) {
							String node_val = TD.getTextContent();
							// normalize
							node_val = (i == 0 ? normalizeHeader(node_val) : normalizeValue(node_val));
							if (i == 0) {
								logger.debug("\t'" + node_val + "'");
								headers.put(j, node_val);
							} else {
								String colName = headers.get(j);

								logger.debug("\t'" + node_val + "'");
								Element xmlOneCol = xmlDoc.createElement(colName);
								xmlOneCol.setTextContent(node_val);

								xmlOneInstanceType.appendChild(xmlOneCol);
							}
						}
					}
					if (xmlOneInstanceType.hasChildNodes()) {
						xmlRootEle.appendChild(xmlOneInstanceType);
					}
				}
			}
			TransformerFactory trFac = TransformerFactory.newInstance();
			Transformer transformer = trFac.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			File outXmlFile = new File(FilenameUtils.removeExtension(fileAbsPath) + ".xml");
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(outXmlFile);
				StreamResult result = new StreamResult(os);
				transformer.transform(new DOMSource(xmlDoc), result);
			} finally {
				if (os != null) {
					os.flush();
					os.close();
				}
			}

		}
	}

	private static String normalizeValue(String node_val) {
		return node_val.replaceAll("[ \\t]+", " ").trim();
	}

	private static String normalizeHeader(String node_val) {
		return node_val.replaceAll("\\([^\\)]+\\)", "").replaceAll("[ \\t]+", " ").trim()
				.replaceAll("[^a-zA-Z0-9 -]", "").replaceAll("[ -]", "_").toLowerCase(Locale.US);
	}

	public static void main(String[] args) throws Exception {
		Long start = new Date().getTime();
		InstanceTypeMatrix.transHTMLtoXML(InstanceTypeMatrix.class.getResource("/instancetype-matrix.html").getPath());
		Long afterTransHTMLtoXML = new Date().getTime();
		logger.info("InstanceTypeMatrix.transHTMLtoXML: " + (afterTransHTMLtoXML - start));

		ObjectMapper om = new ObjectMapper();
		InstanceTypeMatrix instanceTypeMatrix = new InstanceTypeMatrix();
		for (int i = 0; i < 1; i++) {
			Map<String, String> insType = instanceTypeMatrix.getInstanceType("t2.micro");
			logger.info("insType: '" + om.writeValueAsString(insType) + "'");

			Map<String, Map<String, String>> allInsTypes = instanceTypeMatrix.getAllInstanceType();
			logger.info("allInsTypes: '" + om.writeValueAsString(allInsTypes) + "'");

		}
		Long afterAllGet = new Date().getTime();
		logger.info("after all get instance type: " + (afterAllGet - afterTransHTMLtoXML));

	}
}
