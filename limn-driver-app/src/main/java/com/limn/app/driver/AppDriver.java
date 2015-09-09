package com.limn.app.driver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.limn.app.driver.exception.AppiumException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import net.erdfelt.android.apk.AndroidApk;

public class AppDriver {

	private static AppiumDriver<AndroidElement> driver = null;

	public static int HEIGTH = 0;
	public static int WIDTH = 0;

	private static String appFilePath = null;

	private static AndroidApk APKInfo = null;

	public static void init(String filePath) throws AppiumException {
		if(null != driver){
			return;
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
			driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), dcb);
		} catch (MalformedURLException e) {
			throw new AppiumException("driver hub无法连接");
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
		driver.findElement(by).sendKeys(value);
	}

	public static void setValue(String id, String value) throws AppiumException {
		check();
		try{
			getAndroidElement(By.id(AppDriver.APKInfo.getPackageName() + ":id/" + id)).sendKeys(value);
		} catch(AppiumException e){
			throw new AppiumException(e.getMessage() + AppDriver.APKInfo.getPackageName() + ":id/" + id);
		}
	}

	
	private static AndroidElement getAndroidElement(By by) throws AppiumException{
		AndroidElement ae = null;
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
	 * @param startx
	 *            起始x
	 * @param starty
	 * @param endx
	 * @param endy
	 * @throws AppiumException
	 */
	public static void swipe(int startx, int starty, int endx, int endy) throws AppiumException {
		check();
		driver.swipe(startx, starty, endx, endy, 1000);
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
			throw new AppiumException("文件异常");
		}
	}

	
	/**
	 * 保存截图
	 * @param bitMapPath
	 */
	public static String  screenshot(String bitMapPath) {
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
