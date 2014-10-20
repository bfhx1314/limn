package com.haowu.uitest.hossweb;


import java.util.LinkedHashMap;
import java.util.List;








import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.haowu.common.DataBase;
import com.haowu.exception.HaowuException;
import com.haowu.uitest.common.WebControl;
import com.haowu.uitest.common.WebTable;
import com.haowu.uitest.hossweb.apply.ApplyType;
import com.limn.driver.common.OperateWindows;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.driver.Driver;
import com.limn.frame.control.Test;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.thoughtworks.selenium.SeleniumException;


public class HossWeb {
	
	
	private static String UserName = null;
	
	/**
	 * 初始化浏览器
	 * @param type 浏览器类型 1firefox 2chrome
	 * @param url 地址
	 * @param ip 远程运行IP 可选
	 * @throws HaowuException 
	 */
	public static void startBroswer(int type, String url, String ip) throws HaowuException{
		  Driver.setDriver(type, url, null);
		  try {
			Driver.startBrowser();
		} catch (SeleniumFindException e) {
			throw new HaowuException(10020000, e.getMessage());
		}
	}
	
	/**
	 * 关闭浏览器
	 */
	public static void stopBroswer(){
		Driver.closeBrowser();
	}
	
	/**
	 * 登录
	 * @param username 帐号
	 * @param password 密码
	 * @throws HaowuException 
	 */
	public static void login(String username, String password) throws HaowuException{
		
		
		UserName = username;

		try {
			waitLoadForPage();
			//帐号
			Driver.setText(By.id("username"), username);
			//密码
			Driver.setText(By.id("password"), password);
		} catch (SeleniumFindException e1) {
			
			throw new HaowuException(10010001,"登陆界面<username>或<password>录入元素未定位到");
			
		}
//		Driver.setText("id=username", username);

//		Driver.setText("id=password", password);
		
		
//		Set<Cookie> cookies  = Driver.driver.manage().getCookies();
//		
//		String cookieStr = "";
//		for (Cookie cookie : cookies) {
//			cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
//		}
		
//		HossInterface hif = new HossInterface(ParameterHaowu.HOSS_IP, ParameterHaowu.HOSS_PORT);
//		String code = hif.getCode(cookieStr);

		//验证码填写
			
//		Driver.setText("id=captcha", code);
		
		//
		//登录 
		WebElement webElement;
		try {
			webElement = Driver.getWebElementByXPath("//input[@class='submit hand']");
			WebControl.clickControl(webElement);
		} catch (SeleniumFindException e) {
			throw new HaowuException(10010002,"登陆界面<确定>按钮元素未定位到");
		}
		
		
		
/*		List<WebElement> list = Driver.selectElementsByXPath("//div[@class='error-message']");
		
		String errMessage = "";
		
		while(list.size()>0){
			try{
				errMessage = list.get(0).getText();
				if(RegExp.findCharacters(errMessage, "验证码")){
					Print.log(errMessage + ",重新尝试. 验证码:" +  code, 0);
					code = hif.getCode(cookieStr);
					Driver.setText("id=captcha", code);
					WebControl.clickControl(webElement);
				}else if(!errMessage.isEmpty()){
					Print.log(errMessage, 2);
					break;
				}
				list = Driver.selectElementsByXPath("//div[@class='error-message']");
			}catch(Exception e){
				
				break;
			}
		}*/
		
	}
	
	
	/**
	 * 退出登录
	 * @throws HaowuException 
	 */
	public static void logout() throws HaowuException{

		try {
			waitLoadForPage();
			WebElement webElement;
			webElement = Driver.getWebElementByXPath("//a[@id='logout']");
			WebControl.clickControl(webElement);
			Common.wait(200);
		} catch (SeleniumFindException e) {
			throw new HaowuException(10010000,e.getMessage());
		}

		
	}
	
	
	/**
	 * 选择菜单
	 * @param menu 菜单名称
	 * @throws HaowuException 
	 */
	public static void menu(String menu) throws HaowuException{

		
		//菜单栏的集合
		WebElement menuBar;
		try {
			waitLoadForPage();
			menuBar = Driver.getWebElementByXPath("//ul[@id='nav']");
			//根据名称查找节点
			Print.log("Menu:" + menu, 0);
			if(!Driver.isWebElementExist(By.xpath("//a[text()='" + menu + "']"))){
				waitLoadForPage();
			}
			
			WebControl.clickControl(Driver.getWebElement(menuBar, By.xpath("//a[text()='" + menu + "']")));
//			WebControl.clickControl(menuBar.findElement(By.xpath("//a[text()='" + menu + "']")));
			menuBar = Driver.getWebElementByXPath("//ul[@id='nav']");
			String className = Driver.getWebElement(menuBar, By.xpath("//a[text()='" + menu + "']")).getAttribute("class");
//			String className = menuBar.findElement(By.xpath("//a[text()='" + menu + "']/..")).getAttribute("class");
			while(!className.equalsIgnoreCase("active")){
//				WebControl.clickControl(menuBar.findElement(By.xpath("//a[text()='" + menu + "']")));
				WebControl.clickControl(Driver.getWebElement(menuBar, By.xpath("//a[text()='" + menu + "']")));
				menuBar = Driver.getWebElementByXPath("//ul[@id='nav']");
//				className = menuBar.findElement(By.xpath("//a[text()='" + menu + "']/..")).getAttribute("class");
				className = Driver.getWebElement(menuBar, By.xpath("//a[text()='" + menu + "']/..")).getAttribute("class");
				Print.log("菜单选择失败,再次尝试", 3);
				waitLoadForPage();
			}
			Print.log("菜单选择成功", 1);
			
		} catch (SeleniumFindException e) {
			throw new HaowuException(10010004,e.getMessage());
		}
	}
	
	
	/**
	 * 选择列表项
	 * @param list 列表项名称
	 * @throws HaowuException 
	 */
	public static void list(String list) throws HaowuException{
		Common.wait(100);
	
		//列表的集合
		WebElement menuBar;
		try {
			waitLoadForPage();
			menuBar = Driver.getWebElementByXPath("//ul[@id='subnav']");
			//根据名称查找节点
			WebControl.clickControl(Driver.getWebElement(menuBar, By.xpath("//span[text()='" + list + "']")));
//			menuBar.findElement(By.xpath("//span[text()='" + list + "']")).click();
		} catch (SeleniumFindException e) {
			throw new HaowuException(10010000, e.getMessage());
		}

	}
	
	
	/**
	 * 创建单子
	 * @param applyType 单子类型
	 * @param data 录入的数据
	 * @param submitName 提交类型
	 * @throws SeleniumFindException 
	 */
	public static String createApply(String applyType, LinkedHashMap<String,String> data, String submitName) throws SeleniumFindException{
		waitLoadForPage();
		Driver.getWebElementByXPath("//a[text()='"+ applyType +"']").click();
		Print.log("单子类型:" + applyType, 0);
		
		String no = editApply(applyType,data,submitName,true);
		
		if(Driver.waitForElementPresent(By.id("systemMessage"), 5)){
			Print.log(submitName + "成功",1);
		}else{
			Print.log(submitName + "失败",2);
		}
		return no;
	}
	
