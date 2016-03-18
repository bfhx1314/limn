package com.automation.keyword;

import com.automation.driver.Driver;
import com.automation.exception.*;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.Common;
import com.automation.tool.util.StringUtil;
import com.automation.tool.util.RegExp;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by snow.zhang on 2015/10/29.
 */
public class BaseRunKeyWordImpl {

    /**
     * 启动浏览器
     * @param step excel中所有参数
     */
    public void startBrowser(Map<String, String> step) throws Exception {
        killChrome();
        Driver.setBrowserType(Parameters.BrowserName);
        Driver.setUrl(step.get(ExcelType.PARAM));
        Driver.openBrowser();
    }

    /**
     * 关闭浏览器
     */
    public void closeBrowser() {
        Driver.quit();
    }

    /**
     * 录入控件内容
     * @param step excel中所有参数
     */
    public void input(Map<String, String> step) throws RunException {
        Driver.type(step.get(ExcelType.XPATH),
                step.get(ExcelType.PARAM));
    }

    /**
     * 录入密码，先BASE64解密后调用录入方法
     * @param step
     * @throws RunException
     */
    public void inputPassword(Map<String,String> step) throws RunException {
        String password = step.get(ExcelType.PARAM);
        password = Common.decryptBASE64(password);
        Driver.type(step.get(ExcelType.XPATH),password);
    }

    /**
     * 点击元素
     * @param step excel中所有参数
     */
    public void click(Map<String, String> step) throws RunException {
        Driver.click(step.get(ExcelType.XPATH));
    }

    /**
     * 选择下拉框
     * @param step excel中所有参数
     */
    public void selectOption(Map<String, String> step) throws RunException {
        Driver.selectOption(step.get(ExcelType.XPATH), step.get(ExcelType.PARAM));
    }

    /**
     * 等待时间
     * @param step excel中所有参数
     */
    public void pause(Map<String, String> step) throws RunException {
        Driver.pause(StringUtil.strToInt(step.get(ExcelType.PARAM)), step.get(ExcelType.XPATH));
    }

    /**
     * 切换浏览器页面
     * @param step excel中所有参数
     * @throws com.automation.exception.MException 异常
     */
    public void changeBroTab(Map<String, String> step) throws MException {
        int index;
        try{
            index = Integer.parseInt(step.get(ExcelType.PARAM));
        }catch(Exception e){
            throw new MException("切换浏览器："+step.get(ExcelType.PARAM)+"为非数字");
        }
        if (index>0){
            try {
                Driver.switchBrowser(index);
                Driver.maxBrowser();
            } catch (Exception e) {
                throw new MException("切换浏览器错误："+e.getMessage());
            }
        }else{
            throw new MException("切换浏览器页面参数错误："+index);
        }
    }

    public void killChrome() throws Exception {
        Runtime runTime = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        if(osName.contains("Mac")){
            String cmds[] = {"killall","Google Chrome"};
            runTime.exec(cmds);
        }
        if(osName.contains("Windows")) {
            runTime.exec("TASKKILL /F /IM chrome.exe");
//            runTime.exec("TASKKILL /F /IM chromedriver_for_win.exe");
        }
    }

    /**
     * 关闭浏览器
     * @param step excel中所有参数
     * @throws com.automation.exception.MException 异常
     */
    public void closeBroTab(Map<String, String> step) throws MException{
        int index;
        try{
            index = Integer.parseInt(step.get(ExcelType.PARAM));
        }catch(Exception e){
            throw new MException("关闭浏览器："+step.get(ExcelType.PARAM)+"为非数字");
        }
        if (index>0){
            try {
                Driver.closeBrowser(index);
            } catch (Exception e) {
                throw new MException("关闭浏览器错误："+e.getMessage());
            }
        }else{
            throw new MException("关闭浏览器页面参数错误："+index);
        }
    }

