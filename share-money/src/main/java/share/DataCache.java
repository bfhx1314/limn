package share;

import java.util.Date;
import java.util.HashMap;


public class DataCache {

	public static HashMap<String, Integer> ShareConut = new HashMap<String, Integer>();

	public static HashMap<String, Integer> Ready = new HashMap<String, Integer>();

	public static HashMap<String, Date> All = new HashMap<String, Date>();

	public static Date flag = null;

	public static synchronized void updateReady(String key, int value){
		if(value == -1){
			Ready.remove(key);
		}else{
			Ready.put(key, value);
		}
	}
	
	public static synchronized void updateShareCount(String key, int value){
		if(value == -1){
			ShareConut.remove(key);
		}else{
			ShareConut.put(key, value);
		}
	}
	
	public static synchronized void updateAll(String key,Date value){
		All.put(key, value);
	}
	
}
