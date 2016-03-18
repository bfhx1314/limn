package com.automation.keyword;

import com.automation.exception.ExcelException;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.MException;
import com.automation.exception.RunException;
import com.automation.report.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by snow.zhang on 2015/10/30.
 */
public class BaseKeyWordImpl implements KeyWordDriver {

    BaseRunKeyWordImpl runKeyWord = null;

    public BaseKeyWordImpl(){
        runKeyWord = new BaseRunKeyWordImpl();
    }

    @Override
    public int start(Map step) throws MException {
        int runStatus = 1;
        String keyWord = step.get(ExcelType.STEP).toString();

        try {
            switch (keyWord) {
                case BaseKeyWordType.START_BROWSER:
                    runKeyWord.startBrowser(step);
                    break;
                case BaseKeyWordType.CLOSE_BROWSER:
                    runKeyWord.closeBrowser();
                    break;
                case BaseKeyWordType.INPUT:
                    runKeyWord.input(step);
                    break;
                case BaseKeyWordType.INPUT_PASSWORD:
                    runKeyWord.inputPassword(step);
                    break;
                case BaseKeyWordType.CLICK:
                    runKeyWord.click(step);
                    break;
                case BaseKeyWordType.SELECT_OPTION:
                    runKeyWord.selectOption(step);
                    break;
                case BaseKeyWordType.PAUSE_TIME:
                    runKeyWord.pause(step);
                    break;
                case BaseKeyWordType.ENTER_IFRAME:
                    runKeyWord.enterFrame(step);
                    break;
                case BaseKeyWordType.EXIT_IFRAME:
                    runKeyWord.exitFrame();
                    break;
                case BaseKeyWordType.CHANGE_BRO_TAB:
                    runKeyWord.changeBroTab(step);
                    break;
                case BaseKeyWordType.CLOSE_BRO_TAB:
                    runKeyWord.closeBroTab(step);
                    break;
                case BaseKeyWordType.VERIFY:
                    runKeyWord.verify(step);
                    break;
                case BaseKeyWordType.FUZZY_MATCHING:
                    runKeyWord.fuzzyMatching(step);
                    break;
                case BaseKeyWordType.FUZZY_MATCHING_TABLE:
                    runKeyWord.fuzzyMatchingTable(step);
                    break;
                case BaseKeyWordType.SYNC:
                    runKeyWord.sync(step);
                    break;
                case BaseKeyWordType.MOUSE_OVER:
                    runKeyWord.mouseOver(step);
                    break;
                case BaseKeyWordType.ACCESS_URL:
                    runKeyWord.accessUrl(step);
                    break;
                case BaseKeyWordType.WAIT_WEBELEMENT_DISAPPEAR:
                    runKeyWord.waitWebelementDisappear(step);
                    break;
                case BaseKeyWordType.WAIT_WEBELEMENT_APPEAR:
                    runKeyWord.waitWebelementAppear(step);
                    break;
                case BaseKeyWordType.WAIT_TEXT_APPEAR:
                    runKeyWord.waitTextAppear(step);
                    break;
                case BaseKeyWordType.ENTER:
                    runKeyWord.pressEnter(step);
                    break;
                case BaseKeyWordType.VERIFY_ALERT:
                    runKeyWord.verifyAlert(step);
                    break;
                case BaseKeyWordType.CLOSE_ALERT:
                    runKeyWord.closeAlert(step);
                    break;
                case BaseKeyWordType.GET_WEBELEMENT_VALUE_TO_VAR:
                    runKeyWord.getWebElementValueToVar(step);
                    break;
                default:
                    runStatus = -1;
//                    throw new ExcelException(ExceptionInfo.get(ExcelException.KEYWORD_IS_INVAIL, keyWord));
            }
        } catch (IOException e1) {
            throw new MException(e1);
        } catch (Exception e) {
            throw new MException(e);
        }
        return runStatus;
    }

}
