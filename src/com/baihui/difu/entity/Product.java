package com.baihui.difu.entity;

public class Product {

	/** 产品长代码 */
	private String priductLongCode;
	
	/** 商品名称 */
	private String productName;
	
	/** 规格型号 */
	private String modeNo;
	
	/** 数量 */
	private int quantity;
	
	/**单位*/
	private String unit;
	
	/** 销售单价 */
	private double unitPrice;
	
	/** 销售金额 */
	private double totalPrice;
	
	/**折扣*/
	private double discount;
	
	/**产品id*/
	private String baihuiid;

	public String getPriductLongCode() {
		return priductLongCode;
	}

	public void setPriductLongCode(String priductLongCode) {
		this.priductLongCode = priductLongCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getModeNo() {
		return modeNo;
	}

	public void setModeNo(String modeNo) {
		this.modeNo = modeNo;
	}


	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getBaihuiid() {
		return baihuiid;
	}

	public void setBaihuiid(String baihuiid) {
		this.baihuiid = baihuiid;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
