/**
 *  RuleService.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-12  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.service;


import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import com.transn.camel.MailBean;
import com.transn.camel.RuleMatchResponse;
import com.transn.model.Rule;
import com.transn.page.QueryResult;


public interface RuleService {

	/**
	 * 
	 * addRuleZKNode:添加一条规则,创建对应的ZK Node
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean addRuleZKNode(Rule rule,CountDownLatch latch);
	
	
	/**
	 * 
	 * modifyRuleZKNode:修改一条规则,修改对应的ZK Node的Value
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean modifyRuleZKNode(Rule rule,CountDownLatch latch);
	
	
	
	/**
	 * 
	 * deleteRuleZKNode:删除一条规则,删除对应的ZK Node的Value
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean deleteRuleZKNode(Rule rule,CountDownLatch latch);
	
	/**
	 * 
	 * addRule:添加一条规则
	 *
	 * @param rule 新规则
	 * @param isSyn2DB   是否同步到DB
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean addRule(Rule rule,CountDownLatch latch,boolean isSyn2DB);
	
	/**
	 * 
	 * deleteRule:删除一条规则
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean deleteRule(Rule rule,CountDownLatch latch);
	
	/**
	 * 
	 * modifyRule:修改一条规则
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public boolean modifyRule(Rule rule,CountDownLatch latch);
	
	/**
	 * 
	 * decideEmailByRule:根据规则,判断邮件是否需要翻译 :翻译/不翻译/忽略
	 *
	 * @param email 邮件Bean
	 * @param ruleType  规则类型
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public RuleMatchResponse decideEmailByRule(MailBean email,int ruleType);
	
	
	/**
	 * 
	 * decideEmailByLang:根据邮件语种来判断是否需要翻译
	 *
	 * @param email
	 * @return   int 返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public int decideEmailByLang(MailBean email);
	
	/**
	 * 
	 * getRuleList:通过查询条件获取相应的规则列表
	 *
	 * @param map
	 * @return    参数说明
	 * List<Rule>    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public QueryResult<Rule> getRuleList(HashMap<String, String> map);
	
	/**
	 * 
	 * getRuleByID:通过规则ID获取对应的规则
	 *
	 * @param id
	 * @return    参数说明
	 * Rule    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public Rule  getRuleByID(String id);
	
	/**
	 * 
	 * getValveValue:获取当前 反垃圾邮件服务器总阀门 的节点值
	 *
	 * @return    参数说明
	 * int    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public int getValveValue();
	
	/**
	 * 
	 * setValveValue:设置当前 反垃圾邮件服务器总阀门 的节点值
	 * @param value 新节点值
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public void setValveValue(String value,CountDownLatch latch);
	
}
