package com.limn.frame.keyword;

import com.limn.app.driver.AppDriver;
import com.limn.app.driver.exception.AppiumException;

public class BaseRunAppKeyWordImpl {
	
	/**
	 * 录入
	 * @param steps
	 * @throws AppiumException
	 */
	public static void input(String[] steps) throws AppiumException{
		if(steps[2].equalsIgnoreCase("[Click]")){
			AppDriver.click(steps[1]);
		}else{
			AppDriver.setValue(steps[1], steps[2]);
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
		//windows系统存在:字符需要合并
		for(int i = 1 ;  i <= steps.length ; i++){
			path = path + steps[i];
		}
		AppDriver.init(path);
	}
	
}