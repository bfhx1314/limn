package com.extend.keyword;

import com.automation.driver.Driver;
import com.automation.exception.ErrorInfo;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.RunException;
import com.automation.keyword.ExcelType;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.StringUtil;
import com.google.gson.Gson;

import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by snow.zhang on 2015/12/29.
 */
public class WindOSRunKeyWordImpl {

	public void OSVerification(Map<String, String> step) throws RunException {
		String actuaValue;
		String attribute = step.get(ExcelType.PARAM);
		String xpath = step.get(ExcelType.XPATH);
		WebElement webElement = Driver.getElement(xpath);
		if (StringUtil.isEmpty(attribute)) {
			actuaValue = Driver.getValueByTag(webElement);
		} else {
			actuaValue = Driver.getHtmlAttributeValue(webElement, attribute);
		}
		Parameters.ACTUAL_VALUE = actuaValue;
		boolean validateResult = Driver.checkPointMatch(
				step.get(ExcelType.EXPECT), actuaValue,
				step.get(ExcelType.XPATH), webElement);
		if (!validateResult) {
			ErrorInfo errorInfo = new ErrorInfo(xpath);
			throw new RunException(
					ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
		} else {
			throw new RunException(
					ExceptionInfo.get(RunException.VALIDATE_PASS));
		}
	}

	public void OS_TimeSecond(Map<String, String> step) throws RunException,
			ParseException {
		String attribute = step.get(ExcelType.PARAM).split(",")[0];
		// 时间+10秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str =  Parameters.mapParameters.get(attribute);
		Date dt = sdf.parse(str);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.SECOND,Integer.parseInt(step.get(ExcelType.PARAM).split(",")[1]));
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		String xpath = step.get(ExcelType.XPATH);
		WebElement webElement = Driver.getElement(xpath);
		Driver.type(xpath, reStr);
	}

}
