package com.limn.tool.common;

/**
 * Created by limengnan on 2017/11/22.
 */
public class BaseUntil {


    public static boolean isEmpty(String o){
        if(null == o || o.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String o){
        if(null != o && !o.isEmpty()){
            return true;
        }
        return false;
    }


}
