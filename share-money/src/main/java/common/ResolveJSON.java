package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

public class ResolveJSON {

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
		// �趨����ת����ʽ
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd","yyyy-MM-dd HH:mm:ss" }));
	}

	public static JSONObject bianli(String json) {
		String s = json;
		Map map = new HashMap();
		JsonConfig jc = new JsonConfig();
		jc.setClassMap(map);
		jc.setRootClass(Map.class);
		jc.setArrayMode(JsonConfig.MODE_LIST);
		JSONObject jobj = JSONObject.fromObject(s, jc);
		// �ݹ����
//		decodeJSONObject(jobj);
		return jobj;
	}
	 
	 
	 
	 
	    public static void decodeJSONObject(JSONObject json){  
	        Iterator<String> keys=json.keys();  
	        JSONObject jo=null;  
	        Object o;  
	        String key;  
	        while(keys.hasNext()){  
	            key=keys.next();  
	            o=json.get(key);  
	            if(o instanceof JSONObject){  
	                jo=(JSONObject)o;  
	                if(jo.keySet().size()>0){  
	                    decodeJSONObject(jo);  
	                }else{  
	                    System.out.println(key);  
	                }  
	            }else{  
	                System.out.println(o);  
//	                o instanceof JSONArray
//	              ((JSONArray) o).get(0) instanceof JSONObject
//	               (ArrayList<JSONArray>) o
	            }  
	        }  
	    }  

}
