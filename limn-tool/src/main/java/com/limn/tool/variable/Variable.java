package com.limn.tool.variable;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;



//import test.svn.ExportForSVN;

public class Variable {

	private static String AUTOMATION = Parameter.DFAULT_TEST_PATH;
	private static LinkedHashMap<String, String> expression = new LinkedHashMap<String, String>();
	public static void setExpressionName(String key, String value) {
		if(expression.containsKey(key)){
			expression.replace(key, value);
		}else{
			expression.put(key, value);
		}
	}
	public static String getExpressionValue(String key) {
		if (expression.containsKey(key)){
			return expression.get(key);
		}else{
			Print.log("不存在变量：" + key, 2);
			return null;
		}
	}
	public static void removeExpressionAll() {
		Print.log("删除用例中的全部变量:" + expression.size(), 0);
		expression.clear();
	}
	public static void removeExpressionName(String key) {
		if(expression.containsKey(key)){
			Print.log("删除用例中的变量:" + key + "=" + expression.get(key), 0);
			expression.remove(key);
		}
	}
	
	private static Properties variableProps = new Properties();
	private static Properties SVNProps = new Properties();
	
	private static long lastModfiyVar = 0;
	private static long lastModfiySVN = 0;
	
	private static boolean isInit = false;
	
	private static String getVariable(String key){
		
		String retrunValue = null;
		retrunValue = getExpressionValue(key);
//		if(key.equalsIgnoreCase("Automation")){
//			retrunValue = AUTOMATION;
//		}else{
//			if(variableProps.containsKey(key)){
//				retrunValue = variableProps.getProperty(key);
//			}else{
//				System.out.println("不存在变量的值 : " + key);
//				// TODO 报错
//			}
//		}
		return retrunValue;
	}
	
	
/*	private static String getSVNVariable(String key){
		if(SVNProps.containsKey(key)){
			File svnFile = new File(Parameter.DFAULT_TEST_PATH + "/" + key);

			ExportForSVN exp = new ExportForSVN();
			exp.setBranchURL(SVNProps.getProperty(key));
			exp.setExportPath(svnFile.getAbsolutePath());
			exp.setUsername(getVariable("SVNUserName"));
			exp.setPassWord(getVariable("SVNPassWord"));
			exp.execute();

		}else{
			// TODO 报错
		}
		return key;
	}*/
	
	private static void init(){
		InputStreamReader isr = null;
		if (!FileUtil.exists(Parameter.DEFAULT_CONF_PATH)){
			FileUtil.createFloder(Parameter.DEFAULT_CONF_PATH);
		}
		if (!FileUtil.exists(Parameter.DEFAULT_CONF_PATH + "/variable.properties")){
			FileUtil.createFile(Parameter.DEFAULT_CONF_PATH + "/variable.properties");
		}
		lastModfiyVar = new File(Parameter.DEFAULT_CONF_PATH + "/variable.properties").lastModified();

//		lastModfiySVN = new File(Parameter.DEFAULT_CONF_PATH + "/svn.properties").lastModified();
		
		try {
			isr = new InputStreamReader(new FileInputStream(Parameter.DEFAULT_CONF_PATH + "/variable.properties"));
			variableProps.load(isr);
//			isr = new InputStreamReader(new FileInputStream(Parameter.DEFAULT_CONF_PATH + "/svn.properties"));
//			SVNProps.load(isr);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isInit = true;
	}
	
	public static String resolve(String content){
		refresh();
		ArrayList<String> variableList = RegExp.matcherCharacters(content, "\\{.*?\\}");
		String varFormat = null;
		for(String var:variableList){
			varFormat = RegExp.filterString(var, "{}");
			content = resolve(content.replace(var, getVariable(varFormat)));
			
		}
		
//		variableList = RegExp.matcherCharacters(content, "\\{.*?\\}");
//		for(String var:variableList){
//			content = content.replace(var,getSVNVariable(RegExp.filterString(var, "{\\}")));
//		}
		
		return content;
	}
	
	
	private static void refresh(){
		long lastModfiyVarNow = new File(Parameter.DEFAULT_CONF_PATH + "/variable.properties").lastModified();
//		long lastModfiySVNNow = new File(Parameter.DEFAULT_CONF_PATH + "/svn.properties").lastModified();
		if(lastModfiyVar!=lastModfiyVarNow){
			lastModfiyVar = lastModfiyVarNow;
			isInit = false;
		}
//		if(lastModfiySVN!=lastModfiySVNNow){
//			lastModfiySVN = lastModfiySVNNow;
//			isInit = false;
//		}
		if(!isInit){
			init();
		}
	}
	
//	public static void main(String[] args){
////		Parameter.CONFPATH = "C:\\selenium\\conf";
//		Variable.init();
////		System.out.println(Variable.resolve("limn[SVNUserName]{QSConfig}tangxy[SVNPassWord]"));
//		System.out.println(Variable.resolve("limn{Automation}tangxy{Automation}"));
//		
//	}
	
	
}

