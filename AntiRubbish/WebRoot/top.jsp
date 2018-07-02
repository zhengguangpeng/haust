<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>头部</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/jquery.js"></script>
<script  type="text/javascript">
$(function() {

    $("#logout").click(function(){
       parent.location.href="login!logout.action";
    });
});
</script>
</head>

<body>
<div class="header">
	<a class="logo"></a>
	<div class="loginInfo">
    	<ul class="loginP">欢迎您，${sessionScope.monitorUser }！</ul>
        <a class="logout" title="注销"  id="logout" ></a>
    </div>
</div>
</body>
</html>
