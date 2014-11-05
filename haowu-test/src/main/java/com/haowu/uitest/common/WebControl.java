package com.haowu.uitest.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.haowu.exception.HaowuException;
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
	 * @throws HaowuException 
	 */
	private static void selectList(WebElement web, String value) throws HaowuException {
		Select select = new Select(web);
		Print.log("list选项:" + value, 0);
		try{
			select.selectByVisibleText(value);
		}catch(Exception e){
			throw new HaowuException(10010001, e.getMessage());
		}
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
	 * @throws HaowuException 
	 */
	public static void setValue(String locator, String value) throws HaowuException{
		
		WebElement web = WebControl.getWebElementBylocator(locator);
		
		Print.log("录入数据 Key:" + locator,1);
		Print.log("录入数据 Value:" + value,1);
		
		if(web == null){
			Print.log("定位失败 locator:" + locator, 2);
			return;
		}
		
		if(RegExp.findCharacters(value, "\\[.*?\\]")){
			webEvent(web,RegExp.filterString(value, "[\\]"));
			return;
		}
		
		if(web.getTagName().equalsIgnoreCase("select")){
			selectList(web,value);
		}else{
			inputValue(web, value);
		}
		
//		try {
//			Driver.cancelHighLightWebElement(findWebElement);
//		} catch (SeleniumFindException e) {
//			Print.log("取消高亮失败", 2);
//		}
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
	
	public static void main(String[] args){
		getWebElementBylocator("xpath(zzzzz)");
	}
	
	/**
	 * 根据locator获取webElement
	 * @param locator
	 * @return
	 */
	public static WebElement getWebElementBylocator(String locator){
		return Driver.getWebElementBylocator(locator);
	}
	

	
}

