package com.limn.tool.log;

public interface LogControlInterface {

	public void printLog(String log,int style);
	
	public void printLocalLog(String log,int style);

	public boolean isStart();
	
	public void clearLog();
	
}
