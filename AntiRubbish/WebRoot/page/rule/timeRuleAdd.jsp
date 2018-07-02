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
<title>添加时间规则</title>
<link href="<%=basePath%>css/style.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/jquery.js"></script>
<script src="<%=basePath%>js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript">
	$(function() {
		//名称
		$("#name").focus();
		//默认内容规则,时间灰化
		//$("#startTime").attr("readonly",true);
		//$("#endTime").attr("readonly",true);
		//默认非排他规则
		$("#exclude").val(0);
		//默认优先级为中
		$("#priority").val(3);
		
		
		$('#exclude').change(function(){
			var type = $(this).val();
			if (type =="1"){
				$("#priority").val(1);
			}else{
			
			}
		});
		
		$("#receiverFlag").change(function(){
			var flag = $(this).val();
			if (flag =="0"){
				$("#regTip").show();
			}else{
				$("#regTip").hide();
			}
		});
		
		
		$("#name").blur(function(){
			var name = $.trim($("#name").val());
			if (name==""){
				//alert("规则名称不能为空");
				$("#name").focus();
				return;
			}
			$.ajax({
			    url: 'ruleAction!checkRuleName.action',
			    type:"POST",
			    data:{name:name},
			    success: function(data){			    	
			    	if (data.result =='no'){
			    		//alert("当前规则名称已经存在,请重新命名");
			    		$("#name").focus();
			    		$("#tip").show();
			    		//$("#tip").val("当前规则名称已经存在,请重新命名!");
			    		$("#submitbtn").attr("disabled",true);		    		
			    	}else{
			    		$("#tip").hide();
			    		$("#submitbtn").attr("disabled",false);
			    	}
			    }
			});
		});
		
		/*$('#type').change(function(){
			var type = $(this).val();
			if (type =="1"){
				$("#startTime").attr("readonly",true);
				$("#endTime").attr("readonly",true);
			}else{
				$("#startTime").attr("readonly",false);
				$("#endTime").attr("readonly",false);
			}
		});*/
		
		
		$("#submitbtn").click(function(){ 
			         var name = $.trim($("#name").val());
	                 var type = "2";// $("#type").val();
	                 var exclude = $("#exclude").val();	                 
	                 var sender = "";
	                 var senderFlag = "1";
	                 var receiver = $.trim($("#receiver").val());
	                 var receiverFlag = $("#receiverFlag").val();
	                 var title = "";
	                 var titleFlag = "1";
	                 var tagType = $("#tagType").val();
	                 var priority = $("#priority").val();
	                 var apply = $("#apply").val();
	                 var startTime = $.trim($("#startTime").val());
	                 var endTime = $.trim($("#endTime").val());
	                 var remark =  $.trim($("#remark").val());
	              
	                 if($.isNumeric(name )){
	                	 alert("规则名称不能全为数字");
	                	 return;
	                 }
	                 
	                 if (type=="2" && (startTime=="" && endTime=="")) {
		                alert("时间规则,起始时间和终止时间不能同时为空");
		                return;
		             }
	                 
	                 
	                
	                 $("#submitbtn").attr("disabled",true);
	                  $.ajax({
						    url: 'ruleAction!addRule.action',
						    type:"POST",
						    data:{name:name,type:type,exclude:exclude,
						    	sender:sender,senderFlag:senderFlag,
						    	receiver:receiver,receiverFlag:receiverFlag,
						    	title:title,titleFlag:titleFlag,						    
						    	tagType:tagType,priority:priority,apply:apply,startTime:startTime,
						    	endTime:endTime,remark:remark},
						    success: function(data){
						    	alert(data.result);						    	
						    	$("#submitbtn").attr("disabled",false);
						    }
						});
		      });
	});
</script>
</head>

<body>
<div class="topBar">
<span class="pos_r"><span class="pageIcon flow_02"></span></span>
<span class="posNow">时间规则 >添加规则</span>
</div>
<div class="mainBox">
      <div class="p10 borderAll bg_F5F6FB">
      
      	<label class="pr10">
        <span>
         	规则名称: <input style="width:400px" type="text"   id="name" name="name" />
         	<span id="tip" style="display:none" ><font color="red">当前规则名称已经存在,请重新命名!</font></span>
        </span>
        </label>
        <!-- 
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>规则类型:</span>
        <span>
	         <select name="type"  id="type" >
	              <option value="1">内容</option>
	               <option value="2">时间</option>	          
	           </select>
		     </span>
        </label>
         -->
        <p style="height:20px"></p>    
        <label class="pr10">
        <span>排除规则:</span>
        <span>
	         <select name="exclude"  id="exclude" >
	              <option value="1">是</option>
	              <option value="0">否</option>	             
	           </select>
		     </span>
        </label>
        <p style="height:20px"></p>
        <label class="pr10">
        <span>收件人:<input style="width:400px" type="text"   id="receiver" name="receiver" /></span>
        <span><select name="receiverFlag"  id="receiverFlag"  >
	              <option value="1">精确匹配</option>  
	              <option value="0">模糊匹配</option>
	            </select>
	     </span>
        </label>
        <p style="height:20px"></p>
        <span id="regTip" style="display:none" >
        	<font color="red">提示: 模糊匹配是正在表达式的匹配模式,请确认是有效的正在表达式,举例说明:<br/></font>
        	如果模糊匹配关键字 "<font color="blue">测试</font>",应该定义成为 "<font color="blue">.*测试.*</font>"  [.代表任意字符,*代表零次或者多次]
        </span>
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>系统标签:</span>
        <span><select name="tagType"  id="tagType"  >
        		  <option value="0"  >无</option>
	              <option value="1"  >网站询盘</option>
	              <option value="2"  >系统邮件</option> 
	              <option value="3"  >广告邮件</option>
	              <option value="4"  >展会邮件</option>	                           	           
	         </select>
	     </span>
        </label>
        <p style="height:20px"></p>    
    	<label class="pr10">
        <span>
          起始时间 <input style="width:400px" type="text"  id="startTime" name="startTime" value="2013-01-01 12:00:00" />
        </span>
        </label>
        
        <p style="height:20px"></p>
    	<label class="pr10">
        <span>
         结束时间<input style="width:400px" type="text"  id="endTime" name="endTime" value="2013-01-01 12:00:00" />
        </span>
        </label>
               
    	<p style="height:20px"></p>
    	<label class="pr10">
        <span>优先级:</span>
        <span><select name="priority"  id="priority"  >
	              <option value="1"  >最高</option>
	              <option value="2"  >高</option> 
	              <option value="3"  >中</option>
	              <option value="4"  >低</option>	
	              <option value="5"  >最低</option>	                                  	           
	         </select>
	     </span>
        </label>
    	<p style="height:20px"></p>    
    	<label class="pr10">
        <span>是否应用:</span>
        <span>
	         <select name="apply"  id="apply" >
	              <option value="1">应用</option>
	              <option value="0">不应用</option>	             
	           </select>
		     </span>
        </label>
        
    	 
        <p style="height:20px"></p>    
        <label >
        <span>
         备注： <input style="width:400px" type="text"  id="remark" name="remark"  />
        </span>
        </label>
        
       
        <p style="height:30px"></p>
    	<label >
        <span>
           <input type="button"  id="submitbtn"  value="添加规则"/>
        </span>
        </label>
    </div>


    </div>

</body>
</html>
