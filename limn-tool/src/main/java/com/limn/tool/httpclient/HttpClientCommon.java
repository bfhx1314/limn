//package com.limn.tool.httpclient;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.GetMethod;
//
//import com.limn.tool.common.Print;
//
//public class HttpClientCommon {
//
//	private static HttpClient Client = new HttpClient();
//	
//
//	
//	public static String getResult(String ip, int port , String url,HashMap<String, String> nameValue){
//		setClient(ip, port);
//		return executeMethod(nameValue, url);
//	}
//	
//	
//	private static void setClient(String ip, int port) {
//		Client.getHostConfiguration().setHost(ip, port, "http");
//		
//	}
//	
//	private static String executeMethod(HashMap<String, String> nameValue, String url) {
//		String result = null;
//		NameValuePair[] param = StructureMethod.getNameValuePair(nameValue);
//		GetMethod postMethod = StructureMethod.getGetMethod(param, url);
//		postMethod.setRequestHeader("X-Requested-With", "XMLHttpRequest");
//		try {
//			Client.executeMethod(postMethod);
//			result = inputStream2String(postMethod.getResponseBodyAsStream());
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Print.debugLog("Response:" + result, 4);
//		return result;
//	}
//	
//	private static String inputStream2String(InputStream is) throws IOException {
//		StringBuilder sb1 = new StringBuilder();
//		byte[] bytes = new byte[4096];
//		int size = 0;
//
//		try {
//			while ((size = is.read(bytes)) > 0) {
//				String str = new String(bytes, 0, size, "UTF-8");
//				sb1.append(str);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return sb1.toString();
//	}
//	
//}
