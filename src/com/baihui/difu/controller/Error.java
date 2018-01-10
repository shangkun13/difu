package com.baihui.difu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.baihui.difu.util.Util;

public class Error extends Thread{
	private String string="";
	private HttpServletResponse response=null;
	private HttpServletRequest request=null;
	private ServletContext context=null;
	private HttpSession session=null;

	 public Error(HttpServletResponse response, HttpServletRequest request) {
			this.response = response;
			this.request = request;
			this.context=request.getSession().getServletContext();
			this.session=request.getSession(true);
			
	}
	 
	 
	 public void run(){
		  string+="错误提示:";
	      string+=Util.tip_error;setCookie();
	 }
	void setCookie() {
		  
		  try {
			  session.setAttribute("errorresult", URLEncoder.encode(string,"utf-8"));
			  Cookie cookie=new Cookie("errorresult", URLEncoder.encode(string,"utf-8"));
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
