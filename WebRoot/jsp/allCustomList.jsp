<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.baihui.difu.entity.Contacts"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<script src="js/jquery-1.4.4.min.js"></script>
    <base href="<%=basePath%>">
    
    <title>所有重复数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/common.css" rel="stylesheet" type="text/css" />
	<link href="css/global.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
  <br/>
  <br/>
  <!-- 
  <tr><td><input type="button" style="width: 100px;height: 30px;line-height: 30px;margin-left: 12px" value="批量删除" onclick="deleteAllCheck();"/></td></tr>
 --> 
<div style="margin:10px;">
<table style="table-layout:fixed;" class="bigtable1">
<tr class="bgc_f7f7f7">
<th width="5%">序号</th>
<th width="10%">姓名</th>
<th width="10%">手机</th>
<th width="10%">手机2</th>
<th width="10%">电话</th>
<th width="10%">邮箱</th>
<th width="30%">留言内容</th>
<th width="20%">资源时间</th>
<th width="10%">所属项目</th>
<th width="10%">所有者</th>
<th width="10%">操作</th>
</tr>
 <% 
 	int cpage=(Integer)request.getAttribute("cpage");
 	int pageSize=(Integer)request.getAttribute("pageSize");
 	int count=(Integer)request.getAttribute("count");
 	int pageCount=0;
 	if(count%pageSize==0){
 		pageCount=count/pageSize;
 	}else{
 		pageCount=count/pageSize+1;
 	}
 	
     List<Contacts> list =(List<Contacts>)request.getAttribute("list");
      for(int i=0;i<list.size();i++)
      {
     %>
<tr>
<td><input type="checkbox"  name="checkbox" id="checkbox" value="<%=list.get(i).getBaihuiid()%>"/></td>
<td><%=list.get(i).getName()%></td>
<td><%=list.get(i).getMobile()%></td>
<td><%=list.get(i).getShouji2()%></td>
<td><%=list.get(i).getPhone()%></td>
<td><%=list.get(i).getEmail()%></td>
<td><%=list.get(i).getContext()%></td>
<td><%=list.get(i).getZyDate()%></td>
<td><%=list.get(i).getSuoshuxiangmu()%></td>
<td><%=list.get(i).getOwnnerName()%></td>
<td><a href="javascript:deleteByID('<%=list.get(i).getBaihuiid()%>','<%=list.get(i).getMobile()%>','<%=list.get(i).getShouji2()%>','<%=list.get(i).getPhone()%>','<%=list.get(i).getSuoshuxiangmu()%>');">保留</a></td>
</tr>
<%
} 
%>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="number">
<tr>
					<td align="right"><% if(cpage!=0&&cpage!=1){ %>
    			<a style="color: blue" href="javascript:lastPage();" >上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<%} %>
					</td>
					<td>
						共<%=count %>条        第<input style="width: 30" type="text" id="cpage" name="cpage" value=<%=cpage %> />页，共 <%=pageCount %>页    每页显示<input style="width: 30" type="text" id="pageSize" name="pageSize" value=<%=pageSize %> />条&nbsp;&nbsp;<input type="button" id="go" value="go" onclick="go();"/>
					</td>
					<td>
					<% if(cpage!=pageCount) {%>
    			<a style="color: blue" href="javaScript:nextPage();" >下一页
    			</a>
    			<%} %>
					</td>
				</tr>
			</table>
</div>
<div class="h50"></div>
<div id="overlay_" style="display:none;position:fixed;width:100%;height:100%;top:0px;left:0px;background:#333333;filter:alpha(opacity=30);-moz-opacity:0.30;opacity:0.30;">
	<div id="loading" style="position:fixed;width:32;height:32;top:50%;left:50%;margin-left:-16px;margin-top:-16px;">
		<img src="jsp/loading.gif">
	</div>
</div>
<script>

document.onkeydown = function(e){
   if (event.keyCode == 13) {
      document.getElementById("go").click();
   }
}
function nextPage(){
	window.location.href="queryAll.do?cpage="+ (parseInt($("#cpage").val())+1)+"&pageSize="+$("#pageSize").val();
}
function lastPage(){
	window.location.href="queryAll.do?cpage="+ (parseInt($("#cpage").val())-1)+"&pageSize="+$("#pageSize").val();
}
function go(){
	window.location.href="queryAll.do?cpage="+$("#cpage").val()+"&pageSize="+$("#pageSize").val();
}
function deleteByID(id,mobile,mobile2,phone,suoshuxiangmu){
	if(id==""||id=="null"){
		alert("该数据没有百会ＩＤ，数据错误");
		return;
	}
	 if (!confirm("确认要删除除了所选记录其他重复数据吗？")) {
         return;
     }
	$.ajax({
		type:'POST',
		url:'deleteCrmData.do?id='+id+'&mobile='+mobile+'&mobile2='+mobile2+'&phone='+phone+'&suoshuxiangmu='+suoshuxiangmu,
		success:function(data){
			alert(data);
			window.location.href="queryAll.do?cpage="+$("#cpage").val()+"&pageSize="+$("#pageSize").val();
		}
	});
}

function deleteAllCheck(){
	var message="";
	 if (!confirm("确认要删除所选记录？")) {
	 return;
		}
	 var str =[];    
	  $('input[name="checkbox"]:checked').each(function(){    
		  str.push($(this).val());    
	  });    
	     	$.ajax({
	 		type:'POST',
	 		url:'bulkDeleteCrmData.do?ids='+str,
	 		success:function(data){
	 			alert(data);
	 			 $("#overlay_").show();
	 			 window.location.href="queryAll.do?cpage="+$("#cpage").val()+"&pageSize="+$("#pageSize").val();
	 		}
	 	}); 
	  
	 
}

</script>
  </body>
</html>
