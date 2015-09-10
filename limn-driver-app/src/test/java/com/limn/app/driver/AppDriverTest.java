//package com.limn.app.driver;
//
//import org.testng.annotations.Test;
//
//import com.limn.app.driver.exception.AppiumException;
//import com.limn.tool.common.Common;
//
//public class AppDriverTest {
//
//	@Test
//	public void setValue() {
//		
//		try {
//			
//			
//			AppDriver.init("/Users/limengnan/Downloads/HaowuAgent.apk","127.0.0.1:4723");
//			
//			Common.wait(8000);
//			AppDriver.swipe(AppDriver.WIDTH-10, AppDriver.HEIGTH/2, 10, AppDriver.HEIGTH/2);
//			AppDriver.swipe(AppDriver.WIDTH-10, AppDriver.HEIGTH/2, 10, AppDriver.HEIGTH/2);
//			AppDriver.swipe(AppDriver.WIDTH-10, AppDriver.HEIGTH/2, 10, AppDriver.HEIGTH/2);
//			
//			AppDriver.click("btn_enter");
//			AppDriver.setValue("et_name", "15921788936");
//			AppDriver.setValue("et_passwd", "123456");
//			AppDriver.click("button_login");
//			
//			
//		} catch (AppiumException e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
//}
