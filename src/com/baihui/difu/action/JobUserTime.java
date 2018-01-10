package com.baihui.difu.action;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.baihui.difu.util.PointService;

public class JobUserTime  extends TimerTask{
	public static Logger log = Logger.getLogger(JobUserTime.class);
	private ServletContext servletContext;

	public JobUserTime(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	@Override
	public void run() {
		log.info("JobTime.java/run() 定时任务开始");
		try {
			new PointService().doUser();
		} catch (Exception e) {
			log.error("JobTime.java/run() error",e);
			return;
		}finally{
			log.info("JobTime.java/run() 定时任务结束");
		}
	}
	public ServletContext getServletContext() {
		return servletContext;
	}
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
