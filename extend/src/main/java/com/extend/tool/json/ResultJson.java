package com.extend.tool.json;

import java.util.*;

/**
 * Created by chris.li on 2016/1/18.
 */
public class ResultJson {

    private List resultList = new ArrayList();

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public void addResult(String result) {
        resultList.add(0, result);
    }

    public void addStringResultMap(String key, String wsResult) {
        Map<String, List> resultMap = null;
        if(resultList.size()>1) {
            resultMap = (Map)resultList.get(1);
        }else {
            resultMap = new LinkedHashMap<>();
            resultList.add(resultMap);
        }
        List<String> singleResultList = new ArrayList<String>();
        singleResultList.add(wsResult);
        resultMap.put(key, singleResultList);
        resultList.set(1, resultMap);
    }

    public void addNumberResultMap(String key, String wsResult, String multiplyNum, String scale, String format) {
        Map<String, List> resultMap = null;
        if(resultList.size()>1) {
            resultMap = (Map)resultList.get(1);
        }else {
            resultMap = new LinkedHashMap<>();
            resultList.add(resultMap);
        }
        List<String> singleResultList = new ArrayList<String>();
        singleResultList.add(wsResult);
        singleResultList.add(multiplyNum);
        singleResultList.add(scale);
        singleResultList.add(format);
        resultMap.put(key, singleResultList);
        resultList.set(1, resultMap);
    }
}
