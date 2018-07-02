/**
 *  MailEndPointConst.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-13  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel;




public class MailEndPointConst {

	//入口,新来邮件定义的EndPoint
	public static String EndPoint_Start = "direct:start";
	
	//按照内容进行处理的EndPoint
	public static String EndPoint_Content = "direct:content";
	
	//按照时间进行处理的EndPoint
	public static String EndPoint_Time = "direct:time";
		
	
}