    /**
     * 切换iframe
     * @param step excel中所有参数
     * @throws com.automation.exception.MException 异常
     */
    public void enterFrame(Map<String, String> step) throws MException {
        int index;
        try{
            index = Integer.parseInt(step.get(ExcelType.PARAM));
        }catch(Exception e){
            throw new MException("切换frame："+step.get(ExcelType.PARAM)+"为非数字");
        }
        if (index>=0){
            try {
                Driver.swichToFrame(index);
            } catch (Exception e) {
                throw new MException("切换frame："+e.getMessage());
            }
        }else{
            throw new MException("切换frame页面参数错误："+index);
        }

    }

    /**
     * 切回初始frame
     */
    public void exitFrame() {
        Driver.swichToDefaultFrame();
    }

    /**
     * 验证控件内容
     * @param step excel中所有参数
     */
    public void verify(Map<String, String> step) throws MException{
        String actualValue;
        String attribute = step.get(ExcelType.PARAM);
        String xpath = step.get(ExcelType.XPATH);
        WebElement webElement = Driver.getElement(xpath);
        if (StringUtil.isEmpty(attribute)) {
            actualValue = Driver.getValueByTag(webElement);
        } else {
            actualValue = Driver.getAttributeValue(webElement, attribute);
        }
        Parameters.ACTUAL_VALUE = actualValue;
        boolean validatedResult = Driver.checkPoint(step.get(ExcelType.EXPECT), actualValue, step.get(ExcelType.OBJECT),webElement);
        if(!validatedResult) {
            ErrorInfo errorInfo = new ErrorInfo(xpath);
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
        }else {
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
        }
    }

    /**
     * 模糊验证控件内容
     * @param step excel中所有参数
     */
    public void fuzzyMatching(Map<String, String> step) throws MException{
        String actualValue;
        String attribute = step.get(ExcelType.PARAM);
        String xpath = step.get(ExcelType.XPATH);
        WebElement webElement = Driver.getElement(xpath);
        // 如果PARAM为空，默认获取value属性
        if (StringUtil.isEmpty(attribute)){
            actualValue = Driver.getValueByTag(webElement);
        }else {
            actualValue = Driver.getAttributeValue(webElement, attribute);
        }
        Parameters.ACTUAL_VALUE = actualValue;
        boolean validatedResult = Driver.checkPointMatch(step.get(ExcelType.EXPECT), actualValue, step.get(ExcelType.OBJECT), webElement);
        if(!validatedResult) {
            ErrorInfo errorInfo = new ErrorInfo(xpath);
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
        }else {
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
        }
    }

    /**
     * 模糊验证表格内容
     * @param step excel中所有参数
     */
    public void fuzzyMatchingTable(Map<String, String> step) throws MException {
        String tableXPath = step.get(ExcelType.XPATH);
        String param = step.get(ExcelType.PARAM);
        String pattern = step.get(ExcelType.EXPECT);
        int startCol = 1;
        int endCol = 1;
        if (param.indexOf("~") != -1){
            String[] arr = param.split("~");
            ArrayList<String> arrayList = RegExp.matcherCharacters(arr[0], "\\d+");
            String value = arrayList.get(0);
            if (StringUtil.isEmpty(value)){
                throw new ExcelException(ExceptionInfo.get(ExcelException.PARAM_IS_INVALID, pattern));
            }else {
                startCol = Integer.parseInt(value);
            }
            arrayList = RegExp.matcherCharacters(arr[1], "\\d+");
            value = arrayList.get(0);
            if (StringUtil.isEmpty(value)){
                throw new ExcelException(ExceptionInfo.get(ExcelException.PARAM_IS_INVALID, pattern));
            }else {
                endCol = Integer.parseInt(value);
            }
        }else {
            if(param.length()>2 && param.contains("第") && param.contains("列")) {
                startCol = Integer.parseInt(param.substring((param.indexOf("第")+1),param.indexOf("列")));
                endCol = startCol;
            }
        }
        boolean validatedResult = Driver.checkTableCellValue(tableXPath, startCol, endCol, pattern);
        if(!validatedResult) {
            ErrorInfo errorInfo = new ErrorInfo(tableXPath);
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
        }else {
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
        }
    }

