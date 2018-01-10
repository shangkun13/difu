package com.baihui.difu.baihui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baihui.difu.entity.Order;
import com.baihui.difu.entity.Product;
import com.baihui.difu.util.Util;
import com.baihui.difu.util.Constants;

import org.apache.log4j.*;


public class PurchaseOrders extends DataToCrm{
	
	public static Logger log=Logger.getLogger(PurchaseOrders.class);
	public Vendors vendor=null;
	public Product product=null;
	public Products products=null;
	DataToCrm datatocrm=null;

	/**
	 * 插入订单到CRM中
	 * @param order
	 */
	public String insert(Order order,String email){
		log.info("PurchaseOrder/insert() method beginning!");
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
		String jxs_baihuiid="";//经销商的baihuiid
		String product_baihuiid="";//产品的baihuiid
		if(order!=null){//order不等于空做两件事
			//第一件:根据经销商名称获取经销商的baihuiid
			try {
				jxs_baihuiid=datatocrm.getBaihuiIDByModuleidOrModulename("vendorname", "Vendors", token, Util.StrTrim(order.getStorehouse()));
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
			
			
			//第二件:拼接采购单的xmldata的主题部分
			productList=order.getProductList();
			xmdata += "<PurchaseOrders>";
			xmdata += "<row no='"+1+"'> " 
					+ "<FL val='Purchase Order Owner'><![CDATA[ " + email + "]]></FL>" //代理商采购所有者
					+ "<FL val='Subject'><![CDATA[ "+order.getOrderNo() +" ]]></FL>"//主题
					+ "<FL val='部门'><![CDATA[ "+order.getDepartment() +" ]]></FL>"//部门	
					//+ "<FL val='PO Date'><![CDATA[ "+ Util.getStringDate("单据编号为:"+order.getOrderNo()+"的订单,其\"实收数量\"", order.getDate()) +" ]]></FL>"//日期	
					+ "<FL val='日期'><![CDATA[ "+ Util.getStringDate("单据编号为:"+order.getOrderNo()+"的订单,其\"实收数量\"", order.getDate()) +" ]]></FL>"//日期
					+ "<FL val='VENDORID'>" + jxs_baihuiid + "</FL>"//收货单位的baihuiid
					+ "<FL val='Vendor Name'><![CDATA[ "+order.getStorehouse() +" ]]></FL>"//收货单位
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
						log.info("产品id-->product_baihuiid:["+product_baihuiid+"]");
						product_baihuiid=products.insert(Constants.MODULE_PRODUCTS, token, Util.StrTrim(product.getPriductLongCode()));
					}
					
					//2.拼接采购单xmldata的产品列表部分
					//拼接'商品长代码+规格型号+单位 放到CRM的"产品描述"中
					String str="商品名称:"+product.getProductName()+"商品长代码:"+product.getPriductLongCode()+" ,规格型号:"+product.getModeNo()+" ,单位:"+product.getUnit();
					xmdata += "<product no='"+(i+1)+"'>"
							+ "<FL val='Product Id'>"+product_baihuiid+"</FL>"
							+ "<FL val='Product Name'> <![CDATA[ "+product.getPriductLongCode()+" ]]> </FL>"//产品名称
							+ "<FL val='Quantity'> "+product.getQuantity()+" </FL>"//数量
							+ "<FL val='List Price'> "+product.getUnitPrice()+" </FL>"//定价
							+ "<FL val='Unit Price'><![CDATA["+product.getUnitPrice()+"]]></FL>"// 单价
							+ "<FL val='Total'>"+product.getTotalPrice()+"</FL>"//共计
							//+ "<FL val='Discount'>0.0</FL>"
							+ "<FL val='Total After Discount'>"+product.getTotalPrice()+"</FL>"//折后总计
							+ "<FL val='Net Total'>"+product.getTotalPrice()+"</FL>"//净总计
							//+ "<FL val='Tax'>0.0</FL>"
							+ "<FL val='Product Description'>"+str+"</FL>"//商品长代码+规格型号+单位
							+"</product>";
				}
				xmdata += "</FL>";
				xmdata += "<FL val='Sub Total'> " + xiaoji  + "</FL>"//小计
				//+ "<FL val='Discount'><![CDATA[ 0 ]]></FL>"//折扣
				//+ "<FL val='Tax'><![CDATA[ 0 ]]></FL>"//税
				//+ "<FL val='Adjustment'><![CDATA[ " + xiaoji + " ]]></FL>"//调整
				+ "<FL val='Grand Total'> " + leiji + "</FL>";//累计
			}
			xmdata += "</row> "
					+ "</PurchaseOrders>";
			if (xmdata.length()<30){
				return null ;
			}
			
