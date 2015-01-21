package com.limn.tool.httpclient;




import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.regexp.RegExp;



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
       
        GetMethod getMethod = new UTF8GetMethod(url);
        getMethod.setQueryString(param);
//        getMethod.addRequestHeader("Content-type" , "text/html; charset=utf-8");
        try {
			Print.debugLog("URL:" + getMethod.getURI(),4);
		} catch (URIException e) {
		}
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
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
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
				String value = nameValue.get(key);
				if(RegExp.findCharacters(value, "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")){
					value = String.valueOf(Common.getParseTimeStamp(value));
				}
				param[i] = new NameValuePair(key,value);
				i++;
			}
		}
		return param;
	}
	
	/**
	 * 上传附近
	 * @param client
	 * @param file
	 * @param url
	 */
	public static String uploadFile(HttpClient client, File file, String url) {  
        if (!file.exists()) {  
            return null;  
        }  
        PostMethod postMethod = new PostMethod(url);  
        try {  
            //FilePart：用来上传文件的类  
        FilePart fp = new FilePart("filedata", file);  
            Part[] parts = { fp };  
  
            //对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装  
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());  
            postMethod.setRequestEntity(mre);  
            client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 设置连接时间  
            int status = client.executeMethod(postMethod);  
            if (status == HttpStatus.SC_OK) {  
                return postMethod.getResponseBodyAsString();  
            } else {  
                System.out.println("fail");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
  
        	
            //释放连接  
            postMethod.releaseConnection();  
        }
		return null;  
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


class UTF8GetMethod extends GetMethod {
	public UTF8GetMethod(String url) {
		super(url);
	}

	@Override
	public String getRequestCharSet() {
		// return super.getRequestCharSet();
		return "UTF-8";
	}
}