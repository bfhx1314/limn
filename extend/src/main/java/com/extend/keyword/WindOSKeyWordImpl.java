package com.extend.keyword;

import com.automation.exception.MException;
import com.automation.keyword.ExcelType;
import com.automation.keyword.KeyWordDriver;

import java.util.Map;

public class WindOSKeyWordImpl implements KeyWordDriver {
	WindOSRunKeyWordImpl osRunKeyWord = null;

	public WindOSKeyWordImpl() {
		this.osRunKeyWord = new WindOSRunKeyWordImpl();
	}

	public int start(Map step) throws MException {
		int runStatus = 1;
		String keyWord = step.get(ExcelType.STEP).toString();
		try {
			String str1;
			switch (keyWord) {
            case WindOSKeyWordType.OS_verification:
				osRunKeyWord.OSVerification(step);
				break;
			case WindOSKeyWordType.OS_TimeSecond:
				osRunKeyWord.OS_TimeSecond(step);
				break;
			default:
				runStatus = -1;
			}
		} catch (Exception e) {
			throw new MException(e);
		}

		return runStatus;
	}
}