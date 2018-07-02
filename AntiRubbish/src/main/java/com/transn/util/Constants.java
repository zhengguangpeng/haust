/**
 *  Constants.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-13  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.util;


public class Constants {

	//规则类型--内容
	public static final int RULETYPE_CONTENT = 1;
	//规则类型--时间
	public static final int RULETYPE_TIME = 2;
	//规则类型--其它
	public static final int RULETYPE_OTHER = 3;

	// 翻译邮件
	public static final int FILTER_TRANSLATE = 1;
	// 不翻译邮件
	public static final int FILTER_NOTRANSLATE = 2;
	// 忽略
	public static final int FILTER_IGNORE = 4;
	
	//不关注时间
	public static final String IGNORE_TIME = "-1";
	
	//Camel输出头字段
	//被排除
	public static final String HEADER_EXCLUDE = "exclude";
	//系统标签
	public static final String HEADER_TAG = "tag";
	//翻译结果
	public static final String HEADER_RESULT = "result";
	
	//输出字段以哪个为基准 //1代表第一个isExclude,2代表tagType,4代表result	
	public static final int BASEFIELD_EXCLUDE = 1;
	public static final int BASEFIELD_TAG = 2;
	public static final int BASEFIELD_RESULT = 4;
	//以哪个字段为准
	public static final String HEADER_BASEFIELD = "baseField"; 
	
	//根据规则判断出来的结果
	public static final String HEADER_RULERESULT = "ruleResult";
	//匹配到的规则ID列表
	public static final String HEADER_RULELIST = "ruleList";
		
	
	//语种定义
	public static final int LANG_CHINESE = 1; //中文语种
	public static final int LANG_UNKNOWN = 9997; //未知语种
	
	//提取邮件正文的部分内容,暂定为取前250个字符
	public static final int SUB_HEAD_MAILCONTENT = 250;
	//邮件标题过短判断为未知,多少个字符为短,目前以15个字符为准
	public static final int SHORT_TITLE_LENGTH = 15;
	
}
