package com.baihui.difu.entity;

import java.util.Date;

public class Leads {
	private String id;
	private String ownner;
	private String shengFen;
	private String diShi;
	private String Xian;
	private String GongSi;
	private String name;
	private String sex;
	private String age;
	private String zhiWei;
	private String email;
	private String phone;
	private String mobile;
	private String phone2;
	private String mobile2;
	private String QQ;
	private String weiXin;
	private String xianSuoZhuangTai;
	private String xianSuoFenLei;
	private String daiLiShang;
	private String yuanSuoJianCheng;
	private String yuanSuoXingZhi;
	private String guanXiDengJi;
	private String keHuLaiYuan;
	private String keHuBeiZhu;
	private String yuanSuoShuDi;
	private String yuanSuoMianJi;
	private String haiZiShuLiang;
	private String yueShouFei;
	private String yuanSuoDiZhi;
	private String dianYaoXiaCiDate;
	private String dianYaoGenJinJiLu;
	private String weiCanHuiYuanYin;
	private String canHuiWeiChengJiaoYuanYin;
	private String shouHuiYuanYin;
	private String yiCanJiaHuiYi;
	private String huiXiaoGenJinJiLu;
	private String huiXiaoXiaCiDate;
	private String xinXiLaiYuan;
	private String fangWenGuanJianZi;
	private String keHuJiBie;
	private String shiFouHeHuoTouZi;
	private String heHuoRenName;
	private String heHuoRenPhone;
	private String shiFouFaSongZiLiao;
	private String shiFouYouChangDI;
	private String yuSuan;
	private String liuYanDate;
	private String yuanSuoFenLei;
	private String zhaoShangXiaCiDate;
	private String zhaoShangGenJinJiLu;
	private String leadNo;
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
	

	public Leads() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Leads(String id, String ownner, String shengFen, String diShi,
			String xian, String gongSi, String name, String sex, String age,
			String zhiWei, String email, String phone, String mobile,
			String phone2, String mobile2, String qQ, String weiXin,
			String xianSuoZhuangTai, String xianSuoFenLei, String daiLiShang,
			String yuanSuoJianCheng, String yuanSuoXingZhi,
			String guanXiDengJi, String keHuLaiYuan, String keHuBeiZhu,
			String yuanSuoShuDi, String yuanSuoMianJi, String haiZiShuLiang,
			String yueShouFei, String yuanSuoDiZhi, String dianYaoXiaCiDate,
			String dianYaoGenJinJiLu, String weiCanHuiYuanYin,
			String canHuiWeiChengJiaoYuanYin, String shouHuiYuanYin,
			String yiCanJiaHuiYi, String huiXiaoGenJinJiLu,
			String huiXiaoXiaCiDate, String xinXiLaiYuan,
			String fangWenGuanJianZi, String keHuJiBie,
			String shiFouHeHuoTouZi, String heHuoRenName, String heHuoRenPhone,
			String shiFouFaSongZiLiao, String shiFouYouChangDI, String yuSuan,
			String liuYanDate, String yuanSuoFenLei, String zhaoShangXiaCiDate,
			String zhaoShangGenJinJiLu, String baihuiid, Date bModifiedTime,
			Date bCreatedTime, Date createTime, Date modifiedTime,
			String storehouse_baihuiid) {
		super();
		this.id = id;
		this.ownner = ownner;
		this.shengFen = shengFen;
		this.diShi = diShi;
		Xian = xian;
		GongSi = gongSi;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.zhiWei = zhiWei;
		this.email = email;
		this.phone = phone;
		this.mobile = mobile;
		this.phone2 = phone2;
		this.mobile2 = mobile2;
		QQ = qQ;
		this.weiXin = weiXin;
		this.xianSuoZhuangTai = xianSuoZhuangTai;
		this.xianSuoFenLei = xianSuoFenLei;
		this.daiLiShang = daiLiShang;
		this.yuanSuoJianCheng = yuanSuoJianCheng;
		this.yuanSuoXingZhi = yuanSuoXingZhi;
		this.guanXiDengJi = guanXiDengJi;
		this.keHuLaiYuan = keHuLaiYuan;
		this.keHuBeiZhu = keHuBeiZhu;
		this.yuanSuoShuDi = yuanSuoShuDi;
		this.yuanSuoMianJi = yuanSuoMianJi;
		this.haiZiShuLiang = haiZiShuLiang;
		this.yueShouFei = yueShouFei;
		this.yuanSuoDiZhi = yuanSuoDiZhi;
		this.dianYaoXiaCiDate = dianYaoXiaCiDate;
		this.dianYaoGenJinJiLu = dianYaoGenJinJiLu;
		this.weiCanHuiYuanYin = weiCanHuiYuanYin;
		this.canHuiWeiChengJiaoYuanYin = canHuiWeiChengJiaoYuanYin;
		this.shouHuiYuanYin = shouHuiYuanYin;
		this.yiCanJiaHuiYi = yiCanJiaHuiYi;
		this.huiXiaoGenJinJiLu = huiXiaoGenJinJiLu;
		this.huiXiaoXiaCiDate = huiXiaoXiaCiDate;
		this.xinXiLaiYuan = xinXiLaiYuan;
		this.fangWenGuanJianZi = fangWenGuanJianZi;
		this.keHuJiBie = keHuJiBie;
		this.shiFouHeHuoTouZi = shiFouHeHuoTouZi;
		this.heHuoRenName = heHuoRenName;
		this.heHuoRenPhone = heHuoRenPhone;
		this.shiFouFaSongZiLiao = shiFouFaSongZiLiao;
		this.shiFouYouChangDI = shiFouYouChangDI;
		this.yuSuan = yuSuan;
		this.liuYanDate = liuYanDate;
		this.yuanSuoFenLei = yuanSuoFenLei;
		this.zhaoShangXiaCiDate = zhaoShangXiaCiDate;
		this.zhaoShangGenJinJiLu = zhaoShangGenJinJiLu;
		this.baihuiid = baihuiid;
		this.bModifiedTime = bModifiedTime;
		this.bCreatedTime = bCreatedTime;
		this.createTime = createTime;
		this.modifiedTime = modifiedTime;
		this.storehouse_baihuiid = storehouse_baihuiid;
	}

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

