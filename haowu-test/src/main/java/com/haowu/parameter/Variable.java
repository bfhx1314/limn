package com.haowu.parameter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.limn.tool.common.Print;



public class Variable {
	
	private static Properties props = new Properties();
	
	private static boolean flag = false;
	
	public static String getValue(String name) {
		if(!flag){
			Print.log("未指定文件Properties",2);
		}
		
		return props.getProperty(name);
		
	}

	/**
	 * 初始化
	 */
	public static void init(String path){
		
		InputStreamReader isr = null;
		
		try {
			isr = new InputStreamReader(new FileInputStream(path),"UTF-8");
			props.load(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		flag = true;
	}
	
	public static void close(){
		flag = false;
		props.clear();
	}

}
