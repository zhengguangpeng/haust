package com.transn.page;

import java.io.Serializable;
import java.util.Map;

public class PageFlip implements Serializable {

	/**
	 * serialVersionUID:
	 * 
	 * @since ver 1.0
	 */

	private static final long serialVersionUID = -6562212815816173025L;

	/**
	 * 构建分页类
	 */

	/** 每页显示记录数 **/
	private Integer pageRows;

	/** 当前页 **/
	private Integer currentpage = 1;

	/** 总记录数 **/
	private Integer totalRecord;

	/** 总页数 **/
	private Integer totalpage;

	/** 每页5个，当前为第几页 **/
	private Integer pages;

	/** 查询条件 **/

	private Map<String, Object> conditionMap;

	public PageFlip() {

	}

	// 翻页的基本构造方法
	public PageFlip(Integer pageRows, Integer currentpage, Integer totalRecord,
			Integer totalpage) {
		super();
		this.pageRows = pageRows;
		this.currentpage = currentpage;
		this.totalRecord = totalRecord;
		this.totalpage = totalpage;
	}

	public Integer getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(Integer currentpage) {
		int pg = currentpage / 5;
		int cpg = currentpage % 5;
		if (cpg == 0) {
			pg = pg -1;
		}
		this.currentpage = currentpage;
		this.pages = pg;
	}

	public Integer getPageRows() {
		return pageRows;
	}

	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}

	public Integer getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public Map<String, Object> getConditionMap() {
		return conditionMap;
	}

	public void setConditionMap(Map<String, Object> conditionMap) {
		this.conditionMap = conditionMap;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	// 当设置总记录数的时候即设置总页数
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
		setTotalpage(this.totalRecord % this.pageRows == 0 ? this.totalRecord
				/ this.pageRows : this.totalRecord / this.pageRows + 1);
		if (this.getCurrentpage() > this.getTotalpage()) {
			this.currentpage = 1;
		}
	}

}
