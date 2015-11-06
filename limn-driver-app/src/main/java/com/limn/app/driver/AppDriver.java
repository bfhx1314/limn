package com.limn.app.driver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.limn.app.driver.exception.AppiumException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.regexp.RegExp;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import net.erdfelt.android.apk.AndroidApk;

public class AppDriver {

	
	public static AppiumDriver<AndroidElement> driver = null;

	public static int HEIGTH = 0;
	public static int WIDTH = 0;

	private static String appFilePath = null;

	private static AndroidApk APKInfo = null;

	
	
	public static void init(String filePath) throws AppiumException {
		init(filePath, "127.0.0.1:4723");
	}
	
	public static void runScript(String driverCommand, Map<String,?> parameters){
		if(null == driver){
			return;
		}
		
		driver.execute(driverCommand, parameters);
	}
	
	
	public static void init(String filePath, String IP) throws AppiumException {
		
		
		if(null != driver){
			return;
		}
		
		if(null == IP || IP.isEmpty()){
			IP = "127.0.0.1:4723";
		}
		
		AppDriver.appFilePath = filePath;
		initAPKInfo();
		File appFile = new File(filePath);

		if (!appFile.exists()) {
			throw new AppiumException(appFile.getAbsolutePath() + " 文件不存在");
		}

		DesiredCapabilities dcb = new DesiredCapabilities();
		dcb.setCapability("deviceName", "Android Emulator"); // 后期增加配置
		dcb.setCapability("paltformVersion", "4.4"); // 后期增加配置
		dcb.setCapability("app", appFile.getAbsolutePath());
		dcb.setCapability("appPackage", APKInfo.getPackageName());
		dcb.setCapability("unicodeKeyboard", "true");
		dcb.setCapability("resetKeyboard", "true");

		try {
			driver = new AndroidDriver<>(new URL("http://" + IP + "/wd/hub"), dcb);
		} catch (MalformedURLException e) {
			throw new AppiumException("driver hub无法连接");
		} catch(SessionNotCreatedException e){
			throw new AppiumException("请重新启动 Driver HUB,A new session could not be created");
		} catch(UnreachableBrowserException e){
			throw new AppiumException("Appium未连接 Connect to " + IP);
		}
		AppDriver.HEIGTH = driver.manage().window().getSize().getHeight();
		AppDriver.WIDTH = driver.manage().window().getSize().getWidth();

		Common.wait(7000);
		
	}

	/**
	 * 录入数据
	 * 
	 * @param element
	 * @param value
	 * @throws AppiumException
	 */
	public static void setValue(By by, String value) throws AppiumException {
		check();
		getAndroidElement(by).sendKeys(value);
	}

	public static void setValue(String id, String value) throws AppiumException {
		check();
		try{
			String key = id;
			if(!RegExp.findCharacters(id, "^" + AppDriver.APKInfo.getPackageName() + ":id/")){
				key = AppDriver.APKInfo.getPackageName() + ":id/" + id;
			}
			getAndroidElement(By.id(key)).sendKeys(value);
		} catch(AppiumException e){
			throw new AppiumException(e.getMessage() + AppDriver.APKInfo.getPackageName() + ":id/" + id);
		}
	}

	
	private static AndroidElement getAndroidElement(By by) throws AppiumException{
		AndroidElement ae = null;
		
		//判断元素是否存在多个
		List<AndroidElement> listEle= driver.findElements(by);
		if(listEle.size()>1){
			throw new AppiumException("存在多个此元素:");
		}
		
		try{
			ae = driver.findElement(by);
		} catch(NoSuchElementException e){
			throw new AppiumException("元素未找到:");
		}
		return ae;
		
	}
	
	
	/**
	 * 点击
	 * 
	 * @param by
	 * @throws AppiumException
	 */
	public static void click(By by) throws AppiumException {
		check();
		getAndroidElement(by).click();
	}

	public static void click(String id) throws AppiumException {
		check();
		try{
			getAndroidElement(By.id(AppDriver.APKInfo.getPackageName() + ":id/" + id)).click();
		} catch (AppiumException e) {
			throw new AppiumException(e.getMessage() + AppDriver.APKInfo.getPackageName() + ":id/" + id);
		}
	}

	/**
	 * 滑动屏幕
	 * 
	 * @param startx 起始x
	 * @param starty
	 * @param endx
	 * @param endy
	 * @throws AppiumException
	 */
	public static void swipe(int startx, int starty, int endx, int endy) throws AppiumException {
		
		check();
		driver.swipe(startx, starty, endx, endy, 1000);
	}
	
	
	public static void touchAction(By by){
		TouchAction action = new TouchAction(driver);
		action.longPress(driver.findElement(By.id("xx"))).waitAction(1000).release().perform();
	}
	
	

	private static void check() throws AppiumException {
		if (driver == null) {
			throw new AppiumException("drver 不存在");
		} else if (APKInfo == null) {
			throw new AppiumException("app信息无法获取");
		}
	}

	public static String getPackageName() throws AppiumException {
		check();
		return APKInfo.getPackageName();
	}

	private static void initAPKInfo() throws AppiumException {
		try {
			APKInfo = new AndroidApk(new File(AppDriver.appFilePath));
		} catch (ZipException e) {
			throw new AppiumException("文件占用异常");
		} catch (IOException e) {
			Print.log("apkFile:" + AppDriver.appFilePath , 2);
			throw new AppiumException("文件异常");
		}
	}

	
	/**
	 * 保存截图
	 * @param bitMapPath
	 */
	public static String  screenshot(String bitMapPath) {
		try {
			check();
		} catch (AppiumException e1) {
			Print.log("截图失败",2);
			e1.printStackTrace();
		}
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(bitMapPath));
			Print.debugLog("save snapshot path is:" + bitMapPath,1);
		} catch (IOException e) {
			Print.log("save snapshot error! path is:" + bitMapPath,2);
		}
		return bitMapPath;
	}

}
