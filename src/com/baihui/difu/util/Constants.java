package com.baihui.difu.util;
/**
 * 定义数据常量
 * @author luoxl
 *
 */
public class Constants {

	/**查询所有的订单信息*/
	public static String SQL_GET_ALL_BY_ORDERID="SELECT o.id, o.oid, o.baihuiid, o.type,o.bmodifiedTime,o.bcreatedTime,o.createdTime,o.modifiedTime FROM orders o WHERE 1=1 ";//查询所有的订单
	
	/**查询销售单*/
	public static String SQL_GET_ALL_BY_ORDERID_TYPE_1="SELECT o.id, o.oid, o.baihuiid, o.type,o.bmodifiedTime,o.bcreatedTime,o.createdTime,o.modifiedTime FROM orders o WHERE 1=1 AND o.type=1 ";//销售单
	
	/**查询采购单*/
	public static String SQL_GET_ALL_BY_ORDERID_TYPE_2="SELECT o.id, o.oid, o.baihuiid, o.type,o.bmodifiedTime,o.bcreatedTime,o.createdTime,o.modifiedTime FROM orders o WHERE 1=1 AND o.type=0 ";//采购单
	
	/**查询所有的订单信息baihuiId*/
	public static String SQL_GET_BAIHUIID_BY_ORDERID="SELECT o.baihuiid FROM orders o WHERE 1=1 ";//查询所有的订单
	
	/**查询销售单baihuiId*/
	public static String SQL_GET_BAIHUIID_BY_ORDERID_TYPE_1="SELECT o.baihuiid FROM orders o WHERE 1=1 AND o.type=1 ";//销售单
	
	/**查询采购单baihuiId*/
	public static String SQL_GET_BAIHUIID_BY_ORDERID_TYPE_2="SELECT o.baihuiid FROM orders o WHERE 1=1 AND o.type=0 ";//采购单

	/**经销商模块*/
	public static String MODULE_VENDORS="Vendors";
	
	/**线索模块*/
	public static String MODULE_LEADS="Leads";
	
	/**联系人模块*/
	public static String MODULE_CONTACTS="Contacts";
	
	/**重复的客户*/
	public static String MODULE_CHONGFU="CustomModule5";
	
	public static final String CRM_MODULE_CHINESE_LEADS = "线索模块";
	
	/**产品模块*/
	public static String MODULE_PRODUCTS="Products";
	
	/**医院模块*/
	public static String MODULE_ACCOUNTS="Accounts";
	
	/**在excel表中有多少列*/
	public static int COLOMNNUM=50;
	/** crm操作每页数量 */
	public static final int DB2CRM_DO_UNIT_COUNT = 100;
	/** crm查询每页数量 */
	public static final int DB2CRM_SELECT_UNIT_COUNT = 200;
	/** CRM产品请求前缀 */
//	public static final String CRM_URL = "https://crm.baihui.com/crm/private/json/";
	public static final String CRM_URL = "https://crm.zoho.com.cn/crm/private/json/";
	/** 数据库每次提交数量 */
	public static final int DB_BATCH_DO_NUM = 1000;
	
	public static String CRM_SELECTCOLUMNS_LEADS = "Leads(LEADID,Last Name,Mobile,线索编号,SMOWNERID,Phone,Created Time,Modified Time)";
	
	public static String CRM_SELECTCOLUMNS_CONTACTS = "Contacts(CONTACTID,Last Name,Mobile,Phone,Email,手机2,所属项目,留言内容,资源日期,SMOWNERID,Contact Owner,Created Time,Modified Time)";
	/*
	 * 通用判断1(正向思维)
	 */
	/** 通用判断1_否 */
	public static final String BRANCH_FLAG1_NO = "0";

	/** 通用判断1_是 */
	public static final String BRANCH_FLAG1_YES = "1";
}
