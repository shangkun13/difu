package com.baihui.difu.baihui;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baihui.difu.dao.LeadsDao;
import com.baihui.difu.util.BaseService;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Tools;
import com.baihui.difu.util.Util;
import com.taobao.api.internal.util.WebUtils;
import org.apache.log4j.*;


public class DataToCrm {
	public static Logger log=Logger.getLogger(DataToCrm.class);
	public static String token = PropertyManager.getProperty("difu_TOKEN");//获取德灵科的token

	/**
	 * 获取相应模块的所有数据(分批查询crm数据,然后将这批数据与数据库中进行比较,看是新增还是修改)
	 * @param module
	 * @param token
	 * @param selectColumns
	 * @param lastModifiedTime
	 * @throws Exception
	 */
	public static void syncAllData(String module,String token,String selectColumns) throws Exception{
		log.info("DataToCrm/syncAllData(String module,String token,String selectColumns) method begin!");
		log.info("module:["+module+"]");
		log.info("token:["+token+"]");
		log.info("selectColumns:["+selectColumns+"]");
		int insertLength = 0;
		int updateLength = 0;
		int fromIndex = 1;
		int toIndex = Constants.DB2CRM_SELECT_UNIT_COUNT;
		int pageSize = Constants.DB2CRM_SELECT_UNIT_COUNT;
		LeadsDao leadsDao = new LeadsDao();
		while ( pageSize == Constants.DB2CRM_SELECT_UNIT_COUNT ) {
			log.warn("fromIndex:["+fromIndex+"]");
		    log.warn("toIndex:["+toIndex+"]");
			List<Map<String, String>> middileList = getRecordsByLastModifiedTime(module, token, selectColumns, "", fromIndex, toIndex);
			if( middileList == null || middileList.isEmpty() ){
				pageSize = 0;
			}else{
				pageSize = middileList.size();
			}
			//将查询到的所有数据拼接到list集合中
		    log.info("本次循环查询到记录的条数,pageSize:"+ pageSize);
		    Timestamp sqlNow = Tools.getNowTimestamp();
		    Map<String, Object[]> insertHashMap = new HashMap<String, Object[]>();
		    Map<String, Object[]> updateHashMap = new HashMap<String, Object[]>();
			for (int i = 0; i < pageSize; i++) {
				Map<String, String> listMap = middileList.get(i);
				// 获取来自CRM的baihuiId
				String baihuiIdFromCRM = Tools.trim(listMap.get("LEADID"));
				// 获取来自CRM的name
				String leadName = Tools.trim(listMap.get("Last Name"));
				// 获取来自CRM的mobile
				String mobile = Tools.trim(listMap.get("Mobile"));
				//通过baihuiId去数据库查询记录是否存在
				boolean isExists = leadsDao.checkLeadsBaihuiId(baihuiIdFromCRM);
				if (isExists) {
					updateHashMap.put(baihuiIdFromCRM, new Object[] {mobile,baihuiIdFromCRM});
				}else{
					sqlNow = Tools.getNowTimestamp();
					Date crmCreateDate = (Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
							listMap.get("Created Time")));
					Date crmModifiedDate = Tools.strToDatetime("ContactService/syncFromCRM()/product.setCrmCreatedTime",
							listMap.get("Modified Time"));
					insertHashMap.put(baihuiIdFromCRM, new Object[] { leadName, mobile, baihuiIdFromCRM, 
							crmCreateDate, crmModifiedDate, sqlNow, sqlNow});
				}
			}
			//需要新增的数据的参数集合
			List<Object []> insertParamsList = new ArrayList<Object[]>();
			//需要修改的数据的参数集合
			List<Object []> updateParamsList = new ArrayList<Object[]>();
			insertParamsList.addAll(insertHashMap.values());
			updateParamsList.addAll(updateHashMap.values());
			// 批量处理
			BaseService baseService = new BaseService();
			insertLength += insertParamsList.size();
			updateLength += updateParamsList.size();
			baseService.handleDBResult(insertParamsList, updateParamsList,
					Constants.CRM_MODULE_CHINESE_LEADS, leadsDao);
			
		    fromIndex = toIndex+1;
		    toIndex += pageSize;
		}
	    log.warn("syncAllData allLength:["+(toIndex-Constants.DB2CRM_SELECT_UNIT_COUNT)+"]");
	    log.warn("syncAllData insertLength:["+insertLength+"]");
	    log.warn("syncAllData updateLength:["+updateLength+"]");
		log.info("DataToCrm/syncAllData() method end!");
	}
	
