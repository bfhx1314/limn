package com.limn.frame.keyword;

import java.util.ArrayList;
import java.util.HashMap;






import org.apache.commons.lang.StringUtils;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.Test;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.parser.Parser;
import com.limn.tool.parser.SyntaxTree;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

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
		if (RegExp.findCharacters(step[2], "\\{.*\\}")){
			String var = RegExp.filterString(step[2], "{}");
			step[2] = Variable.getExpressionValue(var);
		}
		Driver.setValue(xpath,step[2]);
		
	}

	/**
	 * 表达式支持所有运算
	 * @param step 用例
	 * @throws ParameterException 
	 */
	public static void executeExpression(String[] step) throws ParameterException {
		String variable = null;
		String variableValue = null;
		if (RegExp.findCharacters(step[1], "=")){
			String[] arr = RegExp.splitWord(step[1], "=");
			if (RegExp.findCharacters(arr[0], "^\\{.*\\}$")){
				if (!arr[1].equals("")){
					variableValue = getExpressionValue(arr[1]);
					if (null != variableValue){
						ArrayList<String> arrL = RegExp.matcherCharacters(arr[0], "(?<=\\{)(.+?)(?=\\})");
						variable = arrL.get(0);
						Variable.setExpressionName(variable, variableValue);
					}else{
						throw new ParameterException("语法解析失败，表达式："+arr[1]);
					}

				}else{
					throw new ParameterException("表达式不完整："+step[1]);
				}
			}else{
				throw new ParameterException("表达式缺少“{ }变量”"+step[1]);
			}
		}else{
			throw new ParameterException("表达式缺少“=”"+step[1]);
		}
	}
	/**
	 * 获取用例变量值，支持变量再次运算
	 * @param str 变量表达式
	 * @return 解析后的结果
	 * @throws ParameterException
	 */
	public static String getExpressionValue(String str) throws ParameterException {
		String variableValue = null;
		String exp = Variable.resolve(str);
		SyntaxTree tree = new SyntaxTree();
		Parser parser = new Parser();
		String[] array = {};
		if (RegExp.findCharacters(exp, "&")){
			array = exp.split("&");
		}
		if (array.length == 0){
			try{
				variableValue = parser.eval(null, exp, tree, null).toString();
			}catch(Exception e){
//				throw new ParameterException("语法解析失败，表达式："+str);
			}
		}else{
			for(int i=0;i<array.length;i++){
				try{
					array[i] = parser.eval(null, array[i], tree, null).toString();
				}catch(Exception e){
					
				}finally{
					array[i] = "'"+array[i]+"'";
				}
			}
			String strArr = StringUtils.join(array,"&");
			try {
				variableValue = parser.eval(null, strArr, tree, null).toString();
			} catch (Exception e) {
//				throw new ParameterException("语法解析失败，表达式："+str);
			}
		}
		return variableValue;
	}
	
	public static void main(String[] args){
		
//		try {
//			String[] testP = {"setVar","{setVar}=getAutoIncrement(5)"};
//			executeExpression(testP);
//			
//			System.out.println(Variable.getExpressionValue("setVar"));
//			
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String[] testP = {"setVar","{setVar}='testVar'"};
//		try {
//			executeExpression(testP);
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String[] testPa = {"getVar","{getVar}={setVar}&'addString'"};
//		String a = "{asd}";
//		try {
//			executeExpression(testPa);
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		boolean b = RegExp.findCharacters(a, "^\\{.*\\}$");
//		ArrayList<String> arrL = RegExp.matcherCharacters(a, "(?<=\\{)(.+?)(?=\\})");
//		System.out.println();
	}
	
}

