<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int seq = 0;
String user_email = request.getParameter("user_email");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>反馈信息</title>
<script src="<%=basePath%>js/jquery.js"></script>
<script type="text/javascript">
	$(function() {
	  
	      $("#submitbtn").click(function(){ 
                 var content = $("#content").val();
                 var user_email = $("#user_email").val();
                 $.ajax({
					type: "POST",  
	                data:{content:content,userEmail:user_email},
	                dataType: "json",  
	                url: "sysInfoAction!feedback.action",  
					    success: function(data){
					    	alert(data.result);
					    }
					});
	      });
	      	      
	});
</script>
  </head>
  <body>
    <input type="hidden" id="user_email" name="user_email"  value="<%=user_email %>" />
    <div style="TEXT-ALIGN:center">
    <p/>
    <span>意见反馈信息</span>
   <p/><p/>
   <textarea id="content" name="content" cols="100" rows="20"></textarea>
   <p/>
    <input type="button"  id="submitbtn" value="提交"/>
    </div>
  </body>
</html>
