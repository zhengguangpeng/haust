<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录反垃圾邮件系统</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>

<body >

<div class="topBar">
<span class="pos_r"><span class="pageIcon flow_02"></span></span>
<span class="posNow"> Welcome</span>
</div>
<div class="mainBox" >
     
	 
	<ul>
		当前SessionID :<%=session.getId()%>
	
	</ul>
</div>

</body>
</html>
