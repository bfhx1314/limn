package com.limn.common;

public class Common {

	public static void wait(int sec){
		try {
			Thread.sleep(sec*500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
