package com.limn.tool.parameter;

import java.util.LinkedHashMap;

import com.limn.tool.common.Print;

/**
 * 对用例中的变量的操作
 * @author zhangli	
 *
 */
public class Expression {
	
	private static LinkedHashMap<String, String> expression = new LinkedHashMap<String, String>();

	public static void setExpressionName(String key, String value) {
		if(expression.containsKey(key)){
			expression.remove(key);
			expression.put(key, value);
		}else{
			expression.put(key, value);
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
	
	
}
