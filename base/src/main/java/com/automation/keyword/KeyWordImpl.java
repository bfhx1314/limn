package com.automation.keyword;


import com.automation.exception.ExcelException;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.MException;
import com.automation.report.Log;
import com.automation.tool.util.Print;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/1.
 */
public class KeyWordImpl implements KeyWordDriver {
    private Log logs;
    private LinkedHashMap<String, KeyWordDriver> KWD = new LinkedHashMap<String, KeyWordDriver>();

//    public List<Map<String,String>> runKeyWord = null;

    public KeyWordImpl(){
        if (KWD.size() == 0){
            KWD.put("基础关键字", new BaseKeyWordImpl());
        }
    }

    @Override
    public int start(Map step) throws MException{
        LinkedHashMap<String,KeyWordDriver> kwd = getKWD();// KeyWordInfoPanel.getKWD();
//        String errorInfo = "";
        boolean isRun = false;
        for (String key : kwd.keySet()) {
            KeyWordDriver runKeyWord = kwd.get(key);
            Print.setLogs(logs);
            try {
                int runStatus = runKeyWord.start(step);
                if (runStatus == 1){
                    isRun = true;
                    break;
                }
                setLogs(Print.getLogs());
//                errorInfo = "";
            } catch (Exception e){
//                errorInfo = e.getMessage();
//                if (null != errorInfo && errorInfo.contains(ExceptionInfo.get(ExcelException.KEYWORD_IS_INVAIL))){
//                    continue;
//                }else {
                    throw new MException(e);
//                }
            }
        }
        if (!isRun){
            throw new ExcelException(ExceptionInfo.get(ExcelException.KEYWORD_IS_INVAIL, step.get(ExcelType.STEP).toString()));
        }
        return 0;
    }



    public void setLogs(Log logs) {
        this.logs = logs;
    }

    public Log getLogs() {
        return this.logs;
    }

    /*  public List<Map<String,String>> getReturnData(){
          return runKeyWord.getReturnData();
      }
  */
    public void addKWD(String keyWord, KeyWordDriver keyWordDriver){
        this.KWD.put(keyWord,keyWordDriver);
    }

    public void setKWD(LinkedHashMap<String, KeyWordDriver> kwd){
        this.KWD = kwd;
    }

    public LinkedHashMap<String, KeyWordDriver> getKWD(){
        return this.KWD;
    }
}
