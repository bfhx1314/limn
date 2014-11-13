package com.limn.frame.keyword;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;

public class BaseRunKeyWordImpl {
	
	/**
	 * 启动浏览器
	 * @param step 数组参数 0 可选参数 1浏览器类型 可选参数  2URL
	 * @throws SeleniumFindException
	 */
	public static void startBrowser(String[] step) throws SeleniumFindException{
		if (step.length > 1) {
			if(step[1].equalsIgnoreCase("firefox")){
				Parameter.BROWSERTYPE = 1;
			}else if(step[1].equalsIgnoreCase("ie")){
				Parameter.BROWSERTYPE = 3;
			}else if(step[1].equalsIgnoreCase("chrome")){
				Parameter.BROWSERTYPE = 2;
			}else{
				throw new SeleniumFindException("不支持此浏览器类型:" + step[1] + " 支持的类型有:firefox,chrome,ie");
			}
		}
		
		if(step.length > 2){
			Parameter.URL = step[2];
		}
		startBroswer(Parameter.BROWSERTYPE, Parameter.URL, null);
	}
	
	
	/**
	 * 初始化浏览器
	 * @param type 浏览器类型 1firefox 2chrome
	 * @param url 地址
	 * @param ip 远程运行IP 可选
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public static void startBroswer(int type, String url, String ip) throws SeleniumFindException {
		Driver.setDriver(type, url, ip);
		Driver.startBrowser();
	}
	
	
	/**
	 * 关闭浏览器
	 */
	public static void stopBroswer(){
		Driver.closeBrowser();
	}
	
	/**
	 * 页面跳转
	 * @param step
	 * @throws SeleniumFindException
	 */
	public static void toURL(String[] step) throws SeleniumFindException{
		String url = step[1];
		if(step.length >= 2){
			url = step[1] + ":" + step[2];
		}
		Print.log("URL:" + url,0);
		Driver.changeURL(url);
	}
	
	public static void keyBoardEvent(String[] step){
		Common.wait(2000);
		Driver.keyBoardEvent(step[1]);
	}
	
}
