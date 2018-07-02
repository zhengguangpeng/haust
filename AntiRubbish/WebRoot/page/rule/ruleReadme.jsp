<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录反垃圾邮件系统</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />

<script  type="text/javascript">	

function  confirmApply(){ 
	str= "谨慎,请确认是否修改？";
  	if (confirm(str)) {
  		document.valveControl.submit(); 						
  	 }  	
} 

</script>
</head>

<body >

<div class="topBar">
<span class="pos_r"><span class="pageIcon flow_02"></span></span>
<span class="posNow"> Welcome</span>
</div>
<div class="mainBox" >
     
	<% HttpSession s= request.getSession(); %>
	<ul>
		 当前session Id: <%= s.getId() %>
	
	</ul>
	<!--  
	<form name="valveControl" id="valveControl" action="ruleAction!valveControl.action" method="post">
	<p style="height:30px"></p>
    <label >
       <span>
       	   反垃圾邮件服务器总阀门:
       	   <select name="control"  id="control" >
       	 	  <option value="-1">请选择</option>
       	 	  <option value="0" <c:if test="${valve == 0}">selected="selected"</c:if>>停止运行</option>
       	 	  <option value="1" <c:if test="${valve == 1}">selected="selected"</c:if>>试运行</option>
       	 	  <option value="2"  <c:if test="${valve == 2}"> selected="selected"</c:if>>正式运行</option>	                           
	       </select>
		</span>       
    </label>
    
     <a  onclick="confirmApply()">确认修改</a>  
     </form>-->
</div>

</body>
</html>
