package com.baihui.difu.baihui;

/**
 * 与销售商有关操作的类
 */
import java.io.IOException;
import org.json.JSONException;
import com.baihui.difu.baihui.DataToCrm;
import org.apache.log4j.*;


public class Vendors {
	public static Logger log=Logger.getLogger(Vendors.class);
	
	public DataToCrm dataToCrm=null;
	public Vendors vendor=null;
	public String vender_baihuiId="";
	
	/**
	 * 拼接Vendor的xmldata
	 */
	public String getXmldataContainsId(String vendorid,String vendorName){
		System.out.println("Vendor/getXmldataContainsId() method beginning!");
		log.info("vendorid:["+vendorid+"]");
		log.info("vendorName:["+vendorName+"]");
		String xmdata="";
		xmdata += "<Vendors>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='VENDORID'>"+vendorid+"</FL>" //经销商id
				+ "<FL val='Vendor Name'><![CDATA["+vendorName +"]]></FL>"//经销商名称
				+ "</row>";
		xmdata += "</Vendors>";
		log.info("xmdata:["+xmdata+"]");
		System.out.println("Vendor/getXmldataContainsId() method end!");
		return xmdata;
	}
	
	
	/**
	 * 拼接Vendor的xmldata
	 */
	public String getXmldataNotContainsId(String vendorName){
		log.info("Vendor/getXmldataNotContainsId() method beginning!");
		log.info("vendorName:["+vendorName+"]");
		String xmdata="";
		xmdata += "<Vendors>";
		xmdata += "<row no='"+1+"'> " 
				+ "<FL val='Vendor Name'><![CDATA["+vendorName+"]]></FL>"//经销商名称
				+ "</row>";
		xmdata += "</Vendors>";
		log.info("xmdata:["+xmdata+"]");
		log.info("Vendor/getXmldataNotContainsId() method end!");
		return xmdata;
	}

	
	/**
	 * 将数据插入到CRM中Vendor模块中
	 * @param moudle  调用的模块,这里是Vendors模块
	 * @param token 用户的token
	 * @param vendorName  要插入到字段值,这里是要插入的Vendors模块的Vendor Name的值
	 * @return baihuiid
	 */
	public String insert(String moudle,String token,String vendorName){
		log.info("Vendor/insert() method beginning!");
		log.info("moudle:["+moudle+"]");
		log.info("token:["+token+"]");
		log.info("vendorName:["+vendorName+"]");
		dataToCrm=new DataToCrm();
		vendor=new Vendors();
		String xmldata=vendor.getXmldataNotContainsId(vendorName);
		try {
			vender_baihuiId=dataToCrm.insert(moudle, token, xmldata);
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
			log.info("Vendor/insert() method end!");
		}
		log.info("vender_baihuiId:["+vender_baihuiId+"]");
		return vender_baihuiId;
	}
}
