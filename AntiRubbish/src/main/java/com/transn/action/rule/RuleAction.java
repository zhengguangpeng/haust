/**
 *  RuleAction.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-12  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.action.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.transn.action.base.BaseAction;
import com.transn.dao.rule.RuleDao;
import com.transn.model.Rule;
import com.transn.page.PageFlip;
import com.transn.page.QueryResult;
import com.transn.service.EmailAlarmService;
import com.transn.service.RuleService;
import com.transn.util.CommonUtils;
import com.transn.util.Constants;
import com.transn.util.FcHttpClient;


@Controller
@Scope("prototype")
public class RuleAction extends BaseAction {
	
	//日志
	private static final Logger logger = LoggerFactory.getLogger(RuleAction.class);
	private static final long serialVersionUID = -6947677245658908600L;
	@Resource
	private EmailAlarmService emailAlarmService;

	@Resource
	private RuleDao ruleDao;
	
	@Resource
	private RuleService ruleService;

	/**
	 * 
	 * addRule:响应监控平台上添加一条规则
	 *    参数说明
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public void addRule(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			HttpServletRequest request = getRequest();
			Rule rule = new Rule();
			
			rule.setName(CommonUtils.decode(request.getParameter("name")));
			rule.setType(Integer.parseInt(request.getParameter("type")));
			rule.setExclude(Integer.parseInt(request.getParameter("exclude")) == 1);
			rule.setSender(CommonUtils.decode(request.getParameter("sender")));
			rule.setSenderFlag(CommonUtils.getFlag(request.getParameter("senderFlag")));		
			rule.setTitle(CommonUtils.decode(request.getParameter("title")));
			rule.setTitleFlag(CommonUtils.getFlag(request.getParameter("titleFlag")));		
			rule.setContent(CommonUtils.decode(request.getParameter("content")));
			rule.setContentFlag(CommonUtils.getFlag(request.getParameter("contentFlag")));		
			rule.setReceiver(CommonUtils.decode(request.getParameter("receiver")));
			rule.setReceiverFlag(CommonUtils.getFlag(request.getParameter("receiverFlag")));		
			rule.setTagType(Integer.parseInt(request.getParameter("tagType")));
			rule.setPriority(Integer.parseInt(request.getParameter("priority")));		
			rule.setStartTime(CommonUtils.decode(request.getParameter("startTime")));
			rule.setEndTime(CommonUtils.decode(request.getParameter("endTime")));
			rule.setApply(CommonUtils.getFlag(request.getParameter("apply")));
			rule.setRemark(CommonUtils.decode(request.getParameter("remark")));
						
			//调用ZK 增加ZK节点
			CountDownLatch latch = new CountDownLatch(1);
			ruleService.addRuleZKNode(rule,latch);
			
			try {
				latch.await(10, TimeUnit.SECONDS);
			} catch(Exception e){
				
			}
			map.put("result", "添加规则成功");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		this.printJson(map);
	}
	
	
	
	public void checkRuleName(){

		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			HttpServletRequest request = getRequest();
			String name = CommonUtils.decode(request.getParameter("name"));
			if (ruleDao.getRuleByName(name) != null){
				//说明有此规则
				map.put("result", "no");
			}else{
				map.put("result", "yes");
			}
			
		}catch(Exception e){			
			map.put("result", "no");
		}
		this.printJson(map);
	}
	
	/**
	 * 
	 * readme:监控上对于规则的说明
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String readme(){
		//获取当前“反垃圾邮件服务器总阀门” 的值
		int valve = ruleService.getValveValue();		
		HttpServletRequest request = super.getRequest();
		request.setAttribute("valve", valve);
		
		return "readme";
	}
	
	/**
	 * 
	 * contentRuleAdd:监控上添加规则
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String contentRuleAdd(){
		return "contentRuleAdd";
	}
	
	/**
	 * 
	 * timeRuleAdd:监控上添加规则
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String timeRuleAdd(){
		return "timeRuleAdd";
	}
	
	/**
	 * 
	 * valveControl:
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String valveControl(){
		
		HttpServletRequest request = super.getRequest();
		String control = request.getParameter("control");
		CountDownLatch latch = new CountDownLatch(1);
		ruleService.setValveValue(control,latch);
		try {
			latch.await(10, TimeUnit.SECONDS);
		} catch(Exception e){
			
		}
		return readme();
	}
	
	/**
	 * 
	 * contentRuleDetail:监控上修改时间规则
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String contentRuleDetail() {
		HttpServletRequest request = super.getRequest();
		String ruleID = request.getParameter("ruleID");
		String page = request.getParameter("pageNum");
		
		request.setAttribute("rule", ruleDao.find(Rule.class,ruleID));
		request.setAttribute("ruleID", ruleID);
		
		request.setAttribute("pageNum", page);
		return "contentRuleDetail";
	}
	
	/**
	 * 
	 * timeRuleDetail:监控上修改时间规则
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String timeRuleDetail() {
		HttpServletRequest request = super.getRequest();
		String ruleID = request.getParameter("ruleID");
		String page = request.getParameter("pageNum");
		
		request.setAttribute("rule", ruleDao.find(Rule.class,ruleID));
		request.setAttribute("ruleID", ruleID);
		
		request.setAttribute("pageNum", page);
		return "timeRuleDetail";
	}
	
	/**
	 * 
	 * ruleOperation:删除/应用/停用 规则的操作
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String ruleOperation(){
		HttpServletRequest request = super.getRequest();
		
		String ruleID = request.getParameter("ruleID");
		String opType = request.getParameter("opType");
		String type = request.getParameter("type");
		
		Rule rule = ruleDao.find(Rule.class, ruleID);
		if (rule == null){
			logger.error("没有找到对应的规则");
			return ruleList(Constants.RULETYPE_CONTENT,request);
		}
		if (opType.equals("1")){
			//停用规则
			rule.setApply(false);
			//TODO
			CountDownLatch latch = new CountDownLatch(1);
			ruleService.modifyRuleZKNode(rule,latch);
			try {
				latch.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}else if (opType.equals("2")){
			//启用规则
			rule.setApply(true);
			//TODO
			CountDownLatch latch = new CountDownLatch(1);
			ruleService.modifyRuleZKNode(rule,latch);
			try {
				latch.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}else if (opType.equals("3")){
			//删除规则			
			CountDownLatch latch = new CountDownLatch(1);
			ruleService.deleteRuleZKNode(rule,latch);
			try {
				latch.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}
		
		return ruleList(Integer.parseInt(type),request);
			
	}
	
	/**
	 * 
	 * contentRuleList:监控上查看内容规则列表
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String contentRuleList(){	
		//如果ruleID参数不空,则修改之
		HttpServletRequest request = super.getRequest();
		String ruleID = request.getParameter("ruleID");
		if (StringUtils.isNotEmpty(ruleID)){
			Rule rule = ruleDao.find(Rule.class, ruleID);
			if (rule == null){
				//
				logger.error("没找到要修改的规则");
			}else{
				//暂时不关注类型
				rule.setType(Constants.RULETYPE_CONTENT);
				rule.setExclude(Integer.parseInt(request.getParameter("exclude")) == 1);
				rule.setSender(CommonUtils.decode(request.getParameter("sender")));
				rule.setSenderFlag(CommonUtils.getFlag(request.getParameter("senderFlag")));		
				rule.setTitle(CommonUtils.decode(request.getParameter("title")));
				rule.setTitleFlag(CommonUtils.getFlag(request.getParameter("titleFlag")));		
				rule.setContent(CommonUtils.decode(request.getParameter("content")));
				rule.setContentFlag(CommonUtils.getFlag(request.getParameter("contentFlag")));		
				rule.setReceiver(CommonUtils.decode(request.getParameter("receiver")));
				rule.setReceiverFlag(CommonUtils.getFlag(request.getParameter("receiverFlag")));		
				rule.setTagType(Integer.parseInt(request.getParameter("tagType")));
				rule.setPriority(Integer.parseInt(request.getParameter("priority")));		
				rule.setStartTime(CommonUtils.decode(request.getParameter("startTime")));
				rule.setEndTime(CommonUtils.decode(request.getParameter("endTime")));
				rule.setApply(CommonUtils.getFlag(request.getParameter("apply")));
				rule.setRemark(CommonUtils.decode(request.getParameter("remark")));
				
				//TODO
				CountDownLatch latch = new CountDownLatch(1);
				ruleService.modifyRuleZKNode(rule,latch);
				try {
					latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
				
				
			}
		}
		
		
		return ruleList(Constants.RULETYPE_CONTENT,request);		
	}
	
	
	/**
	 * 
	 * timeRuleList:监控上查看时间规则列表
	 *
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String timeRuleList(){	
		//如果ruleID参数不空,则修改之
		HttpServletRequest request = super.getRequest();
		String ruleID = request.getParameter("ruleID");
		if (StringUtils.isNotEmpty(ruleID)){
			Rule rule = ruleDao.find(Rule.class, ruleID);
			if (rule == null){
				//
				logger.error("没找到要修改的规则");
			}else{
				rule.setType(Constants.RULETYPE_TIME);
				rule.setExclude(Integer.parseInt(request.getParameter("exclude")) == 1);
				rule.setReceiver(CommonUtils.decode(request.getParameter("receiver")));
				rule.setReceiverFlag(CommonUtils.getFlag(request.getParameter("receiverFlag")));		
				rule.setTagType(Integer.parseInt(request.getParameter("tagType")));
				rule.setPriority(Integer.parseInt(request.getParameter("priority")));		
				rule.setStartTime(CommonUtils.decode(request.getParameter("startTime")));
				rule.setEndTime(CommonUtils.decode(request.getParameter("endTime")));
				rule.setApply(CommonUtils.getFlag(request.getParameter("apply")));
				rule.setRemark(CommonUtils.decode(request.getParameter("remark")));
				
				//TODO
				CountDownLatch latch = new CountDownLatch(1);
				ruleService.modifyRuleZKNode(rule,latch);
				try {
					latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					
				}
			}
		}
		return ruleList(Constants.RULETYPE_TIME,request);		
	}
	
	
	public String ruleList(int type,HttpServletRequest request){

		try {
			HashMap<String,String> map = new HashMap<String,String>();
			String pageNum = "20";
			// 分页
			if (null == page) {
				page = new PageFlip();
			}
	
			if (null == page.getPageRows()) {
				page.setPageRows(Integer.valueOf(pageNum));
			}
			Long resultNum  = 0l;		
			
			map.put("pageno", page.getCurrentpage().toString());
			map.put("maxSize", page.getPageRows().toString());
			map.put("type", type+"");
	
			//发件人
			String sender1 = request.getParameter("sender1");
			if (StringUtils.isNotEmpty(sender1)) {
				map.put("sender1", sender1.trim());
			}
			//收件人
			String receiver1 = request.getParameter("receiver1");
			if (StringUtils.isNotEmpty(receiver1)) {
				map.put("receiver1", receiver1.trim());
			}
			//标题
			String title1 = request.getParameter("title1");
			if (StringUtils.isNotEmpty(title1)) {
				map.put("title1", title1.trim());
			}
			
			
	        List<Rule> ruleList = null;
	        QueryResult<Rule> qr  = ruleService.getRuleList(map);
	        if(null != qr)
			{
	        	ruleList = qr.getResultList();
				resultNum = qr.getTotalRecord();
				page.setTotalRecord(resultNum.intValue());
			}
			this.getRequestObj().put("ruleList", ruleList);
			this.getRequestObj().put("sender1", sender1);
			this.getRequestObj().put("receiver1", receiver1);
			this.getRequestObj().put("title1", title1);
			
			
			int intCount = resultNum.intValue()/Integer.valueOf(pageNum);     //总页数
			if(resultNum.intValue()%Integer.valueOf(pageNum) > 0 )
			{
				intCount = intCount +1;
			}
			page.setTotalpage(intCount);
			List<Integer> iLis =new ArrayList<Integer>();
			for(int idx = 0; idx<intCount; idx++)
			{
				iLis.add(idx+1);
			}
			this.getRequestObj().put("ResultNum", resultNum);
			this.getRequestObj().put("PageNum", iLis);
	        
      
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (type == Constants.RULETYPE_CONTENT){
			return "contentRuleList";
		}else if (type == Constants.RULETYPE_TIME){
			return "timeRuleList";
		}
		return "";
	
	}

	/**
	 * main:(这里用一句话描述这个方法的作用)
	 *
	 * @param args    参数说明
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		FcHttpClient httpclient = new FcHttpClient();

		NameValuePair[] nvp = {
				new NameValuePair("name", "icbase_tag_4"),
				new NameValuePair("type", "1"),
				new NameValuePair("exclude",	"0"),
				new NameValuePair("sender",	"admin@efind.ru"),
				new NameValuePair("senderFlag",	"1"),
				new NameValuePair("receiver", ".*@icbase.com"),
				new NameValuePair("receiverFlag",	"0"),
				new NameValuePair("tagType",	"1"),
				new NameValuePair("priority",	"1"),
				new NameValuePair("apply",	"1"),

		};

		String result = httpclient
				.getResult(
						"http://127.0.0.1/rubbish/ruleAction!addRule.action",
						nvp);
		System.out.println(result);
	}

}
