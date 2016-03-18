package com.automation.tool.dictory;

/**
 * Created by snow.zhang on 2015-11-05.
 */
public class WebElementLocatorDescribe {
    /**
     * WebElement的序列和属性内容
     * 例：value = input{ type=text class=ui-pg-input}
     */
    private Integer key = 0;
    private String value = null;

    public WebElementLocatorDescribe(Integer key, String value){
        this.key = key;
        this.value = value;
    }


    public String value(){
        return value;
    }

    public int key(){
        return key;
    }
}
