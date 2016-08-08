package com.limn.frame.control;

import java.util.LinkedHashMap;

import com.limn.frame.testcase.TestCase;

public class RunAutoTestingParameter {
	
	private static ThreadLocal<TestCase> runAutoTestingBean = new ThreadLocal<>();
	
	private static ThreadLocal<LinkedHashMap<String, String>> aliasBean = new ThreadLocal<>();

	public static TestCase getTestCase() {
		return runAutoTestingBean.get();
	}

	public static void setTestCase(TestCase testcase) {
		runAutoTestingBean.set(testcase);
	}

	public static LinkedHashMap<String, String> getAlias() {
		return aliasBean.get();
	}

	public static void setAlias(LinkedHashMap<String, String> alias) {
		aliasBean.set(alias);
	}
	
}
