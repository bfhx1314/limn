package com.limn.frame.keyword;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebElement;

import com.limn.driver.Driver;
import com.limn.driver.common.DriverParameter;
import com.limn.driver.common.OperateWindows;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.RunAutoTestingParameter;
import com.limn.frame.control.Test;
import com.limn.tool.bean.RunParameter;
import com.limn.tool.common.Common;
import com.limn.tool.common.ConvertCharacter;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

public class BaseRunKeyWordImpl {
	
	/**
	 * 启动浏览器
	 * @param step 数组参数 0 可选参数 1浏览器类型 可选参数  2URL
	 * @throws SeleniumFindException
	 */
	public void startBrowser(String[] step) throws SeleniumFindException{
		int stepLen = step.length;
		
		if(null == RunParameter.getStartPaht().getComputer() || RunParameter.getStartPaht().getComputer().equals("本机")){
			RunParameter.getStartPaht().setIP(null);
		}
		
		for(int i=1;i<stepLen;i++){
			if (RegExp.findCharacters(step[i], "(?i)^http|^https")){
				if (stepLen <= i + 1) {
					throw new SeleniumFindException("URL地址为空。");
				}
				RunParameter.getStartPaht().setURL(step[i] + ":" + step[i + 1]);
				i++;
			}else if(RegExp.findCharacters(step[i], "(?i)firefox|ie|chrome")){
				
				
				if(step[i].equalsIgnoreCase("firefox") || step[i].equalsIgnoreCase("ie") ||
				step[i].equalsIgnoreCase("chrome")){
					RunParameter.getStartPaht().setBrowserType(step[i]);
				}else{
					throw new SeleniumFindException("不支持此浏览器类型:" + step[i] + " 支持的类型有:firefox,chrome,ie");
				}
			}else if(RegExp.findCharacters(step[i], "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])(\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5][1-9\\d]|\\d)){3}")){
				RunParameter.getStartPaht().setIP(step[i]);
			}
		}
		
//		if(step.length > 2){
//			Parameter.URL = step[2];
//		}
		startBroswer(RunParameter.getStartPaht().getBrowserType(), RunParameter.getStartPaht().getURL(), RunParameter.getStartPaht().getIP());
	}
	
	
	/**
	 * 初始化浏览器
	 * @param type 浏览器类型 1firefox 2chrome
	 * @param url 地址
	 * @param ip 远程运行IP 可选
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public void startBroswer(String type, String url, String ip) throws SeleniumFindException {
		DriverParameter.getDriverPaht().setDriver(type, url, ip);
		DriverParameter.getDriverPaht().startBrowser();
	}
	
	
	/**
	 * 关闭浏览器
	 */
	public  void stopBroswer(){
		DriverParameter.getDriverPaht().closeBrowser();
	}
	
	/**
	 * 页面跳转
	 * @param step
	 * @throws SeleniumFindException
	 */
	public  void toURL(String[] step) throws SeleniumFindException{
		String url = step[1];
		if(step.length >= 2){
			url = step[1] + ":" + step[2];
		}
		Print.log("URL:" + url,0);
		RunParameter.getStartPaht().setURL(url);
		DriverParameter.getDriverPaht().changeURL(url);
	}
	
	public  void keyBoardEvent(String[] step){
		Common.wait(2000);
		DriverParameter.getDriverPaht().keyBoardEvent(step[1]);
	}
	
	/**
	 * 
	 * @param step
	 * @throws SeleniumFindException
	 * @throws ParameterException 
	 */
	public  void inputValue(String[] step)throws SeleniumFindException {
		try{
			HashMap<String,String> traXPath = null; 
			if(step.length >= 4 && RegExp.findCharacters(step[step.length-1], "^HASHMAP")){
				// DEBUG模式
				traXPath = TransformationMap.transformationByString(step[step.length-1]);
			}else{
				// START模式
//				traXPath = Test.TRA_NAME;
				traXPath = RunAutoTestingParameter.getAlias();
//				String context = RunAutoTestingParameter.getTestCase().getAssociatedProperites();
//				if(null == context){
//					traXPath = null;
//				}else{
//					traXPath = TransformationMap.transformationByString(context);
//				}
			}

			String xpath = null;
			if(null != traXPath){
				if(traXPath.containsKey(step[1])){
					xpath = traXPath.get(step[1]);
				}else{
					xpath = step[1];
				}
			}else{
				xpath = step[1];
			}
	
			DriverParameter.getDriverPaht().setValue(xpath,step[2]);
			
		}catch(Exception e){
			throw new SeleniumFindException("错误:" + e.getMessage());
//			Print.log(e.getMessage(), 2);
		}
	}

