package com.automation.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow.zhang on 2015/12/17.
 */
public class CaseInfo{

    private int index = 1;
    //用例名称
    private String caseName = "";
    //用例状态
    public enum status{DONE,PASS,FAIL,ERROR,SKIP};

    /**
     * 单个用例的状态，成功||失败
     */
    private status caseStatus;
    //花费时间
    private String spendTime = "";

    //文件名称
    private String fileName = "";

    //日志信息
    private List<Log> logs = new ArrayList<Log>();
//    private StringBuffer logs;

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

/*    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public void addLogs(Log log){
        this.logs.add(log);
    }

    public status getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(status caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

/*    public void addLog(String log){
        logs.append(log);
        logs.append("\r\n");
    }

    public StringBuffer getLogs() {
        return logs;
    }*/

}
