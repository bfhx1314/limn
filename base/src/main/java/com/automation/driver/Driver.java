package com.automation.driver;

import com.automation.exception.ExcelException;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.RunException;
import com.automation.frame.panel.LogPanel;
import com.automation.keyword.ExcelType;
import com.automation.report.Log;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.StringUtil;
import com.automation.tool.util.Print;
import com.automation.tool.util.RegExp;
import com.automation.tool.util.Screenshot;
import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by snow.zhang on 2015/10/29.
 */
public class Driver {

    /**
     * webDriver
     */
    private static WebDriver webDriver;
    /**
     * js执行器
     */
    private static JavascriptExecutor javascriptExecutor;
    /**
     * js代码
     */
    private static String jsScript;

    /**
     * 浏览器类型：1:firefox，2：chrome，3：ie
     */
    public static int browserType = 2;
    /**
     * url地址
     */
    public static String url;
    /**
     * 实际结果
     */
    private static Map<String, String> returnData = new HashMap<>();

    /**
     * 高亮的WebElement列表
     */
    private static List<String> listHighLightWebElementId = new ArrayList<>();

    private static List<WebElement> listHighLightWebElement = new ArrayList<>();

    /**
     * 启动浏览器
     * @throws RunException 运行时错误
     */
    public static void openBrowser() throws RunException {
        printLog("正在启动浏览器，并打开"+url+"，请稍等。。。", 1);
        switch (getBrowserType()){
            case 1:
                webDriver = new FirefoxDriver();
                break;
            case 2:
                if(Parameters.OS_NAME.contains("Mac")){
                    System.setProperty("webdriver.chrome.driver", Parameters.chromeDriverMacPath);
                }else {
                    System.setProperty("webdriver.chrome.driver", Parameters.chromeDriverPath);
                }
                File downloadDir = new File(Parameters.savePath);
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> preferences = new HashMap<String, Object>();
                preferences.put("download.default_directory", downloadDir.getAbsolutePath());
                preferences.put("download.prompt_for_download", false);
                chromeOptions.setExperimentalOption("prefs", preferences);
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case 3:
                throw new RunException("启动失败，暂时没有IE Driver。");
        }
//        webDriver.manage().window().maximize();
//        selenium = new WebDriverBackedSelenium(webDriver, url);
        // 异步脚本的超时时间
        webDriver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        // 定位对象时超时的时间
        webDriver.manage().timeouts().implicitlyWait(Integer.parseInt(Parameters.implicitlyWait), TimeUnit.SECONDS);
        // 页面加载超时时间
        webDriver.manage().timeouts().pageLoadTimeout(Integer.parseInt(Parameters.pageLoadTimeout), TimeUnit.SECONDS);
        // 清除cookie
        webDriver.manage().deleteAllCookies();
        setJsScript(getTestJavaScript());
        javascriptExecutor = (JavascriptExecutor) webDriver;
        if (Parameters.OS_NAME.contains("Mac")){
            maxBrowserJs();
        }else {
            maxBrowser();
        }
        webDriver.get(getUrl());
        printLog("启动完成。", 1);
    }

    //使窗口最大化函数
    public static void maxBrowserJs() {
//        driver.manage().window().maximize();
		javascriptExecutor.executeScript("window.open('','testwindow','width=400,height=200')");
	    webDriver.close();
        webDriver.switchTo().window("testwindow");
        javascriptExecutor.executeScript("window.moveTo(0,0);");
	   /*1280和1024分别为窗口的宽和高，可以用下面的代码得到
	            screenDims = Toolkit.getDefaultToolkit().getScreenSize();
	            width = (int) screenDims.getWidth();
	            height = (int) screenDims.getHeight();
        */
////	    js.executeScript("window.resizeTo(1024,768);");
        javascriptExecutor.executeScript("window.resizeTo(1366,768);");

    }

    /**
     * 输入
     * @param xpath xpath
     * @param value 参数
     * @throws RunException 运行时错误
     */
    public static void type(String xpath, String value) throws RunException {
        operate(xpath, value, OperationType.TYPE);
    }

    public static void click(String xpath) throws RunException {
        operate(xpath,"",OperationType.CLICK);
    }