    /**
     * 同步控件
     * @param step
     */
    public void sync(Map<String, String> step) throws MException {
        String expect = step.get(ExcelType.EXPECT);
        String parentNodeXPath = step.get(ExcelType.PARAM);
        if (StringUtil.isEmpty(expect)){
            throw new ExcelException(ExceptionInfo.get(ExcelException.EXPECT_IS_EMPTY,""));
        }
        boolean result;
        if (!StringUtil.isEmpty(parentNodeXPath)){
            result = Driver.syncText(expect);
        }else {
            result = Driver.sync(expect,parentNodeXPath);
        }
        if(result){
            Driver.printLog("内容：" + expect + ",同步成功！");
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
        }else {
//            driver.captureAndLog("内容：" + expect + ",同步失败！");
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL));
        }
    }

    /**
     * 鼠标mouseOver事件
     * @param step
     */
    public void mouseOver(Map<String, String> step) throws MException {
        Driver.mouseOver(step.get(ExcelType.XPATH));
    }

    /**
     * 访问指定Url
     * @param step
     */
    public void accessUrl(Map<String, String> step) throws RunException {
        Driver.accessUrl(step.get(ExcelType.PARAM));
    }

    /**
     * 等待控件消失
     * @param step
     */
    public void waitWebelementDisappear(Map<String, String> step) throws RunException {
        int time = StringUtil.strToInt(step.get(ExcelType.PARAM));
        boolean isDisappear = Driver.syncNoElement(step.get(ExcelType.XPATH), time);
        if (!isDisappear){
            throw new RunException(ExceptionInfo.get(RunException.WEBELEMENT_NOT_DISAPPEAR));
//            Driver.handleFailure("“加载控件”未消失，数据可能加载失败！");
        }
    }

    public void waitWebelementAppear(Map<String, String> step) throws RunException {
        int time = StringUtil.strToInt(step.get(ExcelType.PARAM));
        boolean isAppear = Driver.syncElement(step.get(ExcelType.XPATH), time);
        if (!isAppear){
            throw new RunException(ExceptionInfo.get(RunException.WEBELEMENT_NOT_APPEAR));
        }
    }

    public void waitTextAppear(Map<String, String> step) throws RunException {
        int time = StringUtil.strToInt(step.get(ExcelType.PARAM));
        boolean isAppear = Driver.syncText(step.get(ExcelType.EXPECT), time);
        if (!isAppear){
            throw new RunException(ExceptionInfo.get(RunException.TEXT_NOT_APPEAR));
        }
    }

    public void pressEnter(Map<String,String> step) throws MException {
        try {
            Driver.enter(step.get(ExcelType.XPATH));
        }catch (RunException e){
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }
    }

    public void verifyAlert(Map<String,String> step) throws MException {
        String actualValue = Driver.getAlertText();
        Parameters.ACTUAL_VALUE = actualValue;
        boolean validatedResult = Driver.checkPoint(step.get(ExcelType.EXPECT), actualValue, step.get(ExcelType.OBJECT), null);
        if(!validatedResult) {
            throw new MException("验证弹出框文字失败");
        }else {
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
        }
    }

    public void closeAlert(Map<String,String> step) throws MException {
        try {
            boolean acceptFlag = true;
            String message = "关闭弹出框";
            if("F".equals(StringUtil.trimAllSpace(step.get(ExcelType.PARAM)))) {
                acceptFlag = false;
                message = "取消弹出框";
            }
            boolean flag = Driver.alertAccept(acceptFlag);
            if(flag) {
                Driver.printLog(message);
            }else {
                throw new MException(message + "失败");
            }
        }catch (Exception e) {
            throw new MException(e);
        }
    }

    public void getWebElementValueToVar(Map<String,String> step) throws RunException {
        String xpath = step.get(ExcelType.XPATH);
        // 获取的属性
        String param = step.get(ExcelType.PARAM);
        // 变量名
        String var = step.get(ExcelType.OBJECT);
        if (Parameters.mapParameters.containsKey(var)){
            Driver.printLog("已存在变量:"+param+", 进行替换.",3);
        }
        WebElement webElement = Driver.getElement(xpath);
        String value = Driver.getAttributeValue(webElement, param);
        Parameters.mapParameters.put(var, value);
        Driver.printLog("存入变量:"+param+" = "+value);
    }


}
