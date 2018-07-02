/**
 *  FcHttpClient.java
 *  描述：  模拟客户端发送Http请求
 *
 *  版本:    v1.0
 *  作者:    leiliu.liu
 *  日期:    2012-11-1  
 *  版权所有 2005-2012 传神(中国)网络科技有限公司
 */
package com.transn.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class FcHttpClient {
	

	//日志对象

	private static final Logger logger = Logger
			.getLogger(FcHttpClient.class);

	

	public String getResult(String url, NameValuePair[] nvp) {
		
		HttpClient hc = new HttpClient();
		hc.getParams().setBooleanParameter("http.protocol.expect-continue", false);   
		//读取配置文件中的超时时间
		String ConnectionTimeout = CommonUtils.getFromFile("conf.common","ConnectionTimeout");		
		String SoTimeout = CommonUtils.getFromFile("conf.common","SoTimeout");		
		
		//链接超时
		hc.getHttpConnectionManager().getParams().setConnectionTimeout(Integer.parseInt(ConnectionTimeout));  
		//读取超时
		hc.getHttpConnectionManager().getParams().setSoTimeout(Integer.parseInt(SoTimeout));
		String responseStr = "";
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Connection", "close");   
		try {
			if (nvp != null) {	
				post.setRequestBody(nvp);
			}
			hc.executeMethod(post);
			// 打印返回的信息
			responseStr = post.getResponseBodyAsString();
		} catch (HttpException e) {
			logger.error(url+"...请求失败..."+e);
		} catch (IOException e) {
			logger.error(url+"...请求失败..."+e);
		} catch (Exception e ) { 
			logger.error(url+"...请求失败..."+e);
		} finally {
			// 释放连接
			post.releaseConnection();
		}
		return responseStr;
	}

	/**
     * get 方式访问URL 
     * @param urlLink
     * @return
     * @throws IOException
     */
    public  String getLineFromHttpURLConnection(String urlLink){
       
    	HttpURLConnection httpConn =null;
       // BufferedReader in =null;
        String str = "";
        //String line=null;
        try{
        	URL url = new URL(urlLink);
            URLConnection connSpms = url.openConnection();
            if(connSpms instanceof HttpURLConnection){
            	httpConn = (HttpURLConnection)connSpms;
            	httpConn.setConnectTimeout(30000); //毫秒=30秒
            	httpConn.setReadTimeout(30000);
            	
            	httpConn.connect();
            	
            	InputStream in = connSpms.getInputStream();
            	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            	byte[] buff = new byte[256];
                int rc = 0;
                while ( (rc = in.read(buff, 0, 256)) > 0) {
                     outStream.write(buff, 0, rc);
                }
                byte[] b = outStream.toByteArray();
                
                outStream.close();
                in.close();                
                str =new String(b);
                
//                in = new BufferedReader(new InputStreamReader(connSpms.getInputStream()));
//                //String line = in.readLine();
//            	while ((line = in.readLine()) != null) {
//            		str = line;
//            		//str += line+"\r\n";
//        		}
            }
        } catch(Exception e) {
        	e.printStackTrace();
            return null;
        }finally {
        	//in.close();
        	if (httpConn!=null){
        		httpConn.disconnect();
        	}
        }
    	return str;
    }
	
   public static void main(String[] args) {}
	
	
	
}
