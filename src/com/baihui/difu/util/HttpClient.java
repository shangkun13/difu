package com.baihui.difu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClient {

	/**
	 * 
	 * @param url
	 *            url地址
	 * @return Document XML
	 */
	private static final Logger LOG = Logger.getLogger(HttpClient.class);

	public static Document getXMLDataFromURL(String url) {
		long begin = System.currentTimeMillis();
		LOG.info("request:" + url);
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = null;
		try {
			URL u = new URL(url);
			InputStream in = u.openStream();

			doc = saxBuilder.build(in);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (LOG.isInfoEnabled()) {
			long end = System.currentTimeMillis();
			XMLOutputter outp = new XMLOutputter();
			LOG.info("response[" + (end - begin) + "]:" + outp.outputString(doc));
		}

		return doc;
	}
	
	/**
	 * 发送GET请求
	 * 
	 * @param url url地址
	 * @return JSONObject
	 */

	public static String getAjaxDataFromURLForSMS(String url) {
		long begin = System.currentTimeMillis();
		LOG.info("request:" + url);
		String result = null;
		BufferedReader rd = null;

		try {
			URL u = new URL(url);
			InputStream is = u.openStream();
			rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuffer sb = new StringBuffer();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			long end = System.currentTimeMillis();
			LOG.info("response[" + (end - begin) + "]:" + sb.toString());
			result = sb.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return result;
	}
	

	/**
	 * 发送GET请求
	 * 
	 * @param url
	 *            url地址
	 * @return JSONObject
	 */

	public static JSONObject getJSONDataFromURL(String url) {
		long begin = System.currentTimeMillis();
		LOG.info("request:" + url);
		JSONObject jsonObj = null;
		BufferedReader rd = null;

		try {
			URL u = new URL(url);
			InputStream is = u.openStream();
			rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuffer sb = new StringBuffer();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			long end = System.currentTimeMillis();
			LOG.info("response[" + (end - begin) + "]:" + sb.toString());
			jsonObj = new JSONObject(sb.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return jsonObj;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            url地址 params 参数
	 * @return JSONObject
	 */

	public static JSONObject getJSONDataFromURL(String url, String params) {
		long begin = System.currentTimeMillis();
		LOG.info("request:" + url);
		JSONObject jsonObj = null;

		HttpURLConnection huc = null;
		BufferedReader in = null;
		InputStreamReader isr = null;
		InputStream is = null;

		StringBuffer sb = new StringBuffer(333);

		try {

			URL u = new URL(url);
			huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("POST");
			huc.setDoOutput(true);
			huc.getOutputStream().write(params.getBytes("utf-8"));
			huc.getOutputStream().flush();
			huc.getOutputStream().close();
			is = huc.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			in = new BufferedReader(isr);
			String line = null;

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			long end = System.currentTimeMillis();
			LOG.info("response[" + (end - begin) + "]:" + sb.toString());
			jsonObj = new JSONObject(sb.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				huc.disconnect();
				is.close();
				isr.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return jsonObj;
	}

}
