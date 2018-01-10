package com.baihui.difu.baihui;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;

public class Products {

public static Logger log=Logger.getLogger(Products.class);
	
	public DataToCrm dataToCrm=null;
	public Products product=null;
	public String product_baihuiId=null;
	
	/**
	 * 拼接Products的xmldata
	 */
	public String getXmldataContainsId(String productid,String productName){
		log.info("Product/getXmldataContainsId() method beginning!");
		log.info("productid:["+productid+"]");
		log.info("productName:["+productName+"]");
		String xmdata="";
		xmdata += "<Products>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='PRODUCTID'>"+productid+"</FL>" //经销商id
				+ "<FL val='Product Name'><![CDATA["+productName+"]]></FL>"//产品名称
				+ "</row>";
		xmdata += "</Products>";
		log.info("xmdata:["+xmdata+"]");
		System.out.println("Product/getXmldataContainsId() method end!");
		return xmdata;
	}
	
	
	/**
	 * 拼接Products的xmldata
	 */
	public String getXmldataNotContainsId(String productName){
		log.info("Product/getXmldataNotContainsId() method beginning!");
		log.info("productName:["+productName+"]");
		String xmdata="";
		xmdata += "<Products>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='Product Name'><![CDATA["+productName+"]]></FL>"//产品名称
				+ "</row>";
		xmdata += "</Products>";
		log.info("xmdata:["+xmdata+"]");
		log.info("Product/getXmldataNotContainsId() method end!");
		return xmdata;
	}

	
	/**
	 * 将数据插入到CRM中Products模块中
	 * @param moudle  调用的模块,这里是Vendors模块
	 * @param token 用户的token
	 * @param vendorName  要插入到字段值,这里是要插入的Vendors模块的Vendor Name的值
	 * @return baihuiid
	 */
	public String insert(String moudle,String token,String productName){
		log.info("Product/insert() method beginning!");
		log.info("moudle:["+moudle+"]");
		log.info("token:["+token+"]");
		log.info("productName:["+productName+"]");
		dataToCrm=new DataToCrm();
		product=new Products();
		String xmldata=product.getXmldataNotContainsId(productName);
		try {
			product_baihuiId=dataToCrm.insert(moudle, token, xmldata);
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
			System.out.println("Product/insert() method end!");
			log.info("Product/insert() method end!");
		}
		return product_baihuiId;
	}
	
	
	
}
