package com.extend.start;

import com.extend.keyword.OSKeyWordImpl;
import com.extend.keyword.OSKeyWordType;
import com.automation.frame.debug.DebugEditFrame;
import com.extend.keyword.WindOSKeyWordImpl;
import com.extend.keyword.WindOSKeyWordType;

/**
 * Created by snow.zhang on 2015/11/19.
 */
public class DebugStart extends DebugEditFrame {


    public static void main(String[] args){

        OSKeyWordImpl osKeyWord = new OSKeyWordImpl();
        setCustomKeyWordDriver("OS关键字",osKeyWord, OSKeyWordType.class);
        WindOSKeyWordImpl windOsKeyWord = new WindOSKeyWordImpl();
        setCustomKeyWordDriver("WindOS关键字",windOsKeyWord, WindOSKeyWordType.class);
        new DebugEditFrame();
//        DebugEditFrame debugEditFrame = new DebugEditFrame();
//        debugEditFrame.addKeyWordDriver("OS关键字",osKeyWord, OSKeyWordType.class);
    }

}
