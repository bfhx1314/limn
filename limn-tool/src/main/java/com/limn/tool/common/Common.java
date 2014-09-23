package com.limn.tool.common;

public class Common {

	public static void wait(int sec){
		try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
