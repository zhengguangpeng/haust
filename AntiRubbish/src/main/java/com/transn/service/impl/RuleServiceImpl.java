/**
 *  RuleServiceImpl.java
 *  描述：  
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-12  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;


import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.transn.camel.MailBean;
import com.transn.camel.RuleMatchResponse;
import com.transn.dao.rule.RuleDao;
import com.transn.model.Rule;
import com.transn.page.QueryResult;
import com.transn.service.EmailAlarmService;
import com.transn.service.RuleService;
import com.transn.util.CommonUtils;
import com.transn.util.Constants;
import com.transn.util.MicroSoftTranslateUtil;


@Service("RuleServiceImpl")
public class RuleServiceImpl implements RuleService,ApplicationContextAware {
	
	//日志
	private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
	@Resource
	private RuleDao ruleDao;
	@Resource
	private EmailAlarmService emailAlarmService;	

	//ZK　Client
	private CuratorFramework client = null;	
	
	//内容规则优先级队列
	private List<Rule> contentRuleList;
	
	//时间规则优先级队列
	private List<Rule> timeRuleList;
		
	//锁对象
	private byte[] lock = new byte[0];
	
	//HashMap 缓存
	private HashMap<String, String> cacheMap = new HashMap<String, String>();
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		
		
		contentRuleList = new ArrayList<Rule>();
		timeRuleList = new ArrayList<Rule>();
		
		//加载当前已有的规则
		initRuleList();
	}
	
	/**
	 * 
	 * initRuleList:首次启动的时候,创建本服务器所管理的邮件账号ZNode
	 *
	 * @param      参数说明
	 * @return void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private void initRuleList(){
		
		logger.info("开始加载当前所有的规则");
		try {
			//获取到所有的规则
			List<Rule> ruleList = ruleDao.getAllRules();	
			int  totalSize = ruleList.size();
			Rule rule = null;
			
			for(int i=0;i<totalSize;i++){
				rule = ruleList.get(i);
				addRule(rule,null,false);
			}
			
		} catch (Exception e) {
			logger.error("" + e);
		}
		
	}

	@Override
	public boolean addRule(Rule rule,CountDownLatch latch,boolean isSyn2DB){
		
		synchronized (lock) {
			
			//规则检查
			if (!validateRule(rule)){
				return false;
			}
			
			//入库
			if (isSyn2DB){
				rule.setCreateTime(new Date());
				ruleDao.save(rule);
			}
			//更新规则列表
			if (rule.getType() == Constants.RULETYPE_CONTENT){
				contentRuleList.add(0,rule);
				//排序
				Collections.sort(contentRuleList);
			}else if (rule.getType() ==  Constants.RULETYPE_TIME){
				timeRuleList.add(0,rule);
				//排序
				Collections.sort(timeRuleList);
			}
		}
		if (latch !=null){
			latch.countDown();
		}
		return true;
	}

	@Override
	public boolean deleteRule(Rule rule,CountDownLatch latch) {
		synchronized (lock) {
			
			ruleDao.deleteRuleByName(rule.getName());
			// 更新规则列表
			deleteRuleFromMemoryByName(rule.getName(),rule.getType());
			if (rule.getType() == Constants.RULETYPE_CONTENT) {
				//重新 排序			
				Collections.sort(contentRuleList);
			} else if (rule.getType() == Constants.RULETYPE_TIME) {
				//重新 排序			
				Collections.sort(timeRuleList);
			}
		}
		latch.countDown();	
		return false;
		
	}
	
	

	@Override
	public boolean modifyRule(Rule rule,CountDownLatch latch) {
		synchronized (lock) {
			//规则检查
			if (!validateRule(rule)){
				return false;
			}		
			rule.setCreateTime(new Date());
			ruleDao.update(rule);
			// 更新规则列表
			if (rule.getType() == Constants.RULETYPE_CONTENT) {
				//更新内存中的
				deleteRuleFromMemoryByName(rule.getName(),rule.getType());
				contentRuleList.add(0, rule);
				//重新 排序
				Collections.sort(contentRuleList);
			} else if (rule.getType() == Constants.RULETYPE_TIME) {
				//更新内存中的
				deleteRuleFromMemoryByName(rule.getName(),rule.getType());
				timeRuleList.add(0, rule);
				//重新 排序
				Collections.sort(timeRuleList);
			}
		}
		latch.countDown();		
		return true;
		
	}

	@Override
	public boolean addRuleZKNode(Rule rule,CountDownLatch latch) {
		
		logger.info("添加一条规则" + rule);
		
		
		return true;
		
	}

	@Override
	public RuleMatchResponse decideEmailByRule(MailBean email,int ruleType){
		
		RuleMatchResponse response = new RuleMatchResponse();
		
		//遍历规则,优先级的已经自动排序在前面了
		Iterator<Rule>  iterator = null;
		if ( ruleType == Constants.RULETYPE_CONTENT ){
			iterator = contentRuleList.iterator();
		}else if (ruleType == Constants.RULETYPE_TIME){
			iterator = timeRuleList.iterator();
		}
		Rule rule = null;
		boolean isMatched = false;
		while (iterator.hasNext()){
			rule = iterator.next();
			//是否匹配
			if (isRuleMatched(rule,email)){
				//记录匹配的规则
				response.getRuleIDList().add(rule.getId());
				isMatched = true;
				break;
			}
		}
		if (isMatched){
			if (rule.isExclude()){
				//如果是排除原则
				response.setBaseField(Constants.BASEFIELD_EXCLUDE);
				response.setExclude(true);
				logger.info("mail 被此条规则匹配,排除在外了,需要后续再处理了 " + rule );
			}else if (rule.getTagType() !=0 ){
				//如果是Tag
				response.setBaseField(Constants.BASEFIELD_TAG);
				response.setTagType(rule.getTagType());
				logger.info("mail 被此条规则匹配,自动打Tag " + rule );
			}else{
				//那就只有一个是不翻译了
				response.setBaseField(Constants.BASEFIELD_RESULT);
				response.setResult(Constants.FILTER_NOTRANSLATE);
				logger.info("mail 被此条规则匹配,将不会被翻译 " + rule );
			}
		}else{
			//都没有找到,那就忽略了
			response.setBaseField(Constants.BASEFIELD_RESULT);
			response.setResult(Constants.FILTER_IGNORE);
		}
		
		
		return response;
		
	}

	
	
	/**
	 * 
	 * isRuleMatched:判断规则是否匹配了此邮件
	 *
	 * @param rule
	 * @param email
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private boolean isRuleMatched(Rule rule,MailBean email){
	
		//规则不应用,则不关注
		if (!rule.isApply()){
			return false;
		}
		
		boolean isMatched = true;
		
		if (rule.getType() == Constants.RULETYPE_CONTENT){
//			logger.info("rule = "+rule);
			//按照内容
			//Sender是否匹配
			if (StringUtils.isNotEmpty(rule.getSender())){
				if (rule.isSenderFlag()){
					//精确匹配
					if (!email.getSender().equalsIgnoreCase(rule.getSender())){
						return false;
					}
				}else{
					//模糊匹配
					if (!email.getSender().toLowerCase().matches(rule.getSender())){
						return false;
					}
				}			
			}
			
			//Reciver是否匹配
			if (StringUtils.isNotEmpty(rule.getReceiver())){
				if (rule.isReceiverFlag()){
					//精确匹配
					if (!email.getReceiver().equalsIgnoreCase(rule.getReceiver())){
						return false;
					}
				}else{
					//模糊匹配
					if (!email.getReceiver().toLowerCase().matches(rule.getReceiver())){
						return false;
					}
				}			
			}
			//Title是否匹配
			if (StringUtils.isNotEmpty(rule.getTitle())){
				if (rule.isTitleFlag()){
					//精确匹配
					if (!email.getTitle().equalsIgnoreCase(rule.getTitle())){
						return false;
					}
				}else{
					//模糊匹配
					if (!email.getTitle().toLowerCase().matches(rule.getTitle())){
						return false;
					}
				}			
			}		
			//邮件正文内容先不关注了		
			
		}else if (rule.getType() == Constants.RULETYPE_TIME ){
			//按照时间
			//Reciver是否匹配
			if (StringUtils.isNotEmpty(rule.getReceiver())){
				if (rule.isReceiverFlag()){
					//精确匹配
					if (!email.getReceiver().equalsIgnoreCase(rule.getReceiver())){
						return false;
					}
				}else{
					//模糊匹配
					if (!email.getReceiver().toLowerCase().matches(rule.getReceiver())){
						return false;
					}
				}			
			}
			//起始时间
			if (!rule.getStartTime().equalsIgnoreCase(Constants.IGNORE_TIME)){
				if (email.getTime().compareTo(rule.getStartTime()) <0){
					return false;
				}
			}
			//终止时间
			if (!rule.getEndTime().equalsIgnoreCase(Constants.IGNORE_TIME)){
				if (email.getTime().compareTo(rule.getEndTime()) >0){
					return false;
				}
			}
		}else{
			//
		}
		
		return isMatched;
	}
	
	/**
	 * 
	 * validateRule:规则检查,看是否与当前已有的规则产生冲突
	 *
	 * @param rule
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private boolean validateRule(Rule rule){
		
		//Sender/Receiver/Title 不能都为空
		if (StringUtils.isEmpty(rule.getSender()) &&
				StringUtils.isEmpty(rule.getTitle())&& 
				StringUtils.isEmpty(rule.getReceiver())
				){
			rule.setValidate(false);
			rule.setRemark("Sender/Title/Receiver至少其中之一不为空");
			return false;
		}
		
		rule.setValidate(true);
		//rule.setRemark("");
		
		//如果产生冲突，后面的不应用
		return true;
	}

	@Override
	public int decideEmailByLang(MailBean email) {
		
		int result = Constants.FILTER_IGNORE;
		
		if (StringUtils.isEmpty(CommonUtils.titleFilter(email.getTitle()))  && StringUtils.isEmpty(email.getContent())){
			return Constants.FILTER_NOTRANSLATE;
		}
		
		int emailLangCode = getLanCodeByEmail(email);
		if (Constants.LANG_CHINESE == emailLangCode) {
			result = Constants.FILTER_NOTRANSLATE;
		} else if (Constants.LANG_UNKNOWN == emailLangCode) {
			// 如果是未知语种，则翻译
			result = Constants.FILTER_TRANSLATE;
		} else {
			// 如果是明确语种，则还要判断是否是在用户接收范围之内
			if(isContainsSpecialLanCode(email.getLanuage(),emailLangCode)) {
				result = Constants.FILTER_TRANSLATE;
			} else {
				result = Constants.FILTER_NOTRANSLATE;
			}				
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * getLanCodeByEmail:通过调用微软翻译接口获取当前邮件语种
	 *
	 * @param email
	 * @return    参数说明
	 * int    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private int getLanCodeByEmail(MailBean email){
		
		int langCode = Constants.LANG_UNKNOWN;
		int titleLanCode = Constants.LANG_UNKNOWN;
		int contentLanCode  = Constants.LANG_UNKNOWN;
		boolean isTitleEmpty = false;
		boolean isContentEmpty = false;
		try {
			//邮件标题
			String title = email.getTitle();
			if (StringUtils.isEmpty(title)){
				//标题为空,记录下日志就可以了
				isTitleEmpty = true;
			}else{
				titleLanCode = MicroSoftTranslateUtil.detectTitleLanguage(CommonUtils.titleFilter(title));
				logger.info("title: " + title+ " langCode = " + titleLanCode);
			}
			
			//邮件正文
			String content = email.getContent();	
			if (content.length() > Constants.SUB_HEAD_MAILCONTENT){
				content = content.substring(0,Constants.SUB_HEAD_MAILCONTENT);				
			}
			if (StringUtils.isEmpty(content)){
				//正文为空,记录下日志就可以了
				isContentEmpty = true;
			}else{
				//如果包含中文标识,则直接作为未知语种处理
				if (isContentContainsFlag(content)){
//					如果标题是中文并且是历史回复邮件的话
//					if (titleLanCode == Constants.LANG_CHINESE ){
//						logger.info("邮件标题是中文并且是历史回复邮件,进一步截取后进行语种判断");
//						String realContent = CommonUtils.getRealContent(content, true);
//						contentLanCode = MicroSoftTranslateUtil.detectTitleLanguage(realContent);
//					}else{
//						contentLanCode = Constants.LANG_UNKNOWN;
//						logger.info("content: " + content+ " 包含历史邮件内容的标识,作为未知语种处理");
//					}
					//放开标题语种的判断了
					logger.info("邮件是历史回复邮件,进一步截取后进行语种判断");
					String realContent = CommonUtils.getRealContent(content, true);
					contentLanCode = MicroSoftTranslateUtil.detectTitleLanguage(realContent);
					
				}else{
					contentLanCode = MicroSoftTranslateUtil.detectTitleLanguage(content);
					logger.info("content: " + content+ " langCode = " + contentLanCode);
				}
			}
			
			//根据判断逻辑进行处理
			/* 判断逻辑:
		         A:标题为空，正文不为空，取正文语种；
		         B:标题不空，正文空，取标题语种
		         C:标题和正文的都不为空: 
		         	标题(明确)语种和正文(明确)语种都不在用户接收语种之内,设置为正文内容语种;
		         D:标题 和正文的都不为空:
		                              正文明确，标题未知但是短于15个字符串，设置为正文内容语种
		     */
			if (isTitleEmpty && !isContentEmpty){
				langCode = contentLanCode;
			}else if (!isTitleEmpty && isContentEmpty){
				langCode = titleLanCode;
			}else if (!isTitleEmpty && !isContentEmpty){
				if (contentLanCode == titleLanCode){
					//两个一致,随便
					langCode = contentLanCode;
				}else{
					//两个不一致,并且都为明确语种
					if (contentLanCode != Constants.LANG_UNKNOWN && titleLanCode != Constants.LANG_UNKNOWN){
						//都不在接收语种列表之内
						if (!isContainsSpecialLanCode(email.getLanuage(),titleLanCode) && !isContainsSpecialLanCode(email.getLanuage(),contentLanCode)){
							langCode = contentLanCode; //设置为正文内容的语种
						}
					}else if (contentLanCode != Constants.LANG_UNKNOWN && titleLanCode == Constants.LANG_UNKNOWN){
						//内容明确,标题太短而判断为未知的
						if (title.length() <= Constants.SHORT_TITLE_LENGTH){
							langCode = contentLanCode; //设置为正文内容的语种
						}
					}else{
						langCode = Constants.LANG_UNKNOWN; //多余的赋值,只为逻辑清晰
					}
				}
			}else{
				//都为空,仍然保持未知语种状态
				langCode = Constants.LANG_UNKNOWN; //多余的赋值,只为逻辑清晰
			}
			
		} catch (Exception e) {			
			logger.error("获取邮件语种的时候出错了..." + e);
		}
		
		return langCode;
	
	}
	
	/**
	 * 判断内容是否包含历史邮件特定字符串标识，如果包含，则直接将内容设置为未知语种
	 * @param content
	 * @return
	 */
	private boolean isContentContainsFlag(String content){
		//中文标识
		String specialChineseFlag = "[\\s\\S]*(原邮件信息|发件人:|发件人：" +
				"|From：|From:|原始邮件|Original Message|원본 메일|" +
				"보낸사람:|제목:)+[\\s\\S]*";
		if (content.matches(specialChineseFlag)){
			return true;
		}
		return false;
	}

	/**
	 * 用户指定接收语种列表之内是否包括指定的语种
	 * @param receiveList 用户接收语种列表,逗号分隔的字符串
	 * @param special 指定语种
	 * @return
	 */
	private boolean isContainsSpecialLanCode(String receiveList,int special){
		String[] lanCodeArray = org.apache.commons.lang.StringUtils.split(receiveList, ",");
		for (String lanCode:lanCodeArray){
			if (lanCode.equals(""+special)){
				//如果相等,则包含
				return true;
			}
		}
		
		return false;
	}

	@Override
	public QueryResult<Rule> getRuleList(HashMap<String, String> map) {
		return ruleDao.getRuleList(map);
		
	}

	@Override
	public Rule getRuleByID(String id) {
		return ruleDao.getRuleByID(id);
		
	}

	@Override
	public boolean modifyRuleZKNode(Rule rule,CountDownLatch latch) {
		
		logger.info("修改一条规则" + rule);
		
		
		return true;
		
	}

	@Override
	public boolean deleteRuleZKNode(Rule rule,CountDownLatch latch) {
		
		logger.info("删除一条规则" + rule);
		
		return true;
		
	}
	
	/**
	 * 
	 * deleteRuleFromMemoryByName:根据规则名称从内存中删除规则
	 *
	 * @param name   规则名称
	 * @param ruleType   规则类型
	 * void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private void deleteRuleFromMemoryByName(String name,int ruleType){
		Iterator<Rule>  iterator = null;
		if ( ruleType == Constants.RULETYPE_CONTENT ){
			iterator = contentRuleList.iterator();
		}else if (ruleType == Constants.RULETYPE_TIME){
			iterator = timeRuleList.iterator();
		}
		
		while (iterator.hasNext()){
			if (iterator.next().getName().equals(name)){
				iterator.remove();
				break;
			}
		}
	}
	
	/**
	 * 
	 * getValveValue:获取当前 反垃圾邮件服务器总阀门 的节点值
	 *
	 * @return    参数说明
	 * int    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public int getValveValue(){		
		
		if (cacheMap.get("valve") != null){
			return Integer.parseInt(cacheMap.get("valve"));
		}
		
		String result = "0";
		if (StringUtils.isEmpty(result)){
			cacheMap.put("valve", "-1");
			return -1;
		}
		cacheMap.put("valve", result);
		return Integer.parseInt(result);
	}

	@Override
	public void setValveValue(String value,CountDownLatch latch) {		
		
		
	}
	
}
