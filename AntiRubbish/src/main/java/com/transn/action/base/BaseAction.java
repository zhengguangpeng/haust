/**
 *  BaseAction.java
 *  描述：基础的Action操作
 *
 *  版本:    v1.0
 *  作者:    allenzhang
 *  日期:    2012-11-1  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.action.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.transn.page.PageFlip;
import com.transn.util.DateJsonValueProcessor;

public class BaseAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6074427893163018679L;	
	
	//声明分页的对象
	public PageFlip page;

	public PageFlip getPage() {
		return page;
	}

	public void setPage(PageFlip page) {
		this.page = page;
	}
	
	/**
     * 把map转换成json串输出
     * @author leiliu.liu
     * @param map
     */
    protected void printJson(Map<String,Object> map)
    {
        //map.put("domains",getDomains());
        JsonConfig cfg = new JsonConfig();
		DateJsonValueProcessor dateJsonProcessor = new DateJsonValueProcessor();
		cfg.registerJsonValueProcessor(Date.class, dateJsonProcessor);
        String result= JSONObject.fromObject(map,cfg).toString();
        HttpServletResponse response = getResponse();
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();
            out.print(result);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	//获取request对象
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRequestObj() {
		//通过ActionContext得到request对象
		return (Map<String, Object>) ActionContext.getContext().get("request");
	}

	//获取session对象
	public Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}

	//获取ServletContext对象
	public Map<String, Object> getApplication() {
		return (Map<String, Object>)ActionContext.getContext().getApplication();
	}
		
	//获取response对象
	public HttpServletResponse getResponse() {
		//通过ServletActionContext类获取HttpServletResponse对象。
		HttpServletResponse response = ServletActionContext.getResponse();
		//设置响应头与字符编码
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		return response;
	}
	
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
}
