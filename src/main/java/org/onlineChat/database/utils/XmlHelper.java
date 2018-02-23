package org.onlineChat.database.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.onlineChat.database.PoolManager;
import org.xml.sax.SAXException;

public class XmlHelper {
	static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	static DocumentBuilder builder;

	static Document commonDocument;
	public static final String configurationName;

	static {
		InputStream stream = PoolManager.class.getClassLoader().getResourceAsStream("config/jdbc.properties");
		Properties props = new Properties();
		String local_fileName = null;
		try {
			props.load(stream);
			local_fileName = props.getProperty("fileName");

			try {
				File file = new File(local_fileName);
				if (!file.exists()) {
					URL url = PoolManager.class.getResource(local_fileName);
					if (url == null)
						throw new FileNotFoundException(
								"Default config file cannot find. Default config will be used.");
					file = new File(url.toURI());
				}
				if (!file.exists())
					throw new FileNotFoundException();
				commonDocument = createDocFromFile(file);
			} catch (URISyntaxException | ParserConfigurationException | SAXException e) {
				System.out.println(e.getMessage());
				// e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
			local_fileName = "configure.xml";
		}
		configurationName = local_fileName;
	}

	public static void saveConfigure(File file) {
		try {
			Document document = new Document();

			document.setDocType(new DocType("xml"));
			Element root = new Element("config");
			Element databaseConfig = new Element("database_config");
			Element driverConfig = new Element("driver");
			Element connect = new Element("connect");
			connect.setAttribute("ip", PoolManager.ip);
			connect.setAttribute("port", PoolManager.port);
			connect.setAttribute("database", PoolManager.database);
			connect.setAttribute("user_name", PoolManager.name);
			connect.setAttribute("password", PoolManager.password);

			driverConfig.setText(PoolManager.driver);

			root.addContent(databaseConfig);
			databaseConfig.addContent(connect);
			databaseConfig.addContent(driverConfig);
			document.addContent(root);
			// checkXml(document);
			saveXML(document, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static JdbcConfig getJdbcConfig() {
		if (commonDocument != null) {
			Document document = commonDocument;
			Element config = document.getRootElement();
			Element databaseConfig = config.getChild("database_config");

			Element connect = databaseConfig.getChild("connect");
			Element driverConfig = databaseConfig.getChild("driver");

			String ip = connect.getAttributeValue("ip");
			String port = connect.getAttributeValue("port");
			String database = connect.getAttributeValue("database");
			String name = connect.getAttributeValue("user_name");
			String password = connect.getAttributeValue("password");
			String driver = driverConfig.getText();
			return new JdbcConfig(name, password, ip, port, database, driver);
		}

		return new JdbcConfig("frank", "123456", "127.0.0.1", "1521", "orl", "oracle.jdbc.OracleDriver");
	}

	public static Document createDocFromFile(File file) throws ParserConfigurationException, SAXException {
		try {
			SAXBuilder builder = new SAXBuilder();
			return builder.build(file);
		} catch (IOException | JDOMException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void saveXML(Document document, File file) throws FileNotFoundException {
		XMLOutputter XMLOut = new XMLOutputter();
		try {
			Format f = Format.getPrettyFormat();
			f.setEncoding("UTF-8");// default=UTF-8
			XMLOut.setFormat(f);
			XMLOut.output(document, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int getBufferMaxSize() {
		if (commonDocument != null) {
			Document document = commonDocument;
			Element config = document.getRootElement();
			Element bufferConfig = config.getChild("buffer_config");

			int size = new Integer(bufferConfig.getAttributeValue("maxSize"));
			return size;
		}
		return 256;
	}
}
