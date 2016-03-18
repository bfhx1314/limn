package com.automation.frame.run;

import com.automation.driver.Driver;
import com.automation.exception.ErrorInfo;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.MException;
import com.automation.exception.RunException;
import com.automation.keyword.BaseKeyWordType;
import com.automation.keyword.ExcelType;
import com.automation.keyword.KeyWordDriver;
import com.automation.keyword.KeyWordImpl;
import com.automation.report.*;
import com.automation.tool.excelEdit.ExcelDataProvider;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.*;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * Created by snow.zhang on 2015/12/18.
 */
public class Run {

    private KeyWordImpl keyWord = new KeyWordImpl();


    private TestGather testGather = new TestGather();

    public void runCase(String[] path){
        int len = path.length;
        String[] testCases = new String[len];
        String userDir = System.getProperty("user.dir");
        for(int i=0;i<len;i++){
            String casePath = userDir + "\\testcase" + path[i];
            if (path[i].contains(":")){
                casePath = path[i];
            }
            System.out.println(casePath);
            testCases[i] = casePath;
        }

        for(String singlePath : testCases){
            if (!FileUtil.exists(singlePath)){
                Print.logRed(singlePath+"不存在。");
                continue;
            }
//            TestGather testGather = new TestGather();
            if (RegExp.findCharacters(singlePath, ".xls|.xlsx$")){
                // 设置开始结束时间、全局变量、创建结果目录
                beforeRun(singlePath,testGather);
                String excelName = singlePath.replace(".xls|.xlsx$", "");
                // 单个用例
                ExcelDataProvider excelDataProvider = new ExcelDataProvider();
                Object excelDataObject = excelDataProvider.getExcelData(singlePath);
                Map<String, ?> excelData = (Map<String, Map<String, List<Map<String, String>>>>) excelDataObject;
                if (null == excelData){
                    Print.logRed(singlePath+"解析失败。");
                    continue;
                }
                int sheetIndex = 1;
                // 创建所有sheet集合对象
                for(Map.Entry<String,?> entry : excelData.entrySet()){
                    String sheetName = entry.getKey();
                    // 创建用例集对象
                    CaseGather caseGather = new CaseGather();
                    caseGather.setIndex(sheetIndex);
                    sheetIndex++;
                    caseGather.setExcelName(excelName);
                    caseGather.setSheetName(sheetName);
                    Map<String,?> sheetMap = (Map<String, ?>) entry.getValue();
                    int caseIndex = 1;
                    for(Map.Entry<String,?> sheetEntry : sheetMap.entrySet()){
                        // 标记每个case是否通过
                        boolean singleCasePassed = true;
                        String caseName = sheetEntry.getKey();
                        List<Map> caseRunStepList = (List<Map>) sheetEntry.getValue();
                        // 创建用例结果集对象
                        CaseInfo caseInfo = new CaseInfo();
                        caseInfo.setIndex(caseIndex);
                        caseIndex++;
                        caseInfo.setCaseName(caseName);
                        long startTime = System.currentTimeMillis();
                        // 跳过步骤
                        boolean startSkip = false;
//                        for(Map<String, String> testDataMap:caseRunStepList) {
                        int stepIndex = 0;
                        boolean doesRerun = true;
                        for(;stepIndex<caseRunStepList.size();stepIndex++) {
                            Map<String, String> testDataMap = caseRunStepList.get(stepIndex);
                            if (testDataMap.get(ExcelType.DOES_RUN).equals("N")){
                                continue;
                            }
                            // 创建log对象
                            Log log = new Log();
                            log.addLog(getStepInfo(testDataMap));
                            if (startSkip && !testDataMap.get(ExcelType.STEP).equals(BaseKeyWordType.CLOSE_BROWSER)){
                                log.addLog("Skip.");
                                log.setStepStatus(CaseInfo.status.SKIP);
                                continue;
                            }
                            keyWord.setLogs(log);
                            try {
                                keyWord.start(testDataMap);
                                log.setStepStatus(CaseInfo.status.DONE);
                            } catch (MException e) {
                                if (e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_FAIL))) {
                                    singleCasePassed = false;
                                    log.setStepStatus(CaseInfo.status.FAIL);
                                } else if (e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_PASS))) {
                                    log.setStepStatus(CaseInfo.status.PASS);
                                } else if (e.getMessage().contains(ExceptionInfo.get(RunException.BROWSER_NO_START))
                                        || e.getMessage().contains(ExceptionInfo.get(RunException.TEXT_NOT_APPEAR))) {
                                    // 启动重跑机制时，不会跳过用例。
                                    boolean doesSkip = true;
                                    if (doesRerun) {
                                        if (stepIndex > 0) {
                                            String keyword = caseRunStepList.get(stepIndex - 1).get(ExcelType.STEP).toString();
                                            if (keyword.equals(BaseKeyWordType.START_BROWSER)
                                                    && e.getMessage().contains(ExceptionInfo.get(RunException.TEXT_NOT_APPEAR))) {
                                                // 此情况为：启动浏览器后文字未出现，启动重跑机制。
                                                log.addLog("启动重跑机制。");
                                                log.setStepStatus(CaseInfo.status.ERROR);
                                                stepIndex--;
                                                stepIndex--;
                                                doesRerun = false;
                                                doesSkip = false;
                                            }
                                        }
                                    }
                                    if (doesSkip){
                                        // 此情况跳过之后的所有步骤。
                                        startSkip = true;
                                        singleCasePassed = false;
                                        log.setStepStatus(CaseInfo.status.ERROR);
                                        MException.printAllStackTrace(e);
                                    }
                                } else {
                                    singleCasePassed = false;
                                    log.setStepStatus(CaseInfo.status.ERROR);
                                    MException.printAllStackTrace(e);
                                }
                            } catch (Exception e1){
                                singleCasePassed = false;
                                log.setErrorInfo(e1.getMessage());
                                log.setStepStatus(CaseInfo.status.ERROR);
                                MException.printAllStackTrace(e1);
                            }
                            // 处理returnData，在用例中增加 实际结果、是否通过信息
                            setReturnDataInfo(testDataMap, log);
                            // 如果是失败、异常高亮截图
                            highLightAndCaptureScreenshot(log);
                            // 需要恢复场景的异常处理
                            recoveryScenarios(log);
                            caseInfo.addLogs(log);
                        }
                        long endTime = System.currentTimeMillis();
                        String time = String.valueOf((endTime-startTime)/1000D);
                        String executeTime = new BigDecimal(StringUtil.strNumberFormat(time, null, 2, RoundingMode.HALF_UP, StringUtil.formatStr)).toPlainString();
                        caseInfo.setSpendTime(executeTime);
                        if (singleCasePassed){
                            caseInfo.setCaseStatus(CaseInfo.status.PASS);
                        }else {
                            caseInfo.setCaseStatus(CaseInfo.status.FAIL);
                        }
                        // 把单个case的所有信息加到 caseGather中
                        caseGather.addCaseInfo(caseInfo);
                    }
                    // 把单个sheet的所有case对象加到 testGather中
                    testGather.addSheetData(caseGather);
                }
                // 数据回写到结果目录下的Excel
                excelDataProvider.setExcelData(excelDataObject,Parameters.RESULT_PRJ_TIME_TESTCASE_PATH);
            }else {
                // 用例文件夹
            }
            // 设置结束时间、执行时间
            testGather.setEndTimeMillis(DateFormat.getCurrentTimeMillis());
            String timeTemp = String.valueOf((testGather.getEndTimeMillis()-testGather.getStartTimeMillis())/1000D);
            String spendTime = new BigDecimal(StringUtil.strNumberFormat(timeTemp, null, 3, RoundingMode.HALF_UP, "0.000#########")).toPlainString();
            testGather.setSpendTime(spendTime);
            //产出report
            try {
                FileUtil.copySourceToFolder("report", Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH, this.getClass());
                ReportConstructor.generateReport(testGather);
            } catch (RunException e) {
                Print.logRed("复制report资源文件异常，生成report失败。");
                MException.printAllStackTrace(e);
            }
        }

        // 运行完结束所有相关进程。
        Runtime runTime = Runtime.getRuntime();
        try {
            if(Parameters.OS_NAME.contains("Mac")){
                String cmds[] = {"killall","Google Chrome"};
                runTime.exec(cmds);
            }else if(Parameters.OS_NAME.contains("Windows")){
                runTime.exec("TASKKILL /F /IM chrome.exe");
                runTime.exec("TASKKILL /F /IM chromedriver_for_win.exe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置实际结果 是否通过，用于输出到结果excel
     * @param testDataMap
     * @param log
     */
    private void setReturnDataInfo(Map<String, String> testDataMap, Log log) {
        try {
            Map<String, String> actualDataMap = Driver.getReturnData();
            String actualValue = "";
            if (actualDataMap.containsKey(ExcelType.ACTUAL)){
                actualValue = actualDataMap.get(ExcelType.ACTUAL);
            }
            testDataMap.put(ExcelType.ACTUAL, actualValue);
            testDataMap.put(ExcelType.ISPASS, log.getStepStatus().toString());
            Driver.initReturnData();
        } catch (Exception e) {
            Print.log("记录实际结果失败："+e.getMessage());
        }
    }

    /**
     * 高亮and截图
     * @param log
     */
    private void highLightAndCaptureScreenshot(Log log) {
        CaseInfo.status stepStatus = log.getStepStatus();
        if (stepStatus == CaseInfo.status.FAIL
                || stepStatus == CaseInfo.status.ERROR){
            ErrorInfo errorInfo = MException.errorInfo;
            String xpath = errorInfo.getXpath();
            WebElement webElement = null;
            if (!StringUtil.isEmpty(xpath)){
                try {
                    webElement = Driver.getElement(xpath,1);
                    Driver.highLightWebElement(webElement);
                } catch (RunException e) {
                    Print.log("高亮失败。");
                }
            }
            Screenshot screenshot = new Screenshot();
            screenshot.snapShot(log);
//            screenshot.snapShot(shotPath);
            if (null != webElement){
                try {
                    Driver.cancelHighLightWebElement(webElement);
                } catch (RunException e) {
                    Print.log(e.getMessage());
                }
            }
            MException.initErrorInfo();
        }
    }

    /**
     * 需要恢复场景的异常处理
     * @param log
     */
    private void recoveryScenarios(Log log) {
        // TODO 处理恢复场景的多种情况。
        String unknowError = log.getErrorInfo();
        if (null != unknowError){
            if (unknowError.contains("No buffer space available")){
                Driver.alertAccept(true);
                Print.log("已处理alert框。");
            }
        }
    }

    /**
     * 初始化
     * @param casePath
     * @param testGather
     */
    private void beforeRun(String casePath, TestGather testGather) {
        // 设置开始时间。
        testGather.setStartTimeMillis(DateFormat.getCurrentTimeMillis());
        testGather.setStartTime(DateFormat.getDate("yyyy-MM-dd HH:mm:ss EEEE",testGather.getStartTimeMillis()));

        casePath = casePath.replaceAll("\\\\","/");
        String arr[] = casePath.split("/");
        int len = arr.length;
        if (len>0){
            String excelName = arr[len-1];
            Parameters.TESTCASE_FULLNAME = excelName;
            excelName = excelName.replaceAll(".xls|.xlsx$","");
            Parameters.TESTCASE_NAME = excelName;
            Parameters.PRJ_NAME = arr[len-2];
        }
        // 生成Result目录
        if (!FileUtil.exists(Parameters.RESULT_PATH)){
            FileUtil.createFloder(Parameters.RESULT_PATH);
        }
        if (!FileUtil.exists(Parameters.RESULT_PRJ_PATH)){
            FileUtil.createFloder(Parameters.RESULT_PRJ_PATH);
        }
        // 生成时间格式的结果文件相关文件夹
        String timeTemp = DateFormat.getDate("yyyy-MM-dd_HH-mm-ss",testGather.getStartTimeMillis());
        if(StringUtil.isEmpty(Parameters.RESULT_PRJ_TIME_PATH)) {
            Parameters.RESULT_PRJ_TIME_PATH = Parameters.RESULT_PRJ_PATH + "\\" + timeTemp;
        }
        Parameters.RESULT_PRJ_TIME_TESTCASE_PATH = Parameters.RESULT_PRJ_TIME_PATH + "\\" + Parameters.TESTCASE_FULLNAME;
        FileUtil.createFloder(Parameters.RESULT_PRJ_TIME_PATH);
        try {
            FileUtil.copyFile(casePath,Parameters.RESULT_PRJ_TIME_TESTCASE_PATH);
            Parameters.RESULT_EXCEL_EXIST = true;
        } catch (IOException e) {
            Parameters.RESULT_EXCEL_EXIST = false;
            Print.logRed("创建结果excel失败，不影响后续执行。");
            MException.printAllStackTrace(e);
        }
        // 创建Report目录，加excel前缀
        Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH = Parameters.RESULT_PRJ_TIME_PATH + "\\"
                                                            + Parameters.TESTCASE_NAME + "_Report";
        FileUtil.createFloder(Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH);
    }

    /**
     * 生产每一个步骤的执行信息
     * @param testDataMap
     * @return
     */
    private String getStepInfo(Map<String, String> testDataMap) {
        String info = "";
        String step = testDataMap.get(ExcelType.STEP);
        if (!StringUtil.isEmpty(step)){
            info += step + ";";
        }
        String object = testDataMap.get(ExcelType.OBJECT);
        if (!StringUtil.isEmpty(object)){
            info += object + ";";
        }
        String parameter = testDataMap.get(ExcelType.PARAM);
        if (!StringUtil.isEmpty(parameter)){
            info += parameter + ";";
        }
        String expect = testDataMap.get(ExcelType.EXPECT);
        if (!StringUtil.isEmpty(expect)){
            info += expect + ";";
        }
        String xpath = testDataMap.get(ExcelType.XPATH);
        if (!StringUtil.isEmpty(xpath)){
            info += xpath + ";";
        }
        return info;
    }

    public void addKeyWordDriver(String key, KeyWordDriver osKeyWord) {
        keyWord.addKWD(key,osKeyWord);
    }


    public TestGather getTestGather() {
        return testGather;
    }

}
