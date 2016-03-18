package com.automation.exception;

/**
 * Created by snow.zhang on 2015/9/7.
 */
public class DBException extends MException {

    public DBException(){
        super();
    }

    public DBException(Exception e){
        super(e);
    }

    public DBException(String message){
        super(message);
    }

    public DBException(int errorCode, String message){
//        if (errorCode == )
        super(errorCode+":"+message);
    }
}
