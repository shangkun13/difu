<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.baihui.difu.util.DB" %>
<%@ page import="com.baihui.difu.entity.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%
		Order order=new Order();
		order.setDate(new Date());
		order.setDepartment("技术部");
		order.setOrderNo("1234");
		order.setPurchaseUnit("个");
		
		
		
	%>

  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
