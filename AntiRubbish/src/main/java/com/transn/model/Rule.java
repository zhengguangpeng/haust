/**
 *  SendEmail.java
 *  描述：  发送用户邮箱信息实体类
 *
 *  版本:    v1.0
 *  作者:    allenzhang
 *  日期:    2012-11-1  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "rule")
public class Rule  implements Serializable,Comparable<Rule>{

	private static final long serialVersionUID = -4502477745588007170L;
	
	//主键
	private String id;  
	//规则名称 唯一
	private String name; 
	//规则类型 1:Content 2:Time 3:Other  --必填
	private Integer type;  
	//是否是排除规则 --必填
	private boolean exclude;
	
	//Sender --可选
	private String sender; 
	//Sender是否是精确匹配,如果不是则用正在表达式匹配的方式 --可选
	private boolean senderFlag;	
	//Title --可选
	private String title; 
	//Title是否是精确匹配,如果不是则用正在表达式匹配的方式 --可选
	private boolean titleFlag;
	//Content--可选
	private String content;
	//Content是否是精确匹配,如果不是则用正在表达式匹配的方式--可选
	private boolean contentFlag;
	//Receiver--可选
	private String receiver;
	//Receiver是否是精确匹配,如果不是则用正在表达式匹配的方式--可选
	private boolean receiverFlag;
	//标签类型--可选
	private Integer tagType;
	//优先级--必选
	private Integer priority;
	
	//时间范围:起始时间 yyyy-MM-dd HH:mm:ss --可选
	private String startTime; 
	//时间范围:终止时间 yyyy-MM-dd HH:mm:ss --可选
	private String endTime;
		
	//规则是否被应用 --必选
	private boolean apply;	
	//创建时间
	private Date createTime;
	//状态，有效还是无效
	private boolean validate;
	//备注，例如和哪个冲突了之类的
	private String remark;
	

	@Override
	public String toString() {
		return "Rule : sender=" + sender + ", senderFlag="
				+ senderFlag + ", title=" + title + ", titleFlag=" + titleFlag
				+ "receiver=" + receiver + ", receiverFlag=" + receiverFlag
				+ ", tagType=" + tagType + ", priority=" + priority;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;		
	}
	
	@Column(name = "sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		if (null != sender) {
			this.sender = sender.toLowerCase();
		}
	}

	@Column(name = "senderFlag")
	public boolean isSenderFlag() {
		return senderFlag;
	}

	public void setSenderFlag(boolean senderFlag) {
		this.senderFlag = senderFlag;
	}
	
	
	@Column(name = "receiver")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		if (null != receiver) {
			this.receiver = receiver.toLowerCase();
		}
	}
	

	@Column(name = "receiverFlag")
	public boolean isReceiverFlag() {
		return receiverFlag;
	}

	public void setReceiverFlag(boolean receiverFlag) {
		this.receiverFlag = receiverFlag;
	}
	
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (null != title) {
			this.title = title.toLowerCase();
		}
	}
	
	@Column(name = "titleFlag")
	public boolean isTitleFlag() {
		return titleFlag;
	}

	public void setTitleFlag(boolean titleFlag) {
		this.titleFlag = titleFlag;
	}
	
	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (null != content) {
			this.content = content.toLowerCase();
		}
	}
	
	@Column(name = "contentFlag")
	public boolean isContentFlag() {
		return contentFlag;
	}

	public void setContentFlag(boolean contentFlag) {
		this.contentFlag = contentFlag;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "tagType")
	public Integer getTagType() {
		return tagType;
	}

	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "exclude")
	public boolean isExclude() {
		return exclude;
	}

	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	@Column(name = "startTime")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	
	@Column(name = "endTime")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "apply")
	public boolean isApply() {
		return apply;
	}

	public void setApply(boolean apply) {
		this.apply = apply;
	}

	@Column(name = "priority")
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
	@Column(name = "validate")
	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {		
		this.remark = remark;		
	}

	@Override
	public int compareTo(Rule o) {
		//先比较优先级，优先级不同的话,那就先比较优先级
		int result =  0;
		result = this.getPriority() - o.getPriority();
		if (result !=0 ){
			return result;
		}
		//这个时候再比较长度,最长规则最优先,暂且不计算正文内容的比较
		int lengthSelf = 0;
		int lengthOther = 0;
		
		if (StringUtils.isNotEmpty(this.getSender())){
			lengthSelf++;
		}
		if (StringUtils.isNotEmpty(this.getReceiver())){
			lengthSelf++;
		}
		if (StringUtils.isNotEmpty(this.getTitle())){
			lengthSelf++;
		}
		
		if (StringUtils.isNotEmpty(o.getSender())){
			lengthOther++;
		}
		if (StringUtils.isNotEmpty(o.getReceiver())){
			lengthOther++;
		}
		if (StringUtils.isNotEmpty(o.getTitle())){
			lengthOther++;
		}
		
		if(lengthSelf >= lengthOther){
			result =  -1;
		}else{
			result =  1;
		}
		return result;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	

	

}
