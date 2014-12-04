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
			Print.log("expected:[" + expected + "] but found [null]", 2);
			return;
		}else if(null == actual && null == expected){
			Print.debugLog(expected + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else if(null != actual && null == expected){
			Print.log("expected:[null] but found [" + actual + "]", 2);
			return;
		}
		
		if(actual.equals(expected)){
			Print.debugLog(expected + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else{
			Print.log("expected:[" + expected + "] but found [" + actual + "]", 2);
		}
		
	}
	
	
	public static void  assertEquals(String actual, String expected, String annotate){
		
		testCase++;
		testCaseAll++;
		if(null == actual && null != expected){
			Print.log(annotate + "FAIL", 2);
			Print.log("expected:[" + expected + "] but found [null]", 2);
			return;
		}else if(null == actual && null == expected){
			Print.debugLog(annotate + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else if(null != actual && null == expected){
			Print.log(annotate + "FAIL", 2);
			Print.log("expected:[null] but found [" + actual + "]", 2);
			return;
		}
		
		if(actual.equals(expected)){
			Print.debugLog(annotate + ":PASS", 1);
			executePass++;
			executePassAll++;
		}else{
			Print.log(annotate + ":FAIL", 2);
			Print.log("expected:" + expected + "but found " + actual, 2);
		}
	}
	
	/**
	 * 单元统计
	 */
	public static void statistics(){
		double avg = (double) executePass/testCase;
		int percent = (int) (avg*100);
		
		String executePassPercent = percent + "%";
		Print.log("总计执行:" + testCase + ",通过率:" + executePassPercent, 4);
		executePass = 0;
		testCase = 0;
	}
	
	/**
	 * 总计
	 */
	public static void statisticsAll(){
		double avg = (double) executePassAll++/testCaseAll;
		int percent = (int) (avg*100);
		
		String executePassPercent = percent + "%";
		Print.log("总计执行:" + testCaseAll + ",通过率:" + executePassPercent, 4);
	}
	

}
