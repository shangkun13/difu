package com.baihui.difu.biz;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.ContactsDao;
import com.baihui.difu.dao.LeadsDao;
import com.baihui.difu.dao.UserDao;
import com.baihui.difu.entity.Contacts;
import com.baihui.difu.entity.Leads;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Tools;
import com.baihui.difu.util.Util;

public class ContactsImpl {
	public static Logger log=Logger.getLogger(ContactsImpl.class);
//	UserDao userDao=new UserDao();
//	LeadsDao leadsDao=new LeadsDao(); 
	ContactsDao contactsDao=new ContactsDao(); 
	/**excelList中的数据可能有订单编号重复的,也就是同一个订单编号下面可能有多个产品.然后把这些相同订单编号的数据组装成一个order
	 *   多个order存放到List<Order>集合中.
	 * 3.返回List<Order>集合
	 * 
	 */
	public List<Contacts> getContactsByExcelList(List<List<String>> excelList,HttpServletRequest request)
			throws Exception {
		StringBuffer msg=new StringBuffer();
		log.info("PurchaseOrderImpl/getMyOrderByExcelList() method beginning!");
		log.info("excelList中的记录数:"+excelList.size());
		List<Contacts> ContactsList=new ArrayList<Contacts>();
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
			System.out.println("eeeee:"+excelList.size());
            for(int i=0;i<excelList.size();i++){
            	int co=i+2;
    			getRow=excelList.get(i);
    			String mobile = getRow.get(2);
    			//如果电话号为空,则跳过此条数据,继续下一行数据
    			if("".equals(mobile)){
    				s = Tools.getNowTime()+"::: 第"+co+"行，手机号为空，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
    			if(contactsDao.checkMobile(mobile)){//如果数据库中有对应的手机号
    				s = Tools.getNowTime()+"::: 第"+co+"行，已经存在手机号为"+mobile+"的数据，过滤掉此条数据";
    				fw.write(s);
    				fw.write("\r\n");
    				continue;
    			}
//    			if(leadsDao.checkPhone(phone)){//如果数据库中有对应的电话号
//    				s = Tools.getNowTime()+"::: 第"+co+"行，已经存在电话号为"+phone+"的数据，过滤掉此条数据";
//    				fw.write(s);
//    				fw.write("\r\n");
//    				continue;
//    			}
//    			if("".equals(email)||email==null){
//    				s = Tools.getNowTime()+"::: 第"+co+"行，所有者为空，过滤掉此条数据";
//    				fw.write(s);
//    				fw.write("\r\n");
//    				// TODO 记录日志
//    				continue;
//    			}
//    			String baihuiID=userDao.getbaihuiIDByEmail(email);
//    			if("".equals(baihuiID)){
//    				s = Tools.getNowTime()+"::: 第"+co+"行，邮箱为："+email+"没有对应的所有者，过滤掉此条数据";
//    				fw.write(s);
//    				fw.write("\r\n");
//    				continue;
//    			}
//    			if("".equals(getRow.get(4).trim())||"".equals(getRow.get(5).trim())){
//    				s = Tools.getNowTime()+"::: 第"+co+"行，公司和姓为必填项，过滤掉此条数据";
//    				fw.write(s);
//    				fw.write("\r\n");
//    				continue;
//    			}
    			Contacts contacts=new Contacts();
    			contacts.setLiuYanTime(getRow.get(0));
    			contacts.setMobile(mobile);
    			contacts.setEmail(getRow.get(3));
    			contacts.setQQ(getRow.get(4));
    			contacts.setPinPai(getRow.get(5));
    			contacts.setAddress(getRow.get(6));
    			contacts.setKeywords(getRow.get(7));
    			contacts.setContext(getRow.get(8));
    			contacts.setWebsite(getRow.get(9));
    			if("".equals(getRow.get(1).trim())){
    				contacts.setName("未知姓名");
    			}else{
    				contacts.setName(getRow.get(1));
    			}
    			//组装所有的Lead订单
    			ContactsList.add(contacts);
    			
            }
//        }  
            count=ContactsList.size();
            s = Tools.getNowTime()+"::: 一共成功导入了"+count+"条数据";
			fw.write(s);
			fw.write("\r\n");
        fw.flush();    
        fw.close();
        Util.message_normal=totalCount+","+count;
		log.info("PurchaseOrderImpl/getMyOrderByExcelList() method end!");
		return ContactsList;
	}

//	public String updataContacts(List<Contacts> contactList) throws SQLException, IOException, InterruptedException {
//		log.info("ContactsImpl/updataLeads() method beginning!");
//		log.info("leadsList():["+contactList.size()+"]");
//		if( contactList == null || contactList.isEmpty() ){
//			return "没有要修改的联系人]";
//		}
//		
//		StringBuffer names=new StringBuffer("");
//		StringBuffer filter=new StringBuffer("");
//		for(int i=0;i<contactList.size();i++){
//			Contacts contact=contactList.get(i);
//			String baihuiId=contactsDao.getContactBaihuiIdByContactNo(contact.getContactNo());
//			if(baihuiId.length()==0){
//				filter.append(contact.getContactNo());
//				continue;
//			}
//			boolean flag=contactsDao.checkMobile(contact.getMobile());
////			if(flag){//如果数据库中有对应的手机号
////				filter.append(contact.getContactNo()+"  ");
////			}
//			StringBuffer xmlStr = new StringBuffer("");
//	    	xmlStr.append(String.format("<row no='%s'>", 1));
//			if(contact.getMobile()!=null&&!flag){
//				xmlStr.append("<FL val='Mobile'><![CDATA[").append(contact.getMobile()).append("]]></FL>");
//				xmlStr.append("<FL val='手机'><![CDATA[").append(contact.getMobile()).append("]]></FL>");
//			}
//			if(contact.getPhone()!=null){
//				xmlStr.append("<FL val='Phone'><![CDATA[").append(contact.getPhone()).append("]]></FL>");
//				xmlStr.append("<FL val='电话'><![CDATA[").append(contact.getPhone()).append("]]></FL>");
//			}
//	    	xmlStr.append("</row>");
//	    	if(xmlStr.toString().contains("<FL val=")){
//	    		DataToCrm.update(Constants.MODULE_CONTACTS, PropertyManager.getProperty("CRM_TOKEN"), String.format("<Contacts>%s</Contacts>", xmlStr.toString()), baihuiId);
//	    		names.append(contact.getContactNo()+"  ");
//	    	}
//		}
//	
//		log.info("ContactsImpl/updataContacts() method end!");
//		return names.toString()+"];过滤掉的数据：["+filter+"]";
//	}
//	

	

    /**
     * 插入客户记录数据到CRM
     * 分批插入数据，最多一次插入100条
     * CREATE BY LIHUA
     */
    public void insertContactToCrm(List<Contacts> list) {
        int count = list.size();
        int limtNum = Constants.DB2CRM_DO_UNIT_COUNT; //CRM限定一次性回写数量
        int page = count % limtNum == 0 ? count / limtNum : count / limtNum + 1;
        for (int i = 0; i < page; i++) {
            int insertNum = limtNum >= count - i * limtNum ? count - i * limtNum : limtNum;
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < insertNum; j++) {
            	Contacts contacts = list.get(i * limtNum + j);
                sb.append(getContactInsertXMLforCRM(contacts, j + 1));
            }
            insertContacts(sb.toString());
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
			/**获取电话号码,电话号码所在列是第3列*/
			String Mobile=getRow.get(2);
			/**将电话号码存入Set<String>集合*/
			Mobiles.add(Mobile);
			log.info("手机号"+i+":"+Mobiles);
		}
		log.info("IMyOrder/getOrderNos() method end!");
		return Mobiles;
		
	}
	
	 
    /**
     * @param
     * @return
     * @throws
     * @Description: 插入客户记录数据
     */
    private void insertContacts(String xmlStr) {
        try {
            DataToCrm.insert(Constants.MODULE_CONTACTS, PropertyManager.getProperty("CRM_TOKEN"),
                    String.format("<Contacts>%s</Contacts>", xmlStr));
        } catch (Exception e) {
            log.error("Exception:", e);
        } 
    }
    public void copyData(List<Contacts> contactsList, int type, String email) {
		log.info("PurchaseOrderImpl/copyData() method beginning!");
		log.info("leadsList.size():["+contactsList.size()+"]");
		if( contactsList == null || contactsList.isEmpty() ){
			return;
		}
		insertContactToCrm(contactsList);
		log.info("PurchaseOrderImpl/copyData() method beginning!");
	
	
	}
    
