package com.limn.frame.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.limn.tool.common.FileUtil;
import com.limn.tool.regexp.RegExp;

public class NewDictionary {
	
	private LinkedHashMap<Object, Object> hashMap = null;
	
	public NewDictionary(){
		hashMap = new LinkedHashMap<Object, Object>();
	}
	
	public void addItem(String key,Object value){
		hashMap.put(key, value);
	}
	public void addItem(int key,Object value){
//		String iString = String.valueOf(key);
		hashMap.put(key, value);
	}
	public int getSize(){
		return hashMap.size();
	}
	/**
	 * 获取所有key
	 * @return
	 */
	public Set<?> getKeys(){
		return hashMap.keySet();
	}
	/**
	 * 获取指定值
	 * @param key
	 * @return
	 */
	public Object getValue(String key){
		return hashMap.get(key);
	}
	
	/**
	 * 获取所有value
	 * @return
	 */
	public Collection<?> getItems(){
		return hashMap.values();
	}
	public boolean existKey(String key){
		return hashMap.containsKey(key);
	}
	public void removeKey(String key){
		if (hashMap.containsKey(key)){
			hashMap.remove(key);
		}
	}
	public void removeAllKeys(){
		hashMap = new LinkedHashMap<>();
	}
	public void setHashMap(LinkedHashMap<Object, Object> linkedHashMap){
		hashMap = linkedHashMap;
	}
	public LinkedHashMap<Object, Object> getHashMap(){
		return hashMap;
	}
	/**
	 * 返回hashMap
	 * @param tempKey 
	 * @param tempValue
	 * @return
	 */
	public HashMap<Object, Object> dataDictionary(String tempKey,String tempValue){
		removeKey(tempKey);
		hashMap.put(tempKey, tempValue);
		return hashMap;
	}
	/**
	 * 获取hashMap的Item，String||Object
	 * @param key
	 * @return
	 */
	public Object getDictionary(String key){
		hashMap = new LinkedHashMap<>();
		Set<?> hashMapKeys = getKeys();
		String realKey = "";
		if (isNumeric(key)){
			int index = Integer.parseInt(key);
//			Iterator<?> ite = hashMapKeys.iterator();
			int iTime = 0;
			for(Iterator<?> ite = hashMapKeys.iterator();ite.hasNext();){
				Object itr = ite.next();
				if (iTime == index){
					realKey = itr.toString();
					break;
				}
			} 
		}else{
			realKey = key;
		}
		return hashMap.get(realKey);
	}
	
	/**
	 * 获取key
	 * @param iIndex
	 * @return
	 */
	public Object getKey(int iIndex){
		if (iIndex > getSize()){
			return null;
		}
		Object realKey = null;
		Set<?> hashMapKeys = getKeys();
//		Iterator<?> ite = hashMapKeys.iterator();
		int iTime = 0;
		for(Iterator<?> ite = hashMapKeys.iterator();ite.hasNext();){
			Object itr = ite.next();
			if (iTime == iIndex){
				realKey = itr;
				break;
			}
			iTime++;
		}
		return realKey;
	}
	
	/**
	 * 获取value
	 * @param iIndex
	 * @return
	 */
	public Object getValue(int iIndex){
		if (iIndex > getSize()){
			return null;
		}
		Object realKey = null;
		Object key = getKey(iIndex);
		realKey = hashMap.get(key);
		return realKey;
	}
	
	/**
	 * 获取备份hashMap
	 * @return
	 */
	public HashMap<Object, Object> getClone(){
		NewDictionary newDict = new NewDictionary();
		newDict.setHashMap(hashMap);
		return newDict.getHashMap();
	}
	
	/**
	 * 'Merges the dictionary with another one
	 * @param hashM
	 */
	public void merge(LinkedHashMap<String, Object> hashM){
		Set<?> hashMapKeys = hashM.keySet();
		for(Iterator<?> ite = hashMapKeys.iterator();ite.hasNext();){
			String itrString = ite.next().toString();
			if (!hashMap.containsKey(itrString)){
				addItem(itrString,hashM.get(itrString));
			}
		}
	}
	
	/**
	 * Exports the dictionary to a text file
	 */
	public void export(String filePath){
		String str = "";
		for(Entry<Object, Object> entry:hashMap.entrySet()){
			str = str + "|" + entry.getKey() + ">" +entry.getValue();
//			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		str = str.replaceFirst("\\|", "");
		FileUtil.setEmpty(filePath);
		FileUtil.writeFile(filePath, str);
		
	}
	/**
	 * Builds the dictionary from an external file
	 */
	public void importF(){
		
	}
	
	public boolean isNumeric(String str){ 
		return RegExp.findCharacters(str, "[0-9]*");
//		return RegExp.findCharacters(str, "-?[0-9]+.?[0-9]+");
//		   Pattern pattern = Pattern.compile("[0-9]*"); 
//		   Matcher isNum = pattern.matcher(str);
//		   if( !isNum.matches() ){
//		       return false; 
//		   } 
//		   return true; 
		}
	
	public static void main(String[] args){
//		System.out.print("|sds>234|".replaceFirst("\\|", ""));
		
		NewDictionary newDictionary = new NewDictionary();
		newDictionary.addItem("1", "a");
		newDictionary.addItem("2", "b");
		newDictionary.addItem("3", "c");
		newDictionary.addItem("4", "d");
		newDictionary.export("c:/a.txt");
		
/*		LinkedHashMap<Object, Object> d = newDictionary.getHashMap();
		Set<Entry<Object, Object>> e = d.entrySet();
		Set<?> a = newDictionary.getKeys();
//		a.iterator().next();
		Collection<?> b = newDictionary.getItems();
//		b.iterator().next();
*/		System.out.print("test");
	}
}

