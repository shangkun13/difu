package com.baihui.difu.util;
/**
 * 定义数据处理类
 */
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.log4j.*;

public class Util 
{
	public static Logger log=Logger.getLogger(Util.class);
	
	
	
	/**
	 * 添加到cookie,session
	 * 记录提示信息
	 * @param args
	 */
	
	public static String message_warn=""; 
	public static String message_error=""; 
	public static String message_normal="";
	public static String tip_error="";
	
	
	/**
	 * 将字符串日期格式,转换为Date型的日期格式
	 * @param str
	 * @return
	 */
	public static Date getDate(String str){
		log.info("Util/getDate()   method beginning");
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try {
			if(str!=null && !"".equals(str)){
				d=sim.parse(str);
			}else{
				log.warn("传递过来的字符串为空");
				System.out.println("传递过来的字符串为空");
			}
		} catch (ParseException e) {
			Util.message_error += "</br>"+str+"内容与规则不符.";
			log.info("</br>"+str+"内容与规则不符.");
			log.error("e:",e);
			e.printStackTrace();
		}
		
		log.info("Util/getDate()   method end");
		return d;
	}
	
	
	/**
	 * 将Date型日期格式转换成String型日期格式
	 */
	public static String getStringDate(String tip,Date date){
		log.info("Util/getStringDate()   method beginning");
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate="";
		try{
			if( date.toString()!=null && !"".equals(date.toString())){
				strDate = simple.format(date);
			}else{
				System.out.println("传递过来的日期格式为空!");
				log.warn("传递过来的日期格式为空!");
			}
		}catch(Exception e){
			Util.message_error += "</br>"+tip+"内容与规则不符.";
			log.info("</br>"+tip+"内容与规则不符.");
			log.error("Util/getStringDate()/e:", e);
		}
		
		log.info("Util/getStringDate()   method end");
		return strDate;

	}
	
	
	/**
	 * 去掉字符串的前后空格
	 */
	public static String StrTrim(String str){
		log.info("Util/StrTrim()   method beginning");
		try {
			if (str!=null && !"".equals(str)) {
				str = str.trim();
			} else {
				System.out.println("传递过来的字符串为空!");
				log.warn("传递过来的字符串为空!");
			}
		} catch (Exception e) {
			//Util.message_error += "</br>您提供的内容与规则不符.";
			log.info("</br>您提供的内容与规则不符:"+str);
			log.error("Util/StrTrim()/e:", e);
		}
		
		log.info("Util/StrTrim()   method end");
		return str;
	}
	
	
	/**
	 * 将url的字符串进行url编码
	 */
	public static String urlDecode(String str){
		log.info("Util/urlDecode()   method beginning");
		String urlStr=null;
		try {
			if(str!=null && !"".equals(str)){
				urlStr=java.net.URLEncoder.encode(str,"utf-8");
			}else{
				System.out.println("传递过来的url地址为空!");
				log.warn("传递过来的url地址为空!");
			}
		} catch (UnsupportedEncodingException e) {
			//Util.message_error+="</br>您的url地址不正确.";
			log.info("</br>您的url地址不正确:"+str);
			log.error("Util/urlDecode()/e:",e);
			e.printStackTrace();
		}
		log.info("Util/urlDecode()   method end");
		return urlStr;   
		
	}
	
	
	/**
	 * 将String类型转换陈double类型
	 */
	public static double getDoubleFromString(String tip,String str){
		log.info("Util/getDoubleFromString()   method end");
		double d=0.00;
		if( str!=null && !"".equals(str)){
			try{
				d=Double.parseDouble(str);
			}catch  (Exception e) {
				Util.message_error+="</br>"+tip+"内容与规则不符.";
				log.info("<br/>"+tip+"内容与规则不符.");
				log.error("Util/getDoubleFromString()/e:",e);
			}
			
		}else{
			System.out.println("传递过来的字符串为空!");
			log.warn("传递过来的字符串为空!");
		}
		
		log.info("Util/getDoubleFromString() method end");
		return d;
	}
	
	
	/**
	 * 判断字符串是否是数字类型 并将字符串转换成int 类型
	 * 
	 * @param args
	 */
	public static int convertIntFromString(String tip,String str) {
		log.info("Util/convertIntFromString()   method begin!");
		int num = 0;
		if (str != null && !"".equals(str)) {
			try {
				num = Integer.valueOf(str);// 把字符串强制转换为数字
			} catch (Exception e) {
				Util.message_error+="</br>"+tip+"内容与规则不符.";
				log.info("</br>"+tip+"内容与规则不符.");
				log.error("Util/convertIntFromString()/e:",e);
			}
		}
		log.info("Util/convertIntFromString()   method end!");
		return num;
	}

	
	
	public static void main(String[] args){
	}

	
}
