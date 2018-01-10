<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据导入</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gbk">  
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" href="css/base.css" type="text/css"></link>
	<script src="js/jquery-1.4.4.min.js"></script>
	<script src="js/ajaxfileupload.js"></script>
	<style type="text/css">
		.windows_600{
	padding:20px;
	width:600px;
	margin: auto;
}
.windows_600 .text18{ font-size:18px; font-family:"宋体"; line-height:50px;}
.windows_600 .button_g{ background-color:#5B9F01; width:180px; height:50px; margin:10px auto; padding:15px 25px; border-bottom:5px #060 solid; text-align:center}
.windows_600 .button_b{ background-color:#73A5D4; width:180px; height:50px; margin:10px auto; padding:15px 25px; border-bottom:5px #5680A7 solid; text-align:center}
.windows_600 .text_w{ color:#FFF;}
.windows_600 a{ text-decoration:none;}
.windows_600 a:hover{ text-decoration:none;}

.windows_600 h2{ font-size:14px;  text-align:center; line-height:30px;}
.windows_600 p{ color:#000; font-size:12px; text-align:center; line-height:24px;}

* {
	padding-bottom: 0px;
	margin: 0px;
	padding-left: 0px;
	padding-right: 0px;
	font-family: "微软雅黑", Tahoma;
	font-size: 12px;
	padding-top: 0px;
}

.colorHui {
	color: #959595;
}

.f14 {
	font-size: 14px;
}

.l30 {
	line-height: 30px;
}

.bg_31 {
	margin: auto;
	width: 1000px;
	background: url(../images/mail/bg_10.gif) #fff repeat-x;
	padding-top: 10px;
}

* {
	padding-bottom: 0px;
	margin: 0px;
	padding-left: 0px;
	padding-right: 0px;
	font-family: "宋体";
	PADDING-TOP: 0px;
}

INPUT {
	padding-bottom: 0px;
	margin: 0px;
	padding-left: 0px;
	padding-right: 0px;
	vertical-align: middle;
	padding-top: 0px;
}

.file-box0922 {
	position: relative;
	width: 340px;
}

.txt0922 {
	border-bottom: #cdcdcd 1px solid;
	border-left: #cdcdcd 1px solid;
	width: 180px;
	height: 22px;
	border-top: #cdcdcd 1px solid;
	border-right: #cdcdcd 1px solid;
}

img {
	border: 0;
}

.btn0922 {
	border-bottom: #cdcdcd 1px solid;
	border-left: #cdcdcd 1px solid;
	background-color: #fff;
	width: 70px;
	height: 24px;
	border-top: #cdcdcd 1px solid;
	border-right: #cdcdcd 1px solid;
}

.file0922 {
	position: absolute;
	filter: alpha(opacity : 0);
	WIDTH: 260px;
	height: 24px;
	top: 5px;
	right: 80px;
	opacity: 0;
}
	
	</style>
	<script>
		var timer1;

		//文件开始上传
		function ajaxFileUpload() {
			//获取下拉框的选项
			var _orderType="";
			var names =document.getElementsByName("orderType");
			for(var i=0;i<names.length;i++)
			{
			    if(names[i].checked==true)
				{
			        _orderType =names[i].value;
			        break;
			    }
			 }
			
			if(_orderType==-1){
				alert("请选择上传文件的类型");
				return;
			}
			if(confirm("请确认您选择的文件类型与上传文档的文件类型一致!")){
				
				//显示"文件开始上传"
				document.getElementById("upload1").style.display = "block";
				
				//jquery的ajax提交时“加载中”提示的处理方法
				$("#loading").ajaxStart(function() {
					$(this).show();
				}).ajaxComplete(function() {
					$(this).hide();
				});
				$.ajaxFileUpload( {
					url : '/difu/file_upload.do?orderType='+_orderType,////需要链接到服务器地址 
					secureuri : false,
					fileElementId : 'fileToUpload',//文件选择框的id属性  
					dataType : 'json',//服务器返回的格式，可以是json
					success : function(data, status)//相当于java中try语句块的用法
					{
						if (typeof (data.error) != 'undefined') {//如果data中返回的错误信息不是未定义undefined
							//让id='success'的div显示 
							document.getElementById("success").style.display = "block";
							//数据有错误
							if (data.error != '') {
								//将错误提示显示在id='success'的div中
								
								document.getElementById("success").innerHTML = '<h2>文件上传失败，请您稍后重试！</h2>';
							} else {//数据成功
								if (data.msg == "success") {
									var counts=data.count.split(",");
									//将div='upload1'的div隐藏
									document.getElementById("upload1").style.display = "none";
									document.getElementById("upload").innerHTML = '<h2>文件上传成功!共'+counts[0]+'条数据，成功导入'+counts[1]+'条，详细信息请点击右侧查看日志</h2></p>';
									//setInterval()方法按照指定的周期来调用函数或计算表达式。
// 									timer1 = setInterval("getResult()", 1000);//1秒钟刷新一次
									
									//上传成功
								} else {
									//上传失败
									alert(data.msg);
								}
							}
						}
					},
					error : function(data, status, e) {
						document.getElementById("upload1").style.display = "none";
						document.getElementById("upload").innerHTML = '<h2>文件上传失败，详细信息如下！</h2></p>';
						document.getElementById("upload").style.display="block";
						timer1 = setInterval("getErrorResult()", 1000);//1秒钟刷新一次
					}
				});
				return false;
			}
		}
		function getResult() {
			$.ajax( {
				type : "POST",
				url : "/difu/jsp/getResult.jsp",
				dataType : "text",
				data : {
					data : ''
				},
				success : function(result) {
					//alert(decodeURIComponent(result));
					document.getElementById("success").innerHTML = "<h2>" + decodeURIComponent(result) + "</h2>";
					if (decodeURIComponent(result).indexOf("所有数据导入完成！") != -1 || decodeURIComponent(result).indexOf("数据导入出错，请重试！") != -1) {
						//clearInterval动作的作用是清楚对setInterval函数的调用
						clearInterval(timer1);
						//让id='success'的div显示 
						document.getElementById("success").style.display = "block";
						//将错误提示显示在id='success'的div中
						//document.getElementById("success").innerHTML = '<h2>数据导入出错，请您稍后重试！</h2>';
						document.getElementById("success").innerHTML = '<h2>'+decodeURIComponent(result)+'</h2>';
					}
				},
				error : function(result) {
					alert(result + " error ");
				}
			});
		}
		
		function getErrorResult() {
			$.ajax( {
				type : "POST",
				url : "/difu/jsp/getError.jsp",
				dataType : "text",
				data : {
					data : ''
				},
				success : function(result) {
					//document.getElementById("success").innerHTML = "<h2>" + decodeURIComponent(result) + "</h2>";
					if (decodeURIComponent(result).indexOf("错误提示:") != -1) {
						//clearInterval动作的作用是清楚对setInterval函数的调用
						clearInterval(timer1);
						//让id='success'的div显示 
						document.getElementById("upload").style.display = "block";
						//将错误提示显示在id='success'的div中
						//document.getElementById("success").innerHTML = '<h2>数据导入出错，请您稍后重试！</h2>';
						document.getElementById("upload").innerHTML = '<h2>'+decodeURIComponent(result)+'</h2>';
					}
				},
				error : function(result) {
					alert(result + " error ");
				}
			});
		}
		</script>

		<script>
// 			function getemail() {
// 				var s = document.createElement('SCRIPT');
// 				s.src = 'http://ipassport.baihui.com/ticket/getloginuser.jsp?';
// 				document.body.appendChild(s);
// 			}
			<%--
			function email_call(email) {
				var s = document.createElement('SCRIPT');
				s.src = 'adduser.jsp?data=' + email;
				document.body.appendChild(s);
			
			}
		--%></script>
  </head>
  
  <body>
		<!--content s-->
		<table width="85%" height="100%" border="0">
			<tr align="center" valign="middle">
				<td width="70%" colspan="7" align="center">
					<table width="70%" border="0" cellpadding="0" cellspacing="0"  id="tables" class="windows_600">
						
						<tr align="center">
							<td height="37" valign="top" align="left">
								
								<%--<select id="orderType" name="orderType" size = "1">
									<option value="-1">请选择上传文件的类型</option>
									<option value="0">经销商采购</option>
									<option value="1">销售出库</option>
								</select>
								
								  
								  --%>
								  <form id="radioForm" name="radioForm">
								  文件类型：
									  <input type="radio" id="orderType" name="orderType" value="1" checked="checked"/> 手机电话修改
      							  </form>
							</td>
						</tr>
						
						<tr align="center">
							<td height="37" valign="top" align="left">
								上传订单数据文件:
							</td>
						</tr>
						<tr>
							<td height="70" align="center">
								<div class="file-box0922" id="upload">
									<form action="#" method="post" enctype="multipart/form-data">
										<input type='text' name='textfield' id='textfield' class='txt0922' />
										<input type='button' class='btn0922' value='浏览...' />
										<input type="file" name="fileToUpload" class="file0922" id="fileToUpload" size="28" onchange="document.getElementById('textfield').value=this.value" onchange="load()" />
										<input type="button" id="buttonUpload" onclick=" return ajaxFileUpload();" value="上传" class="btn0922" style="background-color: #FFF; border: 1px solid #CDCDCD; height: 24px; width: 70px;" />
									</form>
								</div>
							</td>
						</tr>
						<tr>
							<td class="colorHui" height="80">
								仅支持XLS文件
								<br /><br />
							</td>
						</tr>
						<tr align="left">
							<!-- 用来接收页面传递过来的状态 -->
							<td valign="top" align="left" id="upload1" style="display: none">
								<h2>开始上传文件！</h2>
							</td>
						</tr>
						<tr align="left">
							<!-- 当所有数据导入完成时显示 -->
							<td valign="top" align="left" id="success">
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
				<td width="30%" colspan="3" align="left" valign="top">
					<font style="font-size: 14px; line-height: 25px">&nbsp;<br />&nbsp;<br />&nbsp;<br />&nbsp;<br />注意事项：<br />
							1. 请严格参考模板文件。<br />
							2. 必填项不可为空<br />
							3. 日期格式如下 2013-3-15 10:11:28。<br />
							4. 金额、数量必须为数字（如果空，请填写0）<br />
							5. 导入模块均与CRM中系通过对应模块为准，系统中有的信息会被调出，没有的信息将被作为一条新的记录添加进去。<br />
							6. 多个文件时，请一个一个上传，等第一个文件处理完成之后在进行第二个文件的处理。<br />
							7. 文件不可以加密，否则无法识别。<br />
							8. 导入修改数据时，按照模板填写，线索编号和联系人编号是必填的，如果手机或电话不填就不修改。<br />
							9. 如果要修改的手机号已经存在，则不修改手机号。<br />
							<a href="update.xls" target="_blank">下载修改模板</a>
							</br>
							<a href="Import.txt" target="_blank">查看日志</a>
					</font>
				</td>
			</tr>
		</table>
  </body>
</html>
