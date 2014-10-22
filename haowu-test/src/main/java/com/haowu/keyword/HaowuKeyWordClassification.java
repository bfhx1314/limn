package com.haowu.keyword;

import java.util.HashSet;

public class HaowuKeyWordClassification {
	
	private static HashSet<String> isNotWebElement = new HashSet<String>();
	private static boolean flag = true;
	private static void init(){
//		isNotWebElement.add(HaowuKeyWordType.START_BROWSER);
		isNotWebElement.add(HaowuKeyWordType.CLOSE_BROWSER);
	}
	
	public static boolean isWebElementKeyword(String keyWord){
		if(flag){
			flag = false;
			init();
		}
		return !isNotWebElement.contains(keyWord);
		
	}

}
