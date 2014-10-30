package com.limn.frame.keyword;


import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.limn.tool.common.Print;

/**
 * 基础关键字
 * @author limn
 *
 */
public class BaseKeyWordDriverImpl implements KeyWordDriver {
	
	public KeyWordDriver keyWord = null;
	private LinkedHashMap<String,KeyWordDriver> KWD = new LinkedHashMap<String,KeyWordDriver>();
	private LinkedHashMap<String,Class<?>> KWDT = new LinkedHashMap<String,Class<?>>();
	private HashSet<String> allKeyWord = new HashSet<String>();
	private boolean flag = false;
	@Override
	public int start(String[] step) {

		int status = -1;
	
		switch (step[0]) {

		case BaseKeyWordType.START_BROWSER:	
			
			break;		
		case BaseKeyWordType.CLOSE_BROSWER:	
			
			break;
		case BaseKeyWordType.INPUT:
			
			break;
		default:

			for(String key:KWD.keySet()){
				status = KWD.get(key).start(step);
				if(status!=-1){
					return status;
				}
			}
			if(-1 == status){
				Print.log("不存在此关键字:" + step[0], 2);
			}
		}
		return status;

	}


	/**
	 * 判断是否是关键字
	 * @param key
	 * @return
	 */
	public boolean isKeyWord(String key) {
		if(!flag){
			init();
		}
		return allKeyWord.contains(key);
	}

	private void init(){
		for(String key:KWDT.keySet()){
			try {
				Class<?> clazz = KWDT.get(key);
				Field[] fields = clazz.getDeclaredFields();
				for (Field f : fields) {
					if (f.getGenericType().toString().equals("class java.lang.String")) {
						allKeyWord.add((String) f.get(clazz));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}

	/**
	 * 增加关键字驱动与类型
	 * @param keyWord 驱动
	 * @param keyWordType 关键字
	 * @param key 名称
	 */
	public void addKeyWordDriver(String key, KeyWordDriver keyWord, Class<?> keyWordType) {
		KWD.put(key,keyWord);
		KWDT.put(key,keyWordType);
	}


}
