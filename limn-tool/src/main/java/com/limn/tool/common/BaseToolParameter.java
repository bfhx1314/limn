package com.limn.tool.common;

/**
 * Created by limengnan on 2017/11/22.
 */
public class BaseToolParameter {

    private static ThreadLocal<Print> printThreadLocal =  new ThreadLocal<>();

    public static Print getPrintThreadLocal() {
        if(printThreadLocal.get() == null){
            printThreadLocal.set(new Print());
        }
        return printThreadLocal.get();
    }

    public static void setPrintThreadLocal(Print print){
        printThreadLocal.set(print);
    }

}
