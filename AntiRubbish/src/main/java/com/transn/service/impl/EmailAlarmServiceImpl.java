/**
 *  EmailAlarmServiceImpl.java
 *  描述：  报警信息的保存实现类
 *
 *  版本:    v1.0
 *  作者:    allenzhang
 *  日期:    2012-11-19  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import com.transn.service.EmailAlarmService;
import com.transn.util.CommonUtils;
import com.transn.util.FcHttpClient;


@Service("emailAlarmServiceImpl")
public class EmailAlarmServiceImpl implements EmailAlarmService {
	private static final Logger logger = Logger
			.getLogger(EmailAlarmServiceImpl.class);
	

	@Override
	/**
	 * 
	 * sendAlarm:(这里用一句话描述这个方法的作用)
	 *
	 * @param  @param alarmType 告警类型
	 * @param  @param mail_type  邮件类型  A-B   B-A
	 * @param  @param email_id  邮件ID,如果找不到就默认为消息内容
	 * @param  @return      
	 * @return String    返回值说明
	 * @throws 
	 * @since  　Ver 1.1
	 */
	public String sendAlarm(String alarm){

		try {
			sendMailMsg(alarm);
			// 手机告警只有重大场合才报警，如系统关闭等
			sendPhoneMsg(alarm, "15871742024");
				
			
		} catch (Exception e) {
			e.printStackTrace();
			return "发送告警出现异常" + e.toString();
		}
		return "报警发送完成";

	}

	/**
	 * 
	 * sendPhoneMsg:发送手机短信
	 * 
	 * @param @param alarmType 异常类型
	 * @param @param email_id 邮件ID
	 * @param @param phone 用户手机
	 * @return void 返回值说明
	 * @throws
	 * @since 　Ver 1.1
	 */
	private void sendPhoneMsg(String alarm, String phone) {
		try {
			FcHttpClient httpclient = new FcHttpClient();
			String SMServerURL = CommonUtils.getFromFile("conf.common",
					"SMServerURL");
			String str = "&to=" + phone + "&content=" + alarm + "&time=";
			String smsUrl = SMServerURL + str;
			String hc = httpclient.getLineFromHttpURLConnection(smsUrl);
			logger.info("" + hc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * sendMailMsg:发送报警邮件
	 * 
	 * @param @param alarmType 异常类型
	 * @param @param email_id 邮件ID
	 * @param @param toEmail 用户邮箱
	 * @return void 返回值说明
	 * @throws
	 * @since 　Ver 1.1
	 */
	private void sendMailMsg(String alarm) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	

}
