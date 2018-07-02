/**
 *  LoginAction.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    leiliu.liu
 *  日期:    2012-11-12  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.action.rule;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.transn.action.base.BaseAction;

@Controller
@Scope("prototype")
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 5655695899453951848L;
	
	// 用户登录
	@Override
	public String execute() throws Exception {

		String username = getRequest().getParameter("username");
		String password = getRequest().getParameter("password");

		if (username.equals("transn") && password.equals("ts123")) {
			ActionContext.getContext().getSession().put("monitorUser", username);
			this.getRequest().getSession().getId();
			return "main";
		}
		addActionError("用户名或密码错误!");
		return "login";
	}

	public String logout() {
		return "login";
	}

}
