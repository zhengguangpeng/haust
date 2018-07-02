<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int seq = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间规则修改</title>
<link href="<%=basePath%>css/style.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/jquery.js"></script>
<script src="<%=basePath%>js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">	
$(function() {
	//名称不能修改
	$("#name").attr("readonly",true);
	
	});
</script>

		
</head>

<body>
<div class="topBar">
<span class="pos_r"><span class="pageIcon flow_02"></span></span>
<span class="posNow"> 规则管理 >修改规则</span>
</div>
<div class="mainBox">
  <form action="ruleAction!timeRuleList.action">
	       <input type="hidden"  name="ruleID"  value="${ruleID}"/>
	       <input type="hidden"  name="page.currentpage"  value="${pageNum}"/>
      <div class="p10 borderAll bg_F5F6FB">
      
      	<label class="pr10">
        <span>
         	规则名称: <input style="width:400px" type="text"   id="name" name="name" value="${rule.name}" />
        </span>
        </label>
        <!--  
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>规则类型:</span>
        <span>
	         <select name="type"  id="type" >
	              <option value="1"  <c:if test="${1==rule.type}">selected="selected"</c:if>>内容</option>
	              <option value="2"  <c:if test="${2==rule.type}">selected="selected"</c:if>>时间</option>	             
	           </select>
		     </span>
        </label> -->
        <p style="height:20px"></p>    
        <label class="pr10">
        <span>排除规则:</span>
        <span>
	         <select name="exclude"  id="exclude" >
	              <option value="1" <c:if test="${rule.exclude}">selected="selected"</c:if>>是</option>
	              <option value="0" <c:if test="${!rule.exclude}">selected="selected"</c:if>>否</option>	             
	           </select>
		     </span>
        </label>
        <p style="height:20px"></p>
        <label class="pr10">
        <span>收件人:<input style="width:400px" type="text"   id="receiver" name="receiver" value="${rule.receiver}" /></span>
        <span><select name="receiverFlag"  id="receiverFlag"  >
	              <option value="1" <c:if test="${rule.receiverFlag}">selected="selected"</c:if>>精确匹配</option>  
	              <option value="0" <c:if test="${!rule.receiverFlag}">selected="selected"</c:if>>模糊匹配</option>
	            </select>
	     </span>
        </label>
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>系统标签:</span>
        <span><select name="tagType"  id="tagType"  >
        		  <option value="0"  <c:if test="${0==rule.tagType}">selected="selected"</c:if>>无</option>
	              <option value="1"  <c:if test="${1==rule.tagType}">selected="selected"</c:if>>网站询盘</option>
	              <option value="2"  <c:if test="${2==rule.tagType}">selected="selected"</c:if>>系统邮件</option> 
	              <option value="3"  <c:if test="${3==rule.tagType}">selected="selected"</c:if>>广告邮件</option>
	              <option value="4"  <c:if test="${4==rule.tagType}">selected="selected"</c:if>>展会邮件</option>	                           	           
	         </select>
	     </span>
        </label>  
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>
          起始时间 <input style="width:400px" type="text"  id="startTime" name="startTime" value="${rule.startTime}" />
        </span>
        </label>
        <p style="height:20px"></p>
    	<label class="pr10">
        <span>
         结束时间<input style="width:400px" type="text"  id="endTime" name="endTime" value="${rule.endTime}" />
        </span>
        </label>
             
    	<p style="height:20px"></p>
    	<label class="pr10">
        <span>优先级:</span>
        <span><select name="priority"  id="priority"  >
	              <option value="1"  <c:if test="${1==rule.priority}">selected="selected"</c:if>>最高</option>
	              <option value="2"  <c:if test="${2==rule.priority}">selected="selected"</c:if>>高</option> 
	              <option value="3"  <c:if test="${3==rule.priority}">selected="selected"</c:if>>中</option>
	              <option value="4"  <c:if test="${4==rule.priority}">selected="selected"</c:if>>低</option>	
	              <option value="5"  <c:if test="${5==rule.priority}">selected="selected"</c:if>>最低</option>	                                  	           
	         </select>
	     </span>
        </label>
    	<p style="height:20px"></p>    
    	<label class="pr10">
        <span>是否应用:</span>
        <span>
	         <select name="apply"  id="apply" >
	              <option value="1" <c:if test="${rule.apply}">selected="selected"</c:if>>应用</option>
	              <option value="0" <c:if test="${!rule.apply}">selected="selected"</c:if>>不应用</option>	             
	           </select>
		     </span>
        </label>
       
    	 <p style="height:20px"></p>
    	<label class="pr10">
        <span> 
        
         备注： <input style="width:400px" type="text"  id="remark" name="remark" value="${rule.remark}" />
        </span>
        </label>
         <p style="height:20px"></p>
    	<label class="pr10">
        <span>
        
       
        <p style="height:20px"></p>
    	<label >
        <span>
           <input type="submit"  id="submitbtn"  value="修改规则"/>
        </span>
        </label>
    </div>

</form>
    </div>

</body>
</html>
