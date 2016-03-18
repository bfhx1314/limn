package com.limn.frame.keyword;

import java.util.HashMap;

import org.openqa.selenium.WebDriverException;

import com.limn.app.driver.AppDriver;
import com.limn.app.driver.exception.AppiumException;
import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.Test;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class BaseRunAppKeyWordImpl {
	
	/**
	 * 录入
	 * @param steps
	 * @throws AppiumException
	 */
	public static void input(String[] step) throws AppiumException{
		
		try{
			HashMap<String,String> traXPath = null; 
			if(step.length >= 4 && RegExp.findCharacters(step[step.length-1], "^HASHMAP")){
				// DEBUG模式
				traXPath = TransformationMap.transformationByString(step[3]);
				Print.debugLog("加载别名数据完成", 1);
			}else{
				// START模式
				traXPath = Test.TRA_NAME;
				
				
//				String context = Test.getAssociatedProperites();
//				if(null == context){
//					traXPath = null;
//				}else{
//					traXPath = TransformationMap.transformationByString(context);
//					Print.log("加载别名数据完成", 1);
//				}
			}

			String xpath = null;
			if(null != traXPath){
				if(traXPath.containsKey(step[1])){
					xpath = traXPath.get(step[1]);
					Print.log("获取[" + xpath + "] 别名:" + step[1], 0);
				}else{
					xpath = step[1];
				}
			}else{
				xpath = step[1];
			}
	
			if(step[2].equalsIgnoreCase("[Click]")){
				AppDriver.click(xpath);
			}else if(step[2].equalsIgnoreCase("[Touth]")){
				int time = 1000;
				if(step.length>3){
					try{
						time = Integer.valueOf(step[3]) * 1000;
					}catch(NumberFormatException e){
						if(!RegExp.findCharacters(step[3], "^HASHMAP")){
							Print.log("无法解析长按秒数:" + step[3] + ",默认1秒", 3);
						}
						time = 1000;
					}
				}
				AppDriver.touchAction(xpath,time);
			}else{
				AppDriver.setValue(xpath, step[2]);
			}
		}catch(WebDriverException e){
			Print.log("WebDriverException 异常报错 ", 3);
		}catch(Exception e){
			throw new AppiumException("错误:" + e.getMessage());
		}
	}
	
	
	/**
	 * 滑动
	 * @param steps
	 * @throws AppiumException
	 */
	public static void slide(String[] steps) throws AppiumException{
		if(steps[1].equalsIgnoreCase("Left")){
			AppDriver.swipe(AppDriver.WIDTH - 10, AppDriver.HEIGTH/2, 10, AppDriver.HEIGTH/2);
		}else if(steps[1].equalsIgnoreCase("Right")){
			AppDriver.swipe(10, AppDriver.HEIGTH/2, AppDriver.WIDTH - 10, AppDriver.HEIGTH/2);
		}else if(steps[1].equalsIgnoreCase("Top")){
			AppDriver.swipe(AppDriver.WIDTH/2, AppDriver.HEIGTH - 10, AppDriver.WIDTH/2, 10);
		}else if(steps[1].equalsIgnoreCase("Bottom")){
			AppDriver.swipe(AppDriver.WIDTH/2, 10, AppDriver.WIDTH/2, AppDriver.HEIGTH - 10);
		}
	}
	
	/**
	 * 启动app 仅能启动一次。忽视第二次启动
	 * @param steps
	 * @throws AppiumException
	 */
	public static void start(String[] steps) throws AppiumException{
		String path = "";
		if(Parameter.OS.equalsIgnoreCase("Windows")){
			path = steps[1] + ":";
			//windows系统存在:字符需要合并
			for(int i = 2 ;  i < steps.length ; i++){
				path = path + steps[i];
			}
		}else{
			path = steps[1];
		}
		
		
		AppDriver.init(path);
	}
	
}