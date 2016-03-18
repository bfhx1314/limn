package com.automation.exception;

/**
 * Created by snow.zhang on 2015/10/29.
 */
public class ExcelException extends MException {

    /**
     * 浏览器类型无效
     */
    public final static int BROWSER_TYPE_INVAIL = 10001;
    /**
     * 关键字无效
     */
    public final static int KEYWORD_IS_INVAIL = 10002;

    /**
     * 参数无效
     */
    public final static int PARAM_IS_INVALID = 10003;

    /**
     * 预期结果为空
     */
    public final static int EXPECT_IS_EMPTY = 10004;
    public ExcelException(){
        super();
    }

    public ExcelException(String info){
        super(info);
    }


}
