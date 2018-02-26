package com.limn.tool.log;

public interface LogControlInterface {

	void printLog(String log,int style);
	
	void printLocalLog(String log,int style);

	void printContinueLog(String log, int style);

	void printlnLog(String log, int style);

	boolean isStart();
	
	void clearLog();
	
}
