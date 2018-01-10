package com.baihui.difu.service;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.ContactsDao;
import com.baihui.difu.entity.Contacts;
import com.baihui.difu.util.BaseService;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Tools;


public class ContactsService {

	public static Logger log = Logger.getLogger(ContactsService.class);
	private SyncRecordTimeService syncRecordTimeService = null;
	private ContactsDao contactsDao = new ContactsDao();
	
	/**
	 * 从CRM中同步联系人数据到数据库
	 * @throws Exception 
	 */
	public void syncFromCRM() throws Exception {
		log.info("ContactService/syncFromCRM() method begin!");
		syncRecordTimeService = new SyncRecordTimeService();
		List<Map<String, String>> list = null;
		// 当前时间
		Timestamp currentstamp = Tools.getNowTimestamp();
		// 获取数据库中上次同步时间
		String crmLastModifiedTime = syncRecordTimeService
				.getLastSyncTime(Constants.MODULE_CONTACTS);
		log.warn("ContactService syncFromCRM crmLastModifiedTime:[" + crmLastModifiedTime + "]");
		// 根据最后修改时间,查询crm中记录总数
		try {
			list = DataToCrm.getAllData(Constants.MODULE_CONTACTS,
					PropertyManager.getProperty("CRM_TOKEN"),
					Constants.CRM_SELECTCOLUMNS_CONTACTS, crmLastModifiedTime);
		} catch (IOException e) {
			log.error("ContactService/syncFromCRM() IOException", e);
		} catch (JSONException e) {
			log.error("ContactService/syncFromCRM() IOException", e);
		} catch (InterruptedException e) {
			log.error("ContactService/syncFromCRM() IOException", e);
		}
		log.warn("本次需要从CRM同步的数据有    "+ (list == null?0:list.size()) +"   条");
		// 如果这次查询从crm中没有查到记录,那么依然记录这次的时间,并将其写入数据库中
		if (list == null || list.isEmpty()) {
			// 修改数据库同步时间
			syncRecordTimeService.updateSyncTime(currentstamp,
					Constants.MODULE_CONTACTS);
			return;
		}
		contactsDao = new ContactsDao();

		// 从crm中查询到的记录数
		int listLength = list.size();
		log.warn("ContactService syncFromCRM 从CRM中查询到的记录数:" + listLength);
		Map<String, Object[]> insertHashMap = new HashMap<String, Object[]>();
		Map<String, Object[]> updateHashMap = new HashMap<String, Object[]>();
		// 存放从CRM中查询到的具体某一条记录的集合
		Map<String, String> listMap = null;
		// 获取数据库的当前时间
		Timestamp sqlNow = null;
		//做统计是,有多少条数据没有处理
		int total = 0;
		// 遍历list集合,获取里面的元素
		for (int i = 0; i < listLength; i++) {
			listMap = list.get(i);
			// 获取来自CRM的baihuiId
			String baihuiIdFromCRM = listMap.get("CONTACTID");
			// 获取来自CRM的leadName
			String contactName = Tools.trim(listMap.get("Last Name"));
			// 获取来自CRM的mobile
			String mobile = Tools.trim(listMap.get("Mobile"));
			// 获取来自CRM的Phone
			String phone = Tools.trim(listMap.get("Phone"));
			String Email = Tools.trim(listMap.get("Email"));
			String shouji2=Tools.trim(listMap.get("手机2"));
			String context = Tools.trim(listMap.get("留言内容"));
			String suoshuxiangmu=Tools.trim(listMap.get("所属项目"));
			String owner = Tools.trim(listMap.get("SMOWNERID"));
			String ownerName = Tools.trim(listMap.get("Contact Owner"));
			String zyDate="";
			if(!"".equals(listMap.get("资源日期"))&&listMap.get("资源日期")!=null&&!"null".equals(listMap.get("资源日期"))){
			zyDate = Tools.trim(listMap.get("资源日期"));
			}else{
				zyDate=listMap.get("Created Time");
			}
			String creatTime = Tools.trim(listMap.get("Created Time"));
			String ModifiTime = Tools.trim(listMap.get("Modified Time"));
			log.warn("同步到第  " + i + "  条数据,联系人姓名:"+contactName+",手机号码:"+mobile+",电话:"+phone+"。");
			boolean isExists = contactsDao.checkContactsBaihuiId(baihuiIdFromCRM);
			Date crmModifiedDate = Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
					listMap.get("Modified Time"));
			if (isExists) {
				updateHashMap.put(baihuiIdFromCRM, new Object[] {contactName,mobile,phone,Email,shouji2,context,suoshuxiangmu,owner,ownerName,zyDate, baihuiIdFromCRM});
			}else{
				sqlNow = Tools.getNowTimestamp();
				Date crmCreateDate = (Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
						listMap.get("Created Time")));
				
				insertHashMap.put(baihuiIdFromCRM, new Object[] {contactName, mobile,phone,Email,shouji2,context,suoshuxiangmu,owner,ownerName,zyDate, baihuiIdFromCRM, 
						crmCreateDate, crmModifiedDate, sqlNow, sqlNow,1});
			}
		}
		
		//需要新增的数据的参数集合
		List<Object []> insertParamsList = new ArrayList<Object[]>();
		//需要修改的数据的参数集合
		List<Object []> updateParamsList = new ArrayList<Object[]>();
		insertParamsList.addAll(insertHashMap.values());
		updateParamsList.addAll(updateHashMap.values());
		log.warn("这次从CRM中同步的数据到DB,一共要同步"+listLength+"条;");
		log.warn("有"+total+"条数据在DB中已经存在,不做处理");
		log.warn("有"+(insertParamsList == null?0:insertParamsList.size())+"条数据需要新增到DB;");
		log.warn("有"+(updateParamsList==null?0:updateParamsList.size())+"条数据需要修改");
		// 批量处理
		BaseService baseService = new BaseService();
		baseService.handleDBResult(insertParamsList, updateParamsList,Constants.MODULE_CONTACTS, contactsDao);
		// 最后修改这次数据同步时间
		syncRecordTimeService.updateSyncTime(currentstamp,Constants.MODULE_CONTACTS);
		log.info("ContactService/syncFromCRM() method end!");
	}
	/**
	 * @描述 
		 * @项目名称 difu
		 * @类名称 ContactsService.java
		 * @作者 dennis
		 * @创建日期 2015-5-21 下午4:33:35
		 * @修改者 dennis
		 * @修改日期 2015-5-21 下午4:33:35
		 * @return
	 * @throws SQLException 
	 */
    public void syncFromDelToCRM(String id) throws SQLException {
    	if("".equals(id)||id==null){
    		System.out.println("ID为空"+id);
    		return;
    	}
    	 List<Contacts> insertCustomerCenters = new ArrayList<Contacts>();
    	 
    	 insertCustomerCenters=contactsDao.getContactsByBaihuiID(id);
    	 if(insertCustomerCenters.size()>0){
    		 insertContactToCrm(insertCustomerCenters);
    	 }
    }
    
    

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
                sb.append(getChongFuInsertXMLforCRM(contacts, j + 1));
            }
            insertContacts(sb.toString());
        }
    }
    
    
    /**
     * 插入crm设置所有者为默认的
     * CREATE BY LIHUA
     */
    private String getChongFuInsertXMLforCRM(Contacts contacts, int rowIndex) {
        StringBuffer xmlStr = new StringBuffer("");
        xmlStr.append(String.format("<row no='%s'>", rowIndex));
        xmlStr.append(String.format("<FL val='CustomModule5 Name'><![CDATA[%s]]></FL>", contacts.getName()));
        xmlStr.append(String.format("<FL val='手机'><![CDATA[%s]]></FL>", contacts.getMobile()));
        xmlStr.append(String.format("<FL val='留言内容'><![CDATA[%s]]></FL>", contacts.getContext()));
        xmlStr.append(String.format("<FL val='SMOWNERID'><![CDATA[%s]]></FL>", contacts.getOwnner()));
        xmlStr.append("</row>");
        return xmlStr.toString();
    }
    /**
     * @param
     * @return
     * @throws
     * @Description: 插入重复的客户数据
     */
    private void insertContacts(String xmlStr) {
        try {
            DataToCrm.insert(Constants.MODULE_CHONGFU, PropertyManager.getProperty("CRM_TOKEN"),
                    String.format("<CustomModule5>%s</CustomModule5>", xmlStr));
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }
    
    public List<Contacts> getChongFuIds(String baihuiID,String mobile,String mobile2,String phone,String suoshuxiangmu) throws SQLException{
    	return contactsDao.getChongFuIds(baihuiID,mobile, mobile2, phone,suoshuxiangmu);
    }
}
