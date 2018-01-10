package com.baihui.difu.dao;



import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.baihui.difu.entity.SyncRecordTimeDB;




/**
 * @author jason
 */
public class SyncRecordTimeDAO extends BaseDAO{

	
	public static Logger log = Logger.getLogger(SyncRecordTimeDAO.class);

	
	/**
	 *
	 * 从数据库中获取开始同步时间时间/结束同步时间
	 * @param beginSyncTime 开始同步时间
	 * @param module 模块名称
	 * @return 返回上次同步的开始日期
	 * @throws Exception 
	 */
	public SyncRecordTimeDB getSyncRecordTimeByModule(String module) throws Exception{
		log.info("SyncRecordTimeDao/getSyncRecordTimeByModule() method begin");
		log.info("module:["+module+"]");
		SyncRecordTimeDB syncRecordTime = null;
		String sql = "select id, module, lastSyncTime, thisSyncTime, createdTime, modifiedTime, description from syncrecordtime where module=?";
		try{
			rs = super.sqlQuery(sql, module);
			while(rs.next()){
				syncRecordTime = new SyncRecordTimeDB();
				syncRecordTime.setId(rs.getInt("id"));
				syncRecordTime.setModule(rs.getString("module"));
				syncRecordTime.setLastSyncTime(rs.getTimestamp("lastSyncTime"));
				syncRecordTime.setThisSyncTime(rs.getTimestamp("thisSyncTime"));
				syncRecordTime.setCreatedTime(rs.getTimestamp("createdTime"));
				syncRecordTime.setModifiedTime(rs.getTimestamp("modifiedTime"));
				syncRecordTime.setDescription(rs.getString("description"));
			} 
		} finally {
			closeAll();
		}
		log.info("SyncRecordTimeDao/getSyncRecordTimeByModule() method end");
		return syncRecordTime;
	}

	
	
	/**
	 * 根据module修改数据库的这次和上次的同步时间
	 * @param params 查询的参数集合
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	
	public int updateTimeByModule(Object...params) throws SQLException, Exception{
		
		String sql = "update syncrecordtime set lastSyncTime=?,thisSyncTime=? where module=?" ;
		return super.sqlExecute(sql, params);
	}
	
	
}
