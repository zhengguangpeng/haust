package com.transn.service.impl;

import javax.annotation.Resource;


import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.transn.camel.MailAggregationStrategy;
import com.transn.camel.MailBean;
import com.transn.camel.MailEndPointConst;
import com.transn.camel.RuleMatchResponse;
import com.transn.camel.processor.ContentProcessor;
import com.transn.camel.processor.ReplyProcessor;
import com.transn.camel.processor.TimeProcessor;
import com.transn.service.MailRouteService;
import com.transn.service.RuleService;
import com.transn.util.CommonUtils;
import com.transn.util.Constants;
import com.transn.util.EHCacheUtil;

@Service("MailRouteService")
public class MailRouteServiceImpl extends RouteBuilder implements InitializingBean, CamelContextAware,MailRouteService {
    
	//日志
	private static final Logger logger = LoggerFactory.getLogger(MailRouteServiceImpl.class);
	
	@Resource
	private RuleService ruleService;
	
	private CamelContext camelContext;
	private ProducerTemplate producerTemplate;
	private EHCacheUtil md5Cache = new EHCacheUtil("md5Cache");
	private byte[] lock = new byte[0];
	
	
    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
        
    }

    public void afterPropertiesSet() throws Exception {
    	producerTemplate = camelContext.createProducerTemplate();
        if (camelContext != null) {
            // setup a timer to send the cafe order
            camelContext.addRoutes(this);
        }
    }

    @Override
    public void configure() throws Exception {
    	
    	from(MailEndPointConst.EndPoint_Start).process(
   			new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					MailBean bean = exchange.getIn().getBody(MailBean.class);
					// TODO Auto-generated method stub
					logger.info("收到一个判断邮件的请求,只记录下日志: " + bean);
				}
   			}
   		)
   		.multicast(new MailAggregationStrategy()).parallelProcessing()
		.to(MailEndPointConst.EndPoint_Content, MailEndPointConst.EndPoint_Time)
		.end()
		// and prepare the reply message
		.process(new ReplyProcessor(ruleService));
		
    	from(MailEndPointConst.EndPoint_Content).process(new ContentProcessor(ruleService));
    	
    	from( MailEndPointConst.EndPoint_Time).process(new TimeProcessor(ruleService));
    	
    	
    }

	@Override
	public RuleMatchResponse translateMail(MailBean email) {
		
		//首先判断当前 反垃圾邮件服务器总阀门 的状态值
		if (ruleService.getValveValue() == 0 ){
			//0 -停止运行,直接返回
			RuleMatchResponse response = new RuleMatchResponse();
			response.setValveState(0);
			return response;
		}
		// 1--试运行  2 -- 正式运行  应用进行判断
		Object object = producerTemplate.sendBody(MailEndPointConst.EndPoint_Start,ExchangePattern.InOut,email);
		if (object instanceof RuleMatchResponse ){
			RuleMatchResponse response = (RuleMatchResponse)object;
			//设置状态
			response.setValveState(ruleService.getValveValue());
			return response;
		}
		return null;	
		
	}

	@Override
	public String getSameEmailID(MailBean email,RuleMatchResponse response) {
		
		if (email ==null){
			return null;
		}
		synchronized (lock) {			
		
			//邮件标题和内容的MD5进行合并
			String md5OfEmail = 
					CommonUtils.getMD5(email.getTitle().getBytes())+"_"+
					CommonUtils.getMD5(email.getContent().getBytes());
			
			if (md5Cache.isExist(md5OfEmail)){
				Object value = md5Cache.getParam(md5OfEmail);
				if (value != null && !value.toString().isEmpty()){
					return value.toString();
				}				
			}
			
			//如果不存在,加入到缓存中,并且返回null
			String value = email.getUserID()+"_"+email.getMailUID();
			md5Cache.putParam(md5OfEmail,value);
			
			if(response.getBaseField() == Constants.BASEFIELD_RESULT &&
					response.getResult() != Constants.FILTER_NOTRANSLATE){
			
				md5Cache.putParam(md5OfEmail+"updated", "1");
			}
			return null;
		}
		
	}

	@Override
	public void updateMD5CacheOnce(MailBean email) {
		if (email == null){
			return ;
		}
		synchronized (lock) {
		
			//邮件标题和内容的MD5进行合并
			String md5OfEmail = 
					CommonUtils.getMD5(email.getTitle().getBytes())+"_"+
					CommonUtils.getMD5(email.getContent().getBytes());
			
			if (md5Cache.isExist(md5OfEmail+"updated")){
				return ;
			}
			logger.info(email + " found the same email as before,but replaced by new one: " + email.getUserID()+"_"+email.getMailUID());
			
			String value = email.getUserID()+"_"+email.getMailUID();
			md5Cache.putParam(md5OfEmail,value);
			md5Cache.putParam(md5OfEmail+"updated", "1");
		}
	}
	
}