    public static void selectOption(String xpath, String value) throws RunException {
        operate(xpath,value,OperationType.SELECT);
    }
    /**
     * 根据操作类型执行。默认等待10秒
     * @param xpath xpath
     * @param value 参数
     * @param type 操作类型
     * @throws RunException 运行时错误
     */
    public static void operate(String xpath, String value, String type) throws RunException {
        operate(xpath,value,type,10);
    }

    /**
     * 根据操作类型执行。
     * @param xpath xpath
     * @param value 参数
     * @param operateType 操作类型
     * @param intSecond 等待时间
     * @throws RunException 运行时错误
     */
    public static void operate(String xpath, String value, String operateType, int intSecond) throws RunException {
        WebElement webElement = getElement(xpath, intSecond);
        switch (operateType){
            case OperationType.TYPE:
                String tagName = webElement.getTagName();
                switch (tagName.toLowerCase()){
//                        case "input":

//                            break;
                    case OperationType.SELECT:
                        Select select = new Select(webElement);
                        select.selectByVisibleText(value);
                        break;
                    default:
                        webElement.clear();
                        webElement.sendKeys(value);
                }
                break;
            case OperationType.CLICK:
                webElement.click();
                if(!isAlertOpen()) {
                    waitForPageLoad();
                }
                break;
        }
        printLog("操作动作：" + operateType + "，已完成。");
    }

