package com.automation.tool.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 定义正则表达式
 * 
 * @author limn
 *
 */
public class RegExp {
	
	
	/**
	 * 用符号分隔字符串
	 * 
	 * @param characters 被分隔字符串
	 * @param pattern 分隔符号
	 * @return
	 */
	public static String[] splitWord(String characters,String pattern){
		String[] m = Pattern.compile(pattern).split(characters);
		return m;
	}
	
	/**
	 * 是否含中文字符
	 * 
	 * @param characters 被检查字符串
	 * @return
	 */
	public static boolean isChineseCharacters(String characters) {
		Matcher m = Pattern.compile("[^u4e00-u9fa5]").matcher(characters);
		return m.find();
	}
	
	/**
	 * 是否含英文字符
	 * 
	 * @param characters 被检查字符串
	 * @return
	 */
	public static boolean isEnglishCharacters(String characters) {
		Matcher m = Pattern.compile("[u4e00-u9fa5]").matcher(characters);
		return m.find();
	}
	
	/**
	 * 是否含英文、数字字符
	 * 
	 * @param characters 被检查字符串
	 * @return
	 */
	public static boolean isEnglishNumCharacters(String characters) {
		Matcher m = Pattern.compile("^[a-zA-Z0-9@_]+$").matcher(characters);
		return m.find();
	}
	
	/**
	 * 是否含中文字符（判断每个字符）
	 * 
	 * @param characters 被检查字符串
	 * @return
	 */
	public static boolean isChineseCharactersEach(String characters) {
		boolean isChinese = false;
		int charactersLen = characters.length();
		for(int i=0;i<charactersLen;i++){
			if (isChineseCharacters(String.valueOf(characters.charAt(i)))){
				isChinese = true;
				break;
			}
		}
		return isChinese;
	}

	
	/**
	 * 查找字符
	 * @param characters 被查找字符串
	 * @param pattern 要查找的字符串
	 * @return
	 */
	public static boolean findCharacters(String characters, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(characters);
		return m.find();
	}
	
	/**
	 * 匹配字符串，返回ArrayList
	 * @param characters 被查找字符串
	 * @param pattern 要查找的字符串
	 * @return
	 */
	public static ArrayList<String> matcherCharacters(String characters, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(characters);
		// 结合arrlist 返回合集
		ArrayList<String> matcherArray = new ArrayList <String>();
		while(m.find()){
			matcherArray.add(m.group());
		}
		return matcherArray;
	}
	
	/**
	 * 过滤字符
	 * @param characters 被过滤的字符串
	 * @param filterchar 需要过滤的字符
	 * @return
	 */
	public static String filterString(String characters ,String filterchar){
		String strRe = "";
		ArrayList<String> arrayList = matcherCharacters(characters,"[^\\"+filterchar+"]{1,}");
		for (int i =0; i < arrayList.size(); i++) {
			String str = arrayList.get(i);
			strRe = strRe + str;
		}
		return strRe;
	}

    /**
     * 正则匹配字符串
     * @param str
     * @param patternStr
     * @return
     */
    public static boolean matchRegExp(String str, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher match = pattern.matcher(str);
        return match.matches();
    }
	
	
	/**
	 * 实例
	 */
	public static void main(String[] args){

		boolean aa = findCharacters("[asd,afa,s]","\\[(.*)\\]");
        aa = findCharacters("asda","^^asd$$");
		String bb = "[asd,afa,s]".replaceAll("[\\[\\]]", "");
		System.out.println(aa);
		System.out.println(bb);

	}
	
	
}
