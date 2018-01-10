package com.baihui.difu.entity;

import java.util.Date;

public class Contacts {
	private String id;
	private String ownner;
	private String ownnerName;
	private String name;
	private String phone;
	private String mobile;
	private String email;
	private String context;
	private String liuYanTime;
	private String QQ;
	private String pinPai;
	private String suoshuxiangmu;
	private String address;
	private String keywords;
	private String website;
	private String zyDate;
	private String shouji2;
	
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
	
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnner() {
		return ownner;
	}

	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}

	public String getOwnnerName() {
		return ownnerName;
	}

	public void setOwnnerName(String ownnerName) {
		this.ownnerName = ownnerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getLiuYanTime() {
		return liuYanTime;
	}

	public void setLiuYanTime(String liuYanTime) {
		this.liuYanTime = liuYanTime;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getSuoshuxiangmu() {
		return suoshuxiangmu;
	}

	public void setSuoshuxiangmu(String suoshuxiangmu) {
		this.suoshuxiangmu = suoshuxiangmu;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getZyDate() {
		return zyDate;
	}

	public void setZyDate(String zyDate) {
		this.zyDate = zyDate;
	}

	public Contacts() {
		super();
	}

	public String getPinPai() {
		return pinPai;
	}

	public void setPinPai(String pinPai) {
		this.pinPai = pinPai;
	}

	
	public String getShouji2() {
		return shouji2;
	}

	public void setShouji2(String shouji2) {
		this.shouji2 = shouji2;
	}

	public Contacts(String id, String ownner, String ownnerName, String name,
			String phone, String mobile, String email, String context,
			String liuYanTime, String qQ, String suoshuxiangmu, String address,
			String keywords, String website, String baihuiid,
			Date bModifiedTime, Date bCreatedTime, Date createTime,
			Date modifiedTime, String status) {
		super();
		this.id = id;
		this.ownner = ownner;
		this.ownnerName = ownnerName;
		this.name = name;
		this.phone = phone;
		this.mobile = mobile;
		this.email = email;
		this.context = context;
		this.liuYanTime = liuYanTime;
		QQ = qQ;
		this.suoshuxiangmu = suoshuxiangmu;
		this.address = address;
		this.keywords = keywords;
		this.website = website;
		this.baihuiid = baihuiid;
		this.bModifiedTime = bModifiedTime;
		this.bCreatedTime = bCreatedTime;
		this.createTime = createTime;
		this.modifiedTime = modifiedTime;
		this.status = status;
	}

	
}
