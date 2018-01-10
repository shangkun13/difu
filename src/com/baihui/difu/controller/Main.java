package com.baihui.difu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.baihui.difu.util.Util;


public class Main extends Thread {
	
	private String string="";
	private HttpServletResponse response=null;
	private HttpServletRequest request=null;
	private ServletContext context=null;
	private HttpSession session=null;

	 public Main(HttpServletResponse response, HttpServletRequest request) {
			this.response = response;
			this.request = request;
			this.context=request.getSession().getServletContext();
			this.session=request.getSession(true);
			
	}
	 
	 
	 public void run(){
	       string="文件上传成功！";setCookie();
	       string+="<br/>开始解析文件内容！";setCookie();
		try {
			string+=Util.message_error;setCookie();
			string+="<br/>所有数据导入完成！";setCookie();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			string+="<br/>数据导入出错，请重试！" +e.getMessage();
			e.printStackTrace();
		}
	 }
	void setCookie() {
		  
		  try {
			  session.setAttribute("uploadresult", URLEncoder.encode(string,"utf-8"));
			  Cookie cookie=new Cookie("uploadresult", URLEncoder.encode(string,"utf-8"));
			  cookie.setDomain("baihui.com");
			  cookie.setPath("/");
			  cookie.setMaxAge(1000*60*30);
			  response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public ServletContext getContext() {
		return context;
	}


	public void setContext(ServletContext context) {
		this.context = context;
	}
}
