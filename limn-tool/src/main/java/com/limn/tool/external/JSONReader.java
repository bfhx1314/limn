package com.limn.tool.external;

import java.util.HashMap;
import java.util.Iterator;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * JSON 字符串解析
 * 
 * 主要是将JSON转化成map形式
 * map里面的object在自行根据  instanceof 来判断  JSONObject  or  JSONArray
 * @author limn
 *
 */
public class JSONReader {

	public static HashMap<String, Object> getMapFromJson(String jsonString) {
		setDataFormat2JAVA();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	private static void setDataFormat2JAVA() {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss" }));
	}

	
	public static void decodeJSONObject(JSONObject json) {
		@SuppressWarnings("unchecked")
		Iterator<String> keys = json.keys();
		JSONObject jo = null;
		Object o;
		String key;
		while (keys.hasNext()) {
			key = keys.next();
			o = json.get(key);
			if (o instanceof JSONObject) {
				jo = (JSONObject) o;
				if (jo.keySet().size() > 0) {
					decodeJSONObject(jo);
				} else {
					System.out.println(key);
				}
			} else {
				System.out.println(o);
			}
		}
	}
	
	
	
	/**
	 * 获取解析数据
	 * @param data JSONOBject or JSONArray
	 * @param hierarchy 解析的层次    冒号分割
	 * @return JSONArray or JSONObject
	 */
	public static Object getObejctByHierarchy(Object data, String hierarchy){
		Object res = data;
		String[] hie = hierarchy.split(":");
		for(String key:hie){
			if(res instanceof JSONObject){
				res = ((JSONObject) res).get(key);
			}else if(res instanceof JSONArray){
				res = ((JSONArray) res).get(Integer.valueOf(key));
			}
		}
		return res;
	}

	
	

}
