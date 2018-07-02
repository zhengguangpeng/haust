package com.transn.page;

import java.util.List;

/**
 * 类说明：分页数据结果类
 * 
 * @author 作者：transn
 * @version 创建时间：2012-10-30
 */
public class QueryResult<T> {

	/**
	 * 结果列表
	 */
	private List<T> resultList;
	/**
	 * 记录的总数
	 */
	private long totalRecord;

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

}
