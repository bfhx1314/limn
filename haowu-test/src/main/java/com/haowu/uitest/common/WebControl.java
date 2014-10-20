package com.haowu.uitest.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.regexp.RegExp;

/**
 * 页面控件的所有操作
 * @author limn
 *
 */
public class WebControl {
	
	protected static WebElement findWebElement = null;
	
	protected static int hasThread = 0;

	/**
	 * 下拉框的选择
	 * @param web tagname=select
	 * @param value 界面显示的值
	 */
	private static void selectList(WebElement web, String value) {
		Select select = new Select(web);
		Print.log("list选项:" + value, 0);
		select.selectByVisibleText(value);
	}
	
	/**
	 * 录入数据
	 * @param web input
	 * @param value 值
	 * @param locator 定位
	 */
	private static void inputValue(WebElement web, String value){
		try {
			Driver.setText(web, value);
		} catch (SeleniumFindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Driver.action.keyUp(Keys.NUMPAD0);
	}
	
	/**
	 * 控件操作
	 * @param web webElement
	 * @param value 值，选项，事件
	 * @param locator 定位
	 */
	public static void setValue(WebElement web, String value){
		
		if(RegExp.findCharacters(value, "\\[.*?\\]")){
			webEvent(web,RegExp.filterString(value, "[\\]"));
			return ;
		}
		
		if(web.getTagName().equalsIgnoreCase("select")){
			selectList(web,value);
		}else{
			inputValue(web, value);
		}
		try {
			Driver.cancelHighLightWebElement(findWebElement);
		} catch (SeleniumFindException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 点击事件
	 * @param web
	 */
	public static void clickControl(WebElement web){
//		Driver.runScript("arguments[0].scrollIntoView(true);", web);
		web.click();
//		Driver.action.click(web);
//		Driver.action.click(web).perform();
//		Driver.action.clickAndHold(web)
	}

	/**
	 * 事件
	 * @param web
	 * @param event
	 */
	private static void webEvent(WebElement web, String event){
		if(event.equalsIgnoreCase("click")){
			clickControl(web);
		}else if(event.equalsIgnoreCase("dbclick")){
			web.click();
		}
	}
	
	/**
	 * 根据locator获取webElement
	 * @param locator
	 * @return
	 */
	public static WebElement getWebElementBylocator(String locator){
		WebElement web = null;
		if (RegExp.findCharacters(locator, "^xpath:")) {
			
			String xpath = locator.substring(6);
			try {
				web = Driver.getWebElementByXPath(xpath);
			} catch (SeleniumFindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			findWebElement = web;
		} else {
			findWebElement = null;
			new Thread(new FindWebElements(By.id(locator))).start();
			new Thread(new FindWebElements(By.name(locator))).start();
			hasThread = 2;
			while(true){
				if(null!=findWebElement){
					break;
				}

				if(hasThread==0){
					Print.log("未定位WebElement locator:" + locator, 2);
					break;	
				}
				Common.wait(500);
			}
		}
		if(findWebElement!=null){
			try {
				Driver.highLightWebElement(findWebElement);
			} catch (SeleniumFindException e) {
				e.printStackTrace();
			}
		}
		return findWebElement;
	}
	

	
}


class FindWebElements implements Runnable{
	
	private By locator = null;
	
	public FindWebElements(By locator){
		this.locator = locator;
	}

	@Override
	public void run() {
		try {
			if (Driver.isWebElementExist(locator)){
				try {
					WebElement web = Driver.getWebElement(locator);
					if(null!=web){
						WebControl.findWebElement = web;
					}
				} catch (SeleniumFindException e) {
					
				}
			}
		} catch (SeleniumFindException e) {
			e.printStackTrace();
		}
		WebControl.hasThread --;
	}
}

