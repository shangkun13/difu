<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.baihui.difu.util.PropertyManager"%>
<%@ page import="org.apache.log4j.Logger" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Logger log = Logger.getLogger(this.getClass());
log.info("thlogin validate.jsp begin");
log.info("caseId:["+request.getParameter("caseId")+"]");
log.info("owner:["+request.getParameter("owner")+"]");
log.info("thlogin validate.jsp end");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>订单导入</title>
<link rel="stylesheet" href="css/base.css" type="text/css"></link>
<link rel="stylesheet" href="css/index.css" type="text/css"></link>
<script src="js/jquery-1.4.4.min.js"></script>
<script src="js/ajaxfileupload.js"></script>
<script src="js/slides.min.jquery.js"></script>
<script>
var timer1;

//文件开始上传
function ajaxFileUpload() {
	//获取单选框的选项
	var orderType = $("input:radio[name='orderType'][checked]").val();
	if(confirm("请确认您选择的文件类型与上传文档的文件类型一致!")){
		$('#uploadMsg').html('<h2 color="red">开始上传文件!</h2>');
		//显示"文件开始上传"
		$('#uploadMsg').show();
		/*
		//jquery的ajax提交时“加载中”提示的处理方法
		$("#loading").ajaxStart(function() {
			$(this).show();
		}).ajaxComplete(function() {
			$(this).hide();
		});
		*/
		$.ajaxFileUpload( {
			url : '<%=path%>/file_upload.do?orderType='+orderType,////需要链接到服务器地址 
			secureuri : false,
			fileElementId : 'fileToUpload',//文件选择框的id属性  
			dataType : 'json',//服务器返回的格式，可以是json
			success : function(data, status) {
				$('#uploadMsg').html('<h2>'+data.msg+'</h2></p>');
				/*
				if (typeof (data.error) != 'undefined') {//如果data中返回的错误信息不是未定义undefined
					//数据有错误
					if (data.error != '') {
						//将错误提示显示在id='success'的div中
						$('#resultMsg').html('<h2 color="red">文件上传失败，请您稍后重试！</h2>');
					} else {//数据成功
						if (data.msg == "success") {
							//将div='uploadMsg'的div隐藏
							$('#uploadMsg').hide();
							$('#uploadMsg').html('<h2>文件开始上传，详细信息如下！</h2></p>');
							//setInterval()方法按照指定的周期来调用函数或计算表达式。
							//timer1 = setInterval("getResult()", 1000);//1秒钟刷新一次
							//上传成功
						} else {
							//上传失败
							$('#resultMsg').html("<h2 color='red'>"+data.msg+"</h2>");
							$('#resultMsg').show();
						}
					}
				}
				*/
			},
			error : function(data, status, e) {
				$('#resultMsg').html("<h2 color='red'>文件上传失败，请您稍后重试！</h2>");
				$('#resultMsg').show();
			}
		});
		return false;
	}
}
	
function getResult() {
	$.ajax( {
		type : "POST",
		url : "jsp/getResult.jsp",
		dataType : "text",
		data : {
			data : ''
		},
		success : function(result) {
			var hasDecodeResult = decodeURIComponent(result);
			if (hasDecodeResult.indexOf("所有数据导入完成！") != -1 || hasDecodeResult.indexOf("数据导入出错，请重试！") != -1) {
				clearInterval(timer1);
			}
			$('#resultMsg').html("<h2>" + hasDecodeResult + "</h2>");
			$('#resultMsg').show();
		},
		error : function(result) {
			$('#resultMsg').html("<h2 color='red'>" + hasDecodeResult + "</h2>");
			$('#resultMsg').show();
		}
	});
}
</script>
</head>
<body>
	<table width="85%" height="100%" border="0">
		<tr align="center" valign="middle">
			<td width="70%" colspan="7" align="center">
				<table width="70%" border="0" cellpadding="0" cellspacing="0"  id="tables" class="windows_600">
					<tr align="center">
						<td height="37" valign="top" align="left">
							  文件类型：
							<input type="radio" id="orderType" name="orderType" value="1" checked="checked"/> 经销商订单
							<input type="radio" id="purchaseType" name="orderType" value="0" /> 经销商采购
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
								<input type="file" name="fileToUpload" class="file0922" id="fileToUpload" size="28" 
									onchange="document.getElementById('textfield').value=this.value" />
								<input type="button" id="buttonUpload" onclick=" return ajaxFileUpload();" value="上传" 
									class="btn0922" style="background-color: #FFF; border: 1px solid #CDCDCD; height: 24px; width: 70px;" />
								</form>
							</div>
						</td>
					</tr>
					<tr>
						<td class="colorHui" height="80">
							仅支持<%=PropertyManager.getProperty("FILENAME_EXTENSION") %>文件
							<br /><br />
						</td>
					</tr>
					<tr align="left">
						<!-- 用来接收页面传递过来的状态 -->
						<td valign="top" align="left" id="uploadMsg" style="display: none">
							<h2>开始上传文件！</h2>
						</td>
					</tr>
					<tr align="left">
						<!-- 当所有数据导入完成时显示 -->
						<td valign="top" align="left" id="resultMsg">
							&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td width="30%" colspan="3" align="left" valign="top">
				<font style="font-size: 14px; line-height: 25px">&nbsp;<br />&nbsp;<br />&nbsp;<br />&nbsp;<br />注意事项：<br />
					1.请严格参考模板文件。<br /> 订单明细中各个产品请用回车符分割；<br />产品内部的编号，单价，数量用中文逗号（，）分割，并且不可缺少，单价和数量必须为数字；<br />
					订单金额必须为数字（如果为空，请填写0）；<br />下单时间的格式如下 2013-3-15 10:11:28。 <br />
					2.多个文件时，请一个一个上传，等第一个文件处理完成之后在进行第二个文件的处理。<br /> 3.文件不可以加密，否则无法识别。</font>
			</td>
		</tr>
	</table>
 </body>
</html>
