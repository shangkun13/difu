package com.baihui.difu.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.LeadsDao;
import com.baihui.difu.entity.Leads;
import com.baihui.difu.util.BaseService;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Tools;


public class LeadsService {

	public static Logger log = Logger.getLogger(LeadsService.class);
	private SyncRecordTimeService syncRecordTimeService = null;
	private LeadsDao leadsDao = null;
	private Leads leads = null;
	private List<Leads> LeadsList = null;
	
	public void syncAllData() throws Exception{
		log.info("ContactService/syncFromCRM() method begin!");
		syncRecordTimeService = new SyncRecordTimeService();
		// 当前时间
		Timestamp currentstamp = Tools.getNowTimestamp();
		// 根据最后修改时间,查询crm中记录总数
		DataToCrm.syncAllData(Constants.MODULE_LEADS,
				PropertyManager.getProperty("CRM_TOKEN"),
				Constants.CRM_SELECTCOLUMNS_LEADS);
		syncRecordTimeService.updateSyncTime(currentstamp,
				Constants.MODULE_LEADS);
	}
	/**
	 * 从CRM中同步线索数据到数据库
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
				.getLastSyncTime(Constants.MODULE_LEADS);
		log.warn("ContactService syncFromCRM crmLastModifiedTime:[" + crmLastModifiedTime + "]");
		// 根据最后修改时间,查询crm中记录总数
		try {
			list = DataToCrm.getAllData(Constants.MODULE_LEADS,
					PropertyManager.getProperty("CRM_TOKEN"),
					Constants.CRM_SELECTCOLUMNS_LEADS, crmLastModifiedTime);
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
					Constants.MODULE_LEADS);
			return;
		}
		leadsDao = new LeadsDao();

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
			String baihuiIdFromCRM = listMap.get("LEADID");
			// 获取来自CRM的leadName
			String leadName = Tools.trim(listMap.get("Last Name"));
			// 获取来自CRM的mobile
			String mobile = Tools.trim(listMap.get("Mobile"));
			// 获取来自CRM的Phone
			String phone = Tools.trim(listMap.get("Phone"));
			// 获取来自CRM的线索编号
			String leadNo = Tools.trim(listMap.get("线索编号"));
			log.warn("同步到第  " + i + "  条数据,客户名:"+leadName+",手机号码:"+mobile+",电话:"+phone+",线索编号:"+leadNo+"。");
			boolean isExists = leadsDao.checkLeadsBaihuiId(baihuiIdFromCRM);
			if (isExists) {
				updateHashMap.put(baihuiIdFromCRM, new Object[] {mobile,phone,leadNo, baihuiIdFromCRM});
			}else{
				sqlNow = Tools.getNowTimestamp();
				Date crmCreateDate = (Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
						listMap.get("Created Time")));
				Date crmModifiedDate = Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
						listMap.get("Modified Time"));
				insertHashMap.put(baihuiIdFromCRM, new Object[] {leadName, mobile,phone,leadNo, baihuiIdFromCRM, 
						crmCreateDate, crmModifiedDate, sqlNow, sqlNow});
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
		baseService.handleDBResult(insertParamsList, updateParamsList,Constants.MODULE_LEADS, leadsDao);
		// 最后修改这次数据同步时间
		syncRecordTimeService.updateSyncTime(currentstamp,Constants.MODULE_LEADS);
		log.info("ContactService/syncFromCRM() method end!");
	}

}
