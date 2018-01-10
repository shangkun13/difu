package com.baihui.difu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.baihui.difu.dao.ContactsDao;
import com.baihui.difu.dao.LeadsDao;

public class DeleteByBaiHuiID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(DeleteByBaiHuiID.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LeadsDao leadsDao=new LeadsDao();
		ContactsDao contactsDao=new ContactsDao();
		String baihuiID=request.getParameter("baihuiID");
		try {
			int i=0;
				i=contactsDao.deleteByBaiHuiID(baihuiID);
			if(i>0){
				log.info("CRMwebHooK成功删除一条数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("CRMwebHooK删除数据失败，baihuiID:"+baihuiID);
		}
	}
}
