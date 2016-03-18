package com.extend.keyword;

import com.automation.exception.MException;
import com.automation.keyword.ExcelType;
import com.automation.keyword.KeyWordDriver;

import java.util.Map;

/**
 * Created by snow.zhang on 2015/11/19.
 */
public class OSKeyWordImpl implements KeyWordDriver {

    OSRunKeyWordImpl osRunKeyWord = null;

    public OSKeyWordImpl(){
        osRunKeyWord = new OSRunKeyWordImpl();
    }

    @Override
    public int start(Map step) throws MException {
        int runStatus = 1;
        String keyWord = step.get(ExcelType.STEP).toString();
        try {
            switch (keyWord) {
                case OSKeyWordType.CANVAS_SINGLE_GREATETHEN_ZERO:
                    osRunKeyWord.canvasSingleGreatethenZero(step);
                    break;
                case OSKeyWordType.WS_POST_VERIFY:
                    osRunKeyWord.wsPostVerify(step);
                    break;
                case OSKeyWordType.WS_POST_COLUMN_VERIFY:
                    osRunKeyWord.wsPostVerifyColumn(step);
                    break;
                case OSKeyWordType.SQL_VERIFY_TABLE:
                    osRunKeyWord.sqlVerifyTable(step);
                    break;
                case OSKeyWordType.SQL_VERIFY_CHAR_DATA:
                    osRunKeyWord.sqlVerifyCharData(step);
                    break;
                case OSKeyWordType.SQL_COLUMN_VERIFY:
                    osRunKeyWord.sqlVerifyColumn(step);
                    break;
                case OSKeyWordType.JS_OPERATE:
                    osRunKeyWord.jsOperate(step);
                    break;
                case OSKeyWordType.VERIFY_CURRENT_TIME:
                    osRunKeyWord.verifyCurrentTime(step);
                    break;
                case OSKeyWordType.SQL_POINTS_VERIFY:
                    osRunKeyWord.sqlVerifyPoints(step);
                    break;
                case OSKeyWordType.SQL_POINTS_FUZZY_MATCHING:
                    osRunKeyWord.sqlFuzzyMatchingPoints(step);
                    break;
                default:
                    runStatus = -1;
//                    throw new ExcelException(ExceptionInfo.get(ExcelException.KEYWORD_IS_INVAIL, keyWord));
            }
        }catch (Exception e){
            throw new MException(e);
        }

        return runStatus;
    }


}
