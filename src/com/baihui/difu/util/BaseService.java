package com.baihui.difu.util;

import java.util.List;

import org.apache.log4j.*;

import com.baihui.difu.dao.BaseDAO;

public class BaseService {
	public static Logger log = Logger.getLogger(BaseService.class);

	/**
	 * 处理数据库
	 * @param insertParamsList 要插入的数据集合
	 * @param updateParamsList 要更改的数据集合
	 * @param chineseModule 操作的模块中文名称
	 * @param BaseDAO 这里是父类,可以new 子类来实现
	 * @throws Exception 
	 */
	public void handleDBResult(List<Object []> insertParamsList,List<Object []> updateParamsList,String chineseModule, BaseDAO baseDAO) throws Exception{
		//用来接收批量修改后的结果
 		int[] results = null;
		//成功插入记录的条数
		int insertSuccessRecord = 0;
		//失败插入记录的条数
		int insertFailureRecord = 0;
		//成功修改记录的条数
		int updateSuccessRecord = 0;
		//失败修改记录的条数
		int updateFailureRecord = 0;
		//失败插入记录的 orderNo
		String insertFailureOrderNos = "";
		//失败更新记录的orderNo
		String updateFailureOrderNos = "";
		
		log.warn("handleDBResult insertParamsList.size():["+(insertParamsList == null ? 0 : insertParamsList.size())+"]");
		log.warn("handleDBResult updateParamsList.size():["+(updateParamsList == null ? 0 : updateParamsList.size())+"]");
		
		//批量新增
		if (insertParamsList != null && !insertParamsList.isEmpty()){
			results = baseDAO.insert(insertParamsList);
			//insertList中有多少条记录,results中就应该有多少个结果
			if(results!=null && results.length>0){
				for(int i=0;i<results.length;i++){
					int result = results[i];
					if(result == 1){//记录保存成功
						insertSuccessRecord++;
					}else{//记录保存失败
						insertFailureRecord++;
						//如果insertFailureOrderNos为空,里面还没有数据,则把这条数据直接添加到insertFailureOrderNos中
						if("".equals(insertFailureOrderNos)){
							insertFailureOrderNos = ""+insertParamsList.get(i);
						
						//如果insertFailureOrderNos已经有数据了,则这次加上数据时在前面加一个","
						}else{
							insertFailureOrderNos += "," + insertParamsList.get(i);
						}
					}
				}
				if(!"".equals(insertFailureOrderNos)){
					StringUtil.message_error +="同步CRM中"+chineseModule+"数据---添加新数据到数据库中,成功"+insertSuccessRecord+"条," +
							"失败"+insertFailureOrderNos+"条,失败的订单编号:"+insertFailureOrderNos;
				}
				
			}
		}
		
		
		
		//批量修改
		if (updateParamsList != null && !updateParamsList.isEmpty()){
			results = baseDAO.update(updateParamsList);
			//updateList中有多少条记录,results中就应该有多少个结果
			if(results!=null && results.length>0){
				for(int i=0;i<results.length;i++){
					int result = results[i];
					if(result == 1){//记录保存成功
						updateSuccessRecord++;
					}else{//记录保存失败
						updateFailureRecord++;
						//如果updateFailureOrderNos为空,里面还没有数据,则把这条数据直接添加到updateFailureOrderNos中
						if("".equals(updateFailureOrderNos)){
							updateFailureOrderNos = "" + updateParamsList.get(i);
						}else{//如果updateFailureOrderNos已经有数据了,则这次加上数据时在前面加一个","
							updateFailureOrderNos += "," + updateParamsList.get(i);
						}
					}
				}
				if(!"".equals(updateFailureOrderNos)){
					StringUtil.message_error +="<br />同步CRM中订单数据---修改数据,成功"+updateSuccessRecord+"条," +
							"失败"+updateFailureRecord+"条,失败的订单编号:"+updateFailureOrderNos;
				}
			}
			
			
		}
	}
}
