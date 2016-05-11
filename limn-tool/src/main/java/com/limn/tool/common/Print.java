package com.limn.tool.common;

import com.limn.tool.log.RunLog;

public class Print {

	private static int level = 1;

	public static final int INFO = 1;

	public static final int DEBUG = 0;

	public static final int FLAG = -1;

	/**
	 * 输出
	 * 
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
	 */
	public static void log(String log, int style) {
		if (level < 0) {
			return;
		}
		if (RunLog.isStart()) {
			RunLog.printLog(log, style);
		} else {
			System.out.println(log);
		}
	}

	/**
	 * 输出
	 * 
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black, else black
	 */
	public static void debugLog(String log, int style) {
		if (level == 0) {
			if (RunLog.isStart()) {
				RunLog.printLog("debug:" + log, style);
			} else {
				System.out.println(log);
			}
		} else {
			return;
		}
	}
	

	public static void flagLog(String log, int style) {
		if (level != Print.FLAG) {
			return;
		}
		if (RunLog.isStart()) {
			RunLog.printLog("Flag:" + log, style);
		} else {
			System.out.println(log);
		}
	}

	/**
	 * 设置输出等级 大于 输出debuglog
	 * 
	 * @param lev
	 */
	public static void setLevel(int lev) {
		level = lev;
	}
}
