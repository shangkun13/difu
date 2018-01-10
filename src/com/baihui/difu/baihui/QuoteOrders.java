package com.baihui.difu.baihui;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.baihui.difu.entity.Order;
import com.baihui.difu.entity.Product;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.Util;

public class QuoteOrders  extends DataToCrm{

	public static Logger log=Logger.getLogger(PurchaseOrders.class);
	public Vendors vendor=null;
	public Product product=null;
	public Products products=null;
	public Accounts account=null;
	DataToCrm datatocrm=null;
	
	/**
	 * 插入订单到CRM中
	 * @param order
	 */
	public String insert(Order order,String email){
		log.info("QuoteOrders/insert() method beginning!");
		log.info("order:["+order+"]");
		log.info("email:["+email+"]");
		String baihuiId=null;
		datatocrm=new DataToCrm();
		/**拼接xmdata数据**/
		String xmdata = "";
		List<Product> productList=null;
		Product product=null;//这个产品是存放到List<Product>中的元素
		double xiaoji=0.00;//小计           当前累计和小计设置成一样的值,因为没有折扣
		double leiji=0.00;//累计
		vendor=new Vendors();
		products=new Products();
		account=new Accounts();
		String jxs_baihuiid="";//经销商的baihuiid
		String yiyuan_baihuiid="";//医院的baihuiid---对应的模块是Account
		String product_baihuiid="";//产品的baihuiid
		if(order!=null){//order不等于空做三件事
			//第一件:根据经销商名称获取经销商的baihuiid
			try {
				jxs_baihuiid=datatocrm.getBaihuiIDByModuleidOrModulename("vendorname", Constants.MODULE_VENDORS, token, Util.StrTrim(order.getStorehouse()));
				log.info("经销商id-->jxs_baihuiid:["+jxs_baihuiid+"]");
			} catch (IOException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			} catch (JSONException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			}
			//如果jxs_baihuiid=null,则说明crm中没有这条记录,没有,则新增这条记录,并返回经销商对应的baihuiid
			if("".equals(jxs_baihuiid)||jxs_baihuiid==null){
				jxs_baihuiid=vendor.insert(Constants.MODULE_VENDORS, token, Util.StrTrim(order.getStorehouse()));
				log.info("经销商id-->jxs_baihuiid:["+jxs_baihuiid+"]");
			}
			
			
			//第二件:根据购货单位名称(医院)获取购货单位的baihuiid
			try {
				yiyuan_baihuiid=datatocrm.getBaihuiIDByModuleidOrModulename("accountname", Constants.MODULE_ACCOUNTS, token, Util.StrTrim(order.getPurchaseUnit()));
				log.info("医院id-->yiyuan_baihuiid:["+yiyuan_baihuiid+"]");
			} catch (IOException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			} catch (JSONException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				log.error("PurchaseOrder/insert()/e1:",e1);
			}
			//如果jxs_baihuiid=null,则说明crm中没有这条记录,没有,则新增这条记录,并返回经销商对应的baihuiid
			if("".equals(yiyuan_baihuiid)||yiyuan_baihuiid==null){
				yiyuan_baihuiid=account.insert(Constants.MODULE_ACCOUNTS, token, Util.StrTrim(order.getPurchaseUnit()));
				log.info("医院id-->yiyuan_baihuiid:["+yiyuan_baihuiid+"]");
			}
			
			
			//第三件:拼接采购单的xmldata的主题部分
			productList=order.getProductList();
			xmdata += "<Quotes>";
			xmdata += "<row no='"+1+"'> " 
					+ "<FL val='Purchase Order Owner'><![CDATA[ " + email + "]]></FL>" //代理商采购所有者
					+ "<FL val='Subject'><![CDATA[ "+order.getOrderNo() +" ]]></FL>"//主题
					+ "<FL val='ACCOUNTID'>"+yiyuan_baihuiid+"</FL>" //医院id
					+ "<FL val='Account Name'><![CDATA[ "+Util.StrTrim(order.getPurchaseUnit())+" ]]></FL>"//医院名称
					+ "<FL val='部门'><![CDATA[ "+order.getDepartment() +" ]]></FL>"//部门	
					+ "<FL val='日期'><![CDATA[ "+ Util.getStringDate( "单据编号为:"+order.getOrderNo()+"的订单,其\"实收数量\"",order.getDate()) +" ]]></FL>"//日期	
					//+ "<FL val='VENDORID'>" + jxs_baihuiid + "</FL>"//收货单位的baihuiid
					//+ "<FL val='Vendor Name'><![CDATA[ "+order.getStorehouse() +" ]]></FL>"//收货单位
					+ "<FL val='发货仓库_ID'>" + jxs_baihuiid + "</FL>"//收货单位的baihuiid
					+ "<FL val='发货仓库'><![CDATA[ "+order.getStorehouse() +" ]]></FL>"//收货单位
					+ "<FL val='业务员'><![CDATA[ "+order.getAccountExecutive() +" ]]></FL>";//业务员
			if(productList.size()>0){
				xmdata += "<FL val='Product Details'>";//开始产品明细
				for(int i=0;i<productList.size();i++){//遍历产品信息后做两件事
					product=productList.get(i);
					xiaoji += product.getTotalPrice();
					leiji += product.getTotalPrice();
					
					//1.根据产品名称获取产品的baihuiid
					try {
						product_baihuiid=datatocrm.getBaihuiIDByModuleidOrModulename("productname", "Products", token, Util.StrTrim(product.getPriductLongCode()));
						log.info("产品id-->product_baihuiid:["+product_baihuiid+"]");
					} catch (IOException e2) {
						e2.printStackTrace();
						log.error("PurchaseOrder/insert()/e2:",e2);
					} catch (JSONException e2) {
						e2.printStackTrace();
						log.error("PurchaseOrder/insert()/e2:",e2);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
						log.error("PurchaseOrder/insert()/e2:",e2);
					}
					
					
					//如果product_baihuiid=null,则说明crm中没有这条记录,没有,则新增这条记录,并返回经销商对应的baihuiid
					if("".equals(product_baihuiid)||product_baihuiid==null){
						product_baihuiid=products.insert(Constants.MODULE_PRODUCTS, token, Util.StrTrim(product.getPriductLongCode()));
						log.info("产品id-->product_baihuiid:["+product_baihuiid+"]");
					}
					
					//2.拼接采购单xmldata的产品列表部分
					//拼接'商品长代码+规格型号+单位 放到CRM的"产品描述"中
					String str="商品长代码:"+product.getPriductLongCode()+" ,规格型号:"+product.getModeNo()+" ,单位:"+product.getUnit();
					xmdata += "<product no='"+(i+1)+"'>"
							+ "<FL val='Product Id'>"+product_baihuiid+"</FL>"
							+ "<FL val='Product Name'> <![CDATA[ "+product.getPriductLongCode()+" ]]> </FL>"//产品名称
							+ "<FL val='Quantity'> "+product.getQuantity()+" </FL>"//数量
							+ "<FL val='List Price'> "+product.getUnitPrice()+" </FL>"//定价
							+ "<FL val='Unit Price'><![CDATA["+product.getUnitPrice()+"]]></FL>"// 单价
							+ "<FL val='Total'>"+(product.getQuantity()*product.getUnitPrice())+"</FL>"//共计---计算出来的-->数量*定价
							+ "<FL val='Discount'>" + product.getDiscount() + "</FL>"//折扣
							+ "<FL val='Total After Discount'>"+product.getTotalPrice()+"</FL>"//折后总计
							+ "<FL val='Net Total'>"+product.getTotalPrice()+"</FL>"//净总计
							//+ "<FL val='Tax'>0.0</FL>"
							+ "<FL val='Product Description'>"+str+"</FL>"//商品长代码+规格型号+单位
							+"</product>";
				}
				xmdata += "</FL>";
				xmdata += "<FL val='Sub Total'> " + xiaoji  + "</FL>"//小计
				//+ "<FL val='Discount'>"+product.getDiscount()+"</FL>"//折扣
				//+ "<FL val='Tax'><![CDATA[ 0 ]]></FL>"//税
				//+ "<FL val='Adjustment'><![CDATA[ " + xiaoji + " ]]></FL>"//调整
				+ "<FL val='Grand Total'> " + leiji + "</FL>";//累计
			}
			xmdata += "</row> "
					+ "</Quotes>";
			if (xmdata.length()<30){
				return null ;
			}
			
			try {
				baihuiId=datatocrm.insert("Quotes", token, xmdata);
			} catch (IOException e) {
				log.error("PurchaseOrder/insert()/e:",e);
			} catch (JSONException e) {
				e.printStackTrace();
				log.error("PurchaseOrder/insert()/e:",e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error("PurchaseOrder/insert()/e:",e);
			}
		}
		log.info("baihuiId:["+baihuiId+"]");
		log.info("QuoteOrders/insert() method end!");
		return baihuiId;
	}
	
	
	
	 /**
	  * 根据baihuiid查询记录,判断该记录是否存在
	  * @param baihuiid
	  * @return
	  */
	public String getByBaihuiId(String baihuiid){
		log.info("QuoteOrders/getByBaihuiId() method beginning!");
		log.info("baihuiid:["+baihuiid+"]");
		String id="";
		datatocrm=new DataToCrm();
		try {
			id=datatocrm.getBaihuiIDByModuleidOrModulename("quoteid", "Quotes", token, baihuiid);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("e:",e);
		} catch (JSONException e) {
			e.printStackTrace();
			log.error("e:",e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("e:",e);
		}
		finally{
			log.info("QuoteOrders/getByBaihuiId() method beginning!");
		}
		log.info("id:["+id+"]");
		return id;
	}
	
	
	
	/**
	 * 根据id删除PurchaseOrder记录
	 */
	public int deleteById(String id){
		log.info("QuoteOrders/deleteById() method beiginning!"); 
		log.info("id:["+id+"]");
		int result=0;
		datatocrm=new DataToCrm();
		try {
			result=datatocrm.deleteByID(id, "Quotes", token);
		} catch (IOException e) {
			log.error("e",e);
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			log.error("e",e);
		} catch (InterruptedException e) {
			log.error("e",e);
			e.printStackTrace();
		}finally{
			log.info("QuoteOrders/deleteById() method end!"); 
		}
		return result;
	}
}
