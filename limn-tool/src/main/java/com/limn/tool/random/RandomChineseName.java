package com.limn.tool.random;

import java.io.InputStream;
import java.util.ArrayList;

import com.limn.tool.external.ExcelControl;
import com.limn.tool.regexp.RegExp;



public class RandomChineseName {

	/*
	 * 随机中文姓名使用的变量
	 */
	private static ExcelControl ec = null;
	private static boolean flag = false;
	
	//*********************************************//
	
	/**
	 * 随机获取中文姓名
	 * @return 姓名
	 */
	public static String getChineseName(){
		if(!flag){
			init();
			flag = true;
		}

		String username = null;
		boolean isValid = true;
		
		while(true){
			int col = RandomData.getNumberRange(0, 99);
			int row = RandomData.getNumberRange(0,4999);
			username = ec.getValue(0, col, row);
			//判断汉子数
			ArrayList<String> res = RegExp.matcherCharacters(username, "[\u4e00-\u9fa5]");
			//判断是否存在非汉子以外字符
			isValid = !RegExp.findCharacters(username, "[^\u4e00-\u9fa5]");
			if(res.size()<4 && isValid){
				break;
			}
		}
		return username;
	}
	
	/**
	 * 随机姓名初始化
	 */
	private static void init(){
		InputStream is = RandomData.class.getClassLoader().getResourceAsStream(  
                "data/ChineseName_Data.xls");
		ec = new ExcelControl(is);
	}
	
}

