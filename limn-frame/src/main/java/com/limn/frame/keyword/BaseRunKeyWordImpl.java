package com.limn.frame.keyword;

import java.util.ArrayList;
import java.util.HashMap;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.Test;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class BaseRunKeyWordImpl {
	
	/**
	 * 启动浏览器
	 * @param step 数组参数 0 可选参数 1浏览器类型 可选参数  2URL
	 * @throws SeleniumFindException
	 */
	public static void startBrowser(String[] step) throws SeleniumFindException{
		int stepLen = step.length;
		for(int i=1;i<stepLen;i++){
			if (RegExp.findCharacters(step[i], "(?i)^http|^https")){
				if (stepLen <= i + 1) {
					throw new SeleniumFindException("URL地址为空。");
				}
				Parameter.URL = step[i] + ":" + step[i + 1];
				i++;
			}else if(RegExp.findCharacters(step[i], "(?i)firefox|ie|chrome")){
				if(step[i].equalsIgnoreCase("firefox")){
					Parameter.BROWSERTYPE = 1;
				}else if(step[i].equalsIgnoreCase("ie")){
					Parameter.BROWSERTYPE = 3;
				}else if(step[i].equalsIgnoreCase("chrome")){
					Parameter.BROWSERTYPE = 2;
				}else{
					throw new SeleniumFindException("不支持此浏览器类型:" + step[i] + " 支持的类型有:firefox,chrome,ie");
				}
			}else if(RegExp.findCharacters(step[i], "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])(\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5][1-9\\d]|\\d)){3}")){
				Parameter.REMOTEIP = step[i];
			}
		}
		
//		if(step.length > 2){
//			Parameter.URL = step[2];
//		}
		startBroswer(Parameter.BROWSERTYPE, Parameter.URL, Parameter.REMOTEIP);
	}
	
	
	/**
	 * 初始化浏览器
	 * @param type 浏览器类型 1firefox 2chrome
	 * @param url 地址
	 * @param ip 远程运行IP 可选
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public static void startBroswer(int type, String url, String ip) throws SeleniumFindException {
		Driver.setDriver(type, url, ip);
		Driver.startBrowser();
	}
	
	
	/**
	 * 关闭浏览器
	 */
	public static void stopBroswer(){
		Driver.closeBrowser();
	}
	
	/**
	 * 页面跳转
	 * @param step
	 * @throws SeleniumFindException
	 */
	public static void toURL(String[] step) throws SeleniumFindException{
		String url = step[1];
		if(step.length >= 2){
			url = step[1] + ":" + step[2];
		}
		Print.log("URL:" + url,0);
		Driver.changeURL(url);
	}
	
	public static void keyBoardEvent(String[] step){
		Common.wait(2000);
		Driver.keyBoardEvent(step[1]);
	}
	
	/**
	 * 
	 * @param step
	 * @throws SeleniumFindException
	 */
	public static void inputValue(String[] step) throws SeleniumFindException{
		HashMap<String,String> traXPath = null; 
		if(step.length >= 4 && RegExp.findCharacters(step[3], "^HASHMAP")){
			traXPath = TransformationMap.transformationByString(step[3]);
		}else{
			String context = Test.getAssociatedProperites();
			if(null == context){
				traXPath = null;
			}else{
				traXPath = TransformationMap.transformationByString(context);
			}
		}
		String xpath = null;
		if(null != traXPath){
			if(traXPath.containsKey(step[1])){
				xpath = traXPath.get(step[1]);
			}else{
				xpath = step[1];
			}
		}else{
			xpath = step[1];
		}
		
		Driver.setValue(xpath,step[2]);
		
	}

	/**
	 * 表达式 (“=”右边暂时仅支持一次运算，暂时只支持“+”、“&”运算) 
	 * @param step 用例
	 * @throws ParameterException 
	 */
	public static void executeExpression(String[] step) throws ParameterException {
		String variable = null;
		if (RegExp.findCharacters(step[1], "=")){
			String[] arr = RegExp.splitWord(step[1], "=");
			if (RegExp.findCharacters(arr[0], "^\\{.*\\}$")){
				if (!arr[1].equals("")){
					
				}else{
					throw new ParameterException("表达式不完整："+step[1]);
				}
				ArrayList<String> arrL = RegExp.matcherCharacters(arr[0], "(?<=\\{)(.+?)(?=\\})");
				variable = arrL.get(0);
				
			}else{
				throw new ParameterException("表达式缺少“{ }变量”"+step[1]);
			}
		}else{
			throw new ParameterException("表达式缺少“=”"+step[1]);
		}

	}
	
	public static void exeExp(String str) throws ParameterException {
		String arr[] = null;
		
		if (RegExp.findCharacters(str, "+")){
			arr = RegExp.splitWord(str, "+");
			try{
				int left = Integer.parseInt(arr[0]);
			}catch(NumberFormatException e){
				throw new ParameterException(arr[0] +"不是数字，无法运算。");
			}
			try{
				int right = Integer.parseInt(arr[1]);
			}catch(NumberFormatException e){
				throw new ParameterException(arr[1] +"不是数字，无法运算。");
			}
		}else if (RegExp.findCharacters(str, "&")){
			arr = RegExp.splitWord(str, "&");
			
		}else if(RegExp.findCharacters(str, "\\(|\\)|*|/")){
			throw new ParameterException("暂时不支持次运算："+str);
		}else{
			
		}

	}
	
	public static void main(String[] args){
		String a = "{asd}";
		boolean b = RegExp.findCharacters(a, "^\\{.*\\}$");
		ArrayList<String> arrL = RegExp.matcherCharacters(a, "(?<=\\{)(.+?)(?=\\})");
		System.out.println();
	}
	
}

