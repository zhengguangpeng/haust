/**
 *  MicroSoftTranslateUtil.java
 *  描述：  调用微软翻译API 接口来判断邮件标题的语种
 *
 *  版本:    v1.0
 *  作者:    rockyzheng
 *  日期:    2013-5-28  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
*/
package com.transn.util;


import java.net.URLEncoder;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.memetix.mst.detect.*;
import com.memetix.mst.language.*;

public class MicroSoftTranslateUtil {
	
	private static final Logger log = Logger.getLogger(MicroSoftTranslateUtil.class);
	
	//自己搭建的连接微软API的URL
	private static String ownURL = "http://mailpowerlang.transn.net:9090/lancode";
	
	//已经注册的微软翻译API的用户ID及私钥
	//目前微软提供每月200万字符的免费服务
	private static String[][] allAvailableCount ={
		{"ROCKYZHENG_TRANSLATE","Aq6IDP4Y0yLiyAfdnNchH+qxqozZy/9JKzIfAvILo6k"},
		{"YANXIN_TRANSLATE","0UTbrfo+hpdxV4f8cmXfCATe66yKPKloF6RdveWukTs="},
		{"RELAYMAIL_TRANSLATE","avekbKDG6Sze0LShAVr1fdfEpXIntspIPKBnNTK7YuM="},
		{"RELAYMAIL_TRANSLATE2","ql1uQ345jMfxRNVHUOvpivEAMLDn7qXMJc6rjINCqAk="},
		{"RELAYMAIL_TRANSLATE3","ca3dqf5UZPZ88fFOdp/YeTIz5VKtLoUTAkmJFhu70dM="}
		
	};
	//当前使用的用户
	private static int currentCountIndex = 0;
	
	/**
	 * 
	 * getAvailableAccount:获取可用的微软翻译API的注册用户ID以及私钥
	 * @param  @return    参数说明
	 * @return StringUtils[]    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private static String[] getAvailableAccount(){
		return allAvailableCount[currentCountIndex];
	}

	/**
	 * 
	 * detectTitleLanguage:(这里用一句话描述这个方法的作用)
	 *
	 * @param  @param title
	 * @param  @return    参数说明
	 * @return int[]    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	public static int   detectTitleLanguage(String title){
		
		//记录语种
		 
		int langCode = Constants.LANG_UNKNOWN;
		String[] account = getAvailableAccount();
		
		Detect.setClientId(account[0]);
		Detect.setClientSecret(account[1]);
		
		try {
			Language detectedLanguage = Detect.execute(title);
			langCode = getLangCodeByLang(detectedLanguage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("调用Microsoft Translate API 第一次语种判断的时候出错了 " + title  + e);
			//出错之后等一秒再重试一次,如果再次出错就调动自己服务器上的语种判断，如果再次出错就判断为未知
			try{
				Thread.sleep(1000);
				
				Language detectedSecond = Detect.execute(title);
				langCode = getLangCodeByLang(detectedSecond);
			}catch (Exception tx){
				log.error("调用Microsoft Translate API 第二次语种判断的时候出错了,调用下自己的 " + title  + e);
				langCode =  getLanCodeByOwnServer(title);
			}
					
		}
		
		return langCode;
	}
	
	/**
	 * 
	 * getLangCodeByLang:语种转换
	 *
	 * @param  @param detectedLanguage
	 * @param  @return    参数说明
	 * @return int    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */
	private static int getLangCodeByLang(Language detectedLanguage){
		
		int langCode = Constants.LANG_UNKNOWN;
		
		if (detectedLanguage == Language.CHINESE_SIMPLIFIED){ //中文简体
			langCode = 1;
		}else if (detectedLanguage == Language.CHINESE_TRADITIONAL){ //中文繁体
			langCode = 1; //中文繁体也统一为简体
		}else if (detectedLanguage == Language.ENGLISH){ //英语
			langCode = 2;
		}else if (detectedLanguage == Language.JAPANESE){ //日语
			langCode = 3;
		}else if (detectedLanguage == Language.FRENCH){ //法语
			langCode = 4;
		}else if (detectedLanguage == Language.GERMAN){ //德语
			langCode = 5;
		}else if (detectedLanguage == Language.RUSSIAN){ //俄语
			langCode = 6;
		}else if (detectedLanguage == Language.SPANISH){ //西班牙语
			langCode = 11;
		}else if (detectedLanguage == Language.ARABIC){ //阿拉伯语
			langCode = 13;
		}
		
		return langCode;
	}
	
	/**
	 * 更新URL
	 * @param url
	 */
	public static void setOwnURL(String url){
		ownURL = url;
		log.info("ownURL = " + ownURL);
	}
	
	/**
	 * 
	 * @param title
	 * @return
	 */
	private static int getLanCodeByOwnServer(String title){
	
		FcHttpClient httpclient = new FcHttpClient();
		
		try {
			String value = URLEncoder.encode(title,"UTF-8");		
			NameValuePair[] nvp = {new NameValuePair("text", value)};
			String result = httpclient.getResult(ownURL, nvp);
			log.info("调用自己的服务器获取到语种了:　 " + title + " and lang = " + result);
			return Integer.parseInt(result.trim());
		} catch (Exception e) {
			log.error("调用自己的服务器判断语种出错了" + title);
			e.printStackTrace();
		}
				
		return Constants.LANG_UNKNOWN;
	}
	
	/**
	 * main:(这里用一句话描述这个方法的作用)
	 *
	 * @param  @param args    参数说明
	 * @return void    返回值说明
	 * @throws 
	 * @since  　ver 1.0
	 */

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		String title = "Nuevas ofertas publicada";
		long current = System.currentTimeMillis();
		System.out.println(detectTitleLanguage(title));
		System.out.println("Need time : " + (System.currentTimeMillis() - current));

		try {
			//System.out.println(CommonUtils.getFileSizes(new File("D:/chrome_download/test2.eml")));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

}
