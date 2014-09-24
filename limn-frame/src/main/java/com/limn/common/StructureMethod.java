package com.limn.common;



import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;



/**
 * 公共的  Post发送类  统一处理
 * 
 * @author limn
 *
 */
public class StructureMethod {

	/**
	 * 获取PostMethod
	 * @param param 所有参数
	 * @param url 接口地址
	 * @return PostMethod
	 */
	public static PostMethod getPostMethod(NameValuePair[] param, String url) {
        PostMethod method = new UTF8PostMethod(url);
        method.setRequestBody(param);
        return method;
	}
	
	/**
	 * 执行请求
	 * @param hc HttpClient
	 * @param pm PostMethod
	 * @return
	 */
	public static String execute(HttpClient hc , PostMethod pm){
		String resutls = null;
		try {
			hc.executeMethod(pm);
			resutls = pm.getResponseBodyAsString();
			
		} catch (HttpException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			pm.releaseConnection();
		}
		return resutls;
	}
	
	
}

class UTF8PostMethod extends PostMethod {
	public UTF8PostMethod(String url) {
		super(url);
	}

	@Override
	public String getRequestCharSet() {
		// return super.getRequestCharSet();
		return "UTF-8";
	}
}
