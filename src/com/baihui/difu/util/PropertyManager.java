package com.baihui.difu.util;
/**
 * 定义常量类
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

	private static PropertyManager propertyManager = new PropertyManager();
	private static Properties config = new Properties();
	private static String fn = null;
	private static String proName = "system.properties";

	static {
		try {
			InputStream fin = propertyManager.getClass().getResourceAsStream(
					"/com/baihui/difu/conf/" + proName);
			config.load(fin);
			fin.close();
		} catch (IOException ex) {
			System.out.println("getresource:: >>" + ex.toString());
		} catch (Exception e) {
			System.out.println("adf::" + e.toString());
		}

		fn = proName;
	}

	public PropertyManager() {

	}

	public PropertyManager(String fileName) throws Exception {

		try {
			InputStream fin = getClass().getResourceAsStream("/" + fileName);
			config.load(fin);
			fin.close();
		} catch (IOException ex) {
			System.out.println("getresource::>>" + ex.toString());
			throw new Exception("无法读取指定的配置文件:" + fileName);
		} catch (Exception e) {
			System.out.println("adf::" + e.toString());
		}
		fn = fileName;
	}

	public static String getProperty(String itemName) {
		return config.getProperty(itemName);
	}

	public static String getProperty(String itemName, String defaultValue) {
		return config.getProperty(itemName, defaultValue);
	}

	public void setProperty(String itemName, String value) {
		config.setProperty(itemName, value);
		return;
	}

	public void saveFile(String fileName, String description) throws Exception {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			config.store(fout, description);
			fout.close();
		} catch (IOException ex) {
			throw new Exception("无法保存指定的配置文件:" + fileName);
		}
	}

	public void saveFile(String fileName) throws Exception {
		saveFile(fileName, "");
	}

	public void saveFile() throws Exception {
		if (fn.length() == 0)
			throw new Exception("需指定保存的配置文件名");
		saveFile(fn);
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		PropertyManager.config = config;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		PropertyManager.fn = fn;
	}

	public static String getProName() {
		return proName;
	}

	public static void setProName(String proName) {
		PropertyManager.proName = proName;
	}
	
	public static void main(String [] args) throws Exception{
		System.out.print(PropertyManager.getProperty("EMAIL_CONTENT_CRM_FREE"));
	}
}
