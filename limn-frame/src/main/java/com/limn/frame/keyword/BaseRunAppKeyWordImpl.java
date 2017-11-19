package com.limn.frame.keyword;

import java.util.HashMap;

import org.openqa.selenium.WebDriverException;

import com.limn.app.driver.AppDriver;
import com.limn.app.driver.exception.AppiumException;
import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.RunAutoTestingParameter;
import com.limn.frame.control.Test;
import com.limn.tool.bean.RunParameter;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class BaseRunAppKeyWordImpl {

	/**
	 * 录入
	 * @param step
	 * @throws AppiumException
	 */
	public static void input(String[] step) throws AppiumException{

		try{
			HashMap<String,String> traXPath = null;
			if(step.length >= 4 && RegExp.findCharacters(step[step.length-1], "^HASHMAP")){
				// DEBUG模式
				traXPath = TransformationMap.transformationByString(step[step.length-1]);
				Print.debugLog("加载别名数据完成", 1);
			}else{
				// START模式
				traXPath = RunAutoTestingParameter.getAlias();

//				String context = RunAutoTestingParameter.getTestCase().getAssociatedProperites();
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
		int startX = 0 ;
		int startY = 0;
		int endX = 0;
		int endY = 0;

		int MAX_WIDTH = AppDriver.WIDTH - 10;
		int MAX_HEIGHT = AppDriver.HEIGHT - 10;
		int MIDST_WIDTH = AppDriver.WIDTH/2;
		int MIDST_HEIGHT = AppDriver.HEIGHT/2;
		int MIN_WIDTH = 10;
		int MIN_HEIGHT = 10;

		if(steps.length >= 3 && (steps[2].equalsIgnoreCase("Center") || steps[2].equalsIgnoreCase("C"))){
			MIN_WIDTH = AppDriver.WIDTH/4;
			MIN_HEIGHT = AppDriver.HEIGHT/4;
			MAX_WIDTH = AppDriver.WIDTH/4*3;
			MAX_HEIGHT = AppDriver.HEIGHT/4*3;
		}

		if(steps[1].equalsIgnoreCase("LeftSlide") || steps[1].equalsIgnoreCase("LS")){
			startX = MAX_WIDTH;
			startY = MIDST_HEIGHT;
			endX = MIN_WIDTH;
			endY = MIDST_HEIGHT;
		}else if(steps[1].equalsIgnoreCase("RightSlide") || steps[1].equalsIgnoreCase("RS")){
			startX = MIN_WIDTH;
			startY = MIDST_HEIGHT;
			endX = MAX_WIDTH;
			endY = MIDST_HEIGHT;
		}else if(steps[1].equalsIgnoreCase("UpSlide") || steps[1].equalsIgnoreCase("US")){
			startX = MIDST_WIDTH;
			startY = MAX_HEIGHT;
			endX = MIDST_WIDTH;
			endY = MIN_HEIGHT;
		}else if(steps[1].equalsIgnoreCase("DownSlide") || steps[1].equalsIgnoreCase("DS")){
			startX = MIDST_WIDTH;
			startY = MIN_HEIGHT;
			endX = MIDST_WIDTH;
			endY = MAX_HEIGHT;
		}

		AppDriver.swipe(startX,startY,endX,endY);

	}

	/**
	 * 启动app 仅能启动一次。忽视第二次启动
	 * @param steps
	 * @throws AppiumException
	 */
	public static void start(String[] steps) throws AppiumException{
		String path = "";
		if(Parameter.getOS().equalsIgnoreCase("Windows")){
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