	/**
	 * 表达式支持所有运算
	 * @param step 用例
	 * @throws ParameterException 
	 */
	public void executeExpression(String[] step) throws ParameterException {
		String variable = null;
		String variableValue = null;
		String[] arrT = null;
		if (RegExp.findCharacters(step[1], "=")){
			arrT = RegExp.splitWord(step[1], "=");
			if (RegExp.findCharacters(arrT[0], "^\\{.*\\}$")){
				if (arrT.length == 1){
					ArrayList<String> arrL = RegExp.matcherCharacters(arrT[0], "(?<=\\{)(.+?)(?=\\})");
					variable = arrL.get(0);
					Variable.setExpressionName(variable, "");
					Print.log("变量:" + variable + "  值: " , 3);
				}else if(!arrT[1].equals("")){
					variableValue = Common.getExpressionValue(arrT[1]);
					if (null != variableValue){
						ArrayList<String> arrL = RegExp.matcherCharacters(arrT[0], "(?<=\\{)(.+?)(?=\\})");
						variable = arrL.get(0);
						Variable.setExpressionName(variable, variableValue);
						Print.log("变量:" + variable + "  值:" + Variable.getExpressionValue(variable), 1);
					}else{
						throw new ParameterException("语法解析失败，表达式："+arrT[1]);
					}

				}else{
					throw new ParameterException("表达式不完整："+step[1]);
				}
			}else{
				throw new ParameterException("表达式缺少“{ }变量”"+step[1]);
			}
		}else{
			throw new ParameterException("表达式缺少“=”"+step[1]);
		}
	}
	
	public  void addAttachment(String[] step) throws SeleniumFindException, ParameterException {
		if (step.length>1){
			String filePath = ConvertCharacter.getHtmlChr(step[2]);  // 转回原来的字符串
			if (!RegExp.findCharacters(filePath, "^[a-zA-Z]:")){
				filePath = RunParameter.getResultPaht().getTestCaseFolderPath() + "\\" + filePath;
				String parentPath = FileUtil.getParent(filePath);
				if (!FileUtil.exists(parentPath)){
					FileUtil.createFloder(parentPath);
				}
			}
			Print.log("导入路径："+filePath, 1);
//			OperateDialog.saveFile(path, step[0]);
			//String xPath = "//div[@class=' x-window x-window-noborder' and contains(@style,'visibility: visible')]";
			if (!step[1].equals("取消")){
				if (FileUtil.exists(filePath)){
					step[2] = filePath;
//					if (WebElementByXPath.doesExist(By.xpath(xPath))){
//						WebElement inputFile = Driver.getWebElementByXPath(xPath+"//input[@type='file']");
//						inputFile.sendKeys(filePath);
						inputValue(step);
		//				WebElement webE = Driver.selectElementByXPath(xPath);
//						Driver.getWebElementByXPath(xPath + "//button[contains(text(),'确定')]").click();
//					}else{
//						throw new ParameterException("添加附件框没有找到。");
//					}
				}else{
					throw new ParameterException(filePath+"文件不存在");
				}
			}else{
//				if (WebElementByXPath.doesExist(By.xpath(xPath + "//button[contains(text(),'"+ step[1] +"')]"))){
//					Driver.getWebElementByXPath(xPath + "//button[contains(text(),'"+ step[1] +"')]").click();
//				}else{
//					throw new ParameterException("没有找到按钮："+step[1]);
//				}
			}
		}else{
			throw new ParameterException("用例错误。");
		}
		/* 
		String xPath = "//div[@class=' x-window x-window-noborder' and contains(@style,'visibility: visible')]";
		if (!key.equals("取消")){
			String filePath = FileUtil.getParent(Parameter.TESTCASEPATH) + "\\" + key;
			if (FileUtil.exists(filePath)){
				if (WebElementByXPath.doesExist(By.xpath(xPath))){
	//				WebElement webE = Driver.selectElementByXPath(xPath);
					if (WebElementByXPath.doesExist(By.xpath(xPath + "//button[contains(text(),'浏览')]"))){
						Driver.selectElementByXPath(xPath + "//button[contains(text(),'浏览')]").click();
						String cmdBufferedReader = OperateDialog.importFile(filePath);
						String xPath1 = "//div[@class=' x-window x-window-noborder' and contains(@style,'visibility: visible')]";
						// 返回false，代表提示框已关闭
						if (cmdBufferedReader.indexOf("false") != -1){
							Driver.selectElementByXPath(xPath1 + "//button[contains(text(),'确定')]").click();
						}else{
							Print.log("添加附件失败，dialog框还存在。", 2);
						}
					}
				}else{
					Print.log("添加附件框没有找到。", 2);
				}
			}else{
				Print.log(filePath+"文件不存在", 2);
			}
		}else{
			if (WebElementByXPath.doesExist(By.xpath(xPath + "//button[contains(text(),'"+ key +"')]"))){
				Driver.selectElementByXPath(xPath + "//button[contains(text(),'"+ key +"')]").click();
			}else{
				Print.log("没有找到按钮："+key, 2);
			}
		}
		*/
	}
	
