/**@项目名称 aycrm
 * @类名称 deleteCrmData.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-21 下午1:37:26
 */
package com.baihui.difu.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.ContactsDao;
import com.baihui.difu.dao.LeadsDao;
import com.baihui.difu.entity.Contacts;
import com.baihui.difu.service.ContactsService;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;

/**@项目名称 aycrm
 * @类名称 deleteCrmData.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-21 下午1:37:26
 */
public class deleteCrmData extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(deleteCrmData.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ContactsDao contactsDao=new ContactsDao();
		response.setContentType("text/text;charset=UTF-8");//返回的是txt文本文件
		String result="";
		DataToCrm dt=new DataToCrm();
		String id=request.getParameter("id");
		String mobile=request.getParameter("mobile");
		String mobile2=request.getParameter("mobile2");
		String phone=request.getParameter("phone");
		String suoshuxiangmu=request.getParameter("suoshuxiangmu");
		System.out.println("m:"+mobile+"m2:"+mobile2+"phone:"+phone);
		int s=0;
		//删除CRM中的数据
		if("".equals(id)||id==null){
			result="ID为空,请联系管理员";
			response.getWriter().print(result);
			return;
		}
		List<Contacts> listContact=new ArrayList<Contacts>();
		try {
			listContact=contactsDao.getChongFuIds(id,mobile,mobile2,phone,suoshuxiangmu);
		} catch (NumberFormatException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}//获取重复id的其他重复信息
		String token=PropertyManager.getProperty("CRM_TOKEN");
		if(listContact.size()<1){
			result="找不到重复数据,请联系管理员";
			response.getWriter().print(result);
			return;
		}
		for(int m=0;m<listContact.size();m++){
			try {
				s=dt.deleteByID(listContact.get(m).getBaihuiid(),Constants.MODULE_CONTACTS,token);//成功返回1
			} catch (JSONException e1) {
				e1.printStackTrace();
				result="删除CRM失败,ID:"+listContact.get(m).getBaihuiid();
				response.getWriter().print(result);
				return;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				result="删除CRM失败,ID:"+listContact.get(m).getBaihuiid();
				response.getWriter().print(result);
				return;
			}
			if(s==0){
				result="CRM中删除失败,ID:"+listContact.get(m).getBaihuiid();
				response.getWriter().print(result);
				return;
			}
		
			//删除数据库
			try {
				int i=0;
					i=contactsDao.deleteByBaiHuiID(listContact.get(m).getBaihuiid());
				if(i>0){
					log.info("成功删除一条数据,ID:"+listContact.get(m).getBaihuiid());
				}
			} catch (Exception e) {
				result="删除成功";
				response.getWriter().print(result);
				e.printStackTrace();
				log.error("删除数据失败，baihuiID:"+listContact.get(m).getBaihuiid());
				return;
			}
		}
		//插入CRM提醒
		ContactsService contactsService=new ContactsService();
		try {
			contactsService.syncFromDelToCRM(id);
		} catch (SQLException e) {
			result="插入CRM提醒失败";
			response.getWriter().print(result);
			e.printStackTrace();
			log.error("插入CRM提醒失败，baihuiID:"+id);
		}
		result="删除成功!";
		response.getWriter().print(result);
	}
}
