package com.limn.tool.external;

import java.util.HashMap;
import java.util.Iterator;

import com.limn.tool.regexp.RegExp;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * JSON 字符串解析
 * 
 * 主要是将JSON转化成map形式 map里面的object在自行根据 instanceof 来判断 JSONObject or JSONArray
 * 
 * @author limn
 * 
 */
public class JSONReader {

	public static HashMap<String, Object> getMapFromJson(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	public static HashMap<String, Object> getMapFromJson(String jsonString) {
		setDataFormat2JAVA();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return getMapFromJson(jsonObject);
	}

	private static void setDataFormat2JAVA() {
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
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
	 * 
	 * @param data	JSONOBject or JSONArray
	 * @param hierarchy	解析的层次 冒号分割
	 * @return JSONArray or JSONObject
	 */
	public static Object getObejctByHierarchy(Object data, String hierarchy) {
		Object res = data;
		String[] hie = hierarchy.split(":");
		for (String key : hie) {
			if (res instanceof JSONObject) {
				res = ((JSONObject) res).get(key);
			} else if (res instanceof JSONArray) {
				res = ((JSONArray) res).get(Integer.valueOf(key));
			}
		}
		return res;
	}

	/**
	 * 获取解析数据
	 * 
	 * @param data	JSONOBject or JSONArray
	 * @param hierarchy	解析的层次 冒号分割
	 * @return JSONArray or JSONObject
	 */
	public static Object getObejctByHierarchyByString(String data, String hierarchy) {
		if (RegExp.findCharacters(data, "^\\(")) {
			data = data.substring(1, data.length() - 1);
		}
		Object res = JSONObject.fromObject(data);
		String[] hie = hierarchy.split(":");
		for (String key : hie) {
			if (res instanceof JSONObject) {
				res = ((JSONObject) res).get(key);
			} else if (res instanceof JSONArray) {
				res = ((JSONArray) res).get(Integer.valueOf(key));
			}
		}
		return res;
	}

	/**
	 * 获取解析数据
	 * 
	 * @param data	JSONOBject or JSONArray
	 * @param hierarchy	解析的层次 冒号分割
	 * @return String
	 */
	public static String getStringByHierarchyByString(String data, String hierarchy) {
		if (RegExp.findCharacters(data, "^\\(")) {
			data = data.substring(1, data.length() - 1);
		}
		Object res = JSONObject.fromObject(data);
		String[] hie = hierarchy.split(":");
		for (String key : hie) {
			if (res instanceof JSONObject) {
				res = ((JSONObject) res).get(key);
			} else if (res instanceof JSONArray) {
				res = ((JSONArray) res).get(Integer.valueOf(key));
			}
		}
		return res.toString();
	}
	
	
	
	public static void main(String[] args) {
		getObejctByHierarchyByString("({'status':'1'})", "status");
	}

	
	/**
	 * 格式输出
	 * @param jsonStr  JSON字符串
	 * @return
	 */
	public static String format(String jsonStr) {
		int level = 0;
		StringBuffer jsonForMatStr = new StringBuffer();
		for (int i = 0; i < jsonStr.length(); i++) {
			char c = jsonStr.charAt(i);
			if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
				jsonForMatStr.append(getLevelStr(level));
			}
			switch (c) {
			case '{':
			case '[':
				jsonForMatStr.append(c + "\n");
				level++;
				break;
			case ',':
				jsonForMatStr.append(c + "\n");
				break;
			case '}':
			case ']':
				jsonForMatStr.append("\n");
				level--;
				jsonForMatStr.append(getLevelStr(level));
				jsonForMatStr.append(c);
				break;
			default:
				jsonForMatStr.append(c);
				break;
			}
		}

		return jsonForMatStr.toString();

	}

	private static String getLevelStr(int level) {
		StringBuffer levelStr = new StringBuffer();
		for (int levelI = 0; levelI < level; levelI++) {
			levelStr.append("\t");
		}
		return levelStr.toString();
	}

}
