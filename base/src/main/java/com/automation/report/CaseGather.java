package com.automation.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow.zhang on 2015/12/17.
 */
public class CaseGather {

    private int index = 1;
    //sheet名称
    private String sheetName = "";
    //excel名称
    private String excelName = "";
    //通过数量
    private int passedCount = 0;
    //跳过数量
    private int skippedCount = 0;
    //失败数量
    private int failedCount = 0;
    //通过率
    private String passRate = "0";
    //用例执行结果集
    private List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String name) {
        this.sheetName = name;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public List<CaseInfo> getCaseInfoList() {
        return caseInfoList;
    }

    public void setCaseInfoList(List<CaseInfo> caseInfoList) {
        this.caseInfoList = caseInfoList;
    }

    public void addCaseInfo(CaseInfo caseInfo){
        caseInfoList.add(caseInfo);
    }

    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