    public static Function<WebDriver, Boolean> isPageLoaded() {
        return new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                javascriptExecutor = (JavascriptExecutor) driver;
                return javascriptExecutor.executeScript("return document.readyState").equals("complete");
            }
        };
    }

    public static boolean waitForPageLoad() {
        int clickWaitForPageLoad = 15;
        try {
            clickWaitForPageLoad = Integer.parseInt(Parameters.CLICK_WAIT_FOR_PAGE_LOAD);
        }catch (Exception e){
            printLog("clickWaitForPageLoad配置错误，默认为15s ", 3);
        }
        WebDriverWait wait = new WebDriverWait(webDriver, clickWaitForPageLoad);
        return wait.until(isPageLoaded());
    }

    /**
     * Hover on the page element
     *
     * @param xpath the element's xpath
     */
    public static void mouseOver(String xpath) throws RunException {
        Actions actions = new Actions(webDriver);
        WebElement webElement = getElement(xpath);
        actions.moveToElement(webElement);
        actions.build().perform();
        // Then hover
//            WebElement we = webdriver.findElement(By.xpath(xpath));
        try {
            Robot rb = new Robot();
            rb.mouseMove(0, 0);
            Actions builder = new Actions(webDriver);
            builder.moveToElement(webElement).build().perform();
        } catch (Exception e) {
            throw new RunException(e);
//            e.printStackTrace();
//            handleFailure("Failed to mouseover " + locator);
        }
        printLog("操作动作：MouseOver，已完成。");
//        logger.info(getLogTime() + ": " + "Mouseover " + locator);
//        Reporter.log(getLogTime() + ": " + "Mouseover " + locator + "</br>");
    }

    /**
     * 切换浏览器
     * @param index 从1开始
     */
    public static void switchBrowser(int index) throws RunException {
        operateBrowser(index, OperationType.SWITCH_BROWSER);
    }

    /**
     * 关闭第几个浏览器
     * @param index 从1开始
     */
    public static void closeBrowser(int index) throws RunException {
        operateBrowser(index, OperationType.CLOSE_BROWSER);
    }

    /**
     * 操作浏览器
     * @param index 从1开始
     * @param operate 操作描述
     * @throws RunException 运行时错误
     */
    public static void operateBrowser(int index, String operate) throws RunException {
        //得到所有窗口的句柄
        Set<String> handles = webDriver.getWindowHandles();
        int handlesSize = handles.size();
        if (handlesSize > 0) {
            printLog("一共存在 "+ handlesSize +" 个页面。");
            try{
                int i = 1;
                for(Iterator<?> ite = handles.iterator();ite.hasNext();){
                    String iter = (String) ite.next();
                    if (i == index){
                        switch (operate.toLowerCase()){
                            case OperationType.SWITCH_BROWSER:
                                webDriver.switchTo().window(iter);
                                printLog("切换到第 " + index + " 个页面。");
                                break;
                            case OperationType.CLOSE_BROWSER:
                                webDriver.switchTo().window(iter);
                                printLog("关闭第 " + index + " 个页面。");
                                webDriver.close();
                                handles.remove(iter);
                                if (handles.iterator().hasNext()){
                                    webDriver.switchTo().window(handles.iterator().next());
                                    break;
                                }
                        }
                        break;
                    }else{
                        i++;
                    }
                }
            }catch(Exception e){
                throw new RunException(e);
            }
        }
        printLog("操作动作："+operate+"，已完成。");
    }

    /**
     * 切换frame
     * @param index 从1开始
     */
    public static void swichToFrame(int index) {
        webDriver.switchTo().frame(index-1);
        printLog("切换到第" + index + "个iframe。");
    }

    /**
     * 切回初始frame
     */
    public static void swichToDefaultFrame() {
        webDriver.switchTo().defaultContent();
        printLog("退出iframe。");
    }

    /**
     * 获取WebElement
     * @param xpath xpath
     * @param waitSecond 等待时间
     * @return WebElement
     */
    public static WebElement getElement(String xpath, int waitSecond) throws RunException {
        WebElement webElement = null;
        if (xpath.startsWith("/")) {
            for (int i = 0; i < waitSecond; i++) {
                try {
                    webElement = webDriver.findElement(By.xpath(xpath));
                }catch(Exception e) {
                    if (e.getMessage().contains("chrome not reachable")){
                        throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
                    }else if(i==(waitSecond-1)) {
                        Print.logRed(e.getMessage());
                    }
                }finally {
                    if (null == webElement) {
                        pause(1);
                    } else {
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < waitSecond; i++) {
                try {
                    webElement = webDriver.findElement(By.id(xpath));
                }catch(Exception e) {
                    if (e.getMessage().contains("chrome not reachable")){
                        throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
                    }else if(i==(waitSecond-1)) {
                        Print.logRed(e.getMessage());
                    }
                }finally {
                    if (null == webElement) {
                        pause(1);
                    } else {
                        break;
                    }
                }
            }
        }

        if (null == webElement){
            throw new RunException(ExceptionInfo.get(RunException.WEBELEMENT_IS_NULL,xpath));
        }
        return webElement;
    }

    /**
     * 获取WebElement 默认等待10s
     * @param xpath xpath
     * @return WebElement
     */
    public static WebElement getElement(String xpath) throws RunException {
        return getElement(xpath,10);
    }

    /**
     * 获取webelement text()
     * @param webElement 元素对象
     * @return String
     */
    public static String getValueByTag(WebElement webElement) throws RunException {
        if ("input".equals(webElement.getTagName())) {
            return webElement.getAttribute("value");
        } else if ("select".equals(webElement.getTagName())) {
            //获取id
            String newId = "tempId_" + System.currentTimeMillis();
            if(StringUtil.isEmpty(webElement.getAttribute("id"))) {
                setAttribute(webElement, "id", newId);
            }
            return (String) javascriptExecutor.executeScript("return $('#" + webElement.getAttribute("id") + " option:selected').text();");
        } else {
            return webElement.getText();
        }
    }

    /**
     * 设置webElement DOM属性
     * @param webElement 元素
     * @param attribute 属性名
     * @param value 属性值
     */
    public static void setAttribute(WebElement webElement, String attribute, String value){
        javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", webElement, attribute, value);
    }

    /**
     * 获取webElement的指定属性值
     * @param webElement 元素对象
     * @param attribute 属性名
     * @return String
     * @throws RunException
     */
    public static String getAttributeValue(WebElement webElement, String attribute) throws RunException {
        String attributeValue;
        if (!StringUtil.isEmpty(attribute)) {
//            Object oStr = javascriptExecutor.executeScript("return arguments[0].getAttribute(arguments[1])", webElement, attribute);
            Object oStr = javascriptExecutor.executeScript("return arguments[0]."+attribute, webElement);
            attributeValue = null == oStr ? "null，没有获取到页面值" : oStr.toString();
        } else {
            attributeValue = webElement.getAttribute("textContent");
        }
        return attributeValue;
    }

    /**
     * 获取webElement的HTML上的指定属性值
     * @param webElement 元素对象
     * @param attribute 属性名
     * @return String
     * @throws RunException
     */
    public static String getHtmlAttributeValue(WebElement webElement, String attribute) throws RunException {
        String attributeValue;
        if (!StringUtil.isEmpty(attribute)) {
            Object oStr = javascriptExecutor.executeScript("return arguments[0].getAttribute(arguments[1])", webElement, attribute);
//            Object oStr = javascriptExecutor.executeScript("return arguments[0]."+attribute, webElement);
            attributeValue = null == oStr ? "null，没有获取到页面值" : oStr.toString();
        } else {
            attributeValue = webElement.getAttribute("textContent");
        }
        return attributeValue;
    }

    /**
     * 验证预期结果和实际结果
     * @param expectValue 预期结果
     * @param actualValue 实际结果
     * @param notice 信息
     * @param webElement 验证对象
     * @return true||false
     */
    public static boolean checkPoint(String expectValue, String actualValue, String notice, WebElement webElement) {
        boolean result;
//        printLog("预期值：" + expectValue + "  实际值：" + actualValue + "\r\n");
        if ((expectValue == null && actualValue == null) || (expectValue != null && actualValue.equals(expectValue))) { // strExpect.equals(strActual)
            printLog(notice + "，预期值：" + expectValue + "  实际值：" + actualValue + "，检查点成功！",1);
            result = true;
        } else {
            printLog(notice + "，预期值：" + expectValue + "  实际值：" + actualValue + "，检查点失败！",2);
//            captureAndLog(notice + "，预期值：" + expectValue + "  实际值：" + actualValue + "，检查点失败！", webElement);
            result = false;
        }
        setReturnDataMap(result,actualValue);
        return result;
    }

    private static void setReturnDataMap(boolean result, String actualValue) {
        returnData.put(ExcelType.ACTUAL,actualValue);
        returnData.put(ExcelType.ISPASS,result+"".toUpperCase());
    }

    /**
     * 模糊验证预期结果和实际结果
     * @param expectValue 预期结果，可以是正则表达式
     * @param actualValue 实际结果
     * @param notice 信息
     * @param webElement 验证对象
     * @return true||false
     */
    public static boolean checkPointMatch(String expectValue, String actualValue, String notice, WebElement webElement) {
        boolean result;
//        printLog("模糊匹配：" + notice + "表达式：" + expectValue + "  实际值：" + actualValue + "\r\n");
        if ((expectValue == null && actualValue == null) || (expectValue != null && RegExp.matchRegExp(actualValue, expectValue))) { // strExpect.equals(strActual)
            printLog("模糊匹配：" + notice + " 表达式：" + expectValue + "  实际值：" + actualValue + "，检查点成功", 1);
            result = true;
        } else {
            printLog("模糊匹配：" + notice + " 表达式：" + expectValue + "  实际值：" + actualValue + "，检查点失败", 2);
//            captureAndLog("模糊匹配：" + notice + " 表达式：" + expectValue + "  实际值：" + actualValue + "，检查点失败", webElement);
            result = false;
        }
        setReturnDataMap(result,actualValue);
        return result;
    }

    /**
     * 验证表格单元格
     * @param tableXPath table元素的xpath
     * @param startCol 开始验证的列
     * @param endCol 结束验证的列
     * @param pattern 正则表达式
     * @return true||false
     */
    public static boolean checkTableCellValue(String tableXPath, int startCol, int endCol, String pattern) throws RunException {
        boolean allResult = true;
        WebElement tableE = getElement(tableXPath);
        List<WebElement> tableTrs = tableE.findElements(By.xpath(".//tr"));
//			Iterator<WebElement> itTr = tableTrs.iterator();
        for (int i = 1; i < tableTrs.size(); i++) {
            WebElement tableTr = tableTrs.get(i);
            List<WebElement> tableTds = tableTr.findElements(By.xpath(".//td"));
//	        	Iterator<WebElement> itTd = tableTds.iterator();
            for (int j = (startCol - 1); i < endCol; i++) {
                WebElement tableTd = tableTds.get(j);
                String autValueT = tableTd.getText();
                if (!RegExp.matchRegExp(autValueT, pattern)) {
                    allResult = false;
                    printLog("列表中第" + i + "行 第" + j + "列的值超出： “" + pattern + "”；", 2);
//                    captureAndLog("列表中第" + i + "行 第" + j + "列的值超出： “" + pattern + "”；", tableTd);
                }
            }
        }
        if (allResult) {
            printLog("列表中的值验证全部通过，匹配范围：" + pattern,1);
        } else {
            printLog("匹配失败，范围：" + pattern, 2);
//            captureAndLog("匹配失败，范围：" + pattern, tableE);
        }
        setReturnDataMap(allResult,"");
        return allResult;
    }

    /**
     * 同步预期值
     * @param expect 预期值
     * @param parentNodeXPath 父元素
     * @return true||false
     */
    public static boolean sync(String expect, String parentNodeXPath) throws RunException {
        return sync(expect, parentNodeXPath, 10);
    }

    /**
     * 同步预期值
     * @param expectValue
     * @param parent
     * @param intSecond
     * @return
     */
    public static boolean sync(String expectValue, String parent, int intSecond) throws RunException {
        boolean blflag = false;
        try {
            boolean isElement = false;
            SearchContext searchContext = webDriver;
            if (parent != null && parent.startsWith("/") && sync(parent, null)) {
                searchContext = webDriver.findElement(By.xpath(parent));
                isElement = true;
            }
            printLog("开始同步\"" + expectValue + "\"对象，预期超时时间为：" + intSecond + "秒");
            if (expectValue.startsWith("/")) {
                for (int i = 0; i < intSecond; i++) {
                    boolean isPresent = searchContext.findElements(By.xpath(expectValue)).size() > 0;
                    if (isPresent) {
                        blflag = true;
                        break;
                    } else {
                        pause(1);
                    }
                }
            } else {
                for (int i = 0; i < intSecond; i++) {
                    //通过文本获取、
                    boolean isPresent;
                    if (!isElement) {
                        isPresent = webDriver.findElement(By.tagName("body")).getText().contains(expectValue);
//                        isPresent = selenium.isTextPresent(expectValue);
                    } else {
                        isPresent = (((WebElement) searchContext).getAttribute("textContent")).contains(expectValue);
                    }
                    if (isPresent) {
                        blflag = true;
                        break;
                    } else {
                        pause(1);
                    }
                }
            }
        }catch (Exception e){
            throw new RunException(e);
        }
        if (blflag) {
            printLog("同步“" + expectValue + "”成功！", 1);
        } else {
            printLog(intSecond + "秒内 “" + expectValue + "” 对象未出现！", 2);
        }
        return blflag;
    }
    /**
     * 浏览器中同步结果
     * @param expectValue 预期值
     * @return
     */
    public static boolean syncText(String expectValue) throws RunException {
        return syncText(expectValue, 10);
    }

    public static boolean syncText(String expectValue, int intSecond) throws RunException {
        boolean blflag = false;
        printLog("开始同步\"" + expectValue + "\"文字，预期超时时间为：" + intSecond + "秒");
        for (int i = 0; i < intSecond; i++) {
            boolean isPresent = false;
            try {
                isPresent = webDriver.findElement(By.tagName("body")).getText().contains(expectValue);
            }catch (Exception e){
//            throw new RunException(e);
            }
            if (isPresent) {
                blflag = true;
                break;
            } else {
                snapShotPerSec();
                pause(1);
            }
        }
        if (blflag) {
            printLog("同步“" + expectValue + "”成功！", 1);
        } else {
            printLog(intSecond + "秒内 “" + expectValue + "” 对象未出现！", 2);
        }
        return blflag;
    }

    /**
     * 访问指定url
     * @param url
     */
    public static void accessUrl(String url) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        webDriver.get(url);
    }

    public static void captureAndLog(String notice, WebElement webElement) {
        printLog(notice,2);
//        highLightWebElement(webElement);
//        String png = capture();
//        cancelHighLightWebElement(webElement);
//        String log = notice + " >> capture screenshot at " + png;
//        logger.info(getLogTime() + ": " + log);
//        Reporter.log(getLogTime() + ": " + "<a href=\"./screenshot/" + png + "\" target=\"_bank\" style=\"color:red\">" + notice + "</a></br>");

    }

    public static byte[] takeScreenshot() throws RunException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;
        return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }

    public static void capture(String xPath, String filePath) throws RunException {
        try {
            WebElement webElement = Driver.getElement(xPath);
            // 获得webElement的位置和大小。
            Point location = webElement.getLocation();
            Dimension size = webElement.getSize();
            // 创建全屏截图。
            BufferedImage originalImage =
                    ImageIO.read(new ByteArrayInputStream(takeScreenshot()));
            // 截取webElement所在位置的子图。
            BufferedImage croppedImage = originalImage.getSubimage(
                    location.getX(),
                    location.getY(),
                    size.getWidth(),
                    size.getHeight());
            ImageIO.write(croppedImage, "png", new File(filePath));
        } catch (Exception e) {
            throw new RunException(e);
        }
    }

    /**
     * 打印log
     * @param log
     */
    public static void printLog(String log) {
        Print.log(log);
    }
    /**
     * 打印log
     * @param log
     */
    public static void printLog(String log, int style) {
        Print.log(log,style);
    }

    /**
     * 设置元素高亮
     * @param webElement
     */
    public static void highLightWebElement(WebElement webElement){
        listHighLightWebElement.add(webElement);
        if(null != webElement){
            javascriptExecutor.executeScript(getJsScript() + "setHighLightByNode(arguments[0])",webElement);
        }
    }

    /**
     * 设置元素高亮
     * @param webElementId
     */
    public static void highLightWebElement(String webElementId){
        listHighLightWebElementId.add(webElementId);
        if(null != webElementId){
            javascriptExecutor.executeScript(getJsScript() + "setHighLightById(arguments[0])",webElementId);
        }
    }

    /**
     * 取消元素高亮
     * @param webElementId
     */
    public static void cancelHighLightWebElement(String webElementId) throws RunException{
        ArrayList<String> listTemp = new ArrayList<>();
        listTemp.addAll(listHighLightWebElementId);
        for(String webEId : listTemp){
            try {
                javascriptExecutor.executeScript(getJsScript() + "cancelHighLightById(arguments[0])", webEId);
            } catch (Exception e) {
                Print.logRed(e.getMessage());
//                throw new RunException(e);
            }finally {
                if (listHighLightWebElementId.contains(webEId)){
                    listHighLightWebElementId.remove(webEId);
                }
            }
        }
    }

    /**
     * 取消元素高亮
     * @param webElement
     */
    public static void cancelHighLightWebElement(WebElement webElement) throws RunException{
        ArrayList<WebElement> listTemp = new ArrayList<>();
        listTemp.addAll(listHighLightWebElement);
        for(WebElement webE : listTemp){
            if(null != webE){
                try {
                    javascriptExecutor.executeScript(getJsScript() + "cancelHighLightByNode(arguments[0])", webE);
                } catch (Exception e) {
                    throw new RunException(e);
                }
            }
        }
    }

    /**
     * 运行脚本，将会执行测试脚本以及commd传进来的内容
     * @param commd 运行内容
     * @return Object
     * @throws RunException
     */
    public static Object runScript(String commd) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        return ((JavascriptExecutor) webDriver).executeScript(getJsScript()==null?"":getJsScript() + commd);
    }

    /**
     * 运行脚本，将会执行测试脚本以及commd传进来的内容
     * @param commd 运行内容
     * @param object 参数
     * @return Object
     * @throws RunException
     */
    public static Object runScript(String commd, Object object) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        return ((JavascriptExecutor) webDriver).executeScript(getJsScript()==null?"":getJsScript() + commd, object);
    }

    /**
     * 运行脚本，将会执行测试脚本以及commd传进来的内容
     * @param commd 运行内容
     * @param object 参数
     * @param attribute 属性
     * @return Object
     * @throws RunException
     */
    public static Object runScript(String commd, Object object, String attribute) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        return ((JavascriptExecutor) webDriver).executeScript(getJsScript()==null?"":getJsScript() + commd, object, attribute);
    }

    /**
     * 根据基本Web元素返回xpath
     * @param webElementId 元素
     * @return String
     * @throws RunException
     */
    public static String getXpathByWebElement(String webElementId) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        String locator;
        try{
            locator = String.valueOf(Driver.runScript("return getLocatorByNode(arguments[0])", webElementId));
        }catch(Exception e){
            throw new RunException(ExceptionInfo.get(RunException.GET_WEBELEMENT_FAIL));
        }
        return locator;
    }
    /**
     * 根据特殊Web元素返回xpath
     * @param webElementId 元素
     * @return String
     * @throws RunException
     */
    public static String getXpathBySpecialWebElement(String webElementId) throws RunException {
        if(null == webDriver){
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
        String locator = null;
        try{
            locator = String.valueOf(Driver.runScript("return getWebElementFullXPath(arguments[0])", webElementId));
        }catch(Exception e){
            throw new RunException(ExceptionInfo.get(RunException.GET_WEBELEMENT_FAIL));
        }
        return locator;
    }

    /**
     * 处理页面alert框
     * @return
     */
    public static boolean alertAccept(boolean flag) {
        try {
            Alert alert = webDriver.switchTo().alert();
            if (flag){
                alert.accept();
            }else {
                alert.dismiss();
            }
            return true;
        } catch (Exception e) {
//            CaptureAndLog("没有ALERT框弹出：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取alert框文字
     * @return
     */
    public static String getAlertText() throws RunException{
        try {
            Alert alert = webDriver.switchTo().alert();
            return alert.getText();
        } catch (Exception e) {
            throw new RunException(e);
        }
    }

    /**
     * 是否有alert框
     * @return
     */
    public static boolean isAlertOpen() {
        try {
            Alert alert = webDriver.switchTo().alert();
            if(alert!=null) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭浏览器
     */
    public static void quit() {
        webDriver.quit();
//        selenium.stop();
    }

    /**
     * Pause
     * @param time in second
     */
    public static void pause(int time, String xpath) throws RunException {
        if (StringUtil.isEmpty(xpath)){
            pause(time);
        }else {
            if (time <= 0) {
                return;
            }
            boolean hadFound = false;
            try {
                int t = time;
                do {
                    if (xpath.startsWith("/")){
                        if (null != webDriver.findElement(By.xpath(xpath))){
                            hadFound = true;
                            break;
                        }
                    }else {
                        if (null != webDriver.findElement(By.id(xpath))){
                            hadFound = true;
                            break;
                        }
                    }
                    Thread.sleep(1000);
                    t--;
                }while (t>0);
//            logger.info("wait " + time + " s");
            } catch (Exception e){
//                throw new RunException(e);
            }
            if (!hadFound){
                throw new RunException(ExceptionInfo.get(RunException.GET_WEBELEMENT_FAIL));
            }
        }
    }
    /**
     * Pause
     * @param time in second
     */
    public static void pause(int time) throws RunException {
        if (time <= 0) {
            return;
        }
        try {
            Thread.sleep(time*1000);
            printLog("wait " + time + " s");
        } catch (InterruptedException e) {
            throw new RunException(e);
        }
    }

    /**
     * 指定时间内等待控件消失
     * @param xPath
     * @param time
     * @return
     */
    public static boolean syncNoElement(String xPath, int time) throws RunException {
        //同步对象
//        pause(3);
        boolean blflag = false;
        try {
            if (xPath.startsWith("/")) {
                for (int i = 0; i < time; i++) {
                    boolean isPresent = webDriver.findElements(By.xpath(xPath)).size() > 0;
                    if (isPresent) {
                        pause(1);
                    } else {
                        blflag = true;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < time; i++) {
                    boolean isPresent = webDriver.findElement(By.tagName("body")).getText().contains(xPath);
//                    boolean isPresent = selenium.isTextPresent(xPath);
                    if (isPresent) {
                        pause(1);
                    } else {
                        blflag = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
            throw new RunException(e);
        }
        return blflag;
    }

    public static boolean syncElement(String xPath, int time) {
        boolean blflag = false;
        try {
            if (xPath.startsWith("/")) {
                for (int i = 0; i < time; i++) {
                    boolean isPresent = webDriver.findElements(By.xpath(xPath)).size() > 0;
                    if (!isPresent) {
                        snapShotPerSec();
                        pause(1);
                    } else {
                        blflag = true;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < time; i++) {
                    boolean isPresent = webDriver.findElement(By.tagName("body")).getText().contains(xPath);
//                    boolean isPresent = selenium.isTextPresent(xPath);
                    if (!isPresent) {
                        snapShotPerSec();
                        pause(1);
                    } else {
                        blflag = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
//            throw new RunException(e);
        }
        return blflag;
    }

    public static void snapShotPerSec(){
        if(!LogPanel.logPanelStart) {
            Screenshot screenshot = new Screenshot();
            screenshot.snapShot();
        }

    }

    /**
     * 刷新页面
     */
    public void refresh(){
        webDriver.navigate().refresh();
    }

    /**
     * 获取当前URL
     * @return URL
     */

    public static String getCurrentUrl() throws RunException {
        if (null != webDriver) {
            return webDriver.getCurrentUrl();
        }else {
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
    }

    /**
     * 浏览器最大化
     */
    public static void maxBrowser() throws RunException {
        if (null != webDriver) {
            webDriver.manage().window().maximize();
        }else {
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }
    }

    /**
     * 设置浏览器类型
     * @param browserName 浏览器名称
     * @throws ExcelException excel相关异常
     */
    public static void setBrowserType(String browserName) throws ExcelException {
        int type;
        switch (browserName.toLowerCase()){
            case "firefox":
                type = 1;
                break;
            case "chrome":
                type = 2;
                break;
            case "ie":
                type = 3;
                break;
            default:
                throw new ExcelException(ExceptionInfo.get(ExcelException.BROWSER_TYPE_INVAIL,browserName));
        }
        setBrowserType(type);
    }

    public static void enter(String xpath) throws RunException {
        try {
            if(!StringUtil.isEmpty(xpath)) {
                WebElement element = getElement(xpath);
                element.sendKeys(Keys.ENTER);
            }else {
                Robot rb = new Robot();
                rb.keyPress(KeyEvent.VK_ENTER);
                rb.keyRelease(KeyEvent.VK_ENTER);
            }
        } catch (Exception e) {
            throw new RunException(e);
        }
        printLog("操作动作：Enter/回车，已完成。");
    }

    public static Map<String, String> getReturnData() {
        return returnData;
    }

    public static void initReturnData() {
        returnData = new HashMap<String, String>();
    }
    /**
     * 获取JS代码
     * @return js代码字符串
     * @throws RunException 运行时错误
     */
    public static String getTestJavaScript() throws RunException {
        InputStream is = Driver.class.getResourceAsStream("JavaScript.js");
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        try {
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            is.close();
        } catch (IOException e) {
            throw new RunException(e);
        }
        return baos.toString();
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public static String getJsScript() {
        return jsScript;
    }

    public static void setJsScript(String jsScript) {
        Driver.jsScript = jsScript;
    }

    public JavascriptExecutor getJavascriptExecutor() {
        return javascriptExecutor;
    }

    public void setJavascriptExecutor(JavascriptExecutor javascriptExecutor) {
        Driver.javascriptExecutor = javascriptExecutor;
    }

    public static int getBrowserType() {
        return browserType;
    }

    public static void setBrowserType(int browserType) {
        Driver.browserType = browserType;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Driver.url = url;
    }


    public static WebElement getWebElement(By by) throws RunException {
        if (null != webDriver) {
            try{
                return webDriver.findElement(by);
            }catch (Exception e) {
                throw new RunException(ExceptionInfo.get(RunException.GET_WEBELEMENT_FAIL));
            }
        }else {
            throw new RunException(ExceptionInfo.get(RunException.BROWSER_NO_START));
        }

    }
    public Log getLogs() {
        return Print.getLogs();
    }

    public void setLogs(Log logs) {
        Print.setLogs(logs);
    }

}
