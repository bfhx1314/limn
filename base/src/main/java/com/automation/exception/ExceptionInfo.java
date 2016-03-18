package com.automation.exception;

/**
 * Created by snow.zhang on 2015/10/29.
 */
public class ExceptionInfo {

    public static String get(int code){
        return get(code,"");
    }


    public static String get(int code,String message){
        String info;
        switch (code){
            // ExcelException
            case ExcelException.BROWSER_TYPE_INVAIL:
                info = "无效的浏览器类型："+message;
                break;
            case ExcelException.KEYWORD_IS_INVAIL:
                info = "无效的关键字信息："+message;
                break;
            case ExcelException.PARAM_IS_INVALID:
                info = "无效的参数："+message;
                break;
            // RunException
            case RunException.WEBELEMENT_IS_NULL:
                info = "未能定位，WebElement对象为null！ xpath："+message;
                break;
            case RunException.VALIDATE_PASS:
                info = "验证成功！";
                break;
            case RunException.VALIDATE_FAIL:
                info = "验证失败！";
                break;
            case RunException.BROWSER_NO_START:
                info = "浏览器没有启动！";
                break;
            case RunException.GET_WEBELEMENT_FAIL:
                info = "获取WebElement元素失败。";
                break;
            case RunException.WEBELEMENT_NOT_DISAPPEAR:
                info = "WebElement元素未消失。";
                break;
            case RunException.WEBELEMENT_NOT_APPEAR:
                info = "WebElement元素未出现。";
                break;
            case RunException.TEXT_NOT_APPEAR:
                info = "WebElement文字未出现。";
                break;
            case RunException.DECRYPT_BASE64_FAIL:
                info = "BASE64解密失败。 "+message;
                break;
            //
            default:
                info = "无效错误代码："+code;
        }
        return info;
    }
}
