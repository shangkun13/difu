<%@page import="java.net.URLDecoder"%><%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger" %>
<%
Logger log = Logger.getLogger(this.getClass());
log.info("test.jsp begin");
log.info("caseId:["+request.getParameter("caseId")+"]");
log.info("owner:["+request.getParameter("owner")+"]");
log.info("test.jsp end");
%>