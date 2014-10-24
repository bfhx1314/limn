package com.limn.driver;import java.io.ByteArrayOutputStream;import java.io.DataInputStream;import java.io.IOException;import java.io.InputStream;import java.net.MalformedURLException;import java.net.Socket;import java.net.URL;import java.net.UnknownHostException;import java.util.List;import java.util.concurrent.TimeUnit;import org.openqa.selenium.By;import org.openqa.selenium.JavascriptExecutor;import org.openqa.selenium.NoSuchElementException;import org.openqa.selenium.WebElement;import org.openqa.selenium.chrome.ChromeDriver;import org.openqa.selenium.firefox.FirefoxDriver;import org.openqa.selenium.htmlunit.HtmlUnitDriver;import org.openqa.selenium.ie.InternetExplorerDriver;import org.openqa.selenium.interactions.Actions;import org.openqa.selenium.remote.DesiredCapabilities;import org.openqa.selenium.remote.RemoteWebDriver;import com.limn.driver.exception.SeleniumFindException;import com.limn.tool.parameter.Parameter;import com.limn.tool.regexp.RegExp;import com.limn.tool.common.CallBat;import com.limn.tool.common.Common;import com.limn.tool.common.Print;public class Driver {	private static String testJS = null;	public static RemoteWebDriver driver = null;		public static Actions action = null;		public static HtmlUnitDriver htmlUnitDriver = null;		private static String URL = null;		private static String IP= null;		private static int TYPE = -1;		public static String remotePort = null;		private static DesiredCapabilities browserType = null;		private static int startType = 0;		private static boolean browserFlag = false;		private static boolean isBrowserStart = false;		private static int startBrowserType = 0;		protected static WebElement findWebElement = null;	protected static int hasThread = 0;	/**	 * @param 浏览器类型	 * @param 测试对应的浏览器地址	 */	public static void setDriver(int type, String url, String ip) {		IP = ip;		TYPE = type;		URL = url;//		htmlUnitDriver = new HtmlUnitDriver();//		setTestJs();//		startBrowser();	}//	/**//	 * //	 * @param 浏览器类型//	 * @param 测试对应的浏览器地址//	 * @param 测试脚本路径//	 *///	public static void setDriver(int type, String url, String testJSPath) {//		driver = getDriver(type, url);//		setTestJs(testJSPath);//		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);//	}		@SuppressWarnings("static-access")	private static void startBrowserInit(){			if(IP!=null){						remoteDriver();			switch (TYPE) {			case 1:				browserType	= new DesiredCapabilities().firefox();				break;			case 2:				browserType= new DesiredCapabilities().chrome();				break;			case 3:				break;			default:				System.out.println("不存在的类型");			}			//			try {				Print.log("远程端口:" + remotePort, 4);				//如果端口为0 是要报错的.				startType = 1;//				driver = new RemoteWebDriver(new URL("http://"+IP + ":" + remotePort +"/wd/hub"), browserType);//			} catch (MalformedURLException e) {//				e.printStackTrace();//			}		}else{			Print.log("正在启动浏览器.....请稍等", 1);			closeBrowserDriver();			switch (TYPE) {			case 1:				startType = 2;//				driver = new FirefoxDriver();					break;			case 2:				startType = 3;				CallBat.exec(Parameter.DFAULT_TEST_PATH + "/bin/chromedriver_x86.exe");				System.setProperty("webdriver.chrome.driver", Parameter.DEFAULT_BIN_PATH + "/chromedriver_x86.exe");//				driver = new ChromeDriver();				break;			case 3:				String IEDriver = null;				startType = 4;				if(Parameter.SYSTEMBIT.equals("64")){					IEDriver = "IEDriverServer_x64.exe";					}else{					IEDriver = "IEDriverServer_x86.exe";				}				CallBat.exec(Parameter.DEFAULT_BIN_PATH + "\\" + IEDriver);//				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();				browserType = DesiredCapabilities.internetExplorer();				browserType.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);//				WebDriver driver = new InternetExplorerDriver(ieCapabilities);				System.setProperty("webdriver.ie.driver", Parameter.DEFAULT_BIN_PATH + "\\" + IEDriver);//				driver = new InternetExplorerDriver(ieCapabilities);				break;			default:				System.out.println("不存在的类型");			}		}		try {			testJS = getTestJavaScript();		} catch (IOException e) {			// TODO Auto-generated catch block			e.printStackTrace();		}		browserFlag = true;	}		/**	 * 启动浏览器	 * @throws SeleniumFindException 	 */	public static void startBrowser() throws SeleniumFindException{		isBrowserStart = true;		if(!browserFlag || startBrowserType!=Parameter.BROWSERTYPE){			startBrowserInit();		}		try {			switch (startType) {			case 1:				driver = new RemoteWebDriver(new URL("http://" + IP + ":" + remotePort + "/wd/hub"), browserType);				break;			case 2:				driver = new FirefoxDriver();				break;			case 3:				driver = new ChromeDriver();				break;			case 4:				driver = new InternetExplorerDriver(browserType);				break;			default:				Print.log("不存在的浏览器类型", 2);				break;			}		} catch (MalformedURLException e) {			throw new SeleniumFindException("启动浏览器失败");//			Print.log("启动浏览器失败", 2);//			return;		}		// 异步脚本的超时时间		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);		// 定位对象时超时的时间		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);		// 页面加载超时时间		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);				//		maximise(); // 最大化浏览器  使用下面的方法		driver.manage().window().maximize();				driver.get(URL);				action = new Actions(driver);//		selenium = new WebDriverBackedSelenium(driver, URL);//		//		selenium.setTimeout("7200000");//		selenium.windowFocus();		Print.log("浏览器启动成功", 1);	}		/**	 * 关闭最后的当前打开的窗。（关闭单个）	 */	public static void closeBrowser(){		isBrowserStart = false;		if(driver!=null){			driver.close();		}	}		/**	 * 获取浏览器	 */	public static String getBrowserName(){		String browserName = "";		switch (TYPE){		case 1:			browserName = "firefox";			break;		case 2:			browserName = "chrome";			break;		case 3:			browserName = "iexplore";			break;		default :			Print.log("没有此类型的浏览器：" + TYPE, 2);		}		return browserName;	}	/**	 * 运行脚本，将会执行测试脚本以及commd传进来的内容	 * 	 * @param 运行内容	 * @throws SeleniumFindException 	 */	public static Object runScript(String commd) throws SeleniumFindException {		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		return ((JavascriptExecutor) driver).executeScript(testJS + commd);	}			public static Object runScript(String commd, Object object) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		return ((JavascriptExecutor) driver).executeScript(testJS + commd, object);	}		public static void highLightWebElement(WebElement webElement) throws SeleniumFindException {		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		if(null != webElement){			driver.executeScript("arguments[0].style.border = \"2px solid yellow\"", webElement);		}	}		public static void cancelHighLightWebElement(WebElement webElement) throws SeleniumFindException {		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		driver.executeScript("arguments[0].style.border = \"\"", webElement);	}	/**	 * 对于文件内容	 * 	 * @param 文件路径	 * @return 文件内容	 * @throws IOException	 */	private static String getTestJavaScript() throws IOException {		InputStream is = Driver.class.getResourceAsStream("JavaScript.js"); 		byte[] buf = new byte[1024];		ByteArrayOutputStream baos = new ByteArrayOutputStream();		int len;		while ((len = is.read(buf)) != -1) {			baos.write(buf, 0, len);		}		is.close();		return baos.toString();	}	/**	 * 返回元素	 * @param locator	 * @return 返回元素	 * @throws SeleniumFindException	 */	public static WebElement getWebElement(By locator) throws SeleniumFindException{		WebElement web = isExist(null,locator);				if(null == web){			throw new SeleniumFindException("无法定位元素xpath:" + locator.toString());		}else{			return web;		}	}		/**	 * 返回元素	 * @param locator	 * @param web	 * @return 返回元素	 * @throws SeleniumFindException	 */	public static WebElement getWebElement(WebElement web, By locator) throws SeleniumFindException{		WebElement webRes = isExist(web,locator);		if(null == webRes){			throw new SeleniumFindException("无法定位元素xpath:" + locator.toString());		}else{			return webRes;		}	}		/**	 * 返回元素	 * @param xpath	 * @return	 * @throws SeleniumFindException 元素没有定位	 */	public static WebElement getWebElementByXPath(String xpath) throws SeleniumFindException{		WebElement web = isExistByXPath(xpath);		if(null == web){			throw new SeleniumFindException("无法定位元素xpath:" + xpath);		}else{			return web;		}	}			/**	 * 返回元素	 * @param xpath	 * @return	 * @throws SeleniumFindException 元素没有定位	 */	public static List<WebElement> getWebElementsByXPath(String xpath) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		return driver.findElementsByXPath(xpath);	}			public static void setText(By locator, String value) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		if(isWebElementExist(locator) && driver.findElement(locator).isEnabled()){			driver.findElement(locator).clear();			driver.findElement(locator).sendKeys(value);		}else{			Print.log("没有定位到元素或者元素未启用:" + locator.toString(), 3);			throw new SeleniumFindException("没有定位到元素或者元素未启用" +  locator.toString());		}	}		public static void setText(WebElement web, String value) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		if(null != web && web.isEnabled()){			web.clear();			web.sendKeys(value);		}else{			Print.log("元素为null或者元素未启用", 3);			throw new SeleniumFindException("元素为null或者元素未启用");		}	}			/**	 * 判断元素是否存在	 * @param selector 元素的定位By.	 * @return	 * @throws SeleniumFindException 	 */	public static boolean isWebElementExist(By selector) throws SeleniumFindException{		if(null==isExist(null,selector)){			return false;		}else{			return true;		}	}		/**	 * 判断元素是否存在	 * @param selector 元素的定位By.	 * @return	 * @throws SeleniumFindException 	 */	public static boolean isWebElementExist(String xpath) throws SeleniumFindException{		if(null==isExistByXPath(xpath)){			return false;		}else{			return true;		}	}	/**	 * 判断元素是否存在	 * @param selector	 * @return 存在返回,不存在返回null	 * @throws SeleniumFindException 	 */	private static WebElement isExist(WebElement web, By selector) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}		if(null==web){			try {				return driver.findElement(selector);			} catch (NoSuchElementException e) {				return null;			}		}else{			try {				return web.findElement(selector);			} catch (NoSuchElementException e) {				return null;			}		}	}		/**	 * 判断元素是否存在	 * @param xpath	 * @return 存在返回,不存在返回null	 * @throws SeleniumFindException 	 */	private static WebElement isExistByXPath(String xpath) throws SeleniumFindException{		if(!isBrowserStart){			throw new SeleniumFindException("浏览器未启动");		}				try {			return driver.findElementByXPath(xpath);		} catch (NoSuchElementException e) {			return null;		}	}			/**	 * 等到元素出现	 * @param selector 元素的定位By.	 * @param timeOut int 等待的时间(单位为秒)	 * @throws SeleniumFindException 	 */	public static boolean waitForElementPresent(By selector, int timeOut) throws SeleniumFindException {		timeOut = timeOut * 5;		while(timeOut > 0) {			timeOut--;			if (Driver.isWebElementExist(selector)) {				return true;			} else {				Common.wait(200);			}		}		return false;	}			/**	 * 关闭所有浏览器驱动	 */	public static void closeBrowserDriver(){		CallBat.closeProcess("chromedriver_x86.exe");		CallBat.closeProcess("IEDriverServer_x64.exe");		CallBat.closeProcess("IEDriverServer_x86.exe");	}			private static void remoteDriver(){		CallBat.closeProcessByTitle("ServerHub");		CallBat.returnExecByNewWindows("ServerHub" ," java -jar "+ Parameter.DEFAULT_BIN_PATH +"\\selenium-server-standalone.jar -role hub");		Print.log("正在启动本地电脑Hub.....请稍等", 1);		try {			Thread.sleep(3000);		} catch (InterruptedException e) {			e.printStackTrace();		}		new Thread(new RemoteConnect(IP)).start();		Print.log("正在启动远程电脑Node.....请稍等", 1);		try {			Thread.sleep(3000);		} catch (InterruptedException e) {			e.printStackTrace();		}	}			public static WebElement getWebElementBylocator(String locator){		WebElement web = null;		if (RegExp.findCharacters(locator, "^/")) {						String xpath = locator;						try {				web = Driver.getWebElementByXPath(xpath);			} catch (SeleniumFindException e) {			}			findWebElement = web;		} else {			findWebElement = null;			new Thread(new FindWebElements(By.id(locator))).start();			new Thread(new FindWebElements(By.name(locator))).start();			hasThread = 2;			while(true){				if(null!=findWebElement){					break;				}				if(hasThread==0){					Print.log("未定位WebElement locator:" + locator, 2);					break;					}								Common.wait(500);			}		}		return findWebElement;	}		}class RemoteConnect implements Runnable{	private Socket sc = null;	private DataInputStream dis = null;//	private DataOutputStream dos = null;	private boolean flag = true;	public RemoteConnect(String IP){		try {			sc = new Socket(IP,Parameter.REMOTEPORT);			dis = new DataInputStream(sc.getInputStream());			Driver.remotePort = dis.readUTF();		} catch (UnknownHostException e) {			e.printStackTrace();		} catch (IOException e) {			e.printStackTrace();		}	}		public void run() {				while(flag){			try {				sc.sendUrgentData(0xFF);				Thread.sleep(10000);			} catch (InterruptedException e) {				e.printStackTrace();			} catch (IOException e) {				flag = false;			}		}	}}/** *  *  * @author limn * */class FindWebElements implements Runnable{		private By locator = null;		public FindWebElements(By locator){		this.locator = locator;	}		public void run() {		try {			WebElement web = Driver.getWebElement(locator);			if (null != web) {				Driver.findWebElement = web;			}		} catch (SeleniumFindException e) {			e.printStackTrace();		} finally {			Driver.hasThread--;		}	}}