	private static String editApply(String applyType, LinkedHashMap<String,String> data, String submitName ,boolean isCreate) throws SeleniumFindException{
		
		String dataTable = getTableName(applyType);

		if(data!=null){
			applyEdit(data);
		}
		

		
		if(isCreate){
			
			applySubmitButton(submitName);
			
			OperateWindows.dealPotentialAlert(true);
			
			Common.wait(1000);
			
			String flow_no = DataBase
					.executeSQL("select flow_no from "+ dataTable +" where "
							+ "creater in (select id from auth_user where user_name='"
							+ UserName + "' ) " + "order by id desc")[0][0];
	
			Print.log("单据编号:" + flow_no,0);
			
			return flow_no;
		}else{
			return "";
		}
		
	}
	
	
	/**
	 * 编辑申请单
	 * @param applyNo 申请的编号
	 * @param applyType 申请的类型名称
	 * @param data 数据
	 * @param submitName 提交类型
	 * @throws SeleniumFindException 
	 */
	public static void modifyApply(String applyNo, String applyType, LinkedHashMap<String,String> data, String submitName) throws SeleniumFindException{
		waitLoadForPage();
		String dataTable = getTableName(applyType);
		String buinessKey = DataBase.executeSQL("select id from "+ dataTable +" where flow_no = '"+applyNo+"'")[0][0];
		Print.log("buinessKey:" + buinessKey, 0);
		WebControl.clickControl(Driver.getWebElementByXPath("//button[@data-businesskey='" + buinessKey + "' and text()='编辑']"));
		if(null!=data){
			editApply(applyType,data,submitName,false);
		}
		Common.wait(2);
		
		Common.wait(1000);
		applySubmitButton(submitName);
		Common.wait(1000);
		
		OperateWindows.dealPotentialAlert(true);
	}
	
