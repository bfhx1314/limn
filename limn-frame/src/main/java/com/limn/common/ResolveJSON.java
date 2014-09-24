package com.limn.common;

import java.util.HashMap;
import java.util.Iterator;
import net.sf.ezmorph.object.DateMorpher;
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
public class ResolveJSON {
	
	
	
	/**
	 * 
	 * @param jsonString
	 * @return
	 */
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

	public static String getValueByHierarchy(String hierarchy){
		String[] hie = hierarchy.split(":");
//		hie
		return null;
	}
	
	
	
	
	private static void setDataFormat2JAVA() {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss" }));
	}
	/**
	 * 遍历
	 * @param json
	 */
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

}
