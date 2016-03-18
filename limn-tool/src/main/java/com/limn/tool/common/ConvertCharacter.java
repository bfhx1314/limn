package com.limn.tool.common;

import java.util.ArrayList;

import com.limn.tool.regexp.RegExp;


/**
 * 处理用例中的"\:"、"\;"
 */
public class ConvertCharacter {
	
	public static String string = null;
	
	/**
	 * '转换特殊字符"\"
	 * @param str
	 * @return
	 */
	public static String getHtmlAsc(String str){
		int leng = str.indexOf("\\");
		int len = str.length();

		if (leng-1 < len && leng != -1 && leng+1 != len){
			string = str.substring(0, leng) + "&#" + (byte)str.charAt(leng+1) + "@" + str.substring(leng +2, len);
			string = getHtmlAsc(string);
		}else{
			string = str;
		}

		return string;
	}
	
	/**
	 * '转换转义符，与GetHtmlAsc 相反
	 * @param str
	 * @return
	 */
	public static String getHtmlChr(String str) {
		string = str;
		ArrayList<String> arr = RegExp.matcherCharacters(str, "&#\\d{1,}@");
		for(int i=0;i<arr.size();i++){
			ArrayList<String> arrEx = RegExp.matcherCharacters(arr.get(i), "\\d{1,}");
			char cha = (char)Integer.parseInt(arrEx.get(0));
			String stringCha = cha+"";
			string = string.replace(arr.get(i),stringCha);
		}
		return string;
	}
}
