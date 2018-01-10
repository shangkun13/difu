package com.baihui.difu.service;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.baihui.difu.dao.SyncRecordTimeDAO;
import com.baihui.difu.entity.SyncRecordTimeDB;
import com.baihui.difu.util.Tools;

public class SyncRecordTimeService {
	public static Logger log = Logger.getLogger(SyncRecordTimeService.class);
	private SyncRecordTimeDAO syncRecordTimeDao = null;
	SyncRecordTimeDB syncRecordTime=null;
	/**
	 *
	 * 从数据库中获取开始同步时间时间/结束同步时间
	 * @param beginSyncTime 开始同步时间
	 * @param module 模块名称
	 * @return 返回上次同步的开始日期
	 * @throws Exception 
	 */
	public SyncRecordTimeDB getSyncRecordTimeByModule(String module) throws Exception{
		log.info("SyncRecordTimeService/getSyncRecordTimeByModule() method begin");
		SyncRecordTimeDB syncRecordTime=syncRecordTimeDao.getSyncRecordTimeByModule(module);
		log.info("SyncRecordTimeService/getSyncRecordTimeByModule() method end");
		return syncRecordTime;
	}
	/**
	 * 根据module修改数据库的这次和上次的同步时间
	 * @param beforeSync  同步前的时间
	 * @param afterSync 同步后的时间
	 * @throws SQLException
	 * @throws Exception
	 */
	public void updateTimeByModule(Timestamp beforeSync,Timestamp afterSync,String module) throws SQLException, Exception{
		log.info("SyncRecordTimeService/updateBeginSyncTimeByModule() method begin");
		log.info("beforeSync:[" + beforeSync + "]");
		log.info("afterSync:[" + afterSync + "]");
		log.info("module:[" + module + "]");
		syncRecordTimeDao = new SyncRecordTimeDAO();
		//获取上次修改时间
		Object [] param = new Object [3];
		param[0] = beforeSync;
		param[1] = afterSync;
		param[2] = module;
		//调用syncRecordTimeDao方法修改数据库
		syncRecordTimeDao.updateTimeByModule(param);
		log.info("SyncRecordTimeService/updateBeginSyncTimeByModule() method end");
	}
	
	
	/**
	 *  获取上次数据同步时间作为这次数据同步的时间
	 * @param module
	 * @return
	 */
	public String getLastSyncTime(String module){
		log.info("getLastSyncTime method begin!");
		String crmLastModifiedTime = "";
		// 查询数据库,获取SyncRecordTime对象信息
		try {
			syncRecordTimeDao = new SyncRecordTimeDAO();
			syncRecordTime = syncRecordTimeDao.getSyncRecordTimeByModule(module);
			log.info("syncRecordTime:[" + syncRecordTime + syncRecordTime + "]");
		} catch (Exception e) {
			log.info("getLastSyncTime method end!");
			log.error("ProductService/operate() Exception", e);
		}
		
		//判断syncRecordTime对象是否为空
		if(syncRecordTime != null){
			//获取数据库中的上次数据同步开始时间
			Date lastSyncTime = syncRecordTime.getLastSyncTime();
			// 获取上次数据同步时间作为这次数据同步的时间
			crmLastModifiedTime = Tools.getSyncDateStr(lastSyncTime); 
		}
		log.info("getLastSyncTime method end!");
		return crmLastModifiedTime;
		
	}
	
	/**
	 * 修改数据库同步时间
	 * @param currentstamp
	 * @param module
	 */
	public void updateSyncTime(Timestamp currentstamp,String module){
		log.info("SyncRecordTimeService/updateSyncTime(Timestamp currentstamp,String module) method begin!");
		// 修改同步时间
		try {
			updateTimeByModule(currentstamp, Tools.getNowTimestamp(), module);
		} catch (SQLException e) {
			log.error("SyncRecordTimeService/updateSyncTime SQLException",e);
		} catch (Exception e) {
			log.error("SyncRecordTimeService/updateSyncTime Exception",e);
		}finally{
			log.info("SyncRecordTimeService/updateSyncTime(Timestamp currentstamp,String module) method end!");
		}
	}

}
