/**
 *  RuleDao.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-11  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.dao.rule;

import java.util.HashMap;
import java.util.List;

import com.transn.dao.base.BaseDao;
import com.transn.model.Rule;
import com.transn.page.QueryResult;

public interface RuleDao extends BaseDao<Rule> {

	/**
	 * 
	 * getAllRules:获取当前所有的规则
	 *
	 * @return    List<Rule> 规则列表
	 * @throws 
	 * @since  　ver 1.0
	 */
	public List<Rule> getAllRules();
	
	/**
	 * 
	 * getRuleByName:根据规则名称获取响应的规则
	 *
	 * @param ruleName
	 * @return    参数说明
	 * Rule    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public Rule getRuleByName(String ruleName);
	
	
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
	 * deleteRuleByName:根据名称删除规则
	 *
	 * @param name    参数说明
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public void deleteRuleByName(String name);
}
