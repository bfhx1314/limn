package com.automation.report;

import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.StringUtil;
import com.automation.tool.util.FileUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * Created by chris.li on 2015/12/25.
 */
public class ReportConstructor {

    public static void generateReport(TestGather testGather) {
        if(testGather!=null && testGather.getSheetList()!=null && testGather.getSheetList().size()>0) {
            StringBuffer outPutLogs = new StringBuffer();
            int suiteIndex = 1;
            List<CaseGather> listCaseGather =  testGather.getSheetList();
            for(CaseGather caseGather:listCaseGather) {
                if(caseGather!=null && caseGather.getCaseInfoList()!=null && caseGather.getCaseInfoList().size()>0) {
                    int passedCount = 0;
                    int skippedCount = 0;
                    int failedCount = 0;
                    //case的report
                    int testIndex = 1;
                    for(CaseInfo caseInfo:caseGather.getCaseInfoList()) {
                        if(caseInfo==null) {
                            // TODO 用例为空
                            continue;
                        }
                        //状态统计
                        if(CaseInfo.status.FAIL.equals(caseInfo.getCaseStatus())) {
                            failedCount++;
                        }else if(CaseInfo.status.SKIP.equals(caseInfo.getCaseStatus())) {
                            skippedCount++;
                        }else if(CaseInfo.status.PASS.equals(caseInfo.getCaseStatus())){
                            passedCount++;
                        }
                        //生成单个suite的html
                        String fileName = "suite"+suiteIndex+"_test"+testIndex+"_results.html";
                        StringBuffer singleSuiteLog = generateSingleSuite(caseInfo, fileName);
                        outPutLogs.append(singleSuiteLog + "</br>");
                        testIndex++;
                    }
                    //塞入执行结果个数统计
                    caseGather.setPassedCount(passedCount);
                    caseGather.setFailedCount(failedCount);
                    caseGather.setSkippedCount(skippedCount);
                }else {
                    // TODO case没有信息要怎么办捏
                }
                suiteIndex++;
            }
            // 生成suites.html
            writeSuites(listCaseGather);
            // 生成overview.html
            writeOverView(testGather);
            // 生成output.html
            writeOutPutLogs(outPutLogs);

            // 生成testng-result.xml
            writeTestngResult(testGather.isAllCasePass());
        }else {
            // TODO sheet没有信息要怎么办捏
        }
    }

