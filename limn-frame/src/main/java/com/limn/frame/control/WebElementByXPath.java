package com.limn.frame.control;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.limn.driver.Driver;
import com.limn.tool.common.Print;






public class WebElementByXPath {
	/**
	 * 判定页面元素是否存在
	 * @param selector 传：By.xpath("")
	 * @return
	 */
	public static boolean doesExist(By selector){ 
	     try
	     { 
	    	 Driver.driver.findElement(selector); 
	         return true; 
	     } 
	     catch (NoSuchElementException e) 
	     { 
	         return false; 
	     } 
	}
	public static boolean doesExist(WebElement webElement, By selector){ 
	     try
	     { 
	    	 webElement.findElement(selector); 
	         return true; 
	     } 
	     catch (NoSuchElementException e) 
	     { 
	         return false; 
	     } 
	}
	
	/**
	 * 判断是否存在包含期望关键字的元素。
	 * @param content 找到的元素
	 * @param xpath xpath表达式
	 * @return
	 */
	public static boolean isContentAppeared(String content, String xpath) {  
		boolean status = false;
		try {
			Driver.driver.findElement(By.xpath(xpath)); // "//*[contains(.,'" + content + "')]"
			status = true;
		} catch (NoSuchElementException e) {
			status = false;
			Print.log("'" + content + "' doesn't exist!", 2);
		}
		return status;
	}
}
