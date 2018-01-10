package com.baihui.difu.biz;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.LeadsDao;
import com.baihui.difu.dao.UserDao;
import com.baihui.difu.entity.Leads;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Tools;
import com.baihui.difu.util.Util;

public class LeadsImpl {
	public static Logger log=Logger.getLogger(LeadsImpl.class);
	UserDao userDao=new UserDao();
	LeadsDao leadsDao=new LeadsDao(); 
	/**excelList中的数据可能有订单编号重复的,也就是同一个订单编号下面可能有多个产品.然后把这些相同订单编号的数据组装成一个order
	 *   多个order存放到List<Order>集合中.
	 * 3.返回List<Order>集合
	 * 
	 */
	public List<Leads> getLeadsByExcelList(List<List<String>> excelList,HttpServletRequest request)
			throws Exception {
		StringBuffer msg=new StringBuffer();
		log.info("PurchaseOrderImpl/getMyOrderByExcelList() method beginning!");
		log.info("excelList中的记录数:"+excelList.size());
		List<Leads> LeadList=new ArrayList<Leads>();
		//获取订单编号的集合set<String>
		Set<String> mobiles=getMobiles(excelList);
		log.info("一共有"+excelList.size()+"条线索");
		String totalCount=excelList.size()+"";
		String url=request.getSession().getServletContext().getRealPath("/");
		FileWriter fw = new FileWriter(url+"/Import.txt");    
        String s = "";  
        s = Tools.getNowTime()+"::: Excel中共有"+excelList.size()+"条数据";
		fw.write(s);
		fw.write("\r\n");
		int m=1;
		int count=0;//记录成功导入的记录数
		//拼接指定类型的线索
			List<String> getRow=new ArrayList<String>();
            for(int i=0;i<excelList.size();i++){
            	int co=i+2;
    			getRow=excelList.get(i);
    			String mobile = getRow.get(11);
    			String phone=getRow.get(10);
    			String email=getRow.get(0);
    			//如果电话号和手机号为空,则跳过此条数据,继续下一行数据
    			if("".equals(mobile)&&"".equals(phone)){
    				s = Tools.getNowTime()+"::: 第"+co+"行，手机号和电话号都为空，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
    			if(leadsDao.checkMobile(mobile)){//如果数据库中有对应的手机号
    				s = Tools.getNowTime()+"::: 第"+co+"行，已经存在手机号为"+mobile+"的数据，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
    			if(leadsDao.checkPhone(phone)){//如果数据库中有对应的电话号
    				s = Tools.getNowTime()+"::: 第"+co+"行，已经存在电话号为"+phone+"的数据，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
    			if("".equals(email)||email==null){
    				s = Tools.getNowTime()+"::: 第"+co+"行，所有者为空，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				// TODO 记录日志
    				continue;
    			}
    			String baihuiID=userDao.getbaihuiIDByEmail(email);
    			if("".equals(baihuiID)){
    				s = Tools.getNowTime()+"::: 第"+co+"行，邮箱为："+email+"没有对应的所有者，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
//    			if("".equals(getRow.get(4).trim())||"".equals(getRow.get(5).trim())){
//    				s = Tools.getNowTime()+"::: 第"+co+"行，公司和姓为必填项，过滤掉此条数据";
//    				fw.write(s);
//    				fw.write("\r\n");
//    				continue;
//    			}
    			Leads leads=new Leads();
    			leads.setOwnner(baihuiID);//所有者
    			leads.setShengFen(getRow.get(1));//省份
    			leads.setDiShi(getRow.get(2));
    			leads.setXian(getRow.get(3));
    			if("".equals(getRow.get(4).trim())){
    				leads.setGongSi("未知公司");
    			}else{
    				leads.setGongSi(getRow.get(4));
    			}
    			if("".equals(getRow.get(5).trim())){
    				leads.setName("未知姓名");
    			}else{
    				leads.setName(getRow.get(5));
    			}
    			leads.setSex(getRow.get(6));
    			leads.setAge(getRow.get(7));
    			leads.setZhiWei(getRow.get(8));
    			leads.setEmail(getRow.get(9));
    			leads.setPhone(getRow.get(10));
//    			leads.setMobile2(getRow.get(11));
//    			leads.setPhone2(getRow.get(12));
    			leads.setMobile(getRow.get(11));
    			leads.setQQ(getRow.get(12));
    			leads.setWeiXin(getRow.get(13));
    			leads.setXianSuoZhuangTai(getRow.get(14));
    			leads.setXianSuoFenLei(getRow.get(15));
    			leads.setDaiLiShang(getRow.get(16));
    			leads.setYuanSuoJianCheng(getRow.get(17));
    			leads.setYuanSuoXingZhi(getRow.get(18));
    			leads.setGuanXiDengJi(getRow.get(19));
    			leads.setKeHuLaiYuan(getRow.get(20));
    			leads.setKeHuBeiZhu(getRow.get(21));
    			leads.setYuanSuoShuDi(getRow.get(22));
    			leads.setYuanSuoMianJi(getRow.get(23));
    			leads.setHaiZiShuLiang(getRow.get(24));
    			leads.setYueShouFei(getRow.get(25));
    			leads.setYuanSuoDiZhi(getRow.get(26));
    			leads.setDianYaoXiaCiDate(getRow.get(27));
    			leads.setDianYaoGenJinJiLu(getRow.get(28));
    			leads.setWeiCanHuiYuanYin(getRow.get(29));
    			leads.setCanHuiWeiChengJiaoYuanYin(getRow.get(30));
    			leads.setShouHuiYuanYin(getRow.get(31));
    			leads.setYiCanJiaHuiYi(getRow.get(32));
    			leads.setHuiXiaoGenJinJiLu(getRow.get(33));
    			leads.setHuiXiaoXiaCiDate(getRow.get(34));
    			leads.setXinXiLaiYuan(getRow.get(35));
    			leads.setFangWenGuanJianZi(getRow.get(36));
    			leads.setKeHuJiBie(getRow.get(37));
    			leads.setShiFouHeHuoTouZi(getRow.get(38));
    			leads.setHeHuoRenName(getRow.get(39));
    			leads.setHeHuoRenPhone(getRow.get(40));
    			leads.setShiFouFaSongZiLiao(getRow.get(41));
    			leads.setShiFouYouChangDI(getRow.get(42));
    			leads.setYuSuan(getRow.get(43));
    			leads.setLiuYanDate(getRow.get(44));
    			leads.setYuanSuoFenLei(getRow.get(45));
    			leads.setZhaoShangXiaCiDate(getRow.get(46));
    			leads.setZhaoShangGenJinJiLu(getRow.get(47));
    			//组装所有的Lead订单
    			LeadList.add(leads);
    			
            }
//        }  
            count=LeadList.size();
            s = Tools.getNowTime()+"::: 一共成功导入了"+count+"条数据";
			fw.write(s);
			fw.write("\r\n");
        fw.flush();    
        fw.close();
        Util.message_normal=totalCount+","+count;
		log.info("PurchaseOrderImpl/getMyOrderByExcelList() method end!");
		return LeadList;
	}

	public void copyData(List<Leads> leadsList, int type, String email) {
		log.info("PurchaseOrderImpl/copyData() method beginning!");
		log.info("leadsList.size():["+leadsList.size()+"]");
		if( leadsList == null || leadsList.isEmpty() ){
			return;
		}
		insertContactToCrm(leadsList);
		log.info("PurchaseOrderImpl/copyData() method beginning!");
	
	
	}
	
	

    /**
     * 插入客户记录数据到CRM
     * 分批插入数据，最多一次插入100条
     * CREATE BY LIHUA
     */
    public void insertContactToCrm(List<Leads> list) {
        int count = list.size();
        int limtNum = Constants.DB2CRM_DO_UNIT_COUNT; //CRM限定一次性回写数量
        int page = count % limtNum == 0 ? count / limtNum : count / limtNum + 1;
        for (int i = 0; i < page; i++) {
            int insertNum = limtNum >= count - i * limtNum ? count - i * limtNum : limtNum;
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < insertNum; j++) {
            	Leads leads = list.get(i * limtNum + j);
                sb.append(getContactInsertXMLforCRM(leads, j + 1));
            }
            insertLeads(sb.toString());
        }
    }

/**
	 * 拼接Set<String>电话号码
	 * @param excelList
	 * @return
	 */
	
	public Set<String> getMobiles(List<List<String>> excelList){
		log.info("IMyOrder/getOrderNos() method beginning!");
		log.info("excelList.size():["+excelList.size()+"]");
		Set<String> Mobiles=new HashSet<String>();
		/**获取excelList中的第一层元素*/
		List<String> getRow=new ArrayList<String>();
		log.info("电话号码");
		for(int i=0;i<excelList.size();i++){
			/**获取一行数据*/
			getRow=excelList.get(i);
			/**获取电话号码,电话号码所在列是第14列*/
			String Mobile=getRow.get(13);
			/**将电话号码存入Set<String>集合*/
			Mobiles.add(Mobile);
			log.info("手机号"+i+":"+Mobiles);
		}
		log.info("IMyOrder/getOrderNos() method end!");
		return Mobiles;
		
	}
	
	 /**
     * 插入crm设置所有者为默认的
     * CREATE BY LIHUA
     */
    private String getContactInsertXMLforCRM(Leads leads, int rowIndex) {
        StringBuffer xmlStr = new StringBuffer("");
//        String gongsi = leads.getGongSi();
//        if ("".equals(gongsi)) {
//        	
//        }
        xmlStr.append(String.format("<row no='%s'>", rowIndex));
        xmlStr.append(String.format("<FL val='SMOWNERID'><![CDATA[%s]]></FL>", leads.getOwnner()));
        xmlStr.append(String.format("<FL val='Company'><![CDATA[%s]]></FL>", leads.getGongSi()));
        xmlStr.append(String.format("<FL val='Last Name'><![CDATA[%s]]></FL>", leads.getName()));
        xmlStr.append(String.format("<FL val='Email'><![CDATA[%s]]></FL>", leads.getEmail()));
        xmlStr.append(String.format("<FL val='Phone'><![CDATA[%s]]></FL>", leads.getPhone()));
        xmlStr.append(String.format("<FL val='Mobile'><![CDATA[%s]]></FL>", leads.getMobile()));
        if(leads.getMobile()!=null&&leads.getMobile().length()>7){
        	xmlStr.append(String.format("<FL val='手机'><![CDATA[%s]]></FL>", leads.getMobile().substring(0,7)+"****"));
        }
        if(leads.getPhone()!=null&&leads.getPhone().length()>7){
        	xmlStr.append(String.format("<FL val='电话'><![CDATA[%s]]></FL>", leads.getPhone().substring(0,7)+"****"));
        }
        xmlStr.append(String.format("<FL val='Lead Status'><![CDATA[%s]]></FL>", leads.getXianSuoZhuangTai()));
        // TODO 暂时没有导入供应商字段，因为暂无关联供应商模块需求
        xmlStr.append(String.format("<FL val='会销跟进记录'><![CDATA[%s]]></FL>", leads.getHuiXiaoGenJinJiLu()));
        xmlStr.append(String.format("<FL val='会销下次联系日期'><![CDATA[%s]]></FL>", leads.getHuiXiaoXiaCiDate()));
        xmlStr.append(String.format("<FL val='年龄'><![CDATA[%s]]></FL>", leads.getAge()));
        xmlStr.append(String.format("<FL val='访问关键字'><![CDATA[%s]]></FL>", leads.getFangWenGuanJianZi()));
        xmlStr.append(String.format("<FL val='客户级别'><![CDATA[%s]]></FL>", leads.getKeHuJiBie()));
        xmlStr.append(String.format("<FL val='已参加会议'><![CDATA[%s]]></FL>", leads.getYiCanJiaHuiYi()));
        xmlStr.append(String.format("<FL val='是否合伙投资'><![CDATA[%s]]></FL>", leads.getShiFouHeHuoTouZi()));
        xmlStr.append(String.format("<FL val='合伙人姓名'><![CDATA[%s]]></FL>", leads.getHeHuoRenName()));
        xmlStr.append(String.format("<FL val='信息来源'><![CDATA[%s]]></FL>", leads.getXinXiLaiYuan()));
        xmlStr.append(String.format("<FL val='是否发送资料'><![CDATA[%s]]></FL>", leads.getShiFouFaSongZiLiao()));
        xmlStr.append(String.format("<FL val='是否有场地'><![CDATA[%s]]></FL>", leads.getShiFouYouChangDI()));
        xmlStr.append(String.format("<FL val='投资预算'><![CDATA[%s]]></FL>", leads.getYuSuan()));
        xmlStr.append(String.format("<FL val='留言日期'><![CDATA[%s]]></FL>", leads.getLiuYanDate()));
        xmlStr.append(String.format("<FL val='线索分类'><![CDATA[%s]]></FL>", leads.getXianSuoFenLei()));
        xmlStr.append(String.format("<FL val='园所简称'><![CDATA[%s]]></FL>", leads.getYuanSuoJianCheng()));
        xmlStr.append(String.format("<FL val='园所性质'><![CDATA[%s]]></FL>", leads.getYuanSuoXingZhi()));
        xmlStr.append(String.format("<FL val='关系等级'><![CDATA[%s]]></FL>", leads.getGuanXiDengJi()));
        xmlStr.append(String.format("<FL val='客户来源'><![CDATA[%s]]></FL>", leads.getKeHuLaiYuan()));
        xmlStr.append(String.format("<FL val='省份'><![CDATA[%s]]></FL>", leads.getShengFen()));
        xmlStr.append(String.format("<FL val='地市'><![CDATA[%s]]></FL>", leads.getDiShi()));
        xmlStr.append(String.format("<FL val='客户备注'><![CDATA[%s]]></FL>", leads.getKeHuBeiZhu()));
        xmlStr.append(String.format("<FL val='性别'><![CDATA[%s]]></FL>", leads.getSex()));
        xmlStr.append(String.format("<FL val='QQ'><![CDATA[%s]]></FL>", leads.getQQ()));
        xmlStr.append(String.format("<FL val='园所属地'><![CDATA[%s]]></FL>", leads.getYuanSuoShuDi()));
        xmlStr.append(String.format("<FL val='电邀下次联系日期'><![CDATA[%s]]></FL>", leads.getDianYaoXiaCiDate()));
        xmlStr.append(String.format("<FL val='微信'><![CDATA[%s]]></FL>", leads.getWeiXin()));
        xmlStr.append(String.format("<FL val='电邀跟进记录'><![CDATA[%s]]></FL>", leads.getDianYaoGenJinJiLu()));
//        xmlStr.append(String.format("<FL val='手机2'><![CDATA[%s]]></FL>", leads.getMobile2()));
//        xmlStr.append(String.format("<FL val='电话2'><![CDATA[%s]]></FL>", leads.getPhone2()));
        xmlStr.append(String.format("<FL val='园所面积'><![CDATA[%s]]></FL>", leads.getYuanSuoMianJi()));
        xmlStr.append(String.format("<FL val='孩子数量'><![CDATA[%s]]></FL>", leads.getHaiZiShuLiang()));
        xmlStr.append(String.format("<FL val='月收费'><![CDATA[%s]]></FL>", leads.getYueShouFei()));
        xmlStr.append(String.format("<FL val='园所地址'><![CDATA[%s]]></FL>", leads.getYuanSuoDiZhi()));
        xmlStr.append(String.format("<FL val='未参会原因'><![CDATA[%s]]></FL>", leads.getWeiCanHuiYuanYin()));
        xmlStr.append(String.format("<FL val='参会未成交原因'><![CDATA[%s]]></FL>", leads.getCanHuiWeiChengJiaoYuanYin()));
        xmlStr.append(String.format("<FL val='职位'><![CDATA[%s]]></FL>", leads.getZhiWei()));
        xmlStr.append(String.format("<FL val='收回原因'><![CDATA[%s]]></FL>", leads.getShouHuiYuanYin()));
        xmlStr.append(String.format("<FL val='园所分类'><![CDATA[%s]]></FL>", leads.getYuanSuoFenLei()));
        xmlStr.append(String.format("<FL val='县'><![CDATA[%s]]></FL>", leads.getXian()));
        xmlStr.append(String.format("<FL val='招商下次联系时间'><![CDATA[%s]]></FL>", leads.getZhaoShangXiaCiDate()));
        xmlStr.append(String.format("<FL val='招商跟进记录'><![CDATA[%s]]></FL>", leads.getZhaoShangGenJinJiLu()));
        xmlStr.append(String.format("<FL val='合伙人手机'><![CDATA[%s]]></FL>", leads.getHeHuoRenPhone()));
        
        xmlStr.append("</row>");
        return xmlStr.toString();
    }
    /**
     * @param
     * @return
     * @throws
     * @Description: 插入客户记录数据
     */
    private void insertLeads(String xmlStr) {
        try {
            DataToCrm.insert(Constants.MODULE_LEADS, PropertyManager.getProperty("CRM_TOKEN"),
                    String.format("<Leads>%s</Leads>", xmlStr));
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }
    
}