    /**
     * 生成testng-result.xml文件
     * @param isAllCasePass
     */
    private static void writeTestngResult(boolean isAllCasePass) {
        String status = "FAIL";
        if (isAllCasePass){
            status = "PASS";
        }
        StringBuffer testngResultXml = new StringBuffer();
        appendWithEnter(testngResultXml,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        appendWithEnter(testngResultXml,"<testng-results skipped=\"0\" failed=\"0\" total=\"0\" passed=\"0\">");
        appendWithEnter(testngResultXml,"<suite name=\"\" duration-ms=\"\" started-at=\"\" finished-at=\"\">");
        appendWithEnter(testngResultXml,"<test name=\"\" duration-ms=\"\" started-at=\"\" finished-at=\"\">");
        appendWithEnter(testngResultXml,"<class name=\"\">");
        appendWithEnter(testngResultXml,"<test-method status=\""+status+"\" signature=\"\" name=\"\" is-config=\"\" duration-ms=\"\" started-at=\"\" finished-at=\"\">");
        appendWithEnter(testngResultXml,"</test-method>");
        appendWithEnter(testngResultXml,"</class>");
        appendWithEnter(testngResultXml,"</test>");
        appendWithEnter(testngResultXml,"</suite>");
        appendWithEnter(testngResultXml,"</testng-results>");
        String sourcePath = Parameters.RESULT_PRJ_PATH + "\\testng-results.xml";
        if (FileUtil.exists(sourcePath)){
            FileUtil.deleteFloder(sourcePath);
        }
        FileUtil.writeFile(sourcePath,testngResultXml.toString());
    }

    /**
     * 生成overview.html
     * @param testGather
     */
    private static void writeOverView(TestGather testGather) {
        Properties properties = System.getProperties();
        String userName = properties.getProperty("user.name");
        String javaVersion = properties.getProperty("java.version");
        String osName = properties.getProperty("os.name");
        String runTime = testGather.getStartTime();
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        StringBuffer overviewHtml = new StringBuffer();
        List<CaseGather> listCaseGather = testGather.getSheetList();
        for(CaseGather caseGather : listCaseGather){
            appendWithEnter(overviewHtml,"<table class=\"overviewTable\">");
            appendWithEnter(overviewHtml,"<tr>");
            appendWithEnter(overviewHtml,"<th colspan=\"6\" class=\"header suite\">");
            appendWithEnter(overviewHtml,"<div class=\"suiteLinks\">");
            appendWithEnter(overviewHtml,"</div>");
            appendWithEnter(overviewHtml,caseGather.getSheetName());
            appendWithEnter(overviewHtml,"</th>");
            appendWithEnter(overviewHtml,"</tr>");
            appendWithEnter(overviewHtml,"<tr class=\"columnHeadings\">");
            appendWithEnter(overviewHtml,"<td>&nbsp;</td>");
            appendWithEnter(overviewHtml,"<th>Duration</th>");
            appendWithEnter(overviewHtml,"<th>Passed</th>");
            appendWithEnter(overviewHtml,"<th>Skipped</th>");
            appendWithEnter(overviewHtml,"<th>Failed</th>");
            appendWithEnter(overviewHtml,"<th>Pass Rate</th>");
            appendWithEnter(overviewHtml,"</tr>");
            List<CaseInfo> listCaseInfo = caseGather.getCaseInfoList();
            for(CaseInfo caseInfo : listCaseInfo){
                int passedNumber = 0;
                int skippedNumber = 0;
                int failedNumber = 0;
                String casePassedClassName = "zero number";
                String caseSkippedClassName = "zero number";
                String caseFailedClassName = "zero number";
                CaseInfo.status caseStatus = caseInfo.getCaseStatus();
                if (caseStatus == CaseInfo.status.PASS){
                    passedNumber++;
                    casePassedClassName = "passed number";
                }else if (caseStatus == CaseInfo.status.FAIL){
                    failedNumber++;
                    caseFailedClassName = "failed number";
                }else if (caseStatus == CaseInfo.status.SKIP){
                    skippedNumber++;
                }
                appendWithEnter(overviewHtml, "<tr class=\"test\">");
                appendWithEnter(overviewHtml,"<td class=\"test\">");
                appendWithEnter(overviewHtml,"<a href=\""+caseInfo.getFileName()+"\">"+caseInfo.getCaseName()+"</a>");
                appendWithEnter(overviewHtml,"</td>");
                appendWithEnter(overviewHtml,"<td class=\"duration\">");
                appendWithEnter(overviewHtml,caseInfo.getSpendTime()+"s");
                appendWithEnter(overviewHtml,"</td>");
                appendWithEnter(overviewHtml,"<td class=\""+casePassedClassName+"\">"+passedNumber+"</td>");
                appendWithEnter(overviewHtml,"<td class=\""+caseSkippedClassName+"\">"+skippedNumber+"</td>");
                appendWithEnter(overviewHtml,"<td class=\""+caseFailedClassName+"\">"+failedNumber+"</td>");
                appendWithEnter(overviewHtml,"<td class=\"passRate\">");
                String passRateTemp = String.valueOf(passedNumber/((passedNumber+skippedNumber+failedNumber)*1D) *100);
                String passRate = new BigDecimal(StringUtil.strNumberFormat(passRateTemp, null, 2, RoundingMode.HALF_UP, StringUtil.formatStr)).toPlainString();
                appendWithEnter(overviewHtml,passRate+"%");
                appendWithEnter(overviewHtml,"</td>");
                appendWithEnter(overviewHtml,"</tr>");
            }
            int passedNumber = caseGather.getPassedCount();
            int skippedNumber = caseGather.getSkippedCount();
            int failedNumber = caseGather.getFailedCount();
            // 计算总case的失败数量
            testGather.addFailedCount(failedNumber);
            testGather.addPassedCount(passedNumber);
            testGather.addSkippedCount(skippedNumber);
            String caseTotalPassedClass = "zero number";
            String caseTotalSkipedClass = "zero number";
            String caseTotalFailedClass = "zero number";

            appendWithEnter(overviewHtml,"<tr class=\"suite\">");
            appendWithEnter(overviewHtml,"<td colspan=\"2\" class=\"totalLabel\">Total</td>");
            appendWithEnter(overviewHtml,"<td class=\""+caseTotalPassedClass+"\">"+passedNumber+"</td>");
            appendWithEnter(overviewHtml,"<td class=\""+caseTotalSkipedClass+"\">"+skippedNumber+"</td>");
            appendWithEnter(overviewHtml,"<td class=\""+caseTotalFailedClass+"\">"+failedNumber+"</td>");
            appendWithEnter(overviewHtml,"<td class=\"passRate suite\">");
            String passRateTemp = String.valueOf(passedNumber/((passedNumber+skippedNumber+failedNumber)*1D) *100);
            String passRate = new BigDecimal(StringUtil.strNumberFormat(passRateTemp, null, 2, RoundingMode.HALF_UP, StringUtil.formatStr)).toPlainString();
            appendWithEnter(overviewHtml,passRate+"%");
            appendWithEnter(overviewHtml,"</td>");
            appendWithEnter(overviewHtml,"</tr>");
            appendWithEnter(overviewHtml,"</table>");
        }

        int passedNumber = testGather.getPassedCount();
        int skippedNumber = testGather.getSkippedCount();
        int failedNumber = testGather.getFailedCount();
        String casePassedClassName = "zero number";
        String caseFailedClassName = "zero number";
        if (passedNumber > 0){
            casePassedClassName = "passed number";
        }else if (failedNumber > 0){
            caseFailedClassName = "failed number";
        }
        // 设置全部用例是否通过
        if (failedNumber > 0){
            testGather.setAllCasePass(false);
        }else if (failedNumber == 0){
            testGather.setAllCasePass(true);
        }
        String passRateTemp = String.valueOf(passedNumber/((passedNumber+skippedNumber+failedNumber)*1D) *100);
        String passRate = new BigDecimal(StringUtil.strNumberFormat(passRateTemp, null, 2, RoundingMode.HALF_UP, StringUtil.formatStr)).toPlainString();
        appendWithEnter(overviewHtml,"<table class=\"allOverviewTable\">");
        appendWithEnter(overviewHtml,"<tr class=\"columnHeadings\">");
        appendWithEnter(overviewHtml,"<td>&nbsp;</td>");
        appendWithEnter(overviewHtml,"<th>Duration</th>");
        appendWithEnter(overviewHtml,"<th>Passed</th>");
        appendWithEnter(overviewHtml,"<th>Skipped</th>");
        appendWithEnter(overviewHtml,"<th>Failed</th>");
        appendWithEnter(overviewHtml,"<th>Pass Rate</th>");
        appendWithEnter(overviewHtml,"</tr>");
        appendWithEnter(overviewHtml,"<tr class=\"test\">");
        appendWithEnter(overviewHtml,"<td colspan=\"1\" class=\"allTotalLabel\">AllTotal</td>");
        appendWithEnter(overviewHtml,"<td class=\"duration\">");
        appendWithEnter(overviewHtml,testGather.getSpendTime()+"s");
        appendWithEnter(overviewHtml,"</td>");
        appendWithEnter(overviewHtml,"<td class=\""+casePassedClassName+"\">"+passedNumber+"</td>");
        appendWithEnter(overviewHtml,"<td class=\"zero number\">"+skippedNumber+"</td>");
        appendWithEnter(overviewHtml,"<td class=\""+caseFailedClassName+"\">"+failedNumber+"</td>");
        appendWithEnter(overviewHtml,"<td class=\"passRate suite\">");
        appendWithEnter(overviewHtml,passRate+"%");
        appendWithEnter(overviewHtml,"</td>");
        appendWithEnter(overviewHtml,"</tr>");
        appendWithEnter(overviewHtml,"</table>");


        String sourcePath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\overview.html";
        String getOverviewHtml = FileUtil.readFile(sourcePath);
        FileUtil.setEmpty(sourcePath);
        String newOverviewHtml = getOverviewHtml.replace("${runTime}",runTime)
                .replace("${userName}",userName)
                .replace("${javaVersion}",javaVersion)
                .replace("${osName}",osName)
                .replace("${hostName}",hostName)
                .replace("${overviewHtml}", overviewHtml);
        FileUtil.writeFile(sourcePath, newOverviewHtml);
    }

    /**
     * 生成suites.html
     * @param listCaseGather
     */
    private static void writeSuites(List<CaseGather> listCaseGather) {
        StringBuffer suitesHtml = new StringBuffer();
        for(CaseGather caseGather : listCaseGather){
            int sheetIndex = caseGather.getIndex();
            appendWithEnter(suitesHtml,"<thead><tr><th class=\"header suite\" onclick=\"toggleElement('tests-"+sheetIndex+"', 'table-row-group'); toggle('toggle-"+sheetIndex+"')\">");
            appendWithEnter(suitesHtml,"<span id=\"toggle-"+sheetIndex+"\" class=\"toggle\">&#x25bc;</span>"+caseGather.getSheetName()+"</th></tr></thead>");
            appendWithEnter(suitesHtml,"<tbody id=\"tests-"+sheetIndex+"\" class=\"tests\">");
            List<CaseInfo> listCaseInfo = caseGather.getCaseInfoList();
            for(CaseInfo caseInfo : listCaseInfo){
                String caseStatusClass = "failureIndicator";
                String caseStatusTitle = "Some tests failed.";
                String caseStatus = "&#x2718;";
                String caseName = caseInfo.getCaseName();
                String caseLink = caseInfo.getFileName();
                if (caseInfo.getCaseStatus() == CaseInfo.status.PASS){
                    caseStatusClass = "successIndicator";
                    caseStatusTitle = "All tests passed.";
                    caseStatus = "&#x2714;";
                }

                appendWithEnter(suitesHtml,"<tr>");
                appendWithEnter(suitesHtml,"<td class=\"test\">");
                appendWithEnter(suitesHtml,"<span class=\""+caseStatusClass+"\" title=\""+caseStatusTitle+"\">"+caseStatus+"</span>");
                appendWithEnter(suitesHtml,"<a href=\""+caseLink+"\" target=\"main\">"+caseName+"</a>");
                appendWithEnter(suitesHtml,"</td>");
                appendWithEnter(suitesHtml,"</tr>");
            }
            appendWithEnter(suitesHtml,"</tbody>");
        }
        String sourcePath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\suites.html";
        String suiteHtml = FileUtil.readFile(sourcePath);
        FileUtil.setEmpty(sourcePath);
        String newSuiteHtml = suiteHtml.replace("${suitesHtml}", suitesHtml);
        FileUtil.writeFile(sourcePath, newSuiteHtml);
    }


    private static StringBuffer generateSingleSuite(CaseInfo caseInfo, String fileName) {
        String sourcePath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\suite_test_results.html";
        //拼log
        StringBuffer logs = new StringBuffer();
        //状态
        String headerClass = "header passed";
        String headerName = "Passed Tests";
        if(CaseInfo.status.PASS.equals(caseInfo.getCaseStatus())) {
            headerClass = "header passed";
            headerName = "Passed Tests";
        }else if(CaseInfo.status.FAIL.equals(caseInfo.getCaseStatus())) {
            headerClass = "header failed";
            headerName = "Failed Tests";
        }else {
            headerClass = "header skipped";
            headerName = "Skipped Tests";
        }

        for(Log log:caseInfo.getLogs()) {
            List<String> screenShotPicForSecond = log.getScreenShotPicForSecond();
            String logStr = (log.getLogs()==null)?"":log.getLogs().toString().replace("\r\n", "</br>");
            if (screenShotPicForSecond.size() == 0){
                logStr = logStr.replace("!@#$","");
                buildLogHtml(log.getStepStatus(), logs, logStr, log.getScreenShotPic());
            }else {
                int picIndex = 0;
                String[] arrLog = logStr.split("</br>");
                for(String singleLog : arrLog){
//                    String singleLog = arrLog[i];
                    if (singleLog.contains("!@#$")){
                        singleLog = singleLog.replace("!@#$","");
                        String screenShotPic = screenShotPicForSecond.get(picIndex);
//                        appendWithEnter(logs, "<span><a href=\""+screenShotPicForSecond.get(i)+);
                        switch (log.getStepStatus()) {
                            case DONE:
                                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\">" + singleLog + "</a>" + "</span></br>");
//                                appendWithEnter(logs, "<span>" + singleLog + "</span>");
                                break;
                            case PASS:
                                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\">" + singleLog + "</a>" + "</span></br>");
//                                appendWithEnter(logs, "<span>" + singleLog + "</span>");
                                break;
                            case FAIL:
                                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\" style=\"color:red\">" + singleLog + "</a>" + "</span></br>");
                                break;
                            case ERROR:
                                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\" style=\"color:red\">" + singleLog + "</a>" + "</span></br>");
                                break;
                            case SKIP:
                                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\" style=\"color:##ffaa00\">" + singleLog + "</a>" + "</span></br>");
//                                appendWithEnter(logs, "<span style=\"color:##ffaa00\">" + singleLog + "</br></span>");
                            break;
                            default:
                                break;
                        }
                        picIndex++;
                    }else {
                        buildLogHtml(log.getStepStatus(), logs, singleLog + "</br>", log.getScreenShotPic());
                    }
                }
            }
        }

        //写html
        String suiteHtml = FileUtil.readFile(sourcePath);
        String newSuiteHtml = suiteHtml.replace("${CaseName}", caseInfo.getCaseName())
                .replace("${SpendTime}", caseInfo.getSpendTime())
                .replace("${HeaderClass}", headerClass)
                .replace("${HeaderName}", headerName)
                .replace("${Logs}", logs);
        FileUtil.writeFile(Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\" + fileName, newSuiteHtml);

        //塞名字
        caseInfo.setFileName(fileName);
        return logs;
    }

    private static void buildLogHtml(CaseInfo.status stepStatus, StringBuffer logs, String logStr, String screenShotPic){
        switch (stepStatus) {
            case DONE:
                appendWithEnter(logs, "<span>" + logStr + "</span>");
                break;
            case PASS:
                appendWithEnter(logs, "<span>" + logStr + "</span>");
                break;
            case FAIL:
                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\" style=\"color:red\">" + logStr + "</a>" + "</span>");
                break;
            case ERROR:
                appendWithEnter(logs, "<span><a href=\"" + screenShotPic + "\" target=\"_bank\" style=\"color:red\">" + logStr + "</a>" + "</span>");
                break;
            case SKIP:
                appendWithEnter(logs, "<span style=\"color:##ffaa00\">" + logStr + "</br></span>");
                break;
            default:
                break;
        }
    }

    /**
     * 写output.html
     * @param outPutLogs
     */
    private static void writeOutPutLogs(StringBuffer outPutLogs) {
        String sourcePath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\output.html";
        String suiteHtml = FileUtil.readFile(sourcePath);
        FileUtil.setEmpty(sourcePath);
        String newSuiteHtml = suiteHtml.replace("${outputLog}", outPutLogs);
        FileUtil.writeFile(sourcePath, newSuiteHtml);
    }
    public static StringBuffer appendWithEnter(StringBuffer originalStr, String appendStr) {
        return originalStr.append(appendStr).append("\r\n");
    }
}
