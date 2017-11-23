package com.limn.tool.common;

import com.limn.tool.log.RunLog;

public class Print {

	private int level = 1;

	public final int INFO = 1;

	public final int DEBUG = 0;

	public final int FLAG = -1;

	private RunLog runLog = null;

	public void setRunLog(RunLog runLog){
		this.runLog = runLog;
	}

	public boolean isStart(){
		return runLog==null?false:runLog.isStart();
	}


	/**
	 * 输出
	 * 
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
	 */
	public void log(String log, int style) {
		if (level < 0) {
			return;
		}
		printlnLog(log, style);
	}

	public void logNoLine(String log, int style) {
		if (level < 0) {
			return;
		}
		printLog(log, style);
	}


	/**
	 * 输出
	 * 
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black, else black
	 */
	public void debugLog(String log, int style) {
		if (level == 0) {
			printlnLog("debug:" + log, style);
		}
	}

	private void printLog(String log, int style){
		if (isStart()) {
			runLog.printLog(log, style);
		} else {
			System.out.println(log);
		}
	}

	private void printlnLog(String log, int style){
		if (isStart()) {
			runLog.printlnLog(log, style);
		} else {
			System.out.println(log);
		}
	}


	public void flagLog(String log, int style) {
		if (level != FLAG) {
			printlnLog("Flag:" + log, style);
		}

	}


	boolean continuePrintClose = false;
	public void printContinueLogThread(String content, int style, int waitTime){
		if(continuePrintClose){
			log("有进程怎在处理中无法打印动态日志",2);
			return;
		}
		continuePrintClose = true;
		new Thread(new ContinuePrintLog(content,style,waitTime)).start();
	}

	public void stopContinueLogThread(){
		continuePrintClose = false;
	}


	/**
	 * 设置输出等级 大于 输出debuglog
	 * 
	 * @param lev
	 */
	public void setLevel(int lev) {
		level = lev;
	}


	public RunLog getRunlog(){
		return runLog;
	}

	class ContinuePrintLog implements Runnable{

		private String content;
		private int waitTime;
		private int style;

		public void setClose(boolean close) {
			this.close = close;
		}

		boolean close = false;
		public ContinuePrintLog(String content, int style, int waitTime){
			this.content = content;
			this.waitTime = waitTime;
			this.style = style;
		}

		@Override
		public void run(){

			while (continuePrintClose) {
				Common.wait(waitTime);
				if (!continuePrintClose) {
					break;
				}

				if (level < 0) {
					return;
				}
				if (isStart()) {
					runLog.printContinueLog(content, style);
				} else {
					System.out.println(content);
				}
			}

		}

	}

}

