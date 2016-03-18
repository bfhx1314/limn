package com.automation.frame.debug;

import com.automation.frame.panel.BaseFrame;
import com.automation.frame.panel.CustomPanel;
import com.automation.frame.panel.KeyWordInfoPanel;
import com.automation.frame.panel.MultiplePanel;
import com.automation.keyword.BaseKeyWordImpl;
import com.automation.keyword.BaseKeyWordType;
import com.automation.keyword.KeyWordDriver;
import sun.security.krb5.internal.crypto.CksumType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugEditFrame{

    private static Map<String, KeyWordDriver> customKeyWordDriver = new HashMap<>();
    private static Map<String, Class> customKeyWordType = new HashMap<>();

    public static void main(String[] args){
        new DebugEditFrame();
    }

    public DebugEditFrame(){
        addKeyWordDriver("基础关键字", new BaseKeyWordImpl(), BaseKeyWordType.class);
        for(String key : customKeyWordDriver.keySet()){
            addKeyWordDriver(key, customKeyWordDriver.get(key), customKeyWordType.get(key));
        }
        init();
    }

    private void init(){
        new BaseFrame("用例调试器 V1.0");
    }

    /**
     * 增加关键字
     * @param keyWord 关键字key
     * @param keyWordDriver 关键字驱动
     * @param keyWordType 关键字描述
     */
    public static void addKeyWordDriver(String keyWord, KeyWordDriver keyWordDriver, Class<?> keyWordType) {
        CustomPanel.addKeyWordDriver(keyWord, keyWordDriver, keyWordType);
        KeyWordInfoPanel.addKeyWord(keyWord, keyWordType);
    }

    public static void setCustomKeyWordDriver(String keyWordDriverName, KeyWordDriver keyWordDriver, Class<?> keyWordType){
        customKeyWordDriver.put(keyWordDriverName, keyWordDriver);
        customKeyWordType.put(keyWordDriverName,keyWordType);
    }

    /**
     * 添加关键字面板
     */
    public void addKeyWordPanel(){
        MultiplePanel.addKeyWordPanel();
    }

}











