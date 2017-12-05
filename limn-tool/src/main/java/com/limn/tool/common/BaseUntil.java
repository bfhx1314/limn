package com.limn.tool.common;

import com.limn.tool.random.RandomData;

import java.util.ArrayList;

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