/*	public String login(String login_id, String password) throws Exception {
		JSONObject json = HttpClient.getJSONDataFromURL(Constant.API_SERVER+"/account/json/Login?login_id=" + login_id + "&password=" + password + "");
		try {
			JSONObject resObj = json.getJSONObject("response");
			if (resObj.getString("result").equals("true")) {
				return resObj.getString("ticket");
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ActionInitException("User Login Fail", Constant.API_SERVER+"/account/json/Login;login_id=" + login_id + ";password=" + password);
		}
	}*/
	
	
	/**
	 * 插入指定模块的数据到CRM中
	 * 
	 * @param module查重的模块
	 * @param token 用户的token
	 * @param data 查重的xmldata
	 * @return 返回插入这条数据后的id
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public static String insert(String module, String token, String data)
			throws IOException, JSONException, InterruptedException {
		log.info("DataToCrm/insert() method beginning!");
		Map<String, String> map = new HashMap<String, String>();
		/*
		 * 查询模块全部数据,获取模块ID,放入list集合中
		 */
		String url = "https://crm.zoho.com.cn/crm/private/json/" + module
				+ "/insertRecords?authtoken=" + token
				+ "&scope=crmapi&version=2&newFormat=2";
		log.info("url:["+url+"]");
		log.info("xmlData:["+data+"]");
		map.put("xmlData", data);
		String reponse = "";
		
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
		} catch (SocketTimeoutException e) {
			Thread.sleep(2000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
			} catch (SocketTimeoutException e1) {
				Thread.sleep(2000l);
				reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
				e1.printStackTrace();
			}

		}
		/**
		 * 获取模块 ID 放入到list中
		 */
		String baihuiID=null;
		if (reponse.contains("\"message\":\"Record(s) added successfully\"")) {
			if (reponse.contains("recorddetail\":[")) {
				JSONArray jArray = new JSONObject(reponse).getJSONObject("response").getJSONObject("result").getJSONArray("recorddetail");
				for (int i = 0; i < jArray.length(); i++) {
					JSONArray josnArray = jArray.getJSONObject(i).getJSONArray("FL");
					for (int j = 0; j < josnArray.length(); j++) {
						if (josnArray.getJSONObject(j).getString("val").equals("Id")){
							System.out.println(josnArray.getJSONObject(j).getString("val").equals("Id"));
							baihuiID=josnArray.getJSONObject(j).getString("content").trim();
							break;
						}
					}
				}
			} else if (reponse.contains("recorddetail\":{")) {
				JSONObject jsonObject = new JSONObject(reponse).getJSONObject("response").getJSONObject("result").getJSONObject("recorddetail");
				JSONArray josnArray = jsonObject.getJSONArray("FL");
				for (int j = 0; j < josnArray.length(); j++) {
					if (josnArray.getJSONObject(j).getString("val").equals("Id")){
						System.out.println(josnArray.getJSONObject(j).getString("val").equals("Id"));
						baihuiID=josnArray.getJSONObject(j).getString("content").trim();
						break;
					}
				}
			}
		} else {
			log.info("reponse:"+reponse);
			throw new RuntimeException(reponse);
		}
		log.info("baihuiID:["+baihuiID+"]");
		log.info("DataToCrm/insert() method end!");
		return baihuiID;
	}

	
	
	/**
	 * 根据模块名称和或者baihuiID查询CRM记录
	 * @param str 查询条件,指定的莫开名称或者baihuiID
	 * @param module 查询的CRM模块
	 * @param token 用户的token
	 * @param str_value 查询条件对应的值;例如 productname='产品A',则str:productname,str_value:产品A
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public String getBaihuiIDByModuleidOrModulename(String str, String module, String token,
			String str_value) throws IOException, JSONException,InterruptedException {
		log.info("DataToCrm/getBaihuiIDByModuleidOrModulename() method beginning!");
		log.info("str_value["+str_value+"]");
		Util.StrTrim(str_value);
		str_value=Util.urlDecode(str_value);
		String url = "https://crm.zoho.com.cn/crm/private/json/" + module
				+ "/getSearchRecordsByPDC?authtoken=" + token 
				+ "&scope=crmapi&searchColumn=" + str
				+ "&searchValue="+str_value;
		log.info("url:["+url+"]");
		Map<String, String> map = new HashMap<String, String>();
		String baihuiid=null;
		String moduleid = module.toUpperCase()
				.substring(0, module.length() - 1)
				+ "ID";
		String selectColumns = module + "(" + moduleid + ")";
		map.put("selectColumns", selectColumns);
		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
		} catch (SocketTimeoutException e) {
			Thread.sleep(1000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
			} catch (Exception e1) {
				Thread.sleep(2000l);
				try {
					reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
				} catch (Exception e2) {
					e2.printStackTrace();
					throw new RuntimeException(reponse);
				}
			}
		}
		if (reponse.contains("{\"response\":{\"nodata\"")) {
			return baihuiid;
		}
		if (reponse.contains("\"row\":[")) {
			JSONObject jObject = new JSONObject(reponse)
					.getJSONObject("response");
			JSONArray rows = jObject.getJSONObject("result").getJSONObject(
					module).getJSONArray("row");
			JSONObject ret = null;
			for (int i = 0; i < rows.length(); i++) {
				ret = rows.getJSONObject(i);
				JSONArray jsonArray = null;
				JSONObject fl=null;
				//这里判断一下是否有多个"FL",如果有多个就使用jsonArray接受,如果只有一个,就用JSONObject接受
				if(ret.length()>2){
					jsonArray = ret.getJSONArray("FL");
					for (int j = 0; j < jsonArray.length(); j++) {
						if (jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase(moduleid)){
							baihuiid = jsonArray.getJSONObject(j).getString("content").trim();
							break;
						}
					}
				}
				else{
					fl=ret.getJSONObject("FL");
					if (fl.getString("val").equalsIgnoreCase(moduleid)){
						baihuiid = fl.getString("content").trim();
					}
				}
			}
		} else if (reponse.contains("\"row\":{")) {
			JSONObject jObject = new JSONObject(reponse).getJSONObject("response");
			JSONObject ret = jObject.getJSONObject("result").getJSONObject(module).getJSONObject("row");
			JSONArray jsonArray=null;
			JSONObject fl=null;
			//这里判断一下是否有多个"FL",如果有多个就使用jsonArray接受,如果只有一个,就用JSONObject接受
			if(ret.length()>2){
				jsonArray = ret.getJSONArray("FL");
				for (int j = 0; j < jsonArray.length(); j++) {
					if (jsonArray.getJSONObject(j).getString("val").equalsIgnoreCase(moduleid)){
						baihuiid = jsonArray.getJSONObject(j).getString("content").trim();
						break;
					}
				}
			}
			else{
				fl=ret.getJSONObject("FL");
				if (fl.getString("val").equalsIgnoreCase(moduleid)){
					baihuiid = fl.getString("content").trim();
				}
			}
		}
		log.info("baihuiid:["+baihuiid+"]");
		log.info("DataToCrm/getBaihuiIDByModuleidOrModulename() method end!");
		return baihuiid;
	} 
	
	
	
	/**
	 * 根据id查询模块记录,返回Map<String,Object>集合,key:记录的id值,value:row集合
	 * @param id
	 * @param module
	 * @param token
	 * @param mainkey
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public JSONArray getOne(String id, String module, String token) throws IOException, JSONException,
			InterruptedException {
		String url = "https://crm.zoho.com.cn/crm/private/json/" + module
				+ "/getRecordById?&authtoken=" + token + "&scope=crmapi&id="
				+ id;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonArrays=new JSONArray();
		//String moduleid = module.toUpperCase().substring(0, module.length() - 1) + "ID";
		//String selectColumns = module + "(" + moduleid + "," + mainkey + ")";//mainkey传递过来的 参数,显示查询后的字段
		//map.put("selectColumns", selectColumns);
		

		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
		} catch (SocketTimeoutException e) {
			Thread.sleep(1000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
			} catch (Exception e1) {
				Thread.sleep(2000l);
				try {
					reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
				} catch (Exception e2) {
					e2.printStackTrace();
					throw new RuntimeException(reponse);
				}
			}
		}
		if (reponse.contains("{\"response\":{\"nodata\"")) {
			return jsonArrays;
		}
		if (reponse.contains("\"row\":[")) {
			JSONObject jObject = new JSONObject(reponse).getJSONObject("response");
			JSONArray rows = jObject.getJSONObject("result").getJSONObject(module).getJSONArray("row");
			jsonArrays=rows;
			
		} else if (reponse.contains("\"row\":{")) {
			JSONObject jObject = new JSONObject(reponse)
					.getJSONObject("response");
			JSONObject ret = jObject.getJSONObject("result").getJSONObject(module).getJSONObject("row");
			jsonArrays.put(ret);
		}
		return jsonArrays;
	}
	
	
	
	/**
	 * 根据id删除记录
	 * 注意:删除的模块内容不能被其他模块调用,否则删除不能成功
	 */
	public int deleteByID(String id, String module, String token)throws IOException, JSONException,InterruptedException {
		int result=0;//0:删除失败,1:删除成功
		String url="https://crm.zoho.com.cn/crm/private/json/"+module+"/deleteRecords?authtoken="+token+"&scope=crmapi&id="+id;
		Map<String, String> map = new HashMap<String, String>();
		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
		} catch (SocketTimeoutException e) {
			Thread.sleep(1000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
			} catch (Exception e1) {
				Thread.sleep(2000l);
				try {
					reponse = WebUtils.doPost(url, map, "utf-8", 3000, 3000);
				} catch (Exception e2) {
					e2.printStackTrace();
					throw new RuntimeException(reponse);
				}
			}
		}
		if (reponse.contains("Record(s) deleted successfully\"")) {
			result=1;//删除成功
			log.info(module+"模块下,订单编号为"+id+"的记录删除成功");
		}else if((reponse.contains("\"error\": {\"code\":4103"))){//如果报4103错误,则可能是数据不存在,或者要删除数据与其他模块数据有关联
			log.info("错误代码:\"4103\".删除失败!原因可能是:则可能是数据不存在,或者要删除数据与其他模块数据有关联");
			System.out.println("错误代码:\"4103\".删除失败!原因可能是:则可能是数据不存在,或者要删除数据与其他模块数据有关联");
			result=0;//删除失败
		
		}else {
			result=0;//删除失败
			System.out.println(reponse);
			log.info("reponse:"+reponse);
			throw new RuntimeException(reponse);
		}
		return result;
	}
	
	
	


	/**
	 * 从CRM中查询数据,查询的内容是selectColumns
	 * 将查询出来的内容,组装成map集合,保存到list中返回
	 * @param module 模块名称
	 * @param token 百会token
	 * @param selectColumns  通过crm查询显示的列
	 * @param lastModifiedTime 最后修改时间
	 * @param fromIndex 第几条数据开始查询
	 * @param toIndex 查到第几条数据结束
	 * @return 将查询出来的内容,组装成map集合,添加到list中返回,这里最多200条
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public static List<Map<String, String>> getRecordsByLastModifiedTime(String module, String token,
			String selectColumns, String lastModifiedTime, int fromIndex, int toIndex)
			throws IOException, JSONException, InterruptedException {
		log.info("DataToCrm/getRecordsByLastModifiedTime(String module, String token,String selectColumns, String lastModifiedTime, int fromIndex, int toIndex) method begin!");
		log.info("module:["+module+"]");
		log.info("token:["+token+"]");
		log.info("mainkey:["+selectColumns+"]");
		log.info("lastModifiedTime:["+lastModifiedTime+"]");
		log.info("fromIndex:["+fromIndex+"]");
		log.info("toIndex:["+toIndex+"]");
		String url = Constants.CRM_URL + module
				+ "/getRecords?authtoken=" + token
				+ "&scope=crmapi&newFormat=2&version=2&sortOrderString=desc";
		log.info("url:["+url+"]");
		Map<String, String> map = new HashMap<String, String>();
		log.info("selectColumns:["+selectColumns+"]");
		map.put("selectColumns", selectColumns);
		map.put("sortColumnString", "Modified Time");
		if(!"".equals(lastModifiedTime)){//第一次同步,lastModifiedTime为空
			map.put("lastModifiedTime", lastModifiedTime);
		}
		
		map.put("toIndex", toIndex + "");
		map.put("fromIndex", fromIndex + "");
		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
			log.info("reponse:["+reponse+"]");
		} catch (SocketTimeoutException e) {
			//如果请求失败,则到catch里面,在重新做一次请求
			log.warn("第一次请求失败,则间隔10秒后进行第二次请求!");
			Thread.sleep(1000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
			} catch (Exception e1) {
				//如果请求还是失败,则到catch里面,在重新做一次请求
				log.warn("第二次请求失败,则间隔20秒后进行第三次请求!");
				Thread.sleep(2000l);
				try {
					reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
				} catch (Exception e2) {
					//如果请求依然失败,抛出异常
					log.error("第三次请求依然失败,抛出异常!", e2);
					log.error("reponse:["+reponse+"]");
					throw new RuntimeException(reponse);
				}
			}
		}
		// 下面是解析从CRM中读取出来的数据
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		map = new HashMap<String, String>();
		log.info("reponse:["+reponse+"]");
		if (reponse.contains("{\"response\":{\"nodata\""))
			return list;
		if (reponse.contains("\"row\":[")) {
			JSONObject jObject = new JSONObject(reponse).getJSONObject("response");
			JSONArray rows = jObject.getJSONObject("result").getJSONObject(module).getJSONArray("row");
			JSONObject ret = null;
			for (int i = 0; i < rows.length(); i++) {
				log.info("当前是第"+i+"个row");
				ret = rows.getJSONObject(i);
				try {
					JSONArray jsonArray = ret.getJSONArray("FL");
					map = new HashMap<String, String>();
					for (int j = 0; j < jsonArray.length(); j++) {
						log.info("第"+j+"组值:"+jsonArray.getJSONObject(j).getString("val")+" = "+jsonArray.getJSONObject(j).getString("content").trim());
						map.put(jsonArray.getJSONObject(j).getString("val").trim(),
								jsonArray.getJSONObject(j).getString("content").trim());
					}
					list.add(map);
				} catch (JSONException e){
					log.error("rows JSONException" , e);
					continue;
				}

			}
		} else if (reponse.contains("\"row\":{")) {
			log.info("当前只有1个row");
			JSONObject jObject = new JSONObject(reponse).getJSONObject("response");
			JSONObject ret = jObject.getJSONObject("result").getJSONObject(module).getJSONObject("row");
			try {
				JSONArray jsonArray = ret.getJSONArray("FL");
				map = new HashMap<String, String>();
				for (int j = 0; j < jsonArray.length(); j++) {
					log.info("第"+j+"组值:"+jsonArray.getJSONObject(j).getString("val")+" = "+jsonArray.getJSONObject(j).getString("content").trim());
					map.put(Tools.trim("DataToCrm/getRecordsByLastModifiedTime ", jsonArray.getJSONObject(j).getString("val")),
							Tools.trim("DataToCrm/getRecordsByLastModifiedTime ", jsonArray.getJSONObject(j).getString("content")));
				}
				list.add(map);
			} catch (JSONException e){
				log.error("rows JSONException" , e);
			}
		}
		log.info("DataToCrm/getRecordsByLastModifiedTime() method end!");
		return list;
	}
	
	
	/**
	 * 获取相应模块的所有数据(一次全部查询)
	 * @param module 模块名称
	 * @param token 百会token
	 * @param selectColumns  通过crm查询显示的列
	 * @param lastModifiedTime 最后修改时间
	 * @return  将getRecordsByLastModifiedTime方法中所有的记录,添加到list中返回,<br />
	 * 这里获得的是根据最后修改时间查询到的全部记录
	 * @throws InterruptedException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static List<Map<String,String>> getAllData(String module,String token,String selectColumns, String lastModifiedTime) throws IOException, JSONException, InterruptedException{
		log.info("DataToCrm/getAllData(String module,String token,String selectColumns, String lastModifiedTime) method begin!");
		log.info("module:["+module+"]");
		log.info("token:["+token+"]");
		log.info("selectColumns:["+selectColumns+"]");
		log.info("lastModifiedTime:["+lastModifiedTime+"]");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int fromIndex = 1;
		int toIndex = Constants.DB2CRM_SELECT_UNIT_COUNT;
		int pageSize = Constants.DB2CRM_SELECT_UNIT_COUNT;
		int pageIndex = 0;
		while ( pageSize == Constants.DB2CRM_SELECT_UNIT_COUNT ) {
			List<Map<String, String>> middileList = getRecordsByLastModifiedTime(module, token, selectColumns, lastModifiedTime, fromIndex, toIndex);
			if( middileList == null || middileList.isEmpty() ){
				pageSize = 0;
			}else{
				pageIndex ++;
				pageSize = middileList.size();
			}
			//将查询到的所有数据拼接到list集合中
			list.addAll(middileList);
		    log.info(module+",getAllData 第"+pageIndex+"次循环查询到记录的条数,pageSize:"+ pageSize);
		    fromIndex = toIndex+1;
		    toIndex += Constants.DB2CRM_SELECT_UNIT_COUNT;
		}
		log.info("本次共有记录的条数,list.size():"+list.size());
		log.info("DataToCrm/getAllData() method end!");
		return list;
	}
	
	/**
	 * 获取所有的机构用户(激活的和非激活的)
	 * 
	 * @param module
	 * @param token
	 * @param selectColumns
	 * @param lastModifiedTime
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public static List<Map<String, String>> getUsers(String token, String type)
			throws IOException, JSONException, InterruptedException {
		log.info("通过CRM API获取所有机构用户 --- DataToCrm/getUsers(String module, String token,String type) method begin!");

		String url = Constants.CRM_URL + "Users" + "/getUsers?authtoken="
				+ token + "&scope=crmapi&newFormat=2";
		log.info("url:[" + url + "]");
		Map<String, String> map = new HashMap<String, String>();
		log.info("type:[" + type + "]");
		map.put("type", type);
		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
		} catch (SocketTimeoutException e) {
			// 如果请求失败,则到catch里面,在重新做一次请求
			log.warn("第一次请求失败,则间隔10秒后进行第二次请求!");
			Thread.sleep(1000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
			} catch (Exception e1) {
				// 如果请求还是失败,则到catch里面,在重新做一次请求
				log.warn("第二次请求失败,则间隔20秒后进行第三次请求!");
				Thread.sleep(2000l);
				try {
					reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
				} catch (Exception e2) {
					// 如果请求依然失败,抛出异常
					log.error("第三次请求依然失败,抛出异常!", e2);
					log.info("通过CRM API获取所有机构用户 --- 请求异常 DataToCrm/getUsers(String module, String token,String type) method end!");
					throw new RuntimeException(reponse);
				}
			}
		}
		log.info("通过CRM API获取所有机构用户 ---  查询结果reponse:[" + reponse + "]");
		// 下面是解析从CRM中读取出来的数据
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if (reponse.contains("{\"response\":{\"nodata\"")) {
			log
					.info("通过CRM API获取所有机构用户 --- DataToCrm/getUsers(String module, String token,String type) method end!");
			return list;
		}
		if (reponse.contains("\"user\":[")) {
			log.info("通过CRM API获取所有机构用户 --- 当前机构下有多个用户");
			JSONObject jObject = new JSONObject(reponse).getJSONObject("users");
			JSONArray users = jObject.getJSONArray("user");
			log.info("开始解析从CRM读取的数据");
			for (int i = 0; i < users.length(); i++) {
				log.info("当前是第" + (i + 1) + "个user");
				map = new HashMap<String, String>();
				map.put("id", users.getJSONObject(i).getString("id").trim());// 用户id
				map.put("email", users.getJSONObject(i).getString("email")
						.trim());// 邮编
				map
						.put("role", users.getJSONObject(i).getString("role")
								.trim());// 职位
				map.put("confirm", users.getJSONObject(i).getString("confirm")
						.trim());// 是否已激活
				map.put("mobile", users.getJSONObject(i).getString("mobile")
						.trim());// 手机
				map.put("profile", users.getJSONObject(i).getString("profile")
						.trim());// 角色
				map.put("zip", users.getJSONObject(i).getString("zip").trim());// 邮编
				map.put("phone", users.getJSONObject(i).getString("phone")
						.trim());// 电话
				map.put("fax", users.getJSONObject(i).getString("fax").trim());// 传真
				map.put("status", users.getJSONObject(i).getString("status")
						.trim());// 激活状态
				map.put("content", users.getJSONObject(i).getString("content")
						.trim());// 用户名
				list.add(map);
			}
		} else if (reponse.contains("\"row\":{")) {
			log.info("通过CRM API获取所有机构用户 --- 当前机构下只有一个用户");
			JSONObject jObject = new JSONObject(reponse).getJSONObject("users");
			JSONObject user = jObject.getJSONObject("user");
			map = new HashMap<String, String>();
			map.put("id", user.getString("id").trim());// 用户id
			map.put("email", user.getString("email").trim());// 邮编
			map.put("role", user.getString("role").trim());// 职位
			map.put("confirm", user.getString("confirm").trim());// 是否已激活
			map.put("mobile", user.getString("mobile").trim());// 手机
			map.put("profile", user.getString("profile").trim());// 角色
			map.put("zip", user.getString("zip").trim());// 邮编
			map.put("phone", user.getString("phone").trim());// 电话
			map.put("fax", user.getString("fax").trim());// 传真
			map.put("status", user.getString("status").trim());// 激活状态
			map.put("content", user.getString("content").trim());// 用户名
			list.add(map);
		}
		log.info("通过CRM API获取所有机构用户 --- DataToCrm/getUsers(String module, String token,String type) method end!");
		return list;
	}

	public static void update(String module, String token, String data,
			String id) throws IOException, InterruptedException {
		log.info("param module:[" + module + "]");
		log.info("param token:[" + token + "]");
		log.info("param data:[" + data + "]");
		log.info("param id:[" + id + "]");
		String url = Constants.CRM_URL + module + "/updateRecords";
		log.warn("url:[" + url + "]");
		Map<String, String> map = new HashMap<String, String>();
		map.put("scope", "crmapi");
		map.put("authtoken", token);
		map.put("xmlData", data);
		map.put("id", id);
		map.put("newFormat", "2");
		map.put("wfTrigger", "true");
		map.put("version", "2");
		String reponse = "";
		try {
			reponse = WebUtils.doPost(url, map, "utf-8", 50000, 50000);
		} catch (SocketTimeoutException e) {
			Thread.sleep(2000l);
			try {
				reponse = WebUtils.doPost(url, map, "utf-8", 80000, 80000);
			} catch (SocketTimeoutException e1) {
				Thread.sleep(2000l);
				reponse = WebUtils.doPost(url, map, "utf-8", 30000, 30000);
				log.error("update erorr", e);
			}
		}
		log.warn("reponse:[" + reponse + "]");
		
		 if(!reponse.contains( "\"message\":\"Record(s) updated successfully\"")) { //如果flag==true的时候,就说明这条数据在crm中真的不存在. 
			 if(reponse.contains("\"error\": {\"code\":401.2")) {
//				try {
//					//insert(module, token, data);
//				} catch (JSONException e) {
//					log.error("insert erorr", e);
//				}
			} else {
				log.error("id:[" + id + "]");
				log.error("targetURL:[" + url + "]");
				log.error("data:[" + data + "]");
				log.error("reponse:[" + reponse + "]");
				log.error("bottom update data to CRM response error, and throw RuntimeException");
				throw new RuntimeException(reponse);
			}
		}
	}
	
	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
		List<Map<String, String>> list = DataToCrm.getAllData(Constants.MODULE_LEADS,
				PropertyManager.getProperty("CRM_TOKEN"),
				Constants.CRM_SELECTCOLUMNS_LEADS, "2015-04-14 18:00:00");
		for(int i=0;i<list.size();i++){
			StringBuffer sb=new StringBuffer("<row no='1'>");
			sb.append("<FL val='手机'><![CDATA[").append(list.get(i).get("Mobile")).append("]]></FL>");
			sb.append("<FL val='电话'><![CDATA[").append(list.get(i).get("Phone")).append("]]></FL>");
			sb.append("</row>");
			DataToCrm.update("Leads", PropertyManager.getProperty("CRM_TOKEN"), 
					String.format("<Leads>%s</Leads>", sb.toString()), list.get(i).get("LEADID"));
		}
	}
}
