package com.baihui.difu.baihui;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;

public class Accounts 
{
public static Logger log=Logger.getLogger(Vendors.class);
	
	public DataToCrm dataToCrm=null;
	public Accounts accounts=null;
	public String account_baihuiId=null;
	
	/**
	 * 拼接Vendor的xmldata
	 */
	public String getXmldataContainsId(String accountid,String accountName){
		System.out.println("Accounts/getXmldataContainsId() method beginning!");
		log.info("accountid:["+accountid+"]");
		log.info("accountName:["+accountName+"]");
		String xmdata="";
		xmdata += "<Accounts>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='ACCOUNTID'>"+accountid+"</FL>" //经销商id
				+ "<FL val='Account Name'><![CDATA["+accountName+"]]></FL>"//经销商名称
				+ "</row>";
		xmdata += "</Accounts>";
		log.info("xmdata:["+xmdata+"]");
		System.out.println("Accounts/getXmldataContainsId() method end!");
		return xmdata;
	}
	
	
	/**
	 * 拼接Vendor的xmldata
	 */
	public String getXmldataNotContainsId(String accountName){
		log.info("Accounts/getXmldataNotContainsId() method beginning!");
		log.info("accountName:["+accountName+"]");
		String xmdata="";
		xmdata += "<Accounts>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='Account Name'><![CDATA["+accountName+"]]></FL>"//经销商名称
				+ "</row>";
		xmdata += "</Accounts>";
		log.info("xmdata:["+xmdata+"]");
		log.info("Accounts/getXmldataNotContainsId() method end!");
		return xmdata;
	}

	
	/**
	 * 将数据插入到CRM中Account模块中
	 * @param moudle  调用的模块,这里是Account模块
	 * @param token 用户的token
	 * @param vendorName  要插入到字段值,这里是要插入的Vendors模块的Vendor Name的值
	 * @return baihuiid
	 */
	public String insert(String moudle,String token,String accountName){
		log.info("Accounts/insert() method beginning!");
		log.info("moudle:["+moudle+"]");
		log.info("token:["+token+"]");
		log.info("accountName:["+accountName+"]");
		dataToCrm=new DataToCrm();
		accounts=new Accounts();
		String xmldata=accounts.getXmldataNotContainsId(accountName);
		try {
			account_baihuiId=dataToCrm.insert(moudle, token, xmldata);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("e:",e);
		} catch (JSONException e) {
			e.printStackTrace();
			log.error("e:",e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("e:",e);
		}finally{
			log.info("Accounts/insert() method end!");
		}
		return account_baihuiId;
	}

}
