<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	System.out.println((String)session.getAttribute("uploadresult"));
	out.print((String)session.getAttribute("uploadresult")); 
%>