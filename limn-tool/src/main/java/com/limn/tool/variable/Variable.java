package com.limn.tool.variable;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class Variable {

	private static LinkedHashMap<String, String> expression = new LinkedHashMap<String, String>();
	private static HashMap<String, String> externalVariable = new HashMap<String, String>();
	private static String saveLocalVariablePath = Parameter.DEFAULT_TEMP_PATH + "/variableLocal.properties";
	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void setExpressionName(String key, String value) {
		if(expression.size()==0){
			getLocal();
		}
		if(expression.containsKey(key)){
			expression.replace(key, value);
		}else{
			expression.put(key, value);
		}
		saveLocal();
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getExpressionValue(String key) {
		if(expression.size()==0){
			getLocal();
		}
		if (expression.containsKey(key)){
			return expression.get(key);
		}else{
			Print.log("不存在变量：" + key, 2);
			return null;
		}
	}
	
	/**
	 * 删除所有的变量
	 */
	public static void removeExpressionAll() {
		Print.log("删除用例中的全部变量:" + expression.size(), 0);
		expression.clear();
	}
	
	/**
	 * 删除变量
	 * @param key
	 */
	public static void removeExpressionName(String key) {
		if(expression.containsKey(key)){
			Print.log("删除用例中的变量:" + key + "=" + expression.get(key), 0);
			expression.remove(key);
		}
	}
	
	private static void saveLocal(){
		Properties props = new Properties();
		
		for(String key:expression.keySet()){
			if(!externalVariable.containsKey(key)){
				props.put(key, expression.get(key));
			}
		}
		
		FileOutputStream fOut = null;
		Writer out = null;
		try {
			fOut = new FileOutputStream(saveLocalVariablePath);
			out = new OutputStreamWriter(fOut, "UTF-8");
			
			props.store(out, "variable local cache");
		} catch (Exception e) {
			Print.log("存储本地化文件变量出错:" + e.getMessage(),2);
			e.printStackTrace();
		} finally{
			try {
				fOut.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	/**
	 * 获取本地的缓存
	 */
	private static void getLocal(){
		
		Properties props = new Properties();
		
		InputStreamReader isr = null;
		
		if(!new File(saveLocalVariablePath).exists()){
			new File(saveLocalVariablePath).getParentFile().mkdirs();
			return ;
		}
		
		try {
			isr = new InputStreamReader(new FileInputStream(saveLocalVariablePath),"UTF-8");
			props.load(isr);
		} catch (Exception e) {
			Print.log("获取本地化文件变量出错:" + e.getMessage(),2);
		}
		
		expression.clear();
		for(Object key:props.keySet()){
			expression.put((String) key, (String) props.get(key));
		}
	}
	
	
	/**
	 * 
	 * @param path
	 */
	public static void addVariableLocal(String path){
		
		if(expression.size()==0){
			getLocal();
		}
		
		path = FileUtil.getFileAbsolutelyPath(Parameter.DEFAULT_CONF_PATH,path);
		
		Properties variableProps = new Properties();
		File newFile = new File(path);
		if(newFile.exists()){
			try {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
				variableProps.load(isr);

				for(Object key:variableProps.keySet()){
					if(expression.containsKey((String) key)){
						Print.log("警告:存在相同的变量名称:" + (String) key , 3);
					}else{
						expression.put((String) key, (String) variableProps.get(key));
						externalVariable.put((String) key, (String) variableProps.get(key));
					}
				}
				
			} catch (Exception e) {
				Print.log("获取本地化文件变量出错:" + e.getMessage(),2);
			}
			
		}else{
			Print.log("本地化文件变量不存在,路径:" + path,2);
		}
		

	}
	
	/**
	 * 查询变量数据
	 * @param content
	 * @return
	 */
	public static String resolve(String content){
		
		ArrayList<String> variableList = RegExp.matcherCharacters(content, "\\{.*?\\}");
		String varFormat = null;
		for(String var:variableList){
			varFormat = RegExp.filterString(var, "{}");
			content = resolve(content.replace(var, "'"+getExpressionValue(varFormat)+"'"));
		}
		return content;
	}
	
	
	
	public static void main(String[] args){
//		Variable.setExpressionName("zzzz", "ccccc");
		Variable.addVariableLocal("apply.properties");
		System.out.println(Variable.getExpressionValue("Manager"));
	}
	
	
}