    /**
     * 插入crm设置所有者为默认的
     * CREATE BY LIHUA
     */
    private String getContactInsertXMLforCRM(Contacts contacts, int rowIndex) {
        StringBuffer xmlStr = new StringBuffer("");
//        String gongsi = leads.getGongSi();
//        if ("".equals(gongsi)) {
//        	
//        }
        xmlStr.append(String.format("<row no='%s'>", rowIndex));
        xmlStr.append(String.format("<FL val='Last Name'><![CDATA[%s]]></FL>", contacts.getName()));
        xmlStr.append(String.format("<FL val='Email'><![CDATA[%s]]></FL>", contacts.getEmail()));
        xmlStr.append(String.format("<FL val='Phone'><![CDATA[%s]]></FL>", contacts.getPhone()));
        xmlStr.append(String.format("<FL val='Mobile'><![CDATA[%s]]></FL>", contacts.getMobile()));
        xmlStr.append(String.format("<FL val='QQ'><![CDATA[%s]]></FL>", contacts.getQQ()));
        xmlStr.append(String.format("<FL val='咨询品牌'><![CDATA[%s]]></FL>", contacts.getPinPai()));
        xmlStr.append(String.format("<FL val='地址'><![CDATA[%s]]></FL>", contacts.getAddress()));
        xmlStr.append(String.format("<FL val='来源关键词'><![CDATA[%s]]></FL>", contacts.getKeywords()));
        xmlStr.append(String.format("<FL val='留言内容'><![CDATA[%s]]></FL>", contacts.getContext()));
        xmlStr.append(String.format("<FL val='来源网站'><![CDATA[%s]]></FL>", contacts.getWebsite()));
        xmlStr.append(String.format("<FL val='创建时间'><![CDATA[%s]]></FL>", contacts.getLiuYanTime()));
        xmlStr.append("</row>");
        return xmlStr.toString();
    }

}
