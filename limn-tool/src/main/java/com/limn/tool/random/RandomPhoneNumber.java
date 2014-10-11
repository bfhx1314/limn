package com.limn.tool.random;

import java.io.InputStream;

import com.limn.tool.external.ExcelEditor;





public class RandomPhoneNumber {
	
	/*
	 * 随机手机号码使用的变量
	 */
	private static ExcelEditor ec = null;
	private static boolean flag = false;
	
	//*********************************************//
	
	/**
	 * 根据城市区号来随机生成该城市的手机号码
	 * @param cityIdentity 城市区号
	 * @return
	 */
	public static String getPhoneNumber(String cityIdentity){
		if(!flag){
			init();
			flag = true;
		}

		int rowCount = 0;
		int col = 0;

		while(true){
			String identity = ec.getValue(0, col, 0);
			
			if(identity==null){
				return null;
			}
			
			String[] res = identity.split("\\*");
			if(res[2].equals(cityIdentity)){
				rowCount = Integer.valueOf(res[1]);
				break;
			}
			col ++;
		}

		int row = RandomData.getNumberRange(1,rowCount);
		return ec.getValue(0, col, row) + RandomData.getNumberRange(1111, 9999);
	}
	
	/**
	 * 随机初始化
	 */
	private static void init(){
		InputStream is = RandomData.class.getClassLoader().getResourceAsStream(  
                "data/Phone_Identity_City.xls");
		ec = new ExcelEditor(is);
	}
}
