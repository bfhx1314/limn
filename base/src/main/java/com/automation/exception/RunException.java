package com.automation.exception;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class RunException extends MException {

    /**
     * 浏览器未启动
     */
    public final static int BROWSER_NO_START = 20002;
    /**
     * 未能定位，对象为null
     */
    public final static int WEBELEMENT_IS_NULL = 20001;
    /**
     * 验证失败
     */
    public final static int VALIDATE_FAIL = 20100;
    /**
     * 验证成功
     */
    public final static int VALIDATE_PASS = 20111;
    /**
     * 获取WebElement元素的XPath失败
     */
    public final static int GET_WEBELEMENT_FAIL = 20200;

    /**
     * 控件未消失
     */
    public final static int WEBELEMENT_NOT_DISAPPEAR = 20300;
    /**
     * 控件未出现
     */
    public final static int WEBELEMENT_NOT_APPEAR = 20301;

    /**
     * 文本未出现
     */
    public final static int TEXT_NOT_APPEAR = 20302;
//    public static final int NO_SUCH_ELEMENT = ;

    /**
     * 解密BASE64失败
     */
    public final static int DECRYPT_BASE64_FAIL= 20400;

    public RunException(String info){
        super(info);
    }
    public RunException(int code){
        super(code);
    }
    public RunException(Exception e){
        super(e);
    }

    public RunException(String info, ErrorInfo errorInfoT){
        super(info,errorInfoT);
    }
}
