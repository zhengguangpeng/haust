/**
 *  DateJsonValueProcessor.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    leiliu.liu
 *  日期:    2012-11-1  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
*/
package com.transn.util;


import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonValueProcessor implements JsonValueProcessor {
	
	private String datePattern = "yyyy-MM-dd hh:mm:ss";
	
    public DateJsonValueProcessor(){
    	
    }

	public DateJsonValueProcessor(String p) {
		if (!p.equals("")) {
			this.datePattern = p;
		}
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
        if(value == null){
        	return null;
        }
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		String str = sdf.format((Date)value);
		return str;
	}
}
