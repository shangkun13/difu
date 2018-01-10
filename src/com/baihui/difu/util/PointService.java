package com.baihui.difu.util;


import org.apache.log4j.Logger;

import com.baihui.difu.service.ContactsService;
import com.baihui.difu.service.LeadsService;
import com.baihui.difu.service.SyncRecordTimeService;
import com.baihui.difu.service.UserService;

public class PointService {
	public static Logger log = Logger.getLogger(PointService.class);
	SyncRecordTimeService syncRecordTimeService = null;
	private LeadsService leadsService = null;
	private ContactsService contactService = null;
	private UserService userService=null;
	private boolean autoFlag = true; 
	private boolean userAutoFlag = true; 
	private boolean isRun = false;
	private boolean userIsRun = false;
	/**
	 * 循环执行
	 * @throws InterruptedException 
	 */
	public synchronized void cricle(String ... operation){
		if ( operation != null && operation.length >= 1 
				&& "stop".equals(operation[0]) ) {
			autoFlag = false;
		}
		if(autoFlag){
			if(isRun){
				log.warn("已经有同步进程在同步.");
				return;
			}
		}
		log.info("autoFlag:["+autoFlag+"]");
		while(autoFlag){
			isRun = true;
//			log.warn("开始同步线索数据");
//			getLeads();
			log.warn("开始同步联系人数据");
			getContacts();
			isRun = false;
			try {
				//休眠5分钟
				log.warn("同步完成休眠10分钟 ， cricle sleep 10*60*1000");
				Thread.sleep(10*60*1000);
			} catch (InterruptedException e) {
				isRun = false;
				log.error("休眠异常:error",e);
			}
		}
	}
	/**
	 * 循环执行
	 * @throws InterruptedException 
	 */
	public synchronized void doUser(String ... operation){
		if ( operation != null && operation.length >= 1 
				&& "stop".equals(operation[0]) ) {
			userAutoFlag = false;
		}
		if(userAutoFlag){
			if(userIsRun){
				log.warn("已经有同步用户进程在同步.");
				return;
			}
		}
		log.info("userAutoFlag:["+userAutoFlag+"]");
		while(userAutoFlag){
			userIsRun = true;
			getUses();
			try {
				//休眠四个小时
				log.warn("doUser cricle sleep 4*60*60*1000");
				Thread.sleep(4*60*60*1000);
			} catch (InterruptedException e) {
				userIsRun = false;
				log.error("休眠异常:error",e);
			}
		}
	}
	
	
	
	/**
	 * 获取线索模块
	 */
	public void getLeads(){
		log.info("getLeads() method begin--请求开始");
		
		//标识是否同步积分完成,pointStatus = 1未完成
		//request.setAttribute("pointStatus","1");
		//同步线索
		leadsService = new LeadsService();
		try {
			leadsService.syncFromCRM();
		} catch (Exception e) {
			log.error("getContactPoint contactService.syncFromCRM() error", e);
		}
		log.info("PointService/PointService() method end--请求结束!");
	}
	
	/**
	 * 获取用户模块
	 */
	public void getUses(){
		log.info("getLeads() method begin--请求开始");
		
		//标识是否同步积分完成,pointStatus = 1未完成
		//request.setAttribute("pointStatus","1");
		//同步线索
		userService = new UserService();
		try {
			userService.batchInsertToDB();
		} catch (Exception e) {
			log.error("getContactPoint contactService.syncFromCRM() error", e);
		}
		log.info("PointService/PointService() method end--请求结束!");
	}
	
	
	/**
	 * 获取联系人模块
	 */
	public void getContacts(){
		log.info("getContacts() method begin--请求开始");
		//同步联系人
		contactService = new ContactsService();
		try {
			contactService.syncFromCRM();
		} catch (Exception e) {
			log.error("同步联系人数据出错", e);
		}
		log.info("getContacts/getContacts() method end--请求结束!");
	}
	
	
}
