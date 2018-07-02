/**
 *  EmailAlarmService.java
 *  描述：  邮件告警服务接口
 *
 *  版本:    v1.0
 *  作者:    allenzhang
 *  日期:    2012-11-19  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
*/
package com.transn.service;


public interface EmailAlarmService {
	
	/**
	 * 
	 * sendAlarm:(这里用一句话描述这个方法的作用)
	 *
	 * @param  @param alarm 告警消息
	 * @param  @return   成功失败消息
	 * @return String    返回值说明
	 * @throws 
	 * @since  　Ver 1.1
	 */
	public String sendAlarm(String alarm);

}
