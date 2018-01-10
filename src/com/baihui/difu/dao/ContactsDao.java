package com.baihui.difu.dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baihui.difu.entity.Contacts;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.Tools;

public class ContactsDao  extends BaseDAO {

	public boolean checkContactsBaihuiId(String baihuiId) throws SQLException{
		log.info("ContactDAO/checkLeadsBaihuiId(baihuiId) method begin!");
		boolean result = false;
		String sql = "select count(*) from contacts where STATUS=? and BAIHUIID=?";
		Object [] params = new String [] {Constants.BRANCH_FLAG1_YES, baihuiId};
		try {
			rs = super.sqlQuery(sql, params);
			if(rs.next()){
				result = rs.getInt(1) >= 1 ? true : false;
			}
		} catch (SQLException e) {
			result = false;
			log.error("ContactDAO/checkContactsBaihuiId(baihuiId) SQLException",e);
		} catch (Exception e) {
			result = false;
			log.error("ContactDAO/checkContactsBaihuiId(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("ContactDAO/checkContactsBaihuiId(baihuiId) method end!");
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
		String sql = "insert into Contacts(NAME,MOBILE,PHONE,EMAIL,MOBILE2,CONTEXT,SUOSHUXIANGMU,OWNNER,OWNNER_NAME,ZY_DATE,BAIHUIID," +
				" CRM_CREATED_TIME, CRM_MODIFIED_TIME, CREATEDTIME,MODIFIEDTIME,STATUS) values " +
				" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info("sql:[" + sql +"]");
		results = super.sqlBatchExecute(sql, objectArrList);
		log.warn("新增客户数据");
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
		String sql="update Contacts set NAME=?,MOBILE=?,PHONE=?,EMAIL=?,MOBILE2=?,CONTEXT=?,SUOSHUXIANGMU=?,OWNNER=?,OWNNER_NAME=?,ZY_DATE=?,MODIFIEDTIME = '"+Tools.getNowTimestamp()+"' where STATUS = 1 and BAIHUIID = ?";
		log.info("sql:[" + sql + "]");
		results=super.sqlBatchExecute(sql, objectArrList);
		log.warn("修改数据的结果:"+results+",<br />此处进行可分页处理,分页后一共有"+results.length +"条");
		log.info("ContactDAO/update(List<Object[]> objectArrList) method end!");
		return results;
	}
	
 	public boolean checkMobile(String mobile) throws SQLException{
		log.info("ContactDAO/checkMobile(mobile) method begin!");
		boolean result = false;
		String sql = "select count(*) from Contacts where STATUS=? and MOBILE=? and MOBILE is not null and MOBILE != '' ";
		Object [] params = new String [] {Constants.BRANCH_FLAG1_YES, mobile};
		try {
			rs = super.sqlQuery(sql, params);
			if(rs.next()){
				result = rs.getInt(1) >= 1 ? true : false;
			}
		} catch (SQLException e) {
			result = false;
			log.error("ContactDAO/checkContactsBaihuiId(baihuiId) SQLException",e);
		} catch (Exception e) {
			result = false;
			log.error("ContactDAO/checkContactsBaihuiId(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("ContactDAO/checkContactsBaihuiId(baihuiId) method end!");
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
		String sql="update Contacts set STATUS=0 where BAIHUIID = ?";
		log.info("sql:[" + sql + "]");
		results=super.sqlExecute(sql, baihuiID);
		log.info("ContactDAO/update(List<Object[]> objectArrList) method end!");
		return results;
	}

	public String getContactBaihuiIdByContactNo(String contactNo) throws SQLException {
		String baihuiId="";
		String sql="select baihuiid from contacts  where contactNo = ?";
		try {
			rs = super.sqlQuery(sql, contactNo);
			if(rs.next()){
				baihuiId = rs.getString(1);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return baihuiId;
	}

	public List<Contacts> queryAllByPage(int beginPage, int count) throws SQLException {
		List<Contacts> list=new ArrayList<Contacts>();
		String sql="select a.SUOSHUXIANGMU,a.ID,a.NAME,a.MOBILE,a.MOBILE2,a.PHONE,a.EMAIL,a.ZY_DATE,a.OWNNER,a.BAIHUIID,a.CRM_CREATED_TIME,a.CRM_MODIFIED_TIME,a.CREATEDTIME,a.MODIFIEDTIME,a.OWNNER_NAME,a.CONTEXT  from (select t.* from((select a.* from contacts a ,contacts b where ((a.MOBILE IS NOT null and a.MOBILE!='' ) and (b.MOBILE2 is not null and b.MOBILE2!=''))and a.MOBILE=b.MOBILE2 AND a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1) UNION (select a.* from contacts a ,contacts b where ((a.MOBILE2 IS NOT null and a.MOBILE2!='' ) and (b.MOBILE is not null and b.MOBILE!=''))and a.MOBILE2=b.MOBILE and a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1)) t UNION(SELECT * from contacts t where (MOBILE!='' and MOBILE is not NULL) and EXISTS (SELECT MOBILE from contacts t1 where t1.MOBILE=t.MOBILE and  t1.STATUS=1 GROUP BY MOBILE,t1.SUOSHUXIANGMU HAVING COUNT(MOBILE)>1)   order by MOBILE)UNION(SELECT * from contacts t where (MOBILE2!='' and MOBILE2 is not NULL) and EXISTS (SELECT MOBILE2 from contacts t1 where t1.MOBILE2=t.MOBILE2 and  t1.STATUS=1 GROUP BY MOBILE2 , t1.SUOSHUXIANGMU HAVING COUNT(MOBILE2)>1) )UNION(SELECT * from contacts t where  (PHONE!='' and PHONE is not NULL) and EXISTS (SELECT PHONE from contacts t1 where t1.PHONE=t.PHONE and  t1.STATUS=1  GROUP BY PHONE , t1.SUOSHUXIANGMU HAVING COUNT(PHONE)>1) ) ) a where a.status=1  ORDER BY a.MOBILE,a.MOBILE2,a.PHONE LIMIT  "+beginPage+","+count;
		//String sql="select ID,NAME,t.MOBILE,PHONE,EMAIL,OWNNER,BAIHUIID,CRM_CREATED_TIME,CRM_MODIFIED_TIME,CREATEDTIME,MODIFIEDTIME,OWNNER_NAME,CONTEXT from contacts t  , (select MOBILE from contacts  where STATUS = 1  and MOBILE!='' GROUP BY MOBILE  HAVING COUNT(ID)>1 order by MOBILE limit 0,100 ) s where  t.MOBILE=s.MOBILE and  STATUS = 1 order by t.MOBILE desc limit "+beginPage+","+count;
		System.out.println("queryAllByPageql::::::::::"+sql);
		try {
			rs = super.sqlQuery(sql);
			while(rs.next()){
				Contacts contact=new Contacts();
				contact.setId(rs.getString("ID"));
				contact.setName(rs.getString("NAME"));
				contact.setMobile(rs.getString("MOBILE"));
				contact.setShouji2(rs.getString("MOBILE2"));
				contact.setPhone(rs.getString("PHONE"));
				contact.setEmail(rs.getString("EMAIL"));
				contact.setOwnner(rs.getString("OWNNER"));
				contact.setBaihuiid(rs.getString("BAIHUIID"));
				contact.setbCreatedTime(rs.getDate("CRM_CREATED_TIME"));
				contact.setbModifiedTime(rs.getDate("CRM_MODIFIED_TIME"));
				contact.setCreateTime(rs.getDate("CREATEDTIME"));
				contact.setModifiedTime(rs.getDate("MODIFIEDTIME"));
				contact.setOwnnerName(rs.getString("OWNNER_NAME"));
				contact.setContext(rs.getString("CONTEXT"));
				contact.setZyDate(rs.getString("ZY_DATE"));
				contact.setSuoshuxiangmu(rs.getString("SUOSHUXIANGMU"));
				list.add(contact);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return list;
	}
	
	public List<Contacts> queryAllByPageToday(int beginPage, int count) throws SQLException {
		List<Contacts> list=new ArrayList<Contacts>();
		Date today=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 如果需要小时、分钟和秒则用字符串"yyyy-MM-dd HH:mm:ss"
		String strDate = df.format(today);
		String sql="select a.SUOSHUXIANGMU,a.ID,a.NAME,a.MOBILE,a.MOBILE2,a.PHONE,a.EMAIL,a.ZY_DATE,a.OWNNER,a.BAIHUIID,a.CRM_CREATED_TIME,a.CRM_MODIFIED_TIME,a.CREATEDTIME,a.MODIFIEDTIME,a.OWNNER_NAME,a.CONTEXT  from (select t.* from((select a.* from contacts a ,contacts b where ((a.MOBILE IS NOT null and a.MOBILE!='' ) and (b.MOBILE2 is not null and b.MOBILE2!=''))and a.MOBILE=b.MOBILE2 AND a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1 and a.OWNNER='72491000000059704' and b.OWNNER='72491000000059704') UNION (select a.* from contacts a ,contacts b where ((a.MOBILE2 IS NOT null and a.MOBILE2!='' ) and (b.MOBILE is not null and b.MOBILE!=''))and a.MOBILE2=b.MOBILE and a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.OWNNER='72491000000059704' and b.OWNNER='72491000000059704' and a.STATUS=1 and b.STATUS=1)) t UNION(SELECT * from contacts t where (MOBILE!='' and MOBILE is not NULL) and EXISTS (SELECT MOBILE from contacts t1 where t1.MOBILE=t.MOBILE and  t1.STATUS=1 and t1.OWNNER='72491000000059704' GROUP BY MOBILE,t1.SUOSHUXIANGMU HAVING COUNT(MOBILE)>1)  order by MOBILE)UNION(SELECT * from contacts t where (MOBILE2!='' and MOBILE2 is not NULL) and EXISTS (SELECT MOBILE2 from contacts t1 where t1.MOBILE2=t.MOBILE2 and  t1.STATUS=1 and t1.OWNNER='72491000000059704' GROUP BY MOBILE2 , t1.SUOSHUXIANGMU HAVING COUNT(MOBILE2)>1)   )UNION(SELECT * from contacts t where (PHONE!='' and PHONE is not NULL) and EXISTS (SELECT PHONE from contacts t1 where t1.PHONE=t.PHONE and t1.OWNNER='72491000000059704' and  t1.STATUS=1  GROUP BY PHONE , t1.SUOSHUXIANGMU HAVING COUNT(PHONE)>1)  ) ) a where a.OWNNER='72491000000059704' and a.status=1  ORDER BY a.MOBILE,a.MOBILE2,a.PHONE LIMIT "+beginPage+","+count;
		// and a.OWNNER='72491000000059704'
		//查询所有者是管理员的数据，目前是秦变玲
		//String sql="select ID,NAME,t.MOBILE,PHONE,EMAIL,OWNNER,BAIHUIID,CRM_CREATED_TIME,CRM_MODIFIED_TIME,CREATEDTIME,MODIFIEDTIME,OWNNER_NAME,CONTEXT from contacts t  , (select MOBILE from contacts  where STATUS = 1 and CRM_MODIFIED_TIME like '"+strDate+"%'  and MOBILE!='' GROUP BY MOBILE  HAVING COUNT(ID)>1 order by MOBILE limit 0,100 ) s where  t.MOBILE=s.MOBILE and  STATUS = 1 order by t.MOBILE desc limit "+beginPage+","+count;
		System.out.println("queryAllByPageToday::::::::::"+sql);
		try {
			rs = super.sqlQuery(sql);
			while(rs.next()){
				Contacts contact=new Contacts();
				contact.setId(rs.getString("ID"));
				contact.setName(rs.getString("NAME"));
				contact.setMobile(rs.getString("MOBILE"));
				contact.setShouji2(rs.getString("MOBILE2"));
				contact.setPhone(rs.getString("PHONE"));
				contact.setEmail(rs.getString("EMAIL"));
				contact.setOwnner(rs.getString("OWNNER"));
				contact.setBaihuiid(rs.getString("BAIHUIID"));
				contact.setbCreatedTime(rs.getDate("CRM_CREATED_TIME"));
				contact.setbModifiedTime(rs.getDate("CRM_MODIFIED_TIME"));
				contact.setCreateTime(rs.getDate("CREATEDTIME"));
				contact.setModifiedTime(rs.getDate("MODIFIEDTIME"));
				contact.setOwnnerName(rs.getString("OWNNER_NAME"));
				contact.setContext(rs.getString("CONTEXT"));
				contact.setZyDate(rs.getString("ZY_DATE"));
				contact.setSuoshuxiangmu(rs.getString("SUOSHUXIANGMU"));
				list.add(contact);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return list;
	}
	
	public int queryCount() throws SQLException {
		String sql="select count(distinct a.ID) from (select t.* from((select a.* from contacts a ,contacts b where ((a.MOBILE IS NOT null and a.MOBILE!='' ) and (b.MOBILE2 is not null and b.MOBILE2!=''))and a.MOBILE=b.MOBILE2 AND a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1) UNION (select a.* from contacts a ,contacts b where ((a.MOBILE2 IS NOT null and a.MOBILE2!='' ) and (b.MOBILE is not null and b.MOBILE!=''))and a.MOBILE2=b.MOBILE and a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1)) t UNION(SELECT * from contacts t where (MOBILE!='' and MOBILE is not NULL) and EXISTS (SELECT MOBILE from contacts t1 where t1.MOBILE=t.MOBILE and  t1.STATUS=1 GROUP BY MOBILE,t1.SUOSHUXIANGMU HAVING COUNT(MOBILE)>1)   order by MOBILE)UNION(SELECT * from contacts t where (MOBILE2!='' and MOBILE2 is not NULL) and EXISTS (SELECT MOBILE2 from contacts t1 where t1.MOBILE2=t.MOBILE2 and  t1.STATUS=1 GROUP BY MOBILE2 , t1.SUOSHUXIANGMU HAVING COUNT(MOBILE2)>1)  order by MOBILE2)UNION(SELECT * from contacts t where  (PHONE!='' and PHONE is not NULL) and EXISTS (SELECT PHONE from contacts t1 where t1.PHONE=t.PHONE and  t1.STATUS=1  GROUP BY PHONE , t1.SUOSHUXIANGMU HAVING COUNT(PHONE)>1) ) ) a  where a.status=1  ";
		try {
			rs = super.sqlQuery(sql);
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return 0;
	}

	public int queryCountToday() throws SQLException {
		Date today=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 如果需要小时、分钟和秒则用字符串"yyyy-MM-dd HH:mm:ss"
		String strDate = df.format(today);
		String sql="select count(distinct a.ID) from (select t.* from((select a.* from contacts a ,contacts b where ((a.MOBILE IS NOT null and a.MOBILE!='' ) and (b.MOBILE2 is not null and b.MOBILE2!=''))and a.MOBILE=b.MOBILE2 AND a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.STATUS=1 and b.STATUS=1 and a.OWNNER='72491000000059704' and b.OWNNER='72491000000059704') UNION (select a.* from contacts a ,contacts b where ((a.MOBILE2 IS NOT null and a.MOBILE2!='' ) and (b.MOBILE is not null and b.MOBILE!=''))and a.MOBILE2=b.MOBILE and a.SUOSHUXIANGMU=b.SUOSHUXIANGMU and a.OWNNER='72491000000059704' and b.OWNNER='72491000000059704' and a.STATUS=1 and b.STATUS=1)) t UNION(SELECT * from contacts t where (MOBILE!='' and MOBILE is not NULL) and EXISTS (SELECT MOBILE from contacts t1 where t1.MOBILE=t.MOBILE and  t1.STATUS=1 and t1.OWNNER='72491000000059704' GROUP BY MOBILE,t1.SUOSHUXIANGMU HAVING COUNT(MOBILE)>1)  order by MOBILE)UNION(SELECT * from contacts t where (MOBILE2!='' and MOBILE2 is not NULL) and EXISTS (SELECT MOBILE2 from contacts t1 where t1.MOBILE2=t.MOBILE2 and  t1.STATUS=1 and t1.OWNNER='72491000000059704' GROUP BY MOBILE2 , t1.SUOSHUXIANGMU HAVING COUNT(MOBILE2)>1)   )UNION(SELECT * from contacts t where (PHONE!='' and PHONE is not NULL) and EXISTS (SELECT PHONE from contacts t1 where t1.PHONE=t.PHONE and t1.OWNNER='72491000000059704' and  t1.STATUS=1  GROUP BY PHONE , t1.SUOSHUXIANGMU HAVING COUNT(PHONE)>1)  ) ) a where a.OWNNER='72491000000059704' and a.status=1  ";
		try {
			rs = super.sqlQuery(sql);
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return 0;
	}
	public List<Contacts> getContactsByBaihuiID(String baihuiID) throws SQLException {
		List<Contacts> list=new ArrayList<Contacts>();
		String sql="select ID,NAME,t.MOBILE,PHONE,EMAIL,OWNNER,BAIHUIID,CRM_CREATED_TIME,CRM_MODIFIED_TIME,CREATEDTIME,MODIFIEDTIME,OWNNER_NAME,CONTEXT from contacts t where  BAIHUIID='"+baihuiID+"' limit 0,1";
		try {
			rs = super.sqlQuery(sql);
			while(rs.next()){
				Contacts contact=new Contacts();
				contact.setId(rs.getString("ID"));
				contact.setName(rs.getString("NAME"));
				contact.setMobile(rs.getString("MOBILE"));
				contact.setPhone(rs.getString("PHONE"));
				contact.setEmail(rs.getString("EMAIL"));
				contact.setOwnner(rs.getString("OWNNER"));
				contact.setBaihuiid(rs.getString("BAIHUIID"));
				contact.setbCreatedTime(rs.getDate("CRM_CREATED_TIME"));
				contact.setbModifiedTime(rs.getDate("CRM_MODIFIED_TIME"));
				contact.setCreateTime(rs.getDate("CREATEDTIME"));
				contact.setModifiedTime(rs.getDate("MODIFIEDTIME"));
				contact.setOwnnerName(rs.getString("OWNNER_NAME"));
				contact.setContext(rs.getString("CONTEXT"));
				list.add(contact);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return list;
	}
	public List<Contacts> getChongFuIds(String baihuiID,String mobile,String mobile2,String phone,String suoshuxiangmu) throws SQLException{
		List<Contacts> list=new ArrayList<Contacts>();
		String sql="";
		if(("null".equals(mobile)||"".equals(mobile.trim())||mobile==null)&&("null".equals(phone)||"".equals(phone.trim())||phone==null)){//只有mobile2不为空
			sql+="select t.BAIHUIID, t.id from contacts t where t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile2+"' or t.MOBILE2 ='"+mobile2+"' )";
		}else if(("null".equals(mobile2)||"".equals(mobile2.trim())||mobile2==null)&&("null".equals(phone)||"".equals(phone.trim())||phone==null)){//只有mobile不为空
			sql+="select t.BAIHUIID, t.id from contacts t where  t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and   t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile+"' or t.MOBILE2 ='"+mobile+"' )";
		}else if(("null".equals(mobile)||"".equals(mobile.trim())||mobile==null)&&("null".equals(mobile2)||"".equals(mobile2.trim())||mobile2==null)&&(!"null".equals(phone)&&!"".equals(phone.trim())&&phone!=null)){//只有phone不为空
			sql+="select t.BAIHUIID, t.id from contacts t where   t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and t.PHONE ='"+phone+"' ";
		}else if(("null".equals(phone)||"".equals(phone.trim())||phone==null)&&(!"null".equals(mobile)&&!"".equals(mobile.trim())&&mobile!=null)&&(!"null".equals(mobile2)&&!"".equals(mobile2.trim())&&mobile2!=null)){//只有phone为空
			sql+="select t.BAIHUIID, t.id from contacts t where  t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile2+"' or t.MOBILE2 ='"+mobile2+"' or t.MOBILE ='"+mobile+"' or t.MOBILE2 ='"+mobile+"')";
		}else if(("null".equals(mobile2)||"".equals(mobile2.trim())||mobile2==null)&&(!"null".equals(phone)&&!"".equals(phone.trim())&&phone!=null)&&(!"null".equals(mobile)&&!"".equals(mobile.trim())&&mobile!=null)){//只有mobile2为空
			sql+="select t.BAIHUIID, t.id from contacts t where  t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile+"' or t.MOBILE2 ='"+mobile+"' or t.PHONE ='"+phone+"')";
		}else if(("null".equals(mobile)||"".equals(mobile.trim())||mobile==null)&&(!"null".equals(phone)&&!"".equals(phone.trim())&&phone!=null)&&(!"null".equals(mobile2)&&!"".equals(mobile2.trim())&&mobile2!=null)){//只有mobile为空
			sql+="select t.BAIHUIID, t.id from contacts t where  t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile2+"' or t.MOBILE2 ='"+mobile2+"' or t.PHONE ='"+phone+"')";
		}else{
			sql+="select t.BAIHUIID, t.id from contacts t where  t.SUOSHUXIANGMU='"+suoshuxiangmu+"' and  t.STATUS=1 and  t.BAIHUIID !='"+baihuiID+"' and (t.MOBILE ='"+mobile2+"' or t.MOBILE2 ='"+mobile2+"' or t.MOBILE ='"+mobile+"' or t.MOBILE2 ='"+mobile+"' or t.PHONE ='"+phone+"' )";
		}
		System.out.println("sssssssss:sssss:"+sql);
			try {
			rs = super.sqlQuery(sql);
			while(rs.next()){
				Contacts contact=new Contacts();
				contact.setId(rs.getString("ID"));
				contact.setBaihuiid(rs.getString("BAIHUIID"));
				list.add(contact);
			}
		} catch (SQLException e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) SQLException",e);
		} catch (Exception e) {
			log.error("contactDAO/getContactBaihuiIdByContactNo(baihuiId) Exception",e);
		} finally{
			closeAll();
			log.info("contactDAO/getContactBaihuiIdByContactNo(baihuiId) method end!");
		}
		return list;
	}
}
