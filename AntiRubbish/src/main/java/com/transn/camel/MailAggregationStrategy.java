/**
 *  MailAggregationStrategy.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-17  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.transn.util.Constants;


public class MailAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // the first time we only have the new exchange
        if (oldExchange == null) {
            return newExchange;
        }

        Integer oldBaseField = oldExchange.getIn().getHeader(Constants.HEADER_BASEFIELD, Integer.class);
       
        List oldRuleList = oldExchange.getIn().getHeader(Constants.HEADER_RULELIST,ArrayList.class);
    	List newRuleList = newExchange.getIn().getHeader(Constants.HEADER_RULELIST,ArrayList.class);
    	
        if (oldBaseField == Constants.BASEFIELD_EXCLUDE){
        	newRuleList.addAll(oldRuleList);
        	newExchange.getIn().setHeader(Constants.HEADER_RULELIST,newRuleList);        	
        	return newExchange; //如果是排除了,那就按照新的
        }else if (oldBaseField == Constants.BASEFIELD_TAG){
        	oldRuleList.addAll(newRuleList);
        	oldExchange.getIn().setHeader(Constants.HEADER_RULELIST,oldRuleList);       
        	return oldExchange; //
        }else if (oldBaseField == Constants.BASEFIELD_RESULT){
        	//
        	Integer result = oldExchange.getIn().getHeader(Constants.HEADER_RESULT, Integer.class);
        	if (result == Constants.FILTER_NOTRANSLATE){
        		oldRuleList.addAll(newRuleList);
            	oldExchange.getIn().setHeader(Constants.HEADER_RULELIST,oldRuleList);  
        		return oldExchange;
        	}else {
        		newRuleList.addAll(oldRuleList);
            	newExchange.getIn().setHeader(Constants.HEADER_RULELIST,newRuleList);
        		return newExchange;
        	}
        }
        
        newRuleList.addAll(oldRuleList);
    	newExchange.getIn().setHeader(Constants.HEADER_RULELIST,newRuleList);  
        return newExchange;
       
//        // return the winner with the lowest rate
//        if (oldQuote.doubleValue() >= newQuote.doubleValue()) {
//            return oldExchange;
//        } else {
//            return newExchange;
//        }
    }

}
