package com.baihui.difu.entity;

import java.util.Date;
import java.util.List;

public class Order
{
	
	
	/** 日期 */
	private Date date;
	
	/** 单据编号 */
	private String OrderNo;
	
	/**购货单位*/
	private String purchaseUnit;
	
	/** 仓库  / 经销商*/
	private String storehouse;
	
	/** 部门 */
	private String department;
	
	/** 业务员 */
	private String accountExecutive;
	
	/**产品*/
	private List<Product> productList;
	
	/** 订单类型 */
	private int type;
	
	/** 百会ID*/
	private String baihuiid;
	
	/**百会中这条记录的最后修改时间*/
	private Date bModifiedTime;
	
	/**百会中这条记录的最后创建时间*/
	private Date bCreatedTime;
	
	/**这条记录的创建时间*/
	private Date createTime;
	
	/**这条记录的最后修改时间*/
	private Date modifiedTime;
	
	/**经销商对应与crm的baihuiid*/
	private String storehouse_baihuiid; 
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getStorehouse() {
		return storehouse;
	}
	public void setStorehouse(String storehouse) {
		this.storehouse = storehouse;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAccountExecutive() {
		return accountExecutive;
	}
	public void setAccountExecutive(String accountExecutive) {
		this.accountExecutive = accountExecutive;
	}
	public String getPurchaseUnit() {
		return purchaseUnit;
	}
	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBaihuiid() {
		return baihuiid;
	}
	public void setBaihuiid(String baihuiid) {
		this.baihuiid = baihuiid;
	}
	public Date getbModifiedTime() {
		return bModifiedTime;
	}
	public void setbModifiedTime(Date bModifiedTime) {
		this.bModifiedTime = bModifiedTime;
	}
	public Date getbCreatedTime() {
		return bCreatedTime;
	}
	public void setbCreatedTime(Date bCreatedTime) {
		this.bCreatedTime = bCreatedTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getStorehouse_baihuiid() {
		return storehouse_baihuiid;
	}
	public void setStorehouse_baihuiid(String storehouseBaihuiid) {
		storehouse_baihuiid = storehouseBaihuiid;
	}
	
	
	
	
	
	

}
