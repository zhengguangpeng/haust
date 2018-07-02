<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="images/favicon.ico" /> 
<title>Login Index</title>
<link type="text/css" rel="stylesheet" href="css/Default.css" />
<link type="text/css" rel="stylesheet" href="css/xtree.css" />
<link type="text/css" rel="stylesheet" href="css/User_Login.css" />
<!--数据有效性校验-->
<script type="text/javascript">

</script>
</head>
<BODY id="userlogin_body">
	<DIV></DIV>
	<form id="loginForm" action="login.action" method="post">
		<DIV id="user_login">
			<DL>
				<DD id="user_top">
					<UL>
						<LI class="user_top_l"></LI>
						<LI class="user_top_c"></LI>
						<LI class="user_top_r"></LI>
					</UL>
					<DD id="user_main">
						<UL>
							<LI class="user_main_l"></LI>
							<LI class="user_main_c">
								<DIV class="user_main_box">
									<UL>
										<LI class="user_main_text">用户名：</LI>
										<LI class="user_main_input"><INPUT
											class="TxtUserNameCssClass" id="username" maxLength=20
											name="username" ></LI>
									</UL>
									<UL>
										<LI class="user_main_text">密 码：</LI>
										<LI class="user_main_input"><INPUT
											class="TxtPasswordCssClass" id="TxtPassword" type="password"
											name="password"></LI>
									</UL>
									<UL>
									<s:actionerror/>
									</UL>
								</DIV>
							</LI>
							<LI class="user_main_r"><INPUT class="IbtnEnterCssClass"
								id="login_img"
								style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
								onclick='javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions("IbtnEnter", "", true, "", "", false, false))'
								type=image src="images/user_botton.gif" name=IbtnEnter>
							</LI>
						</UL>
						<DD id=user_bottom>
						
						
						</DD>
			</DL>
		</DIV>
		<SPAN id=ValrUserName style="DISPLAY: none; COLOR: red"></SPAN><SPAN
			id=ValrPassword style="DISPLAY: none; COLOR: red"></SPAN><SPAN
			id=ValrValidateCode style="DISPLAY: none; COLOR: red"></SPAN>
		<DIV id=ValidationSummary1 style="DISPLAY: none; COLOR: red"></DIV>
	</FORM>
</BODY>
</HTML>