			try {
				baihuiId=datatocrm.insert("PurchaseOrders", token, xmdata);
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
		log.info("插入CRM中PurchaseOrders模块后,返回的baihuiId:["+baihuiId+"]");
		log.info("insert() method end!");
		return baihuiId;
	}
	
	 /**
	  * 根据baihuiid查询记录,判断该记录是否存在
	  * @param baihuiid
	  * @return
	  */
	public String getByBaihuiId(String baihuiid){
		log.info("PurchaseOrder/getByBaihuiId() method beginning!");
		log.info("baihuiid:["+baihuiid+"]");
		String id="";
		datatocrm=new DataToCrm();
		try {
			id=datatocrm.getBaihuiIDByModuleidOrModulename("purchaseorderid", "PurchaseOrders", token, baihuiid);
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
			log.info("PurchaseOrder/getByBaihuiId() method end!");
		}
		
		return id;
	}
	
	
	/**
	 * 根基baihuiid查询记录,返回记录对象
	 * @param id
	 * @return
	 * @throws JSONException 
	 */
	public Order getPurchaseOrderByID(String id) throws JSONException{
		log.info("PurchaseOrders/getPurchaseOrderByID() method beiginning!"); 
		log.info("id:["+id+"]");
		Order order=new Order();
		datatocrm = new DataToCrm();
		JSONArray jsonArrays=new JSONArray();
		JSONObject ret = null;
		List<Product> productList=new ArrayList<Product>();
		Product product=new Product();
		try {
			//jsonArrays是由row组成的集合,里面可能有一个或者多个row,每一个row是一个JsonObject对象
			jsonArrays=datatocrm.getOne(id, "PurchaseOrders", token);
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
		//这里jsonArrays.length()应该等于1,因为是根据id查询
		for(int i=0;i<jsonArrays.length();i++){
			ret = jsonArrays.getJSONObject(i);
			//每一个row下面会有多个FL标签
			JSONArray jsonArray = ret.getJSONArray("FL");
			for (int j = 0; j < jsonArray.length(); j++) {
				//将其组装成Order对象的形式
				
				//日期---订单日期
				if(jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase("日期")){
					log.info("订单日期:["+jsonArray.getJSONObject(j).getString("val")+"]");
					order.setDate(Util.getDate(jsonArray.getJSONObject(j).getString("content").trim()));
				}
				//单据编号----主题
				if(jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase("Subject")){
					log.info("单据编号:["+jsonArray.getJSONObject(j).getString("val")+"]");
					order.setOrderNo(jsonArray.getJSONObject(j).getString("content").trim());
				}
				
				//仓库  / 经销商---经销商名称
				if(jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase("Vendor Name")){
					log.info("仓库:["+jsonArray.getJSONObject(j).getString("val")+"]");
					order.setStorehouse(jsonArray.getJSONObject(j).getString("content").trim());
				}
				//部门 ----部门
				if(jsonArray.getJSONObject(j).getString("val").equals("部门")){
					log.info("部门 :["+jsonArray.getJSONObject(j).getString("val")+"]");
					order.setDepartment(jsonArray.getJSONObject(j).getString("content").trim());
				}
				
				//业务员---业务员
				if(jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase("业务员")){
					log.info("业务员:["+jsonArray.getJSONObject(j).getString("val")+"]");
					order.setAccountExecutive(jsonArray.getJSONObject(j).getString("content").trim());
				}
				
				//Product Details
				if(jsonArray.getJSONObject(j).getString("val").equals("Product Details")){
					//判断Product Details下面只有一条数据
					if (jsonArray.getJSONObject(j).toString().contains("\"product\":{")) {
						//获取这唯一的一个product产品明细
						JSONObject productDetailobject = jsonArray.getJSONObject(j).getJSONObject("product");
						//产品明细下面有多个FL
						JSONArray productJsonArray = productDetailobject.getJSONArray("FL");
						//遍历这多个FL
						for (int m = 0; m < productJsonArray.length(); m++) {
							
							//产品id----Product Id
							if(("Product Id").equals(productJsonArray.getJSONObject(m).getString("val"))){
								log.info("Product Id:["+productJsonArray.getJSONObject(m).getString("content")+"]");
								product.setBaihuiid(productJsonArray.getJSONObject(m).getString("content"));
							}
							//产品长代码---Product Name
							if(("Product Name").equals(productJsonArray.getJSONObject(m).getString("val"))){
								log.info("Product Name:["+productJsonArray.getJSONObject(m).getString("content")+"]");
								product.setPriductLongCode(productJsonArray.getJSONObject(m).getString("content"));
							}
							
							//数量----Quantity
							if(("Quantity").equals(productJsonArray.getJSONObject(m).getString("val"))){
								log.info("Quantity:["+productJsonArray.getJSONObject(m).getString("content")+"]");
								product.setQuantity(Util.convertIntFromString("数量", productJsonArray.getJSONObject(m).getString("content")));
							}
							
							//销售单价---List Price
							if(("List Price").equals(productJsonArray.getJSONObject(m).getString("val"))){
								log.info("List Price:["+productJsonArray.getJSONObject(m).getString("content")+"]");
								product.setUnitPrice(Util.getDoubleFromString("销售订单",productJsonArray.getJSONObject(m).getString("content")));
							}
							//共计---Total
							if(("Total").equals(productJsonArray.getJSONObject(m).getString("val"))){
								log.info("Total:["+productJsonArray.getJSONObject(m).getString("content")+"]");
								product.setTotalPrice(Util.getDoubleFromString("金额",productJsonArray.getJSONObject(m).getString("content")));
							}
						}
						
						productList.add(product);
						
					//Product Details下面有多条数据
					}else if (jsonArray.getJSONObject(j).toString().contains("\"product\":[")) {
						//获取多个product
						JSONArray productDetailArray = jsonArray.getJSONObject(j).getJSONArray("product");
						//循环遍历product
						for (int n = 0; n < productDetailArray.length(); n++) {
							
							JSONArray productJsonArray =  productDetailArray.getJSONObject(n).getJSONArray("FL");
							//遍历这多个FL
							for (int m = 0; m < productJsonArray.length(); m++) {
								
								//产品id----Product Id
								if(("Product Id").equals(productJsonArray.getJSONObject(m).getString("val"))){
									log.info("Product Id:["+productJsonArray.getJSONObject(m).getString("content")+"]");
									product.setBaihuiid(productJsonArray.getJSONObject(m).getString("content"));
								}
								//产品长代码---Product Name
								if(("Product Name").equals(productJsonArray.getJSONObject(m).getString("val"))){
									log.info("Product Name:["+productJsonArray.getJSONObject(m).getString("content")+"]");
									product.setPriductLongCode(productJsonArray.getJSONObject(m).getString("content"));
								}
								
								//数量----Quantity
								if(("Quantity").equals(productJsonArray.getJSONObject(m).getString("val"))){
									log.info("Quantity:["+productJsonArray.getJSONObject(m).getString("content")+"]");
									product.setQuantity(Util.convertIntFromString("数量", productJsonArray.getJSONObject(m).getString("content")));
								}
								
								//销售单价---List Price
								if(("List Price").equals(productJsonArray.getJSONObject(m).getString("val"))){
									log.info("List Price:["+productJsonArray.getJSONObject(m).getString("content")+"]");
									product.setUnitPrice(Util.getDoubleFromString("销售订单",productJsonArray.getJSONObject(m).getString("content")));
								}
								//共计---Total
								if(("Total").equals(productJsonArray.getJSONObject(m).getString("val"))){
									log.info("Total:["+productJsonArray.getJSONObject(m).getString("content")+"]");
									product.setTotalPrice(Util.getDoubleFromString("金额",productJsonArray.getJSONObject(m).getString("content")));
								}
							}
							productList.add(product);
						}
					}
				}
			}
			log.info("产品数量["+productList.size()+"]");
			order.setProductList(productList);
		}
		log.info("PurchaseOrders/getPurchaseOrderByID() method end!");
		return order;
		
		
	}
	
	/**
	 * 根据id删除PurchaseOrder记录
	 */
	public int deleteById(String id){
		log.info("PurchaseOrders/deleteById() method beiginning!"); 
		int result=0;
		datatocrm=new DataToCrm();
		try {
			result=datatocrm.deleteByID(id, "PurchaseOrders", token);
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
			
			log.info("PurchaseOrders/deleteById() method end!"); 
		}
		
		return result;
	}
}
