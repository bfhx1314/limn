package com.limn.tool.log;

import java.io.FileWriter;
import java.io.IOException;

public class LogInformation {
	
	private static FileWriter fw = null;
	private static String logPath = null;
	
	
	public static void init(String path){
		logPath = path;
	}
	
	public static void appLog(String log){
		if(logPath!=null){
			try {
				fw = new FileWriter(logPath,true);
				fw.write(log);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
