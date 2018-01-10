package com.baihui.difu.biz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baihui.difu.entity.Order;
import org.apache.log4j.*;

public abstract class IMyOrder {
	public static Logger log=Logger.getLogger(IMyOrder.class);

	/**将excel数据装配成OrderList*/
	public abstract List<?> getMyOrderByExcelList(List<List<String>> excelList) throws Exception;
	
	/**
	 * 复制数据到CRM中
	 * type:上传文件的类型  0:采购单  1:销售单
	 */
	public abstract void copyData(List<Order> orderList,int type,String email);
	
	/**
	 * 拼接Set<String>订单编号NO
	 * @param excelList
	 * @return
	 */
	
	public Set<String> getOrderNos(List<List<String>> excelList){
		log.info("IMyOrder/getOrderNos() method beginning!");
		log.info("excelList.size():["+excelList.size()+"]");
		Set<String> orderNos=new HashSet<String>();
		/**获取excelList中的第一层元素*/
		List<String> getRow=new ArrayList<String>();
		log.info("订单编号集合");
		for(int i=0;i<excelList.size();i++){
			/**获取一行数据*/
			getRow=excelList.get(i);
			/**获取订单编号,订单编号所在列是第14列*/
			String orderNo=getRow.get(13);
			/**将订单编号存入Set<String>集合*/
			orderNos.add(orderNo);
			log.info("订单编号"+i+":"+orderNo);
		}
		log.info("IMyOrder/getOrderNos() method end!");
		return orderNos;
		
		
	}
}