	/**
	 * 删除表单
	 * @param applyNo 申请的编号
	 * @param applyType 申请的类型名称
	 * @throws SeleniumFindException 
	 */
	public static void deleteApply(String applyNo, String applyType) throws SeleniumFindException{
		waitLoadForPage();
		String dataTable = getTableName(applyType);
		String buinessKey = DataBase.executeSQL("select id from "+ dataTable +" where flow_no = '"+applyNo+"'")[0][0];
		Print.log("buinessKey:" + buinessKey, 0);
		WebControl.clickControl(Driver.getWebElementByXPath("//button[@data-businesskey='"+ buinessKey + "' and text()='删除']"));
		Common.wait(2);
		WebControl.clickControl(Driver.getWebElementByXPath("//a[@data-apply='confirmation']"));
		OperateWindows.dealPotentialAlert(true);
	}
	
	/**
	 * 查看表单
	 * @param applyNo 申请的编号
	 * @param applyType 申请的类型名称
	 * @param submitName 提交类型
	 * @throws SeleniumFindException 
	 */
	public static void seeApply(String applyNo, String applyType, String submitName) throws SeleniumFindException{
		waitLoadForPage();
		String dataTable = getTableName(applyType);
		String buinessKey = DataBase.executeSQL("select id from "+ dataTable +" where flow_no = '"+applyNo+"'")[0][0];
		WebControl.clickControl(Driver.getWebElementByXPath("//button[@data-businesskey='"+ buinessKey + "' and text()='查看']"));
		Common.wait(2);
		applySubmitButton(submitName);
		OperateWindows.dealPotentialAlert(true);
	}
	
	/**
	 * 暂缓
	 * @param applyNo 申请的编号
	 * @param applyType 申请的类型名称
	 * @param days 暂缓天数
	 * @throws SeleniumFindException 
	 */
	public static void deferApply(String applyNo, String applyType, String days) throws SeleniumFindException{
		waitLoadForPage();
		String dataTable = getTableName(applyType);
		String buinessKey = DataBase.executeSQL("select id from "+ dataTable +" where flow_no = '"+applyNo+"'")[0][0];
		Print.log("buinessKey:" + buinessKey, 0);
		Driver.getWebElementByXPath("//button[@data-businesskey='" + buinessKey + "' and text()='审批']").click();
		applySubmitButton("暂缓");
		OperateWindows.dealPotentialAlert(true);
		WebElement web = WebControl.getWebElementBylocator("deferDays");
		WebControl.setValue(web, days);
		WebControl.clickControl(Driver.getWebElementByXPath("//a[text()='确定']"));
		
	}
	
	public static void applyApproval(String billNO, String applyType,LinkedHashMap<String,String> data,String comments,String buttonName) throws SeleniumFindException{
		Common.wait(2);
		waitLoadForPage();
		WebTable talbe = new WebTable(Driver.getWebElementByXPath("//table[@id='searchResultList']"));
	
		int index = talbe.getRowIndexByValue(0,billNO);
		
		WebElement button = talbe.getWebElment(7,index);

		WebControl.clickControl(Driver.getWebElement(button, By.tagName("button")));
//		button.findElement(By.tagName("button")).click();
		
		if(null!=data){
			editApply(applyType,data,buttonName,false);
		}
		
		if(comments==null){
			comments = "";
		}
		//书写审批意见
		Driver.setText(By.id("content"), comments);
		
		//click按钮
		applySubmitButton(buttonName);
		
		OperateWindows.dealPotentialAlert(true);
	}
	
	/**
	 * 审批管理 审批
	 * @param billNO 表单编号
	 * @param comments 审批意见
	 * @param sumbitType 审批类型 0 同意 1退回 2暂缓 3终止
	 * @throws SeleniumFindException 
	 */
	public static void applyApproval(String billNO, String comments,String buttonName) throws SeleniumFindException{
		
		waitLoadForPage();
		
		WebTable talbe = new WebTable(Driver.getWebElementByXPath("//table[@id='searchResultList']"));
	
		int index = talbe.getRowIndexByValue(0,billNO);
		
		WebElement button = talbe.getWebElment(7,index);

		WebControl.clickControl(Driver.getWebElement(button, By.tagName("button")));
//		button.findElement(By.tagName("button")).click();
	
		if(comments==null){
			comments = "";
		}
		
		//书写审批意见
		Driver.setText(By.id("content"), comments);
		//click按钮
		Common.wait(1000);
		
		applySubmitButton(buttonName);
		
		Common.wait(1000);
		
		OperateWindows.dealPotentialAlert(true);
		
		Common.wait(1000);
		if(Driver.waitForElementPresent(By.id("systemMessage"), 5)){
			Print.log(buttonName + "成功",1);
		}else{
			Print.log(buttonName + "失败",2);
		}
	}
	
	
	private static void applyEdit(LinkedHashMap<String, String> data) throws SeleniumFindException {
		
		for (String key : data.keySet()) {
			Common.wait(500);
			waitLoadForPage();
			
			WebElement web = WebControl.getWebElementBylocator(key);
			Print.log("录入数据 Key:" + key,1);
			Print.log("录入数据 Value:" + data.get(key),1);
			if(web==null){
				Print.log("locator:" + key, 2);
			}else{
				WebControl.setValue(web, data.get(key));
			}
			waitLoadForPage();
		}
	}
	
