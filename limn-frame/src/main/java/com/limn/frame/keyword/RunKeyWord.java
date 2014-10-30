package com.limn.frame.keyword;


import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.parameter.Parameter;

public class RunKeyWord {
	
	public static void startBrowser(String[] step) throws SeleniumFindException{
		if (step.length > 1) {
			if(step[1].equalsIgnoreCase("firefox")){
				Parameter.BROWSERTYPE = 1;
			}else if(step[1].equalsIgnoreCase("ie")){
				Parameter.BROWSERTYPE = 3;
			}else if(step[1].equalsIgnoreCase("chrome")){
				Parameter.BROWSERTYPE = 2;
			}else{
//				throw new Selen(10020000, "不支持此浏览器类型:" + step[1] + " 支持的类型有:firefox,chrome,ie");
			}
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
		Driver.setDriver(type, url, null);

		Driver.startBrowser();

	}
	
	
	/**
	 * 关闭浏览器
	 */
	public static void stopBroswer(){
		Driver.closeBrowser();
	}
	
	public static void toURL(String[] step) throws SeleniumFindException{
		//页面跳转
		try{
			Driver.driver.navigate().to(step[1]);
		}catch(Exception e){
			throw new SeleniumFindException(10010000, "页面跳转失败:" + e.getMessage());
		}
	}
	

}
