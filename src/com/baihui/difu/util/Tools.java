/**   
 * @Title: Tools.java
 * @Package com.baihui.haoshili.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author jason    
 * @date 2013-11-29 上午10:09:32
 */

package com.baihui.difu.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.baihui.difu.dao.BaseDAO;

/**
 * @author jason
 */
public class Tools {
	public static Logger log=Logger.getLogger(Tools.class);
	
	
	/**
	 * 时间转字符串(yyyy-MM-dd HH:mm:ss格式)
	 * @Title: datetimeToStr
	 * @param datetime
	 * @return String(年月日时分秒格式)
	 * @author jason
	 * @date 2013-11-29 上午10:40:52
	 */
	public static String datetimeToStr(String tip,Date datetime) {
		String str="";
		if(datetime!=null){
			str = dateToStr("yyyy-MM-dd HH:mm:ss", datetime);
		}else{
			log.info("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		return str;
	}
	
	
	
	/**
	 * 时间转字符串(yyyy-MM-dd HH:mm:ss格式),不含有提示信息
	 * @param datetime
	 * @return
	 */
	public static String datetimeToStr(Date datetime) {
		String str="";
		if(datetime!=null){
			str = dateToStr("yyyy-MM-dd HH:mm:ss", datetime);
		}else{
			log.info("传递过来的参数值是null或者是\"\":" );
		}
		return str;
	}

	
	
	/**
	 * 时间转日期(yyyy-MM-dd)
	 * @Title: dayToStr
	 * @param day
	 * @return String(年月日)
	 * @author jason
	 * @date 2013-11-29 上午10:41:45
	 */
	public static String dayToStr(String tip,Date day) {
		String str="";
		if(day!=null){
			str = dateToStr("yyyy-MM-dd", day);
		}else{
			log.info("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		return str;
	}

	
	
	/**
	 * 字符串转日期(yyyy-MM-dd)
	 * @Title: strToDay
	 * @param str
	 * @return Date日期(年月日)
	 * @author jason
	 * @date 2013-11-29 上午10:42:10
	 */
	public static Date strToDay(String tip,String str) {
		
		Date date = null;
		if(str != null && !"".equals(str)){
			date = strToDate("yyyy-MM-dd",str);
		}else{
			log.info("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		return date;
		
	}

	
	
	/**
	 * 字符串转时间(yyyy-MM-dd HH:mm:ss)
	 * @Title: strToDatetime
	 * @param str
	 * @return Date时间(年月日时分秒)
	 * @author jason
	 * @date 2013-11-29 上午10:43:02
	 */
	public static Date strToDatetime(String tip,String str) {
		Date date=null;
		if(str != null && !"".equals(str)){
			date = strToDate("yyyy-MM-dd HH:mm:ss",str);
		}else{
			log.info("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		return date;
	}

	
	
	/**
	 * 获取现在同步时间<br />
	 * 说明:获取配置文件中需要提前的分钟，然后设置现在同步时间<br />
	 * 例如配置文件中值为2，则应该提前2分钟
	 * @Title: getNowSyncTime
	 * @return String 获取现在同步时间
	 * @author jason
	 * @throws Exception 
	 * @date 2013-11-29 上午10:43:37
	 */
	public static String getNowSyncTimeStr() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(BaseDAO.getSystemTimestamp().getTime()));
		String crmSyncMinute = PropertyManager.getProperty("CRM_SYN_MINUTE");
		if ( crmSyncMinute != null && !"".equals(crmSyncMinute) ){
			cal.add(Calendar.MINUTE, -Integer.parseInt(crmSyncMinute));
		}
		return datetimeToStr(cal.getTime());
	}
	
	/**
	 * 通过时间获取同步时间，为空则返回空字符串
	 * @Title: getSyncDateStr
	 * @param date
	 * @return String
	 * @author jason
	 * @date 2013-11-30 下午02:39:48
	 */
	public static String getSyncDateStr(Date date){
		if( date == null ){
			return "";
		}
		long time = date.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		String crmSyncMinute = PropertyManager.getProperty("CRM_SYN_MINUTE");
		if ( crmSyncMinute != null && !"".equals(crmSyncMinute) ){
			cal.add(Calendar.MINUTE, -Integer.parseInt(crmSyncMinute));
		}
		return datetimeToStr(cal.getTime());
	}
	
	/**
	 * 通过时间戳获取同步时间戳
	 * @Title: getSyncTimestamp
	 * @param timestamp
	 * @return 同步时间戳
	 * @throws Exception Timestamp
	 * @author jason
	 * @date 2013-11-30 下午12:06:41
	 */
	public static Timestamp getSyncTimestamp(Timestamp timestamp){
		Calendar cal = Calendar.getInstance();
		if (timestamp == null ){
			try {
				timestamp = BaseDAO.getSystemTimestamp();
			} catch (Exception e) {
				timestamp = new Timestamp(System.currentTimeMillis());
			}
		}
		cal.setTime(timestamp);
		String crmSyncMinute = PropertyManager.getProperty("CRM_SYN_MINUTE");
		if ( crmSyncMinute != null && !"".equals(crmSyncMinute) ){
			cal.add(Calendar.MINUTE, -Integer.parseInt(crmSyncMinute));
		}
		return new Timestamp(cal.getTimeInMillis());
	}
	
	
	/**
	 * 将String类型转化成Timestamp类型
	 */
	public static Timestamp strToTimestamp(String tip,String str){
		//现将String日期转化成Date日期
		Date date = Tools.strToDatetime(tip, str);
		log.info("date:"+date);
		Timestamp dateTime = new Timestamp(date.getTime());
		return dateTime;
	}
	
	
	/**

	 * 获取数据库当前时间，如果向数据库取时间失败则取系统当前时间
	 * @Title: getNowTimestamp
	 * @return Timestamp 数据库当前时间
	 * @author jason
	 * @date 2013-11-30 下午12:05:53
	 */
	public static Timestamp getNowTimestamp(){
		try{
			return BaseDAO.getSystemTimestamp();
		} catch(Exception e){
			return new Timestamp(System.currentTimeMillis());
		}
	}
	
	/**
	 * 获取数据库时间
	 * @Title: getNowTime
	 * @return 数据库当前时间
	 * @throws Exception String
	 * @author jason
	 * @date 2013-11-30 上午11:14:49
	 */
	public static String getNowTime() throws Exception {
		return datetimeToStr(new Date(BaseDAO.getSystemTimestamp().getTime()));
	}
	
	/**
	 * 将String类型的日期转换成Date类型的日期
	 * @param Patten
	 * @param str
	 * @return
	 */
	private static Date strToDate(String Patten, String str) {
		log.info("Tools/strToDate() method begin");
		log.info("Patten:[" + Patten +"]");
		log.info("str:[" + str +"]");
		Date date = null;
		SimpleDateFormat sim=new SimpleDateFormat(Patten);
		if(str != null && !"".equals(str)){
			try {
				date = sim.parse(str);
			} catch (ParseException e) {
				log.error("Tools/strToDate() ParseException",e);
			}
		}
		log.info("转换后的值是:"+date);
		log.info("StringUtil/strToDate() method end");
		return date;
	}

	/**
	 * 将Date类型的日期转换成String类型的日期
	 * @param Patten
	 * @param date
	 * @return
	 */
	private static String dateToStr(String Patten, Date date) {
		log.info("Tools/dateToStr() method begin");
		log.info("Patten:[" + Patten +"]");
		log.info("date:[" + date +"]");
		String str = null;
		SimpleDateFormat format = new SimpleDateFormat(Patten);
		if(date!=null){
			try{
				str = format.format(date);
			}catch(Exception e){
				log.error("StringUtil/dateToStr() Exception",e);
			}
		}
		log.info("转换后的值是:"+str);
		log.info("Tools/dateToStr() method end");
		return str;
	}

	
	
	/**
	 * 去掉字符串前后的空格
	 * @param tip 提示信息
	 * @param str 要转换的字符串
	 * @return
	 */
	public static String trim(String tip,String str){
		log.info("Tools/trim() method begin");
		log.info("tip:[" + tip +"]");
		log.info("str:[" + str +"]");
		String s = "";
		if ( str != null && !"".equals(str)){
			s = str.trim();
		}else{
			log.warn("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		log.info("转换后的值是:"+s);
		log.info("Tools/trim() method end");
		return s;
	}
	
	
	/**
	 * 去掉字符串前后的空格
	 * @param tip 提示信息
	 * @param str 要转换的字符串
	 * @return
	 */
	public static String trim(String str){
		log.info("str:[" + str +"]");
		String s = "";
		if ( str != null && !"".equals(str)){
			s = str.trim();
		}else{
			log.info("传递过来的值是null或者是\"\":" );
		}
		return s;
	}
	
	
	
	/**
	 * 将string类型转换成int类型
	 * @param tip 提示信息
	 * @param str 要转换的字符串
	 * @return
	 */
	public static int convertIntFromString(String tip,String str){
		log.info("Tools/convertIntFromString() method begin");
		log.info("tip:[" + tip +"]");
		log.info("str:[" + str +"]");
		int result = 0;
		if(str != null && !"".equals(str)){
			result = Integer.parseInt(str);
			log.info("将" + tip + "中的String类型的数值是:" + str + "转换成int类型后的值是" + result);
		}else{
			log.warn("在" + tip + "中,传递过来的值是null或者是\"\":" );
		}
		log.info("转换后的值是:"+result);
		log.info("Tools/convertIntFromString() method end");
		return result;
	}
	
	/**
	 * 将java.util.Date类型转换成java.sql.Date类型
	 * @param tip 提示信息
	 * @param date java.util.Date类型的日期
	 * @return java.sql.Date类型的日期
	 */
	public static java.sql.Date convertSqlDateFromUtilDate(String tip,java.util.Date date){
		log.info("Tools/convertSqlDateFromUtilDate() method begin");
		log.info("tip:[" + tip +"]");
		log.info("date:" + date +"]");
		java.sql.Date sd = null;
		if(date != null){
			sd=new java.sql.Date(date.getTime());
		}else{
			log.warn("在" + tip + "中,传递过来的日期格式的值是:"+date );
		}
		log.info("转换后的值是:"+date);
		log.info("Tools/convertSqlDateFromUtilDate() method end");
		return sd;
	}
	
	
	
	/**
	 * 比较两个时间的大小,返回计较小者
	 */
	public Date compareDate(Date beginSyncDate,Date endSyncDate){
		log.info("Tools/compareDate() method begin");
		log.info("beginSyncDate:["+beginSyncDate+"]");
		log.info("endSyncDate:["+endSyncDate+"]");
		boolean flag = beginSyncDate.before(endSyncDate);
		if(flag){
			log.info("Tools/compareDate() method end");
			return beginSyncDate;
		}else{
			log.info("Tools/compareDate() method end");
			return endSyncDate;
		}
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
				log.info("<br/>"+tip+"内容与规则不符.");
				log.error("Util/getDoubleFromString()/e:",e);
			}
			
		}else{
			log.warn("传递过来的字符串为空!");
		}
		
		log.info("Util/getDoubleFromString() method end");
		return d;
	}
	
	public static double getDoubleFromString(String str){
		log.info("Util/getDoubleFromString() method end");
		double d=0.00;
		if( str!=null && !"".equals(str)){
			try{
				d=Double.parseDouble(str);
			}catch  (Exception e) {
				log.error("Util/getDoubleFromString()/e:",e);
			}
		}else{
			log.warn("传递过来的字符串为空!");
		}
		log.info("Util/getDoubleFromString() method end");
		return d;
	}
	
	/**
	 * 将double类型转换成String类型
	 */
	public static String double2String(Double d){
		return d.toString();
	}
	
	 /**
     * 判断字符串是否为空
     * 
     * @param value
     * @return：为空返回true,不为空返回false
     */
    public static boolean isNullOrEmpty(String value)
    {
        if (value != null && !"".equals(value.trim()) && !"null".equalsIgnoreCase(value.trim()))
        {
            return false;
        }
        return true;
    }

	/**
	 * @Title: main
	 * @param args
	 * return void
	 * @author jason
	 * @throws Exception 
	 * @date 2013-11-29 上午10:09:32
	 */
	public static void main(String[] args) throws Exception {
//		System.out.println(Tools.getSyncTimestamp(null));
//		System.out.println(Tools.getSyncDateStr(new Date()));
		
	}

}