	/**
	 * 获取webElement值
	 * @param step
	 * @throws SeleniumFindException 
	 * @throws ParameterException 
	 */
	public  void getWebElementValueToVar(String[] step) throws SeleniumFindException, ParameterException {
		HashMap<String,String> traXPath = null; 
		if(step.length >= 4 && RegExp.findCharacters(step[step.length-1], "^HASHMAP")){
			traXPath = TransformationMap.transformationByString(step[step.length-1]);
		}else{
//			traXPath = Test.TRA_NAME;
//			String context = Test.getAssociatedProperites();
//			if(null == context){
//				traXPath = null;
//			}else{
//				traXPath = TransformationMap.transformationByString(context);
//			}
		}
		String xpath = null;
		if(null != traXPath){
			if(traXPath.containsKey(step[1])){
				xpath = traXPath.get(step[1]);
			}else{
				xpath = step[1];
			}
		}else{
			xpath = step[1];
		}
		WebElement verWeb = DriverParameter.getDriverPaht().getWebElementBylocator(xpath);
		String webElementValue = "";
		if(verWeb.getTagName().equalsIgnoreCase("input") && verWeb.getTagName().equalsIgnoreCase("textarea")){
			webElementValue = verWeb.getAttribute("value");
		}else if(verWeb.getTagName().equalsIgnoreCase("table")){
			// TODO
		}else{
			webElementValue = verWeb.getText();
			if (webElementValue.equals("")){
				webElementValue = verWeb.getAttribute("value");
			}
		}
		if (RegExp.findCharacters(step[2], "^\\{.*\\}$")){
			ArrayList<String> arrList = RegExp.matcherCharacters(step[2], "(?<=\\{)(.+?)(?=\\})");
			String variable = arrList.get(0);
			Variable.setExpressionName(variable, webElementValue);
		}else{
			throw new ParameterException("缺少“{ }”变量"+step[1]);
		}
	}

	/**
	 * 切换浏览器页面
	 * @param step
	 * @throws ParameterException 
	 * @throws SeleniumFindException 
	 */
	public  void changeBroTab(String[] step) throws ParameterException, SeleniumFindException {
		int index = -1;
		try{
			index = Integer.parseInt(step[1]);
		}catch(Exception e){
			throw new ParameterException(step[1]+"为非数字");
		}
		if (index>0){
			OperateWindows ow = new OperateWindows();
			ow.getNewBrowsers();
			ow.switchNewBrowser(index);
		}else{
			throw new ParameterException("切换浏览器页面参数错误："+index);
		}
	}

	/**
	 * 关闭浏览器页面
	 * @param step
	 * @throws ParameterException 
	 * @throws SeleniumFindException 
	 */
	public  void closeBroTab(String[] step) throws ParameterException, SeleniumFindException {
		int index = -1;
		try{
			index = Integer.parseInt(step[1]);
		}catch(Exception e){
			throw new ParameterException(step[1]+"为非数字");
		}
		if (index>0){
			OperateWindows ow = new OperateWindows();
			ow.getNewBrowsers();
			ow.closeNewBrowsers(index);
		}else{
			throw new ParameterException("关闭浏览器页面参数错误："+index);
		}
	}
	
	public static  void main(String[] args){
		
//		try {
//			String[] testP = {"setVar","{setVar}=getAutoIncrement(5)"};
//			executeExpression(testP);
//			
//			System.out.println(Variable.getExpressionValue("setVar"));
//			
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String[] testP = {"setVar","{setVar}='testVar'"};
//		try {
//			executeExpression(testP);
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String[] testPa = {"getVar","{getVar}={setVar}&'addString'"};
//		String a = "{asd}";
//		try {
//			executeExpression(testPa);
//		} catch (ParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		boolean b = RegExp.findCharacters(a, "^\\{.*\\}$");
//		ArrayList<String> arrL = RegExp.matcherCharacters(a, "(?<=\\{)(.+?)(?=\\})");
//		System.out.println();
	}












	
}

