package com.baihui.difu.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4JInit extends HttpServlet {

	private static final long serialVersionUID = -7224186210183620161L;

	@Override
	public void init() throws ServletException {
		String fileName = this.getInitParameter("Log4jpro");
		System.out.println("Log4j配置文件所在的位置是：" + fileName);
		String file = this.getServletContext().getRealPath("/") + fileName;
		System.out.println("Log4j配置文件的位置是:<-------" + file);

		if (fileName != null) {
			System.out.println("Log4j开始启动 : ----------------");
			PropertyConfigurator.configure(file);// 这句很关键
		} else {
			System.out.println("Log4j启动失败!");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

}
