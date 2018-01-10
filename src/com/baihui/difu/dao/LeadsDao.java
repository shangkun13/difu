package com.baihui.difu.dao;

import java.sql.SQLException;
import java.util.List;

import com.baihui.difu.util.Constants;
import com.baihui.difu.util.Tools;

public class LeadsDao  extends BaseDAO {

	public boolean checkLeadsBaihuiId(String baihuiId) throws SQLException{
		log.info("ContactDAO/checkLeadsBaihuiId(baihuiId) method begin!");
		boolean result = false;
		String sql = "select count(*) from Leads where STATUS=? and BAIHUIID=?";
		Object [] params = new String [] {Constants.BRANCH_FLAG1_YES, baihuiId};
		try {
			rs = super.sqlQuery(sql, params);
			if(rs.next()){
				result = rs.getInt(1) >= 1 ? true : false;
			}
		} catch (SQLException e) {
			result = false;
			log.error("ContactDAO/checkLeadsBaihuiId(baihuiId) SQLException",e);
		} catch (Exception e) {
			result = false;
			log.error("ContactDAO/checkLeadsBaihuiId(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("ContactDAO/checkLeadsBaihuiId(baihuiId) method end!");
		}
		return result;
	} 
	
	/**
	 * 批量插入数据到数据库中
	 * @param args objectArrList参数项分别是
	 * contactName, mobile, phone, baihuiId, crmCreatedTime, crmLastModifiedTime, createdTime, modifiedTime
	 */
	public int[] insert( List<Object[]> objectArrList) throws Exception{
		log.info("ContactDAO/insert(List<Object[]> objectArrList) method begin!");
		log.info("objectArrList:["+objectArrList+"]");
		int[] results;
		String sql = "insert into Leads(NAME,MOBILE,PHONE,LEADNO,BAIHUIID," +
				" CRM_CREATED_TIME, CRM_MODIFIED_TIME, CREATEDTIME,MODIFIEDTIME) values " +
				" (?,?,?,?,?,?,?,?,?)";
		log.info("sql:[" + sql +"]");
		results = super.sqlBatchExecute(sql, objectArrList);
		log.warn("新增线索数据");
		log.info("results:["+results+"]");
		log.info("ContactDAO/insert(List<Object[]> objectArrList) method end!");
		return results;
	}
	/**
	 * 根据  客户名+手机+电话  批量修改baihuiId
	 * @param args  objectArrList 参数的类型分别是
	 *  String baihuiId,String contactName,String mobile,String phone
	 *  return 受影响的行数
	 * @throws Exception 
	 */
 	public int[] update(List<Object[]> objectArrList) throws Exception{
		log.info("ContactDAO/update(List<Object[]> objectArrList) method begin!");
		log.info("objectArrList:["+objectArrList+"]");
		int[] results=null;
		//老数据
		//String sql="update contacts set baihuiId = ? where status = 1 and contactName=? and mobile=? and phone=?";
		//新数据
		String sql="update Leads set MOBILE=?,PHONE=?,LEADNO=?,MODIFIEDTIME = '"+Tools.getNowTimestamp()+"' where STATUS = 1 and BAIHUIID = ?";
		log.info("sql:[" + sql + "]");
		results=super.sqlBatchExecute(sql, objectArrList);
		log.warn("修改数据的结果:"+results+",<br />此处进行可分页处理,分页后一共有"+results.length +"条");
		log.info("ContactDAO/update(List<Object[]> objectArrList) method end!");
		return results;
	}
	
 	public boolean checkMobile(String mobile) throws SQLException{
		log.info("ContactDAO/checkMobile(mobile) method begin!");
		boolean result = false;
		String sql = "select count(*) from Leads where STATUS=? and MOBILE=? and MOBILE is not null and MOBILE != '' ";
		Object [] params = new String [] {Constants.BRANCH_FLAG1_YES, mobile};
		try {
			rs = super.sqlQuery(sql, params);
			if(rs.next()){
				result = rs.getInt(1) >= 1 ? true : false;
			}
		} catch (SQLException e) {
			result = false;
			log.error("ContactDAO/checkLeadsBaihuiId(baihuiId) SQLException",e);
		} catch (Exception e) {
			result = false;
			log.error("ContactDAO/checkLeadsBaihuiId(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("ContactDAO/checkLeadsBaihuiId(baihuiId) method end!");
		}
		return result;
	} 
 	public boolean checkPhone(String phone) throws SQLException{
		log.info("ContactDAO/checkMobile(phone) method begin!");
		boolean result = false;
		String sql = "select count(*) from Leads where STATUS=? and PHONE=? and PHONE is not null and PHONE != '' ";
		Object [] params = new String [] {Constants.BRANCH_FLAG1_YES, phone};
		try {
			rs = super.sqlQuery(sql, params);
			if(rs.next()){
				result = rs.getInt(1) >= 1 ? true : false;
			}
		} catch (SQLException e) {
			result = false;
			log.error("ContactDAO/checkPhone(baihuiId) SQLException",e);
		} catch (Exception e) {
			result = false;
			log.error("ContactDAO/checkPhone(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("ContactDAO/checkPhone(baihuiId) method end!");
		}
		return result;
	} 
 	/**
	 * 根据  客户名+手机+电话  批量修改baihuiId
	 * @param args  objectArrList 参数的类型分别是
	 *  String baihuiId,String contactName,String mobile,String phone
	 *  return 受影响的行数
	 * @throws Exception 
	 */
 	public int deleteByBaiHuiID(String baihuiID) throws Exception{
		log.info("deleteByBaiHuiID(String baihuiID) method begin!");
		int results=0;
		String sql="update Leads set STATUS=0 where BAIHUIID = ?";
		log.info("sql:[" + sql + "]");
		results=super.sqlExecute(sql, baihuiID);
		log.info("ContactDAO/update(List<Object[]> objectArrList) method end!");
		return results;
	}

	public String getLeadBaihuiIdByLeadNo(String leadNo) throws SQLException {
		String baihuiId="";
		String sql="select baihuiid from Leads  where leadNo = ?";
		try {
			rs = super.sqlQuery(sql, leadNo);
			if(rs.next()){
				baihuiId = rs.getString(1);
			}
		} catch (SQLException e) {
			log.error("LeadDAO/checkLeadsBaihuiId(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("LeadDAO/checkLeadsBaihuiId(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("LeadDAO/checkLeadsBaihuiId(baihuiId) method end!");
		}
		return baihuiId;
	}
}
