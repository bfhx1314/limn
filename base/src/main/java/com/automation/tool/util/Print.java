package com.automation.tool.util;


import com.automation.frame.panel.LogPanel;
import com.automation.report.Log;

public class Print {
	
	private static int level = 1;
    /**
     * 日志
     */
    private static Log logs = new Log();

    /**
     * 输出
     * @param log
     */
    public static void log(String log){
        log(log,100);
    }

    /**
     * 输出红色
     * @param log
     */
    public static void logRed(String log){
        log(log,2);
    }

    /**
     * 输出黄色
     * @param log
     */
    public static void logYellow(String log){
        log(log,3);
    }

    /**
     * 输出粗体黑色
     * @param log
     */
    public static void logSoldblack(String log){
        log(log,4);
    }

    /**
     * 输出灰色
     * @param log
     */
    public static void logGray(String log){
        log(log,5);
    }

	/**
	 * 输出
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
	 */
	public static void log(String log,int style){
		if(LogPanel.logPanelStart){
            LogPanel.printLog(log, style);
		}else{
            if (null != logs){
                if (log.contains("wait")){
                    logs.addLog("!@#$"+log);
                }else {
                    logs.addLog(log);
                }
            }
            LogPanel.logger.info(log);
//			System.out.println(log);
		}
	}
	
	
	/**
	 * 输出
	 * @param log
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
	 */
	public static void debugLog(String log,int style){
		if(level>0){
			return;
		}
		if(LogPanel.logPanelStart){
            LogPanel.printLog("debug:" + log, style);
		}else{
			System.out.println(log);
		}
	}
	
	/**
	 * 设置输出等级 
	 * 大于 输出debuglog
	 * @param lev
	 */
	public static void setLevel(int lev){
		level = lev;
	}

    /**
     * 清空log面板内容
     */
    public static void clearLog(){
        LogPanel.clearLog();
    }

    public static void logGreen(String log) {
        log(log,1);
    }

    public static Log getLogs() {
        return logs;
    }

    public static void setLogs(Log oLogs) {
        logs = oLogs;
    }
}
