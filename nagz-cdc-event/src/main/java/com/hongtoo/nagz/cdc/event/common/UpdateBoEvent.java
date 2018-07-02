package com.hongtoo.nagz.cdc.event.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 更新BO的事件记录
 * 
 * @author 肥羊
 *
 */
public class UpdateBoEvent {
	// 数据库(NAGZ_****)
	private String database;
	// 表名(APP_***)
	private String table;
	// 实例ID(id）
	private String entityID;
	// 修改的列
	private List<String> fieldNameList;
	// 修改的时间,单位为毫秒
	private Long timeInMs;
	// 附加信息,保留以后用
	private String remark;
	
	public UpdateBoEvent(){
		database = "";
		table = "";
		entityID = "";
		fieldNameList =  new ArrayList<String>();
		timeInMs = 0l;
		remark = "";
	}
	
	@JsonIgnore  
	public String getKey() {
		return table + "_" + entityID;
	}


	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public List<String> getFieldNameList() {
		return fieldNameList;
	}

	public void setFieldNameList(List<String> fieldNameList) {
		this.fieldNameList = fieldNameList;
	}

	public Long getTimeInMs() {
		return timeInMs;
	}

	public void setTimeInMs(Long timeInMs) {
		this.timeInMs = timeInMs;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "UpdateBoEvent [database=" + database + ", table=" + table
				+ ", entityID=" + entityID + ", fieldNameList=" + fieldNameList
				+ ", timeInMs=" + timeInMs + ", remark=" + remark + "]";
	}
	
	
	
	

}
