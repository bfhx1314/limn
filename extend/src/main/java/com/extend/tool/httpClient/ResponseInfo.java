package com.extend.tool.httpClient;

/**
 * Created by snow.zhang on 2015/9/15.
 */
public class ResponseInfo {


    /**
     * HTTP Status Code
     */
    public int status = -1;
    /**
     * Response Content
     */
    public String content = "";
    /**
     * Execute Time
     * second
     */
    public double time = -1;


    public void setStatus(int status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(double time) {
        this.time = time;
    }

}
