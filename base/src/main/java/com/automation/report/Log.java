package com.automation.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by snow.zhang on 2015/12/23.
 */
public class Log {
    /**
     * 单步步骤的状态，DONE,PASS,FAIL,ERROR
     */
    private CaseInfo.status stepStatus;
    /**
     * 存放未知异常信息。
     */
    private String errorInfo = "";

    /**
     * 每一秒的log，用于同步文件、控件时记录的每秒log
     */
    private List<String> logsForSecond = new ArrayList<>();

    /**
     * 每一秒的截图，用于同步文件、控件时记录的每秒截图
     */
    private List<String> screenShotPicForSecond = new ArrayList<>();


    private StringBuffer logs = new StringBuffer();

    /**
     * 截图路径
     */
    private String screenShotPic = "";

    public void addLog(String log){
        getLogs().append(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "-->" + log);
        getLogs().append("\r\n");
    }

    public StringBuffer getLogs() {
        return logs;
    }

    public void setLogs(StringBuffer logs) {
        this.logs = logs;
    }

    public CaseInfo.status getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(CaseInfo.status stepStatus) {
        this.stepStatus = stepStatus;
    }

    public String getScreenShotPic() {
        return screenShotPic;
    }

    public void setScreenShotPic(String screenShotPic) {
        this.screenShotPic = "./screenshot/" + screenShotPic ;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<String> getScreenShotPicForSecond() {
        return screenShotPicForSecond;
    }

    public void setScreenShotPicForSecond(List<String> screenShotPicForSecond) {
        this.screenShotPicForSecond = screenShotPicForSecond;
    }

    public List<String> getLogsForSecond() {
        return logsForSecond;
    }

    public void setLogsForSecond(List<String> logsForSecond) {
        this.logsForSecond = logsForSecond;
    }

    public void addScreenShotPicForSecond(String screenShotPic){
        getScreenShotPicForSecond().add("./screenshot/"+screenShotPic);
    }

    public void addLogsForSecond(String log){
        getLogsForSecond().add(log);
    }

}
