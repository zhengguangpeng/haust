/**
 *  ReplyProcessor.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-11  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel.processor;


import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.transn.camel.MailBean;
import com.transn.camel.RuleMatchResponse;
import com.transn.service.RuleService;
import com.transn.util.Constants;

public class ReplyProcessor  implements Processor{

	private RuleService  ruleService;
	
	public ReplyProcessor(RuleService  ruleService){
		this.ruleService = ruleService;
	}

	public void process(Exchange exchange) throws Exception {
		
		//首先判断根据规则判断的结果
		Integer baseField = exchange.getIn().getHeader(Constants.HEADER_BASEFIELD, Integer.class);
		
		RuleMatchResponse response = new RuleMatchResponse();
		
		//匹配到的规则
		response.getRuleIDList().addAll(exchange.getIn().getHeader(Constants.HEADER_RULELIST,ArrayList.class));
		
		int translateResult = Constants.FILTER_IGNORE;
		
		if (baseField == Constants.BASEFIELD_EXCLUDE) {
			translateResult = ruleService.decideEmailByLang(exchange.getIn().getBody(MailBean.class));
			response.setBaseField(Constants.BASEFIELD_RESULT);
			response.setResult(translateResult);
		} else if (baseField == Constants.BASEFIELD_TAG) {
			//再判断下邮件语种,如果明确是不翻译的邮件,那么就不打标签,设置为不翻译
			if (Constants.FILTER_NOTRANSLATE == ruleService.decideEmailByLang(exchange.getIn().getBody(MailBean.class))){
				response.setBaseField(Constants.BASEFIELD_RESULT);
				response.setResult(Constants.FILTER_NOTRANSLATE);
			}else{
				response.setBaseField(Constants.BASEFIELD_TAG);
				response.setTagType(exchange.getIn().getHeader(Constants.HEADER_TAG,Integer.class));				
			}
			
		} else if (baseField == Constants.BASEFIELD_RESULT) {
			//
			Integer result = exchange.getIn().getHeader(
					Constants.HEADER_RESULT, Integer.class);
			if (result == Constants.FILTER_NOTRANSLATE) {
				response.setBaseField(Constants.BASEFIELD_RESULT);
				response.setResult(Constants.FILTER_NOTRANSLATE);
			} else {
				translateResult = ruleService.decideEmailByLang(exchange.getIn().getBody(MailBean.class));
				response.setBaseField(Constants.BASEFIELD_RESULT);
				response.setResult(translateResult);
			}
		}
		
		exchange.getOut().setBody(response);

	}

}
