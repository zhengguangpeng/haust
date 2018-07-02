/**
 *  RuleDaoJpa.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-11  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.dao.rule.jpa;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.transn.dao.base.jpa.BaseDaoJpa;
import com.transn.dao.rule.RuleDao;
import com.transn.model.Rule;
import com.transn.page.QueryResult;


@Repository("ruleDaoJpa")
public class RuleDaoJpa extends BaseDaoJpa<Rule> implements RuleDao{

	
	
	@Override
	public List<Rule> getAllRules() {
		
		StringBuffer selectHql = new StringBuffer();
		selectHql.append("select r FROM Rule r ");
		Query q = this.em.createQuery(selectHql.toString());
		List<Rule> list =  q.getResultList();
		return list;		
	}
	
	/**
	 * 根据邮箱获取用户信息
	 * 
	 * @param emailName
	 *            客户端发送者邮箱
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Rule getRuleByName(String ruleName){
		StringBuffer selectHql = new StringBuffer();
		selectHql.append("select rule FROM Rule rule where rule.name=?");
		Query q = this.em.createQuery(selectHql.toString());
		q.setParameter(1, ruleName);
		List<Rule> list =  q.getResultList();
		if(list != null && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public QueryResult<Rule> getRuleList(HashMap<String, String> map) {
		   
		try {
		    int pageno = Integer.parseInt(map.get("pageno").toString() );
		    int maxSize = Integer.parseInt(map.get("maxSize").toString() );
			StringBuffer selectHql = new StringBuffer();
			selectHql.append("	1=1 ");
			if (map.get("type") != null) {
				selectHql.append("	and type = " +map.get("type"));
			}
			
			if (map.get("sender1") != null) {
				selectHql.append("	and sender like '%" +map.get("sender1") + "%' ");
			}
			if (map.get("receiver1") != null) {
				selectHql.append("	and receiver like '%" +map.get("receiver1") + "%' ");
			}
			if (map.get("title1") != null) {
				selectHql.append("	and title like '%" +map.get("title1") + "%' ");
			}
			
			selectHql.append(" order by createTime desc");
			QueryResult<Rule> qr= this.getScrollData(Rule.class, (pageno - 1) * maxSize , maxSize ,selectHql.toString() ,null );
			return qr ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Rule getRuleByID(String id) {
		
		StringBuffer selectHql = new StringBuffer();
		selectHql.append("select rule FROM Rule rule where rule.id=?");
		Query q = this.em.createQuery(selectHql.toString());
		q.setParameter(1, id);
		List<Rule> list =  q.getResultList();
		if(list != null && list.size()>0)
		{
			return list.get(0);
		}
		return null;
		
	}
	
	
	public void deleteRuleByName(String name) {
		
		Rule rule = getRuleByName(name);
		em.remove(rule);
		em.flush();
		
	}

}
