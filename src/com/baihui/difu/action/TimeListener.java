package com.baihui.difu.action;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;


public class TimeListener implements ServletContextListener{
	public static Logger log = Logger.getLogger(TimeListener.class);
	private Timer timer;
	public void contextDestroyed(ServletContextEvent event) {
	    if (timer != null) {
	        timer.cancel();
	    }
	}
	public void contextInitialized(ServletContextEvent event) {
		if(event.getServletContext().getAttribute("difu_TIMELISTNER") == null){
			event.getServletContext().setAttribute("difu_TIMELISTNER", "1");
		}else{
			return;
		}
		
		log.info("[com.baihui.difu.action.TimeListener]  timer task begin");
		timer = new Timer(true);
		timer.schedule(new JobTime(event.getServletContext()), 6*1000);
//		timer = new Timer(true);
//		timer.schedule(new JobUserTime(event.getServletContext()), 15*1000);
		log.info("[com.baihui.difu.action.TimeListener]  timer task end");
	}
}
