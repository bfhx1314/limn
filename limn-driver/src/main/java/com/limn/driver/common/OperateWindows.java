package com.limn.driver.common;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.log.RunLog;
import com.limn.tool.common.Print;


/**
 * 定位网页弹出框
 * @author Administrator
 *
 */
public class OperateWindows {
	
	/**
	 * 当前窗口句柄
	 */
	public String currentHandle;
	
	/**
	 * 所有窗口的句柄
	 */
	public Set<String> handles;
	
	/**
	 * 所有窗口的个数
	 * @return
	 */
	public int getHandlesSize(){
		return handles.size();
	}
	
	/**
	 * 新窗口的个数，不包括当前窗口。
	 * @return
	 */
	public int getNewBrowsersSize(){
		Set<String> hands = handles;
		hands.remove(currentHandle);
		return hands.size();
	}
	
	/**
	 * 处理潜在的1个alert（javascript弹出框）
	 * @param option 确认or取消
	 * @return
	 */
	public static boolean dealPotentialAlert(boolean option) {
		//是否存在
		boolean flag = false;
		//异常捕获
		try {
		    Alert alert = Driver.driver.switchTo().alert();
		    //判断是否存在alert弹框
		    if (null == alert){
		        throw new NoAlertPresentException();
		    }
		    //异常捕获
		    try {
		        //确认or取消
		        if (option) {
		            //确认
		        	Print.log("Accept the alert: " + alert.getText(),1);
		            alert.accept();
		        } else {
		            //取消
		        	Print.log("Dismiss the alert: " + alert.getText(),1);
		            alert.dismiss();
		        }
		        flag = true;
		    } catch (WebDriverException e) {
		        if (e.getMessage().startsWith("Could not find")){
		            System.out.println("There is no alert appear!");
		            Print.log("There is no alert appear!",2);
		        }else{
		            throw e;
		        }
		    }
		} catch (NoAlertPresentException e) {
		    System.out.println("There is no alert appear!");
		    Print.log("There is no alert appear!",2);
		}
		return flag;
	}
	
	/**
	 * 获取当前窗口句柄、所有窗口句柄。
	 */
	public void getNewBrowsers(){
		//当前窗口句柄
		try {
			currentHandle = Driver.driver.getWindowHandle();
		}catch(Exception e){
			Print.log("当前driver没有对象",3);
		}
		
		//得到所有窗口的句柄
		handles = Driver.driver.getWindowHandles();
	}
	
	/**
	 * 切换到新弹出的浏览器页面
	 * @throws SeleniumFindException
	 */
	public void switchNewBrowser() throws SeleniumFindException{
		//不包括当前窗口
		handles.remove(currentHandle);
		int handlesSize = handles.size();
		//存在窗口
		if (handlesSize > 0) {
			Print.log("存在"+ handlesSize +"个新窗口。", 3);
			for(int i=0;i<handlesSize;i++){
			    try{
					//定位窗口
			    	Driver.driver.switchTo().window(handles.iterator().next());
			    	return;
			    }catch(Exception e){
			    	throw new SeleniumFindException("切换浏览器失败");
			    }
			}
		}
	}
	/**
	 * 切换到新弹出的浏览器页面
	 * @param index 第几个页面
	 * @throws SeleniumFindException
	 */
	public void switchNewBrowser(int index) throws SeleniumFindException{
		//不包括第一个浏览器页面
//		handles.remove(currentHandle);
		int handlesSize = handles.size();
		//存在窗口
		if (handlesSize > 0) {
			Print.log("存在"+ handlesSize +"个新窗口。", 3);
		    try{
				//定位窗口
		    	int i = 1;
	    		for(Iterator<?> ite = handles.iterator();ite.hasNext();){
	    			String iter = (String) ite.next();
	    			if (i == index){
	    				Driver.driver.switchTo().window(iter);
	    				return;
	    			}else{
	    				i++;
	    			}
	    		}
		    }catch(Exception e){
		    	throw new SeleniumFindException("切换浏览器失败");
		    }
		}
	}
	/**
	 * 切回第一个浏览器页面
	 * @throws SeleniumFindException 
	 */
	public void switchToDefaultBrowser() throws SeleniumFindException{
		try{
			Driver.driver.switchTo().window(currentHandle);
	    }catch(Exception e){
	    	throw new SeleniumFindException("切回第一个浏览器失败");
	    }
	}
	
	/**
	 * 关闭所有浏览器页面(不包括第一个浏览器页面)。
	 * @return
	 */
	public boolean closeNewBrowsers(){
		//不包括第一个浏览器页面
		handles.remove(currentHandle);
		int handlesSize = handles.size();
		//存在窗口
		if (handlesSize > 0) {
			Print.log("存在"+ handlesSize +"个新窗口。", 3);
			for(int i=0;i<handlesSize;i++){
			    try{
					//定位窗口
			    	Driver.driver.switchTo().window(handles.iterator().next());
			    	Driver.driver.close();
			    }catch(Exception e){
			        System.out.println(e.getMessage());
			        Print.log(e.getMessage(), 2);
			    }
			}
			Driver.driver.switchTo().window(currentHandle);
		}
//		System.out.println("Did not find window");
//		Print.log("Did not find window", 2);
		return true;
	}
	/**
	 * 关闭浏览器页面。
	 * @param index 第几个页面
	 * @return
	 */
	public boolean closeNewBrowsers(int index){
		//不包括当前窗口
		int handlesSize = handles.size();
		//存在窗口
		if (handlesSize > 0) {
			Print.log("存在"+ handlesSize +"个新窗口。", 3);
		    try{
				//定位窗口
		    	int i = 1;
	    		for(Iterator<?> ite = handles.iterator();ite.hasNext();){
	    			String iter = (String) ite.next();
	    			if (i == index){
	    				Driver.driver.switchTo().window(iter);
	    				Driver.driver.close();
	    			}else{
	    				i++;
	    			}
	    		}
		    }catch(Exception e){
		        System.out.println(e.getMessage());
		        Print.log(e.getMessage(), 2);
		    }
		}
//		System.out.println("Did not find window");
//		Print.log("Did not find window", 2);
		return true;
	}
	//处理单个非alert弹窗
	public static boolean testNewBrowser(WebDriver driver){
	    try{
	        //定位窗口
	        driver.switchTo().window(driver.getWindowHandles().iterator().next());
	        return true;
	    }catch(Exception e){
	        System.out.println(e.getMessage());
	        return false;
	    }
	}
}
