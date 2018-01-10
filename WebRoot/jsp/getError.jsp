<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	if(session.getAttribute("errorresult")!=null){
		out.print(session.getAttribute("errorresult").toString()); 
	}else{
		System.out.println("获取\"errorresult\"数据失败");
	}
	
%>