	/**
	 * 
	 * @param data
	 * @throws SeleniumFindException 
	 */
	public static void createProject(LinkedHashMap<String,String> data) throws SeleniumFindException{
		waitLoadForPage();
		WebControl.clickControl(Driver.getWebElementByXPath(".//*[@id='searchResultList']/caption/a/button")); //发起项目
//		WebControl.setValue(WebControl.getWebElementBylocator("project.startTime"), "2013-04-02", "project.startTime");
		applyEdit(data);

		WebElement webElement = Driver.getWebElementByXPath(".//*[@id='addForm']/div/div/div/button");
		WebControl.clickControl(webElement);   //提交
		//WebControl.clickControl(Driver.getWebElementByXPath(".//*[@id='addForm']/div/div/div/button")); //提交
	}
	
	
	private static void applySubmitButton(String buttonName) throws SeleniumFindException{
		
		if(buttonName!=null){
			List<WebElement> webElement = Driver.getWebElementsByXPath("//button[contains(text(),'"+ buttonName +"')]");
			if(webElement.size()>0){
				WebControl.clickControl(webElement.get(0));
			}else{
				WebControl.clickControl(Driver.getWebElementByXPath("//a[contains(text(),'"+ buttonName +"')]"));
//				Driver.runScript("arguments[0].scrollIntoView(true);", Driver.getWebElementByXPath("//a[contains(text(),'"+ buttonName +"')]"));
//				Driver.getWebElementByXPath("//a[contains(text(),'"+ buttonName +"')]").click();;
			}
		}
	}
	
	
	private static String getTableName(String applyType){
		String dataTable = null;
		if(applyType.equals(ApplyType.QZQYS)){
			dataTable = "cm_full_cycle_project_budget";
		}else if(applyType.equals(ApplyType.YDYS)){
			dataTable = "cm_month_budget";
		}else if(applyType.equals(ApplyType.FYSQ)){
			dataTable = "cm_project_fee";
		}else if(applyType.equals(ApplyType.FYBX)){
			dataTable = "cm_project_repay";
		}else if(applyType.equals(ApplyType.WPLY)){
			dataTable = "cm_pro_goods_apply";
		}else if(applyType.equals(ApplyType.WPHX)){
			dataTable = "cm_pro_apply_goods_cancel";
		}else if(applyType.equals(ApplyType.QDFJ)){
			dataTable = "cm_channel_plan";
		}else if(applyType.equals(ApplyType.HTSP)){
			dataTable = "cm_apply_contract";
		}else if(applyType.equals(ApplyType.HTFK)){
			dataTable = "cm_apply_contract_pay";
		}else if(applyType.equals(ApplyType.QDYJJS)){
			dataTable = "cm_channel_commission_settlement";
		}else if(applyType.equals(ApplyType.QDYJBZSQ)){
			dataTable = "cm_commission_settlement_standards";
		}else if(applyType.equals(ApplyType.TGFJM)){
			dataTable = "cm_group_fee_reduce_apply";
		}else if(applyType.equals(ApplyType.BZJFK)){
			dataTable = "cm_project_deposit";
		}else if(applyType.equals(ApplyType.TKSQD)){
			dataTable = "cm_project_refund_apply";
		}
		return dataTable;
	}
	
	public static void verification(){
		String expected = Test.getExpectedResult();
	}
	
	
	
	/**
	 * 页面加载等待
	 * @throws SeleniumFindException 
	 */
	private static void waitLoadForPage() throws SeleniumFindException{
		try{
			
			WebElement web = Driver.getWebElementByXPath("//div[@id='spinner']");
			
			while(web.getCssValue("display") !=null && !web.getCssValue("display").equals("none")){
				Common.wait(1000);
				Print.log("页面加载中", 0);
			}
		}catch (StaleElementReferenceException e){
			waitLoadForPage();
		}
		
	}
	
}
