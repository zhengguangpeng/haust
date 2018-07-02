/**
 *  MailRouteService.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-13  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.service;

import com.transn.camel.MailBean;
import com.transn.camel.RuleMatchResponse;


public interface MailRouteService {

	/**
	 * 
	 * translateMail:邮件是否翻译,通过匹配规则进行处理
	 *
	 * @param email
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public RuleMatchResponse translateMail(MailBean email);
	
	
	/**
	 * 
	 * getSameEmailID:获取内容相同的第一封邮件ID(userID_uid);
	 *
	 * @param MailBean 邮件
	 * @return     String  如果是第一封邮件,返回null,否则返回第一封邮件的userID_uid
	 * @throws 
	 * @since  　ver 1.0
	 */
	public String getSameEmailID(MailBean email,RuleMatchResponse response);
	
	/**
	 * 
	 * updateMD5CacheOnce:更新MD5的缓存内容,如果有相同MD5的邮件,优先选择要翻译的那封邮件为重复邮件,只需要一次
	 *
	 * @param email    参数说明
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public void updateMD5CacheOnce(MailBean email);
}
