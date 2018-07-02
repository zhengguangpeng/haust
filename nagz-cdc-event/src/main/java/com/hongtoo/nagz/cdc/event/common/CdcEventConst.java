package com.hongtoo.nagz.cdc.event.common;

public class CdcEventConst {

	//增删改对应的事件主题名称
	public static final String TOPIC_EVENT_CDC = "nagz_event_cdc";
	
	//修改操作的主题名称
	public static final String TOPIC_UPDATE_OPERATION = "nagz_update_event_1";	
	
	// 默认的Consumer GroupID,消息提醒所用
	public static final String SUBSCRIBE_GROUP_ID = "msg_tip_group";
	 
	// 监控表变化事件的节点,节点的值取当前时间
	public static final String TABLE_CHANGED_NODEPATH = "/nagz_cdc_event/table";
	
	//缓存监控表的Key
	public static final String KEY_CAPTRUE_TALBES = "tables_captured";

}
