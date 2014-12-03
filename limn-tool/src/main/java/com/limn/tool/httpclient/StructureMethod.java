package com.limn.tool.httpclient;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



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
	 * 获取GetMethod
	 * @param param 所有参数
	 * @param url 接口地址
	 * @return PostMethod
	 */
	public static GetMethod getGetMethod(NameValuePair[] param, String url) {
       
        GetMethod getMethod = new GetMethod(url);
        getMethod.setQueryString(param);
        return getMethod;
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
			pm.getParams().setParameter(
				"http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
			hc.executeMethod(pm);
			InputStream res = pm.getResponseBodyAsStream();
			resutls = inputStream2String(res);
//			resutls = pm.getResponseBodyAsString();
			
		} catch (HttpException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			pm.releaseConnection();
		}
		return resutls;
	}
	
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	
	
	public static NameValuePair[] getNameValuePair(HashMap<String,String> nameValue){
		int size = nameValue.size();
		for(String key : nameValue.keySet()){
			if(null == nameValue.get(key)){
				size--;
			}
		}
		
		NameValuePair[] param = new NameValuePair[size];
		
		int i = 0;
		for(String key : nameValue.keySet()){
			if(null != nameValue.get(key)){
				param[i] = new NameValuePair(key,nameValue.get(key));
				i++;
			}
		}
		return param;
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
