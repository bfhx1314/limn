package com.automation.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow.zhang on 2015/12/17.
 */
public class TestGather {

    private String startTime = "";

    private String endTime = "";

    private long startTimeMillis = 0;

    private long endTimeMillis = 0;

    private String spendTime = "";

    private int passedCount = 0;

    private int failedCount = 0;

    private int skippedCount = 0;

    private boolean allCasePass = false;

    private List<CaseGather> sheetList = new ArrayList<CaseGather>();

    public void addSheetData(CaseGather caseGather){
        sheetList.add(caseGather);
    }

    public List<CaseGather> getSheetList() {
        return sheetList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public boolean isAllCasePass() {
        return allCasePass;
    }

    public void setAllCasePass(boolean allCasePass) {
        this.allCasePass = allCasePass;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public void addFailedCount(int num){
        setFailedCount(getFailedCount()+num);
    }

    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

    public void addPassedCount(int num){
        setPassedCount(getPassedCount()+num);
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public void addSkippedCount(int num){
        setSkippedCount(getSkippedCount()+num);
    }
}
