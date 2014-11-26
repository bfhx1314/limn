package com.limn.frame.keyword;


import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;


import com.limn.driver.common.OperateWindows;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.exception.ParameterException;

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

		int status = 1;
		try {

			switch (step[0]) {

			//启动浏览器
			case BaseKeyWordType.START_BROWSER:
				BaseRunKeyWordImpl.startBrowser(step);
				break;
			//关闭浏览器
			case BaseKeyWordType.CLOSE_BROSWER:
				BaseRunKeyWordImpl.stopBroswer();
				break;
			//录入
			case BaseKeyWordType.INPUT:
				cheakKeyWordCount(step.length, 2);
				BaseRunKeyWordImpl.inputValue(step);
				
				break;
			//提示框
			case BaseKeyWordType.DIALOG:
				cheakKeyWordCount(step.length, 1);
				if (step[1].equals("确定") || step[1].equals("是")) {
					OperateWindows.dealPotentialAlert(true);
				} else {
					OperateWindows.dealPotentialAlert(false);
				}
				break;
			//页面跳转	
			case BaseKeyWordType.CHANGE_URL:
				cheakKeyWordCount(step.length, 1);
				BaseRunKeyWordImpl.toURL(step);
				break;
			case BaseKeyWordType.KEYBOARD_EVENT:
				cheakKeyWordCount(step.length, 1);
				BaseRunKeyWordImpl.keyBoardEvent(step);
				
				break;
			case BaseKeyWordType.WAIT:
				cheakKeyWordCount(step.length, 1);
				int waitTime = Integer.valueOf(step[1])*1000;
				Common.wait(waitTime);
				break;
			case BaseKeyWordType.EXPRESSION:
				cheakKeyWordCount(step.length, 1);
				BaseRunKeyWordImpl.executeExpression(step);
				break;
			case BaseKeyWordType.ADDATTACHMENT:
				cheakKeyWordCount(step.length, 1);
				BaseRunKeyWordImpl.addAttachment(step);
				break;
			//自定义关键字
			default:

				for (String key : KWD.keySet()) {
					if(key.equals("基础关键字")){
						continue;
					}
					status = KWD.get(key).start(step);
					if (-1 != status) {
						return status;
					}
				}
				if (-1 == status) {
					Print.log("不存在此关键字:" + step[0], 2);
				}
			}
		} catch (SeleniumFindException e) {
			status = -2;
			Print.log(e.getMessage(), 2);
		} catch (ParameterException e) {
			status = -2;
			Print.log(e.getMessage(), 2);
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
