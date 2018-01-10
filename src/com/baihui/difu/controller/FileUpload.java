package com.baihui.difu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.DiskFileUpload;
import org.apache.tomcat.util.http.fileupload.FileItem;

import com.baihui.difu.biz.ContactsImpl;
import com.baihui.difu.biz.LeadsImpl;
import com.baihui.difu.entity.Contacts;
import com.baihui.difu.entity.Leads;
import com.baihui.difu.util.ParseExcel;
import com.baihui.difu.util.PropertyManager;
import com.baihui.difu.util.Util;
/**
 * 接受页面上传的excel文件,并对文件进行解析
 * @author luoxl
 *
 */

public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Logger log=Logger.getLogger(FileUpload.class);
	
	public FileUpload() {
		super();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("servlet请求开始!");
		HttpSession session = request.getSession();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		int colomuNum=10;//在excel表中有多少列
		String error = "";
		String msg = "";
		InputStream fStream = null;
		String userName = PropertyManager.getProperty("baihui_USERNAME");
		//获取类型 线索导入 0，手机和电话修改  1。
		String orderType=request.getParameter("orderType");
		int type=-1;
		if(!"".equals(orderType)){
			type=Integer.parseInt(orderType);
		}
		/**
		 * 将提示信息内容置空
		 */
		Util.message_error="";
		Util.message_normal="";
		Util.message_warn="";
		Util.tip_error="";
		
		long fileMaxSize = Long.valueOf(PropertyManager.getProperty("FILE_MAX_SIZE"));
		String excelExtension = PropertyManager.getProperty("FILENAME_EXTENSION");
		String [] excelExtensionArr = excelExtension.split(",");
		try {
			DiskFileUpload upload = new DiskFileUpload();
			upload.setHeaderEncoding("UTF-8");
			//上传文件数量
			List<FileItem> list = upload.parseRequest(request);
			if (list == null || list.isEmpty() ){
				Util.tip_error += "<br />没有数据";
				return;
			}
			int length = list.size();
			log.info("共有"+length+"条数据.");
			for (int i = 0; i < length; i++) {
				FileItem item = list.get(i);
				if ( item.isFormField() ){
					continue;
				}
				String fileName = item.getName();
				log.info("fileName:["+fileName+"]");
				if(fileName == null || "".equals(fileName.trim()) ){
					continue;
				}
				// 截取文件名的后四位字符 
				String fileType = fileName.substring(fileName.lastIndexOf("."));
				//如果其中某一个文件格式不正确,则跳出该循环(我们这里指定的格式是".xls"
				if ( !".xls".equals(fileType) ){
					msg = "第"+(i+1)+"个文件格式不正确或文件不存在";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				
				if(item.getSize() >  fileMaxSize){
					msg="文件大小不能超过"+(fileMaxSize/1024/1024)+"MB";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				boolean fileNameIsAllow = false;
				for(String fileExtension : excelExtensionArr){
					if( fileExtension.equals(fileType) ){
						fileNameIsAllow = true;
						break;
					}
				}
				if (!fileNameIsAllow) {
					msg="该文件类型不允许上传,只能上传"+excelExtension+"的格式文件";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				fStream = item.getInputStream();
				System.out.println("fStream:"+fStream);
				//判断文件是否为空
				if (fStream == null) {
					msg = "文件不能为空";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				//1.读取excel中所有sheet页中的数据,将每一行数据存入到list中,返回.
				ParseExcel parseExcel = new ParseExcel();
				List<List<String>> list1 =parseExcel.readExcel(type,fStream,colomuNum);
				//判断文件是否为空
				if (list1.size() ==0) {
					msg = "文件内容不能为空";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				if(!"".equals(Util.tip_error)){
					msg = Util.tip_error;
					session.setAttribute("uploadresult", msg);
					continue ;
				}
				//线索
				if(type==0){
					ContactsImpl contactImpl=new ContactsImpl();
					
					//2.将数据组装成List<Contacts>
					List<Contacts> contactsList=(List<Contacts>) contactImpl.getContactsByExcelList(list1,request);
					
					//3.拷贝数据到CRM
					contactImpl.copyData(contactsList,type,userName);
					msg="success";
				}else if(type==1){//修改手机和电话
					
				}else{
					msg = "订单类型不正确,请选择上传文件的订单类型!";
					session.setAttribute("uploadresult", msg);
					continue;
				}
				// new Main(response, request).start();
				//在start()方法中调用了run()方法,而我们在Main类中重写了run()方法
				//new Main(response, request).start();
				if( msg.equals("")){
					msg = "上传文件成功!";
				}
			}
		} catch (Exception e) {
			msg = "系统错误，上传失败，请稍后再试！";
			e.printStackTrace();
			log.error("FileUpload.java/upload()/e:",e);
			log.info("servlet请求 结束!");
		}
		try {
			if(session.getAttribute("uploadresult")==null||"null".equals(session.getAttribute("uploadresult"))){
				session.setAttribute("uploadresult", "success");
				};
			response.getWriter().write("{\"error\":\""+ error + "\", \"msg\":\"" + msg + "\",\"count\":\"" + Util.message_normal + "\"}");
			log.info("FileUpload.java/upload() method beginning!");
			log.info("servlet请求结束!");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("FileUpload.java/upload()/e:",e);
			log.info("FileUpload.java/upload() method end!");
			log.info("servlet请求结束!");
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	
	public static void main(String [] args){
	}
}
