<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<title>内容规则列表</title>
<link href="<%=basePath%>css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>css/default/reset.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/jquery.js"></script>
<script src="<%=basePath%>js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
<script  type="text/javascript">
	function topage(currentpage){	
		$("#currentpage").val(currentpage);
    	$("#allDateSearch").submit(); 
	}
	
	//停用规则
    function  stopRuleApply(id,pageNum){ 
    	str= "是否停用该记录？";
	  	if (confirm(str)) {
	  		document.allDateSearch.action = "<%=basePath%>ruleAction!ruleOperation.action?ruleID="+ id+"&opType=1&type=1";	
	  		document.allDateSearch.submit(); 						
	  	 }
	  	
  	} 
    
	//应用规则
    function  startApplyRule(id,pageNum){ 
       	str= "是否应用该记录？";
      	if (confirm(str)) {
      		document.allDateSearch.action = "<%=basePath%>ruleAction!ruleOperation.action?ruleID="+ id+"&opType=2&type=1";		
     		document.allDateSearch.submit(); 						
      	 }
      	
    } 		
    function  deleteRule(id,pageNum){ 
    	str= "是否删除该记录？";
	  	if (confirm(str)) {
	  		document.allDateSearch.action = "<%=basePath%>ruleAction!ruleOperation.action?ruleID="+ id+"&opType=3&type=1";	
	  		document.allDateSearch.submit(); 						
	  	 }
	  	
  	} 
</script>
</head>

<body>
<div class="topBar">
<span class="pos_r"><span class="pageIcon flow_02"></span></span>
<span class="posNow">内容规则 >规则列表</span>
</div>
<form name="allDateSearch" id="allDateSearch" action="ruleAction!contentRuleList.action" method="post">
<input type="hidden" name="page.currentpage" id="currentpage" value="${page.currentpage}"/>

<div class="mainBox">
    <div class="p10 borderAll bg_F5F6FB">
        <label>
       		<span>发件人:</span>
       	 	<span><input type="text" class="inputText160"  name="sender1"  value="${sender1}"  /></span>
        </label>       
       	<label>
       		<span>收件人:</span>
        	<span><input type="text" class="inputText160"  name="receiver1"  value="${receiver1}"  /></span>
        </label>        
        <label>
       		<span>标题:</span>
        	<span><input type="text" class="inputText160"  name="title1"  value="${title1}"  /></span>
        </label>      
        
       	<label>
       		<span><input type="submit" class="searchBtn" value="" title="查询"  /></span>
       	</label>
    </div>
    
    <div class="tableDiv">
     	<table width="100%" border="1" cellspacing="1" cellpadding="1">
          <thead>
          <tr>
            <td width="10%">名称</td>
            <td width="8%">排除</td>
            <td width="10%">发件人及模式</td>      
            <td width="10%">标题及模式</td>   
            <td width="10%">收件人及模式</td> 
            <td width="10%">系统标签</td>           
            <td width="8%">优先级</td> 
            <td width="9%">应用状态</td>
            <td width="10%">备注</td>    
            <td width="5%"></td>  
            <td width="5%"></td>
            <td width="5%"></td>      
          </tr>
          </thead>
          <c:choose>
			<c:when test="${!empty ruleList}">
				<c:forEach items="${ruleList}" var="row" varStatus="status">
					<tr>
						<td><div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:90px" title="${row.name}">${row.name}</div></td>
						<td>
							<c:if test="${ row.exclude }"> Yes	</c:if>
							<c:if test="${ !row.exclude }"> No	</c:if>							
						</td>
						
						 <c:if test="${!empty row.sender}">
						 	<td>
								<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:120px" title="${row.sender}">${row.sender}</div>： 
								<c:if test="${ row.senderFlag }"> 精确匹配	</c:if>
								<c:if test="${ !row.senderFlag }"> 模糊匹配	</c:if>
							</td>
						 </c:if>
						 <c:if test="${empty row.sender}">
						 	<td></td>
						 </c:if>
						
						 <c:if test="${!empty row.title}">
						 	<td>
								<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:120px" title="${row.title}">${row.title}</div>： 
								<c:if test="${ row.titleFlag }"> 精确匹配	</c:if>
								<c:if test="${ !row.titleFlag }"> 模糊匹配	</c:if>
							</td>
						 </c:if>
						 <c:if test="${empty row.title}">
						 	<td></td>
						 </c:if>
						 
						 <c:if test="${!empty row.receiver}">
						 	<td>
								<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:120px" title="${row.receiver}">${row.receiver}</div>： 
								<c:if test="${ row.receiverFlag }"> 精确匹配	</c:if>
								<c:if test="${ !row.receiverFlag }"> 模糊匹配	</c:if>
							</td>
						 </c:if>
						 <c:if test="${empty row.receiver}">
						 	<td></td>
						 </c:if>
						
						<td>
							<c:if test="${ row.tagType == 1 }">网站询盘</c:if>
							<c:if test="${ row.tagType == 2 }">系统邮件</c:if>
							<c:if test="${ row.tagType == 3 }">广告邮件</c:if>
							<c:if test="${ row.tagType == 4 }">展会邮件</c:if>
							<c:if test="${ row.tagType == 0||row.tagType==null }"></c:if>
						</td>
					 	<td>
					 		<c:if test="${ row.priority == 1 }">最高</c:if>
							<c:if test="${ row.priority == 2 }">高</c:if>
							<c:if test="${ row.priority == 3 }">中</c:if>
							<c:if test="${ row.priority == 4 }">低</c:if>
							<c:if test="${ row.priority == 5 }">最低</c:if>
					 	</td>
						<td>
							<c:if test="${ row.apply }"> Yes	</c:if>
							<c:if test="${ !row.apply }"> No	</c:if>							
						</td>	
						<td><div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;width:120px" title="${row.remark}">${row.remark}</div></td>
						<td><a href="<%=basePath %>ruleAction!contentRuleDetail.action?ruleID=${row.id}&pageNum=${page.currentpage}" >修改</a></td>
						<td>
							<c:if test="${ row.apply }">
								<a  onclick="stopRuleApply('${row.id}','${page.currentpage}')">停用</a>
							</c:if>
							<c:if test="${ !row.apply }">
								<a  onclick="startApplyRule('${row.id}','${page.currentpage}')">应用</a>
							</c:if>
						</td>
						<td>
							<a  onclick="deleteRule('${row.id}','${page.currentpage}')">删除</a>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="12">暂无记录</td>
				</tr>
			</c:otherwise>
		  </c:choose>
        </table>
    </div>
         <!-- 分页画面 -->
		<%@include file="/commons/pageForm.jsp"%>
</div>

</form>

</body>
</html>
