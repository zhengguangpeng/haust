/**
 *  RuleMatchResponse.java
 *  描述：  规则匹配后的结果
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2014-3-14  
 *  版权所有 2005-2014 传神(中国)网络科技有限公司
*/
package com.transn.camel;

import java.util.ArrayList;
import java.util.List;


public class RuleMatchResponse {

	//以下列哪个结果为准
	private int baseField = 0; //二进制XXX,1-代表第一个isExclude,2代表tagType,4代表result
	//是否是被排除的,如果被排除，说明符合此规则的邮件有可能会被翻译
	private boolean isExclude;
	//结果是要打Tag的
	private int tagType;
	//非前两者的结果,匹配到不翻译或者没有匹配到规则的
	private int result;
	//应用到的规则
	private List<String> ruleIDList = new ArrayList<String>();
	//当前反垃圾邮件服务器总阀门的值
	private int valveState = -1;
	
	public int getBaseField() {
		return baseField;
	}
	public void setBaseField(int baseField) {
		this.baseField = baseField;
	}
	public boolean isExclude() {
		return isExclude;
	}
	public void setExclude(boolean isExclude) {
		this.isExclude = isExclude;
	}
	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	public List<String> getRuleIDList(){
		return this.ruleIDList;
	}

	@Override
	public String toString() {
		return "RuleMatchResponse [baseField=" + baseField + ", isExclude="
				+ isExclude + ", tagType=" + tagType + ", result=" + result
				+ "]" + " ruleIDList = "  + (ruleIDList.size() >0 ? ruleIDList.get(0):"");
	}
	public int getValveState() {
		return valveState;
	}
	public void setValveState(int valveState) {
		this.valveState = valveState;
	}
}
