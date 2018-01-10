/**@项目名称 aycrm
 * @类名称 bulkDeleteCrmData.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-25 下午6:01:18
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
import com.baihui.difu.service.ContactsService;
import com.baihui.difu.util.Constants;
import com.baihui.difu.util.PropertyManager;

/**@项目名称 aycrm
 * @类名称 bulkDeleteCrmData.java
 * @描述
 * @作者 dennis
 * @创建日期 2015-5-25 下午6:01:18
 */
public class bulkDeleteCrmData extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(deleteCrmData.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/text;charset=UTF-8");//返回的是txt文本文件
		StringBuffer result=new StringBuffer();
		DataToCrm dt=new DataToCrm();
		String idstr=request.getParameter("ids");
		System.out.println("idstridstridstr:"+idstr);
		String[] ids=null;
		ids=idstr.split(",");
		int s=0;
		for(int i=0;i<ids.length;i++){
			//删除CRM中的数据
			if("".equals(ids[i])||ids[i]==null){
				result.append("第"+(i+1)+"条ID为空,请联系管理员\n\t");
	//			response.getWriter().print(result);
				continue;
			}
			String token=PropertyManager.getProperty("CRM_TOKEN");
			try {
				s=dt.deleteByID(ids[i],Constants.MODULE_CONTACTS,token);//成功返回1
			} catch (JSONException e1) {
				e1.printStackTrace();
				result.append("第"+(i+1)+"条删除CRM失败,请联系管理员\n\t");
	//			response.getWriter().print(result);
				continue;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				result.append("第"+(i+1)+"条删除CRM失败,请联系管理员\n\t");
	//			response.getWriter().print(result);
				continue;
			}
			if(s==0){
				result.append("第"+(i+1)+"条CRM中删除失败,请联系管理员\n\t");
	//			response.getWriter().print(result);
				continue;
			}
			//删除数据库
			ContactsDao contactsDao=new ContactsDao();
			try {
				int c=0;
					c=contactsDao.deleteByBaiHuiID(ids[i]);
				if(c>0){
					log.info("成功删除一条数据");
				}
			} catch (Exception e) {
				result.append("第"+(i+1)+"条删除数据失败,请联系管理员\n\t");
	//			response.getWriter().print(result);
				e.printStackTrace();
				log.error("删除数据失败，baihuiID:"+ids[i]);
				continue;
			}
			//插入CRM提醒
			ContactsService contactsService=new ContactsService();
			try {
				contactsService.syncFromDelToCRM(ids[i]);
			} catch (SQLException e) {
				result.append("第"+(i+1)+"条插入CRM提醒失败,请联系管理员\n\t");
	//			response.getWriter().print(result);
				e.printStackTrace();
				log.error("插入CRM提醒失败，baihuiID:"+ids[i]);
			}
			result.append("第"+(i+1)+"条删除成功！\n\t");
			}
		response.getWriter().print(result);
	}
}