	public String getShengFen() {
		return shengFen;
	}

	public void setShengFen(String shengFen) {
		this.shengFen = shengFen;
	}

	public String getDiShi() {
		return diShi;
	}

	public void setDiShi(String diShi) {
		this.diShi = diShi;
	}

	public String getXian() {
		return Xian;
	}

	public void setXian(String xian) {
		Xian = xian;
	}

	public String getGongSi() {
		return GongSi;
	}

	public void setGongSi(String gongSi) {
		GongSi = gongSi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getZhiWei() {
		return zhiWei;
	}

	public void setZhiWei(String zhiWei) {
		this.zhiWei = zhiWei;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getXianSuoZhuangTai() {
		return xianSuoZhuangTai;
	}

	public void setXianSuoZhuangTai(String xianSuoZhuangTai) {
		this.xianSuoZhuangTai = xianSuoZhuangTai;
	}

	public String getXianSuoFenLei() {
		return xianSuoFenLei;
	}

	public void setXianSuoFenLei(String xianSuoFenLei) {
		this.xianSuoFenLei = xianSuoFenLei;
	}

	public String getDaiLiShang() {
		return daiLiShang;
	}

	public void setDaiLiShang(String daiLiShang) {
		this.daiLiShang = daiLiShang;
	}

	public String getYuanSuoJianCheng() {
		return yuanSuoJianCheng;
	}

	public void setYuanSuoJianCheng(String yuanSuoJianCheng) {
		this.yuanSuoJianCheng = yuanSuoJianCheng;
	}

	public String getYuanSuoXingZhi() {
		return yuanSuoXingZhi;
	}

	public void setYuanSuoXingZhi(String yuanSuoXingZhi) {
		this.yuanSuoXingZhi = yuanSuoXingZhi;
	}

	public String getGuanXiDengJi() {
		return guanXiDengJi;
	}

	public void setGuanXiDengJi(String guanXiDengJi) {
		this.guanXiDengJi = guanXiDengJi;
	}

	public String getKeHuLaiYuan() {
		return keHuLaiYuan;
	}

	public void setKeHuLaiYuan(String keHuLaiYuan) {
		this.keHuLaiYuan = keHuLaiYuan;
	}

	public String getKeHuBeiZhu() {
		return keHuBeiZhu;
	}

	public void setKeHuBeiZhu(String keHuBeiZhu) {
		this.keHuBeiZhu = keHuBeiZhu;
	}

	public String getYuanSuoShuDi() {
		return yuanSuoShuDi;
	}

	public void setYuanSuoShuDi(String yuanSuoShuDi) {
		this.yuanSuoShuDi = yuanSuoShuDi;
	}

	public String getYuanSuoMianJi() {
		return yuanSuoMianJi;
	}

	public void setYuanSuoMianJi(String yuanSuoMianJi) {
		this.yuanSuoMianJi = yuanSuoMianJi;
	}

	public String getHaiZiShuLiang() {
		return haiZiShuLiang;
	}

	public void setHaiZiShuLiang(String haiZiShuLiang) {
		this.haiZiShuLiang = haiZiShuLiang;
	}

	public String getYueShouFei() {
		return yueShouFei;
	}

	public void setYueShouFei(String yueShouFei) {
		this.yueShouFei = yueShouFei;
	}

	public String getYuanSuoDiZhi() {
		return yuanSuoDiZhi;
	}

	public void setYuanSuoDiZhi(String yuanSuoDiZhi) {
		this.yuanSuoDiZhi = yuanSuoDiZhi;
	}

	public String getDianYaoXiaCiDate() {
		return dianYaoXiaCiDate;
	}

	public void setDianYaoXiaCiDate(String dianYaoXiaCiDate) {
		this.dianYaoXiaCiDate = dianYaoXiaCiDate;
	}

	public String getDianYaoGenJinJiLu() {
		return dianYaoGenJinJiLu;
	}

	public void setDianYaoGenJinJiLu(String dianYaoGenJinJiLu) {
		this.dianYaoGenJinJiLu = dianYaoGenJinJiLu;
	}

	public String getWeiCanHuiYuanYin() {
		return weiCanHuiYuanYin;
	}

	public void setWeiCanHuiYuanYin(String weiCanHuiYuanYin) {
		this.weiCanHuiYuanYin = weiCanHuiYuanYin;
	}

	public String getCanHuiWeiChengJiaoYuanYin() {
		return canHuiWeiChengJiaoYuanYin;
	}

	public void setCanHuiWeiChengJiaoYuanYin(String canHuiWeiChengJiaoYuanYin) {
		this.canHuiWeiChengJiaoYuanYin = canHuiWeiChengJiaoYuanYin;
	}

	public String getShouHuiYuanYin() {
		return shouHuiYuanYin;
	}

	public void setShouHuiYuanYin(String shouHuiYuanYin) {
		this.shouHuiYuanYin = shouHuiYuanYin;
	}

	public String getYiCanJiaHuiYi() {
		return yiCanJiaHuiYi;
	}

	public void setYiCanJiaHuiYi(String yiCanJiaHuiYi) {
		this.yiCanJiaHuiYi = yiCanJiaHuiYi;
	}

	public String getHuiXiaoGenJinJiLu() {
		return huiXiaoGenJinJiLu;
	}

	public void setHuiXiaoGenJinJiLu(String huiXiaoGenJinJiLu) {
		this.huiXiaoGenJinJiLu = huiXiaoGenJinJiLu;
	}

	public String getHuiXiaoXiaCiDate() {
		return huiXiaoXiaCiDate;
	}

	public void setHuiXiaoXiaCiDate(String huiXiaoXiaCiDate) {
		this.huiXiaoXiaCiDate = huiXiaoXiaCiDate;
	}

	public String getXinXiLaiYuan() {
		return xinXiLaiYuan;
	}

	public void setXinXiLaiYuan(String xinXiLaiYuan) {
		this.xinXiLaiYuan = xinXiLaiYuan;
	}

	public String getFangWenGuanJianZi() {
		return fangWenGuanJianZi;
	}

	public void setFangWenGuanJianZi(String fangWenGuanJianZi) {
		this.fangWenGuanJianZi = fangWenGuanJianZi;
	}

	public String getKeHuJiBie() {
		return keHuJiBie;
	}

	public void setKeHuJiBie(String keHuJiBie) {
		this.keHuJiBie = keHuJiBie;
	}

	public String getShiFouHeHuoTouZi() {
		return shiFouHeHuoTouZi;
	}

	public void setShiFouHeHuoTouZi(String shiFouHeHuoTouZi) {
		this.shiFouHeHuoTouZi = shiFouHeHuoTouZi;
	}

	public String getHeHuoRenName() {
		return heHuoRenName;
	}

	public void setHeHuoRenName(String heHuoRenName) {
		this.heHuoRenName = heHuoRenName;
	}

	public String getHeHuoRenPhone() {
		return heHuoRenPhone;
	}

	public void setHeHuoRenPhone(String heHuoRenPhone) {
		this.heHuoRenPhone = heHuoRenPhone;
	}

	public String getShiFouFaSongZiLiao() {
		return shiFouFaSongZiLiao;
	}

	public void setShiFouFaSongZiLiao(String shiFouFaSongZiLiao) {
		this.shiFouFaSongZiLiao = shiFouFaSongZiLiao;
	}

	public String getShiFouYouChangDI() {
		return shiFouYouChangDI;
	}

	public void setShiFouYouChangDI(String shiFouYouChangDI) {
		this.shiFouYouChangDI = shiFouYouChangDI;
	}

	public String getYuSuan() {
		return yuSuan;
	}

	public void setYuSuan(String yuSuan) {
		this.yuSuan = yuSuan;
	}

	public String getLiuYanDate() {
		return liuYanDate;
	}

	public void setLiuYanDate(String liuYanDate) {
		this.liuYanDate = liuYanDate;
	}

	public String getYuanSuoFenLei() {
		return yuanSuoFenLei;
	}

	public void setYuanSuoFenLei(String yuanSuoFenLei) {
		this.yuanSuoFenLei = yuanSuoFenLei;
	}

	public String getZhaoShangXiaCiDate() {
		return zhaoShangXiaCiDate;
	}

	public void setZhaoShangXiaCiDate(String zhaoShangXiaCiDate) {
		this.zhaoShangXiaCiDate = zhaoShangXiaCiDate;
	}

	public String getZhaoShangGenJinJiLu() {
		return zhaoShangGenJinJiLu;
	}

	public void setZhaoShangGenJinJiLu(String zhaoShangGenJinJiLu) {
		this.zhaoShangGenJinJiLu = zhaoShangGenJinJiLu;
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

	public void setStorehouse_baihuiid(String storehouse_baihuiid) {
		this.storehouse_baihuiid = storehouse_baihuiid;
	}

	public String getLeadNo() {
		return leadNo;
	}

	public void setLeadNo(String leadNo) {
		this.leadNo = leadNo;
	} 
	
	
}
