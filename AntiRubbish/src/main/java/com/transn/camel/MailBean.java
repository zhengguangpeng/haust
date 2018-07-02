/**
 *  MailBean.java
 *  描述：  邮件Bean,包含了关键部分属性
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-10  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel;


public class MailBean {
	
	//邮件UID
	private String mailUID;
	//userID
	private String userID;
	//邮件标题
	private String title;
	//邮件发件人
	private String sender;
	//邮件接收人
	private String receiver;
	//邮件正文内容
	private String content;
	//用户需要翻译的语种列表,用逗号分隔
	private String lanuage;
	//邮件时间
	private String time;
	//是否是历史邮件
	private boolean isHistoryEmail;
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLanuage() {
		return lanuage;
	}
	public void setLanuage(String lanuage) {
		this.lanuage = lanuage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMailUID() {
		return mailUID;
	}
	public void setMailUID(String mailUID) {
		this.mailUID = mailUID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public boolean isHistoryEmail() {
		return isHistoryEmail;
	}
	public void setHistoryEmail(boolean isHistoryEmail) {
		this.isHistoryEmail = isHistoryEmail;
	}
	
	@Override
	public String toString() {
		return "MailBean [mailUID=" + mailUID + ", userID=" + userID
				+ ", title=" + title + ", sender=" + sender + ", receiver="
				+ receiver + ", isHistoryEmail=" + isHistoryEmail;
	}
	

}
