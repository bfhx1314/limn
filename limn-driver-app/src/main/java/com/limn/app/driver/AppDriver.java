package com.limn.app.driver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.limn.app.driver.bean.AppiumStartParameterBean;
import com.limn.app.driver.bean.SlidingPathBean;
import com.limn.app.driver.common.AppDriverBaseUtil;
import com.limn.tool.common.*;
import com.limn.tool.random.RandomData;
import io.appium.java_client.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.limn.app.driver.exception.AppiumException;
import com.limn.tool.app.ApkInfo;
import com.limn.tool.app.AppPackageInfo;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class AppDriver {

	public AppiumDriver driver = null;

	public int HEIGHT = 0;
	public int WIDTH = 0;

	private String appFilePath = null;

	private ApkInfo apkInfo = null;

	public String AppType = "";

	private long waitTime = 16*1000;

	private AppiumStartParameterBean ASPB = new AppiumStartParameterBean();


	/**
	 * 默认的监听地址和
	 * @param filePath
	 * @throws AppiumException
	 */
	public void init(String filePath) throws AppiumException {
		ASPB.setAddress("127.0.0.1");
		ASPB.setPort("4723");
		appFilePath = filePath;
		init();
	}

	public void init(String filePath, AppiumStartParameterBean aspb) throws AppiumException {
		ASPB = aspb;
		appFilePath = filePath;
		init();
	}

	public void initAndRunAppiumServer(String filePath, AppiumStartParameterBean aspb) throws AppiumException {
		ASPB = aspb;
		appFilePath = filePath;

		String appiumPath = Variable.getExpressionValue("appium.path");
		if(BaseUntil.isNotEmpty(appiumPath)){
			CallBat.exec("node " + appiumPath + " " + ASPB.toString());
			Common.wait(3000);
		}
		init();
	}


	public void runScript(String driverCommand, Map<String, ?> parameters) {
		if (null == driver) {
			return;
		}
		driver.execute(driverCommand, parameters);
	}

	private void init() throws AppiumException {

		//如果已经初始化  返回
//		if (null != driver) {
//			return;
//		}
		BaseToolParameter.getPrintThreadLocal().log("ASPB : " + ASPB.toString(),0);
		File appFile = new File(appFilePath);

		if(FileUtil.getFileType(appFilePath).equalsIgnoreCase("ipa")){
			AppType = "IOS";
		}else if(FileUtil.getFileType(appFilePath).equalsIgnoreCase("apk")){
			AppType = "Android";
		}else{
			AppType = "";
			throw new AppiumException(appFile.getAbsolutePath() + " APP文件类型错误:" + FileUtil.getFileType(appFilePath));
		}

		if (!appFile.exists()) {
			throw new AppiumException(appFile.getAbsolutePath() + " 文件不存在");
		}

		URL sauceUrl;
		try {
			sauceUrl = new URL("http://" + ASPB.getAddress() + ":" + ASPB.getPort() + "/wd/hub");
		} catch (MalformedURLException e) {
			throw new AppiumException("driver hub无法连接");
		} catch (SessionNotCreatedException e) {
			throw new AppiumException("请重新启动 Driver HUB,A new session could not be created");
		} catch (UnreachableBrowserException e) {
			throw new AppiumException("Appium未连接 Connect to " + ASPB.getAddress());
		}

		DesiredCapabilities dcb = new DesiredCapabilities();
		AppPackageInfo apki = new AppPackageInfo();
		if (AppType.equalsIgnoreCase("Android")) {
			try {
				apkInfo = apki.resolveByAndroid(appFile);
			} catch (Exception e1) {
				throw new  AppiumException("Appium APK包文件异常:" + appFile.getAbsolutePath() + "\n" + e1.getMessage());
			}

			LinkedHashMap<String, String> capability = Variable.getPrivateVariableLocal("capability.properties");

//			dcb.setCapability("deviceName", apkInfo.getApplicationLable()); // 后期增加配置
			dcb.setCapability("appPackage", apkInfo.getPackageName());
			dcb.setCapability("app", appFile.getAbsolutePath());

			BaseToolParameter.getPrintThreadLocal().log("----设置Android基础属性----",0);
			for(String key : capability.keySet()){
				dcb.setCapability(key, capability.get(key));
				BaseToolParameter.getPrintThreadLocal().log("AndroidDesiredCapabilities设置属性 [" + key + " : " + capability.get(key) + "]" , 0);
			}
			BaseToolParameter.getPrintThreadLocal().log("----设置完毕----",0);
// 			Didn't get a new command in 600 secs, shutting down...
//			dcb.setCapability("newCommandTimeout", 600);
//			dcb.setCapability("noReset", "true");
//			dcb.setCapability("paltformVersion", paltformVersion); // 后期增加配置
//			dcb.setCapability("unicodeKeyboard", "true");
//			dcb.setCapability("resetKeyboard", "true");
//			dcb.setCapability("appWaitActivity", "com.jifen.qukan.view.activity.MainActivity");



			try{
				BaseToolParameter.getPrintThreadLocal().logNoLine("App启动中.",0);
				BaseToolParameter.getPrintThreadLocal().printContinueLogThread(".",0,500);
				driver = new AndroidDriver<AndroidElement>(sauceUrl,dcb);


			}catch(SessionNotCreatedException e){
				throw new AppiumException("请重新启动 Driver HUB,A new session could not be created");
			}catch (WebDriverException e){
				throw new AppiumException("找不到Android机器 Could not find a connected Android device");
			}finally {
				BaseToolParameter.getPrintThreadLocal().stopContinueLogThread();
			}

			BaseToolParameter.getPrintThreadLocal().log("App启动成功",0);


		} else if (AppType.equalsIgnoreCase("IOS")) {
			dcb.setCapability("appium-version", "1.0");
			dcb.setCapability("platformName", "iOS");
			dcb.setCapability("platformVersion", "8.4");
			dcb.setCapability("deviceName", "iPhone 6 Plus");
			dcb.setCapability("app", appFile.getAbsolutePath());
			dcb.setCapability("newCommandTimeout", 600);
			driver = new IOSDriver<IOSElement>(sauceUrl, dcb);

		}

		HEIGHT = driver.manage().window().getSize().getHeight();
		WIDTH = driver.manage().window().getSize().getWidth();
		Common.wait(5000);

	}


	/**
	 *
	 * @param id
	 *            or xpath
	 * @param value
	 *            支持变量
	 * @throws AppiumException
	 */
	public void setValue(String id, String value) throws AppiumException {
		check();

		// 录入前先获取变量值
		if (RegExp.findCharacters(value, "\\{.*\\}")) {
			String var = RegExp.filterString(value, "{}");
			value = Variable.getExpressionValue(var);
		}

		try {
			getAndroidElement(id).sendKeys(value);
		} catch (AppiumException e) {
			throw new AppiumException(e.getMessage() + apkInfo.getPackageName() + ":id/" + id);
		}
	}

	private AndroidElement getAndroidElement(String key) throws AppiumException {

		long startTime = DateFormat.getCurrentTimeMillisByLong();
		long endTime = 0l;
		// 判断元素是否存在多个
		List<AndroidElement> listEle = null;

		int status = 0;
		do{

			status = 0;
			if (RegExp.findCharacters(key, "^/")) {
				listEle = driver.findElementsByXPath(key);
			} else {

				if (!RegExp.findCharacters(key, "^" + apkInfo.getPackageName() + ":id/")) {
					key = apkInfo.getPackageName() + ":id/" + key;
				}
				listEle = driver.findElements(By.id(key));
			}

			if (null == listEle) {
//				throw new AppiumException("不存在:");
				status = 1;
			} else if (listEle.size() > 1) {
//				throw new AppiumException("存在多个此元素:");
				status = 2;
			} else if (listEle.size() == 0){
//				throw new AppiumException("界面中不存在此元素");;
				status = 3;
			} else{
				BaseToolParameter.getPrintThreadLocal().log("已定位到元素:" + key, 1);
			}
			endTime = DateFormat.getCurrentTimeMillisByLong();

		}while(endTime - startTime < waitTime && status != 0);

		switch (status){
		case 1:


			throw new AppiumException("不存在:");
		case 2:
			throw new AppiumException("存在多个此元素:");
		case 3:
			throw new AppiumException("界面中不存在此元素");
		default:
			break;
		}

		return listEle.get(0);

	}

	// /**
	// * 点击
	// *
	// * @param by
	// * @throws AppiumException
	// */
	// public static void click(By by) throws AppiumException {
	// check();
	// getAndroidElement(by).click();
	// }

	/**
	 *
	 * @param id
	 *            or xpath
	 * @throws AppiumException
	 */
	public void click(String id) throws AppiumException {
		check();
		try {
			getAndroidElement(id).click();
		} catch (AppiumException e) {
			throw new AppiumException(e.getMessage() + apkInfo.getPackageName() + ":id/" + id);
		}
	}

	/**
	 * 滑动屏幕
	 *
	 * @param startX
	 *            起始x
	 * @param startY
	 * @param endX
	 * @param endY
	 * @throws AppiumException
	 */
	public void swipe(int startX, int startY, int endX, int endY, Boolean is_RuleSlide) throws AppiumException {

		SlidingPathBean spb = new SlidingPathBean();
		spb.setStartX(startX);
		spb.setStartY(startY);
		spb.setEndX(endX);
		spb.setEndY(endY);

		if(is_RuleSlide) {

			int rNum = RandomData.getNumberRange(2, 5);

			ArrayList<SlidingPathBean> spbs = AppDriverBaseUtil.getRandonSlidingPaths(spb,rNum);

			for(SlidingPathBean childspb : spbs){
				swipe(childspb);
			}
		}else{
			swipe(spb);
		}
	}


	public void swipe(SlidingPathBean spb) throws AppiumException {

		check();
		BaseToolParameter.getPrintThreadLocal().log("滑动坐标-> startX : " + spb.getStartX() + " startY : " + spb.getStartY() + " endX : " + spb.getEndX() + " endY : " + spb.getEndY() ,0 );
		TouchAction action = new TouchAction(driver);
		int value = RandomData.getNumberRange(10,300);
		action.longPress(spb.getStartX(),spb.getStartY(),Duration.ofMillis(value)).moveTo(spb.getEndX(),spb.getEndY()).release();
		action.perform();
	}



	/**
	 * 滑动屏幕
	 *
	 * @param startX
	 *            起始x
	 * @param startY
	 * @param endX
	 * @param endY
	 * @throws AppiumException
	 */
	public void swipe(int startX, int startY, int endX, int endY) throws AppiumException {

		check();
		BaseToolParameter.getPrintThreadLocal().log("滑动坐标-> startX : " + startX + " startY : " + startY + " endX : " + endX + " endY : " + endY ,0 );
		TouchAction action = new TouchAction(driver);
		int value = RandomData.getNumberRange(100,2000);
		action.longPress(startX,startY,Duration.ofMillis(value)).moveTo(endX,endY).release();
		action.perform();
	}

	/**
	 * 长按控件
	 *
	 * @param id
	 *            or xpath
	 * @param time
	 * @throws AppiumException
	 */
	public void touchAction(String id, int time) throws AppiumException {
		if (time == 0) {
			time = 1000;
		}
		check();

		try {
			AndroidElement ae = getAndroidElement(id);

			TouchAction action = new TouchAction(driver);
			action.longPress(ae,Duration.ofMillis(time));

//			action.longPress(ae).waitAction(time).release().perform();
		} catch (AppiumException e) {
			throw new AppiumException(e.getMessage() + apkInfo.getPackageName() + ":id/" + id);
		}
	}

	// /**
	// * 长按控件
	// * @param by
	// * @param time
	// * @throws AppiumException
	// */
	// public static void touchAction(By by, int time) throws AppiumException{
	// if(time == 0){
	// time = 1000;
	// }
	// check();
	// try{
	// AndroidElement ae = getAndroidElement(by);
	// TouchAction action = new TouchAction(driver);
	// action.longPress(ae).waitAction(time).release().perform();
	// } catch (AppiumException e) {
	// throw new AppiumException(e.getMessage() + by.toString());
	// }
	// }

	private void check() throws AppiumException {
		if (driver == null) {
			throw new AppiumException("drver 不存在");
		} else if (apkInfo == null && AppType.equalsIgnoreCase("Android")) {
			throw new AppiumException("app信息无法获取");
		}
	}

	public String getPackageName() throws AppiumException {
		check();
		return apkInfo.getPackageName();
	}

	/**
	 * 保存截图
	 *
	 * @param bitMapPath
	 */
	public String screenshot(String bitMapPath) {
		try {
			check();
		} catch (AppiumException e1) {
			BaseToolParameter.getPrintThreadLocal().log("截图失败", 2);
		}
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(bitMapPath));
			BaseToolParameter.getPrintThreadLocal().debugLog("save snapshot path is:" + bitMapPath, 1);
		} catch (IOException e) {
			BaseToolParameter.getPrintThreadLocal().log("save snapshot error! path is:" + bitMapPath, 2);
		}
		return bitMapPath;
	}

}

