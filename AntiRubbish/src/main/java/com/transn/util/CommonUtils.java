/**
 *  CommonUtils.java
 *  描述：  通用操作类，提供系统中用到的公用方法
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2012-10-31  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.util;



import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author
 */
public class CommonUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	
	/**
	 * 从对应文件中得到国际化资源
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getFromFile(String resourceFileName, String key) {

		ResourceBundle resourceBundle = ResourceBundle
				.getBundle(resourceFileName);
		String value = "";
		try {
			value = resourceBundle.getString(key);
		} catch (Exception e) {
			return key;
		}

		return value.trim();
	}

	/**
	 * 
	 * encode: UTF-8编码
	 *
	 * @param str
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public static String encode(String str) {
		if (StringUtils.isEmpty(str)){
			return "";
		}
		
		try {
			str = java.net.URLEncoder.encode(str.trim(), "UTF-8");
			return str;
		} catch (Exception e) {
			logger.error(""+ e);
		}
		return "";
	}
	
	
	/**
	 * 
	 * decode:UTF-8解码
	 * 
	 * @param cccc
	 * @return 参数说明 String 返回值说明
	 * @throws
	 * @since 　ver 1.0
	 */
	public static String decode(String str) {
		if (StringUtils.isEmpty(str)){
			return "";
		}
		
		try {
			str = java.net.URLDecoder.decode(str, "UTF-8");
			return str.trim();
		} catch (Exception e) {
			logger.error(""+ e);
		}
		return "";
	}

	/**
	 * 
	 * getFlag:获取匹配的标志,是精确匹配还是模糊匹配,默认前者
	 *
	 * @param field
	 * @return    参数说明
	 * boolean    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public static boolean getFlag(String field){
		boolean flag = true;
		if (StringUtils.isEmpty(field)){
			return flag;
		}
		
		flag = Integer.parseInt(field.trim()) == 1;
		
		return flag;
	}
	
	/**
	 * 
	 * titleFilter:去除标题中无意义的字符
	 *
	 * @param  @param title
	 * @param  @return    参数说明
	 * @return String    返回值说明
	 * @throws 
	 * @since  　Ver 1.1
	 */
	public static String titleFilter(String title){
		//title 不为空
		title =title.replaceAll("^((?i)((RE:|FW:|FWD:|答复:|答复：|回复:|回复：|转发:|转发：)(\\s)*)*)", "");
		return title;
	}
	
	
	/**
	 * 
	 * getMD5:MD5 摘要
	 *
	 * @param source
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 
	 * getRealContent:如果是历史邮件，获取本次真正的邮件内容
	 *
	 * @param content
	 * @param historyEmail
	 * @return    参数说明
	 * String    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public static String getRealContent(String content,boolean historyEmail) {
		if (!historyEmail){
			return content;
		}
		String[] Keyword = { "发件人：", "发件人:", "From：", "From:","发自我的 iPhone", "De:"};
		String strTranslate = "";
		int idx = 0;
		try {
			Document doc = Jsoup.parse(content);
			Entities.EscapeMode.base.getMap().clear();
			strTranslate = doc.text();
			for (String key : Keyword) {
				int j = strTranslate.indexOf(key);
				if (j != -1 && (j < idx || idx == 0)) {
					idx = j;
				}
			}
			if (idx != 0) {
				strTranslate = strTranslate.substring(0, idx);
				// 如果包含--- 原始邮件---之类的情况
				strTranslate = strTranslate.replaceAll("-", "");
				strTranslate = strTranslate.replaceAll("原始邮件", "");
				strTranslate = strTranslate.replaceAll("Original Message", "");
				strTranslate = strTranslate.replaceAll("原邮件信息", "");
				//开始以Untitled Document开头为网页Title，去掉
				strTranslate = strTranslate.replaceAll("^Untitled Document[.]*", "");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strTranslate;
	}
	
	public static void main(String[] args) {
		String tmp = "";
		System.out.println(getMD5(tmp.getBytes()));
	}
}
