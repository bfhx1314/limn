package com.limn.tool.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.limn.tool.regexp.RegExp;

public class TransformationMap {
	
	
	public static String transformationByMap(HashMap<String,String> data){
		String traString = "HASHMAP\n";
		for(String key : data.keySet()){
			if(traString.equalsIgnoreCase("HASHMAP\n")){
				traString = traString + key + "\t" + data.get(key);
			}else{
				traString = traString + "\n" + key + "\t" + data.get(key);
			}
		}
		return traString;
	}
	
	public static HashMap<String,String> transformationByString(String data){
		data = data.substring(8);
		HashMap<String,String> map = new HashMap<String, String>();
		String[] dataDe = data.split("\n");
		for(String key : dataDe){
			ArrayList<String> keyValue = RegExp.matcherCharacters(key, "[^\t]{1,}");
			map.put(keyValue.get(0), keyValue.get(1));
		}
		return map;
		
	}
	
	public static void main(String[] args){
		HashMap<String,String> a = new HashMap<String, String>();
		a.put("a", "1");
		a.put("b", "2");
		String b = TransformationMap.transformationByMap(a);
		System.out.println(b);
		HashMap<String,String> c = TransformationMap.transformationByString(b);
		System.out.println(c.get("a"));
		System.out.println(c.get("b"));
	}

}
