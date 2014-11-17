package com.limn.tool.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.limn.tool.regexp.RegExp;

public class TransformationMap {
	
	
	public static String transformationByMap(LinkedHashMap<String,String> data){
		String traString = "";
		for(String key : data.keySet()){
			if(traString.isEmpty()){
				traString = "HASHMAP\n" + key + "\t" + data.get(key);
			}else{
				traString = traString + "\n" + key + "\t" + data.get(key);
			}
		}
		return traString;
	}
	
	public static LinkedHashMap<String,String> transformationByString(String data){
		if(RegExp.findCharacters(data, "^HASHMAP\n")){
			data = data.substring(8);
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			String[] dataDe = data.split("\n");
			for(String key : dataDe){
				ArrayList<String> keyValue = RegExp.matcherCharacters(key, "[^\t]{1,}");
				if (keyValue.size() == 0){
					return new LinkedHashMap<String,String>();
				}else{
					map.put(keyValue.get(0), keyValue.get(1));
				}
			}
			return map;
		}else{
			return new LinkedHashMap<String,String>();
		}
		
	}
	
	public static void main(String[] args){
		LinkedHashMap<String,String> a = new LinkedHashMap<String, String>();
		a.put("a", "1");
		a.put("b", "2");
		String b = TransformationMap.transformationByMap(a);
		System.out.println(b);
		HashMap<String,String> c = TransformationMap.transformationByString(b);
		System.out.println(c.get("a"));
		System.out.println(c.get("b"));
	}

}
