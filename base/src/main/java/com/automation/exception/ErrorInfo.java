package com.automation.exception;

/**
 * Created by snow.zhang on 2015/12/28.
 */
public class ErrorInfo {

    private String xpath = "";

    public ErrorInfo(String xpath){
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

}
