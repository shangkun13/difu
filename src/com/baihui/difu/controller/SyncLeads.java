package com.baihui.difu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.baihui.difu.service.LeadsService;


/**
 * 接受页面上传的excel文件,并对文件进行解析
 * @author luoxl
 *
 */

public class SyncLeads extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(SyncLeads.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		LeadsService leadsService = new LeadsService();
		try {
			//同步客户
			//contactService.syncFromCRM();
			//同步所有
			leadsService.syncAllData();
			response.getWriter().write("同步客户记录成功!");
		} catch (Exception e) {
			log.error("Exception", e);
			response.getWriter().write("同步客户记录失败!");
		}
//		OrderService orderService = new OrderService();
//		OrderDetailService orderDetailService = new OrderDetailService();
//		List<SalesOrderCRM> list = null;
//		try {
//			//同步销售订单
//			orderService.syncFromCRM();
//			// 查询一共有多少客户订单
//			long orderCount = orderDetailService.getOrderDetailContactCount();
//			if(orderCount <= 0){
//				response.getWriter().write("没有符合条件的订单需要导入!");
//				return;
//			}
//			//这次查询到的分页,每页200条数据,也就是每次最多插入200条
//			long page = (orderCount -1)/Constants.DB_BATCH_DO_NUM + 1;
//			long beginNum = 0;
//			long endNum = beginNum + Constants.DB_BATCH_DO_NUM;
//			long listLength = 0;
//			for (int i = 0; i < page; i++) {
//				log.info("beginNum:["+beginNum+"]");
//				log.info("endNum:["+endNum+"]");
//				//定义结束的条目数
//				if(endNum > orderCount){
//					endNum = orderCount;
//				}
//				//解析excel,返回List集合
//				list = orderDetailService.getSalesOrderCRMList4Sync(beginNum, endNum);
//				beginNum += Constants.DB_BATCH_DO_NUM;
//				endNum += Constants.DB_BATCH_DO_NUM;
//				if ( list == null || list.isEmpty() ) {
//					continue;
//				}
//				log.warn("SyncOrder every list.size():["+list.size()+"]");
//				listLength += list.size();
//				orderService.importOrder(list);
//				orderService.syncFromCRM();
//			}
//			if(listLength == 0){
//				response.getWriter().write("没有符合条件的订单需要导入!");
//				return;
//			}
//			response.getWriter().write("成功导入"+listLength+"条订单!");
//			return;
//		} catch (SQLException e) {
//			log.error("SQLException", e);
//		} catch (Exception e) {
//			log.error("Exception", e);
//		}
		log.info("SyncOrder/doPost() servlet请求结束!");
		return;
	}
	
	
	public static void main(String [] args){
		
	}
}
