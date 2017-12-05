package com.limn.frame.keyword;


import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.limn.app.driver.exception.AppiumException;
import com.limn.tool.bean.RunParameter;
import com.limn.tool.common.BaseToolParameter;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;

/**
 * 基础关键字
 * @author limn
 *
 */
public class BaseAppKeyWordDriverImpl implements KeyWordDriver {
	
	public KeyWordDriver keyWord = null;
	private LinkedHashMap<String,KeyWordDriver> KWD = new LinkedHashMap<String,KeyWordDriver>();
	private LinkedHashMap<String,Class<?>> KWDT = new LinkedHashMap<String,Class<?>>();
	private HashSet<String> allKeyWord = new HashSet<String>();
	private boolean flag = false;
	
	@Override
	public int start(String[] step) {
		BaseRunAppKeyWordImpl baseRunAppKeyWord = new BaseRunAppKeyWordImpl();
		int status = 1;
		try {

			switch (step[0]) {

			//录入数据
			case BaseAppKeyWordType.M_INPUT:
				cheakKeyWordCount(step.length, 2);
				baseRunAppKeyWord.input(step);
				break;
			case BaseAppKeyWordType.M_SLIDE:
				baseRunAppKeyWord.slide(step,false);
				break;
			case BaseAppKeyWordType.M_START:
				baseRunAppKeyWord.start(step);
				break;
			case BaseAppKeyWordType.M_IFEXISTINPUT:
				cheakKeyWordCount(step.length, 2);
				baseRunAppKeyWord.ifExistInput(step);
				break;
			case BaseAppKeyWordType.M_IRREGULARSLIDE:
				baseRunAppKeyWord.slide(step,true);
				break;
			//自定义关键字
			default:
				return -1;
			}
		} catch (AppiumException e){
			status = -2;
			RunParameter.getResultPaht().setErrorLog(e.getMessage());
			BaseToolParameter.getPrintThreadLocal().log(e.getMessage(), 2);
			e.printStackTrace();
		} catch (Exception e){
			status = -2;
			RunParameter.getResultPaht().setErrorLog(e.getMessage());
			BaseToolParameter.getPrintThreadLocal().log(e.getMessage(), 2);
			e.printStackTrace();
		}
		return status;

	}

	private void cheakKeyWordCount(int keyWordCount, int needCount){
		if(keyWordCount<needCount){
//			throw new HaowuException(10020000, "列表关键字:参数个数有误");
		}
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
