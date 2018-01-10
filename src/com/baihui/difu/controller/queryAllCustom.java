/**@项目名称 aycrm
 * @类名称 queryAllCustom.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-19 下午1:50:18
 */
package com.baihui.difu.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.baihui.difu.dao.ContactsDao;
import com.baihui.difu.entity.Contacts;

/**@项目名称 aycrm
 * @类名称 queryAllCustom.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-19 下午1:50:18
 */
public class queryAllCustom extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(queryAllCustom.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ContactsDao contactsDao=new ContactsDao();
		int cpage=1;
		int pageSize = 50;
		int count=0;
		try {
			count=contactsDao.queryCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if("".equals(request.getParameter("cpage"))||request.getParameter("cpage")==null){
			cpage=1;
		}else{
			cpage=Integer.parseInt(request.getParameter("cpage"));
		}
		if("".equals(request.getParameter("pageSize"))||request.getParameter("pageSize")==null){
			pageSize=50 ;
		}else{
			pageSize=Integer.parseInt(request.getParameter("pageSize"));
		}
		System.out.println("pageSize:::"+pageSize);
		System.out.println("cpage:::"+cpage);
		int startIndex = (cpage-1)*pageSize;
		try {
			System.out.println("sss:"+startIndex);
			List<Contacts> list =contactsDao.queryAllByPage(startIndex, pageSize);
			request.setAttribute("list", list);
			request.setAttribute("cpage", cpage);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("count", count);
			 RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/allCustomList.jsp");    // 使用req对象获取RequestDispatcher对象
		        dispatcher.forward(request, response);   
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
