/**
 *  ContentProcessor.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-13  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.transn.camel.MailBean;
import com.transn.camel.RuleMatchResponse;
import com.transn.service.RuleService;
import com.transn.util.Constants;


public class TimeProcessor   implements Processor{

	private RuleService  ruleService;
	
	public TimeProcessor(RuleService ruleService) {
		this.ruleService = ruleService;
	}
	
	public void process(Exchange exchange) throws Exception {

		MailBean email = exchange.getIn().getBody(MailBean.class);
		
		//按照内容规则去判断邮件结果
		RuleMatchResponse response = ruleService.decideEmailByRule(email,Constants.RULETYPE_TIME);
		if (response.getBaseField() == Constants.BASEFIELD_EXCLUDE){
			exchange.getOut().setHeader(Constants.HEADER_EXCLUDE,"1");
		}else if (response.getBaseField() == Constants.BASEFIELD_TAG){
			exchange.getOut().setHeader(Constants.HEADER_TAG,response.getTagType());			
		}else{
			exchange.getOut().setHeader(Constants.HEADER_RESULT,response.getResult());
		}
		exchange.getOut().setHeader(Constants.HEADER_BASEFIELD, response.getBaseField());
		exchange.getOut().setHeader(Constants.HEADER_RULELIST, response.getRuleIDList());
		exchange.getOut().setBody(email);
	}
}
