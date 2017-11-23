package com.limn.tool.common;

public class Assert {
	
	public static int executePass = 0;
	
	public static int testCase = 0;
	
	public static int executePassAll = 0;
	
	public static int testCaseAll = 0;
	
	/**
	 * 字符对比
	 * @param actual 实际结果
	 * @param expected 预期结果
	 */
	public static void assertEquals(String actual, String expected){
		testCase++;
		testCaseAll++;
		if(null == actual && null != expected){
			BaseToolParameter.getPrintThreadLocal().log("expected:[" + expected + "] but found [null]", 2);
			return;
		}else if(null == actual && null == expected){
			BaseToolParameter.getPrintThreadLocal().debugLog(expected + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else if(null != actual && null == expected){
			BaseToolParameter.getPrintThreadLocal().log("expected:[null] but found [" + actual + "]", 2);
			return;
		}
		
		if(actual.equals(expected)){
			BaseToolParameter.getPrintThreadLocal().debugLog(expected + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else{
			BaseToolParameter.getPrintThreadLocal().log("expected:[" + expected + "] but found [" + actual + "]", 2);
		}
		
	}
	
	
	public static void  assertEquals(String actual, String expected, String annotate){
		
		testCase++;
		testCaseAll++;
		if(null == actual && null != expected){
			BaseToolParameter.getPrintThreadLocal().log(annotate + "FAIL", 2);
			BaseToolParameter.getPrintThreadLocal().log("expected:[" + expected + "] but found [null]", 2);
			return;
		}else if(null == actual && null == expected){
			BaseToolParameter.getPrintThreadLocal().debugLog(annotate + ":PASS", 1);
			executePass++;
			executePassAll++;
			return;
		}else if(null != actual && null == expected){
			BaseToolParameter.getPrintThreadLocal().log(annotate + "FAIL", 2);
			BaseToolParameter.getPrintThreadLocal().log("expected:[null] but found [" + actual + "]", 2);
			return;
		}
		
		if(actual.equals(expected)){
			BaseToolParameter.getPrintThreadLocal().debugLog(annotate + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else{
			BaseToolParameter.getPrintThreadLocal().log(annotate + ":FAIL", 2);
			BaseToolParameter.getPrintThreadLocal().log("expected:" + expected + "but found " + actual, 2);
		}
	}
	
	/**
	 * 单元统计
	 */
	public static void statistics(String title){
		double avg = (double) executePass/testCase;
		int percent = (int) (avg*100);
		
		String executePassPercent = percent + "%";
		BaseToolParameter.getPrintThreadLocal().log("[" + title + "]执行:" + testCase + ",通过率:" + executePassPercent, 4);
		BaseToolParameter.getPrintThreadLocal().log("", 4);
		executePass = 0;
		testCase = 0;
	}
	
	/**
	 * 总计
	 */
	public static void statisticsAll(){
		double avg = (double) executePassAll/testCaseAll;
		int percent = (int) (avg*100);
		
		String executePassPercent = percent + "%";
		BaseToolParameter.getPrintThreadLocal().log("--------------------------", 4);
		BaseToolParameter.getPrintThreadLocal().log("总计执行:" + testCaseAll + ",通过率:" + executePassPercent, 4);
		BaseToolParameter.getPrintThreadLocal().log("--------------------------", 4);
	}
	
	/**
	 * 清除单元统计
	 */
	public static void clearStatistics(){
		executePass = 0;
		testCase = 0;
	}

	/**
	 * 清除所有统计
	 */
	public static void clearStatisticsAll(){
		executePassAll = 0;
		testCaseAll = 0;
	}
	
}
