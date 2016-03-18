package com.automation.exception;

import com.automation.frame.panel.LogPanel;
import com.automation.tool.util.Print;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class MException extends Exception {

    public static ErrorInfo errorInfo = new ErrorInfo("");

    public MException(){
        super();
    }

    public MException(String errorLog){
        super(errorLog);
        printErrorLog(errorLog);
    }
    public MException(String errorLog, ErrorInfo errorInfoT){
        super(errorLog);
        errorInfo = errorInfoT;
        printErrorLog(errorLog);
    }
    public MException(Exception e) {
        super(e);
        printStack(e);
    }

    public MException(Exception e, ErrorInfo errorInfoT) {
        super(e);
        errorInfo = errorInfoT;
        printStack(e);
    }

    public MException(int code) {
        super(String.valueOf(code));
        Print.log("errorCode："+code,2);
    }

    private void printErrorLog(String errorLog){
        if (LogPanel.logPanelStart){
            if (!errorLog.contains(ExceptionInfo.get(RunException.VALIDATE_PASS))
                    && !errorLog.contains(ExceptionInfo.get(RunException.VALIDATE_FAIL))){
                Print.log(errorLog,2);
            }
        }
    }

    private void printStack(Exception e){
        if (LogPanel.logPanelStart){
            if (null != e.getMessage()){
                if (!e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_PASS))
                        && !e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_FAIL))) {
                    printAllStackTrace(e);
/*                    Print.log(e.getMessage(), 2);
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        Print.log(stackTraceElement.toString(), 2);
                    }*/
                }
            }
        }
    }

    public static void printAllStackTrace(Exception e){
        Print.log(e.getMessage(), 2);
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            Print.log(stackTraceElement.toString(), 2);
        }
    }

    public static void initErrorInfo(){
        errorInfo = new ErrorInfo("");
    }

}
